package com.shaksternano.imperishableitems.mixin;

import com.shaksternano.imperishableitems.ImperishableItems;
import com.shaksternano.imperishableitems.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin {

    private ItemEntityMixin() {}

    @Shadow public abstract ItemStack getStack();

    @Shadow private int itemAge;

    // Items with Imperishable are invulnerable to all damage sources.
    @Override
    protected void damageImperishable(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (ImperishableItems.getConfig().imperishableProtectsFromDamage) {
            if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, getStack()) > 0) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void checkImperishable(CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, getStack()) > 0 && !getStack().isEmpty()) {
            // Items with Imperishable don't despawn.
            if (ImperishableItems.getConfig().imperishablePreventsDespawn) {
                if (!world.isClient) {
                    if (itemAge >= 1) {
                        itemAge = 0;
                    }
                } else {
                    // itemAge on the client affects an item entity's visual spin, so reset it infrequently.
                    if (itemAge >= 3000) {
                        itemAge = 0;
                    }
                }
            }

            // Items with Imperishable stop falling when they reach the world's minimum Y, and float up to the world's minimum Y if their Y coordinate is below the world's minimum Y.
            if (ImperishableItems.getConfig().imperishableProtectsFromVoid) {
                if (getPos().y == world.getBottomY()) {
                    setVelocity(Vec3d.ZERO);
                    setPosition(getX(), world.getBottomY(), getZ());
                    onGround = true;
                } else if (getPos().y < world.getBottomY()) {

                    Vec3d velocity = getVelocity();
                    this.setVelocity(velocity.x * 0.97D, velocity.y + velocity.y < 0.06D ? 0.5D : 0.0D, velocity.z * 0.97D);

                    double x = getX() + getVelocity().x;
                    double y = getY() + getVelocity().y;
                    double z = getZ() + getVelocity().z;

                    if (y >= world.getBottomY()) {
                        setVelocity(Vec3d.ZERO);
                        y = world.getBottomY();
                        onGround = true;
                    }

                    setPosition(x, y, z);
                }

                // Set the Item Entity's Y position to 64 blocks below the world's minimum Y position when below 64 blocks below the world's minimum Y position.
                if (getPos().y < world.getBottomY() - 64.0D) {
                    setPosition(getX(), world.getBottomY() - 64.0D, getZ());
                }
            }
        }
    }

    // Items with Imperishable don't appear on fire when in fire or lava.
    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    private void imperishableFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (ImperishableItems.getConfig().imperishableProtectsFromDamage) {
            if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, getStack()) > 0) {
                cir.setReturnValue(true);
            }
        }
    }

    // Items with Imperishable don't get removed when 64 blocks below the world's minimum Y position.
    @Override
    protected void imperishableInVoid(CallbackInfo ci) {
        if (ImperishableItems.getConfig().imperishableProtectsFromVoid) {
            if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, getStack()) > 0) {
                ci.cancel();
            }
        }
    }
}