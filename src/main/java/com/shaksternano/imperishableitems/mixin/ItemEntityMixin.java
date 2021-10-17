package com.shaksternano.imperishableitems.mixin;

import com.shaksternano.imperishableitems.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {

    private ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();

    @Shadow private int age;

    @Shadow private int health;

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;remove()V", shift = At.Shift.AFTER))
    private void damageImperishable(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, getStack()) > 0) {
            removed = false;
            health = 5;
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void checkImperishable(CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, getStack()) > 0 && !getStack().isEmpty()) {
            if (!world.isClient) {
                if (age == 1) {
                    age = 0;
                }
            } else {
                if (age == 3000) {
                    age = 0;
                }
            }

            if (getPos().y <= 0.0D) {
                updatePosition(getX(), 0.0D, getZ());
                updateTrackedPosition(getX(), 0.0D, getZ());
                setVelocity(Vec3d.ZERO);
            }
        }
    }

    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    private void imperishableFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, getStack()) > 0) {
            cir.setReturnValue(true);
        }
    }
}