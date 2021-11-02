package com.shaksternano.imperishableitems.common.network;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import com.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import com.shaksternano.imperishableitems.common.registry.ModEnchantments;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

public final class ModNetworkHandler {

    private ModNetworkHandler() {}

    public static final Identifier EQUIPMENT_BREAK_EFFECTS = new Identifier(ImperishableItems.MOD_ID, "equipment_break_effects");
    public static final Identifier DEBUG_DROP_SET_IMPERISHABLE = new Identifier(ImperishableItems.MOD_ID, "debug_drop_set_imperishable");

    @Environment(EnvType.CLIENT)
    public static void registerClientGlobalReceivers() {
        // Plays item break effects.
        ClientPlayNetworking.registerGlobalReceiver(EQUIPMENT_BREAK_EFFECTS, (client, handler, buf, responseSender) -> {
            int itemId = buf.readInt();

            client.execute(() -> {
                if (client.player != null) {
                    ItemStack stack = new ItemStack(Item.byRawId(itemId));
                    client.player.playEquipmentBreakEffects(stack);
                }
            });
        });
    }

    public static void registerGlobalReceivers() {
        // If debug mode is on and the drop key is pressed, the item in the main hand will be enchanted with Imperishable instead of it being dropped. If the item already has Imperishable, the Imperishable enchantment will be removed from the item.
        ServerPlayNetworking.registerGlobalReceiver(DEBUG_DROP_SET_IMPERISHABLE, (server, player, handler, buf, responseSender) -> {
            server.execute(() -> {
                if (ImperishableItems.getConfig().debugMode) {
                    ItemStack stack = player.getMainHandStack();
                    if (!ImperishableEnchantment.hasImperishable(stack)) {
                        stack.addEnchantment(ModEnchantments.IMPERISHABLE, ModEnchantments.IMPERISHABLE.getMaxLevel());
                    } else {
                        NbtList enchantmentNbtList = stack.getEnchantments();

                        if (enchantmentNbtList.size() == 1) {
                            stack.removeSubNbt("Enchantments");
                            stack.removeSubNbt("RepairCost");
                        } else {
                            Identifier imperishableId = EnchantmentHelper.getEnchantmentId(ModEnchantments.IMPERISHABLE);

                            boolean removed = false;
                            int index = 0;
                            while (index < enchantmentNbtList.size() && !removed) {
                                NbtCompound enchantmentNbt = enchantmentNbtList.getCompound(index);
                                Identifier enchantmentId = EnchantmentHelper.getIdFromNbt(enchantmentNbt);
                                if (enchantmentId != null) {
                                    if (enchantmentId.equals(imperishableId)) {
                                        enchantmentNbtList.remove(index);
                                        removed = true;
                                    }
                                }

                                index++;
                            }
                        }
                    }
                }
            });
        });
    }
}
