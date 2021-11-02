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
            if (client.player != null) {
                int itemId = buf.readInt();
                ItemStack stack = new ItemStack(Item.byRawId(itemId));

                client.execute(() -> client.player.playEquipmentBreakEffects(stack));
            }
        });
    }

    public static void registerGlobalReceivers() {
        // If debug mode is on and the drop key is pressed, the item in the main hand will be enchanted with Imperishable instead of it being dropped. If the item already has Imperishable, the Imperishable enchantment will be removed from the item.
        ServerPlayNetworking.registerGlobalReceiver(DEBUG_DROP_SET_IMPERISHABLE, (server, player, handler, buf, responseSender) -> {
            if (ImperishableItems.getConfig().debugMode) {
                ItemStack stack = player.getMainHandStack();
                if (!ImperishableEnchantment.hasImperishable(stack)) {
                    server.execute(() -> stack.addEnchantment(ModEnchantments.IMPERISHABLE, ModEnchantments.IMPERISHABLE.getMaxLevel()));
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
                                    final int finalIndex = index;
                                    server.execute(() -> enchantmentNbtList.remove(finalIndex));
                                    removed = true;
                                }
                            }

                            index++;
                        }
                    }
                }
            }
        });
    }
}
