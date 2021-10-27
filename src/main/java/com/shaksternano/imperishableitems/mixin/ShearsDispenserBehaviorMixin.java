package com.shaksternano.imperishableitems.mixin;

import com.shaksternano.imperishableitems.ImperishableItems;
import com.shaksternano.imperishableitems.registry.ModEnchantments;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShearsDispenserBehavior.class)
public abstract class ShearsDispenserBehaviorMixin extends FallibleItemDispenserBehavior {

    private ShearsDispenserBehaviorMixin() {}

    @Inject(method = "dispenseSilently", at = @At("HEAD"), cancellable = true)
    private void imperishableDispenserShear(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        if (ImperishableItems.getConfig().imperishablePreventsBreaking) {
            if (stack.isDamageable()) {
                if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, stack) > 0) {
                    if (stack.getDamage() >= stack.getMaxDamage()) {
                        setSuccess(false);
                        cir.setReturnValue(stack);
                    }
                }
            }
        }
    }
}