package io.github.shaksternano.imperishableitems.common.network;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import io.github.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import io.github.shaksternano.imperishableitems.common.registry.ModEnchantments;
import io.github.shaksternano.imperishableitems.common.util.ImperishableProtection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;

public final class ModNetworking {

    private ModNetworking() {}

    public static final Identifier EQUIPMENT_BREAK_EFFECTS = new Identifier(ImperishableItems.MOD_ID, "equipment_break_effects");
    public static final Identifier DEBUG_SET_IMPERISHABLE = new Identifier(ImperishableItems.MOD_ID, "debug_set_imperishable");
    public static final Identifier DEBUG_SET_ONE_DURABILITY = new Identifier(ImperishableItems.MOD_ID, "debug_set_one_durability");
    public static final Identifier DEBUG_SET_ZERO_DURABILITY = new Identifier(ImperishableItems.MOD_ID, "debug_set_zero_durability");
    public static final Identifier DEBUG_INCREMENT_DURABILITY = new Identifier(ImperishableItems.MOD_ID, "debug_increment_durability");
    public static final Identifier DEBUG_DECREMENT_DURABILITY = new Identifier(ImperishableItems.MOD_ID, "debug_decrement_durability");

    public static void registerServerReceivers() {
        // If debug mode is on and the hotbar slot 1 key is pressed, the item in the main hand will be enchanted with Imperishable instead of it being dropped. If the item already has Imperishable, the Imperishable enchantment will be removed from the item.
        ServerPlayNetworking.registerGlobalReceiver(DEBUG_SET_IMPERISHABLE, (server, player, handler, buf, responseSender) -> server.execute(() -> {
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
                        boolean removed = false;
                        int index = 0;

                        while (index < enchantmentNbtList.size() && !removed) {
                            Identifier enchantmentId = EnchantmentHelper.getIdFromNbt(enchantmentNbtList.getCompound(index));
                            if (enchantmentId != null) {
                                if (enchantmentId.getNamespace().equals(ImperishableItems.MOD_ID)) {
                                    if (enchantmentId.getPath().equals(ImperishableEnchantment.ENCHANTMENT_ID)) {
                                        enchantmentNbtList.remove(index);
                                        removed = true;
                                    }
                                }
                            }

                            index++;
                        }
                    }
                }
            }
        }));

        // If debug mode is on and the hotbar slot 2 key is pressed, the durability of the item in the main hand will be set to 1. If the durability is already 1, it will set it to full.
        ServerPlayNetworking.registerGlobalReceiver(DEBUG_SET_ONE_DURABILITY, (server, player, handler, buf, responseSender) -> server.execute(() -> {
            if (ImperishableItems.getConfig().debugMode) {
                ItemStack stack = player.getMainHandStack();
                if (stack.isDamageable()) {
                    if (stack.getDamage() == stack.getMaxDamage() - 1) {
                        stack.setDamage(0);
                    } else {
                        stack.setDamage(stack.getMaxDamage() - 1);
                    }
                }
            }
        }));

        // If debug mode is on and the hotbar slot 3 key is pressed, the durability of the item in the main hand will be set to 0. If the durability is already 0, it will set it to full.
        ServerPlayNetworking.registerGlobalReceiver(DEBUG_SET_ZERO_DURABILITY, (server, player, handler, buf, responseSender) -> server.execute(() -> {
            if (ImperishableItems.getConfig().debugMode) {
                ItemStack stack = player.getMainHandStack();
                if (stack.isDamageable()) {
                    if (stack.getDamage() == stack.getMaxDamage()) {
                        stack.setDamage(0);
                    } else {
                        stack.setDamage(stack.getMaxDamage());
                    }
                }
            }
        }));

        // If debug mode is on and the hotbar slot 4 key is pressed, the durability of the item in the main hand will be decreased by 1 if the durability is not already 0.
        ServerPlayNetworking.registerGlobalReceiver(DEBUG_DECREMENT_DURABILITY, (server, player, handler, buf, responseSender) -> server.execute(() -> {
            if (ImperishableItems.getConfig().debugMode) {
                ItemStack stack = player.getMainHandStack();
                if (stack.isDamageable()) {
                    if (stack.getDamage() < stack.getMaxDamage()) {
                        stack.setDamage(stack.getDamage() + 1);
                    }
                }
            }
        }));

        // If debug mode is on and the hotbar slot 5 key is pressed, the durability of the item in the main hand will be increased by 1 if the durability is not already full.
        ServerPlayNetworking.registerGlobalReceiver(DEBUG_INCREMENT_DURABILITY, (server, player, handler, buf, responseSender) -> server.execute(() -> {
            if (ImperishableItems.getConfig().debugMode) {
                ItemStack stack = player.getMainHandStack();
                if (stack.isDamageable()) {
                    if (stack.getDamage() > 0) {
                        stack.setDamage(stack.getDamage() - 1);
                    }
                }
            }
        }));
    }

    @Environment(EnvType.CLIENT)
    public static void registerClientReceivers() {
        // Plays item break effects when the durability of an item reaches 0.
        ClientPlayNetworking.registerGlobalReceiver(EQUIPMENT_BREAK_EFFECTS, (client, handler, buf, responseSender) -> {
            int itemId = buf.readInt();

            client.execute(() -> {
                if (client.player != null) {
                    Item item = Item.byRawId(itemId);
                    if (ImperishableProtection.isItemProtected(item, ImperishableProtection.ProtectionType.BREAK_PROTECTION)) {
                        client.player.playEquipmentBreakEffects(new ItemStack(item));
                    }
                }
            });
        });
    }
}
