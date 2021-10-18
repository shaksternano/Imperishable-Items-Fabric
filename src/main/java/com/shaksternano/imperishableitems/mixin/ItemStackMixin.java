package com.shaksternano.imperishableitems.mixin;

import com.shaksternano.imperishableitems.registry.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    private ItemStackMixin() {}

    @Shadow public abstract void setDamage(int damage);

    @Shadow public abstract int getMaxDamage();

    @Shadow public abstract Item getItem();

    @Inject(method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private void imperishableDurability(int amount, Random random, @Nullable ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir, int i) {
        if (!(getItem() instanceof ElytraItem)) {
            if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, (ItemStack) (Object) this) > 0) {
                if (i > getMaxDamage()) {
                    setDamage(getMaxDamage());
                } else {
                    setDamage(i);
                }

                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "isEffectiveOn", at = @At("HEAD"), cancellable = true)
    private void imperishableEffectiveOn(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, stack) > 0) {
            if (stack.getDamage() >= stack.getMaxDamage()) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
    private void imperishableNoDurabilitySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, stack) > 0) {
            if (stack.getDamage() >= stack.getMaxDamage()) {
                cir.setReturnValue(1.0F);
            }
        }
    }
}