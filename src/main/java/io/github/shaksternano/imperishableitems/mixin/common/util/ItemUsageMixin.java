package io.github.shaksternano.imperishableitems.mixin.common.util;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ItemUsage.class)
abstract class ItemUsageMixin {

    // The enchantments of the input ItemStack are copied to the output ItemStack.
    @Inject(method = "exchangeStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"))
    private static void transferEnchantments(ItemStack inputStack, PlayerEntity player, ItemStack outputStack, boolean creativeOverride, CallbackInfoReturnable<ItemStack> cir) {
        if (ImperishableItems.getConfig().retainEnchantmentsMoreOften) {
            if (!inputStack.isEmpty()) {
                if (!outputStack.isEmpty()) {
                    if (inputStack.hasEnchantments()) {
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(inputStack);
                        EnchantmentHelper.set(enchantments, outputStack);
                    }
                }
            }
        }
    }
}
