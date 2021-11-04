package com.shaksternano.imperishableitems.mixin.common;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {

    private PlayerEntityMixin() {}

    @Shadow public abstract boolean isSpectator();

    // If debug mode is on, sneaking will set the durability of the item in the main hand to 1. If the durability is already 1, it will set it to 0.
    @Override
    protected void debugSneakSetDamaged(boolean sneaking, CallbackInfo ci) {
        if (ImperishableItems.getConfig().debugMode) {
            if (!isSpectator()) {
                if (sneaking) {
                    ItemStack stack = getMainHandStack();
                    if (stack.isDamageable()) {
                        if (stack.getDamage() == stack.getMaxDamage() - 1) {
                            stack.setDamage(stack.getMaxDamage());
                        } else {
                            stack.setDamage(stack.getMaxDamage() - 1);
                        }
                    }
                }
            }
        }
    }
}
