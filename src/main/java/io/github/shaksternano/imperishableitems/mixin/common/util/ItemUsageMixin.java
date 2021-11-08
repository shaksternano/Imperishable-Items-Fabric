package io.github.shaksternano.imperishableitems.mixin.common.util;

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
    @Inject(method = "method_30270", at = @At("HEAD"))
    private static void retainEnchantments(ItemStack itemStack, PlayerEntity playerEntity, ItemStack itemStack2, boolean bl, CallbackInfoReturnable<ItemStack> cir) {
        if (!itemStack.isEmpty()) {
            if (!itemStack2.isEmpty()) {
                if (itemStack.hasEnchantments()) {
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack);
                    EnchantmentHelper.set(enchantments, itemStack2);
                }
            }
        }
    }
}
