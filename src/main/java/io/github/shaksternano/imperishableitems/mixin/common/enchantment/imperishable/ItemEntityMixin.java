package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import io.github.shaksternano.imperishableitems.common.util.ImperishableBlacklistsHandler;
import io.github.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
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
abstract class ItemEntityMixin extends EntityMixin {

    @Shadow private int itemAge;

    @Shadow public abstract ItemStack getStack();

    // Items with Imperishable are invulnerable to all damage sources.
    @Override
    protected void damageImperishable(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (ImperishableBlacklistsHandler.isItemProtected(getStack(), ImperishableBlacklistsHandler.ProtectionType.DAMAGE_PROTECTION)) {
            if (ImperishableEnchantment.hasImperishable(getStack())) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void checkImperishable(CallbackInfo ci) {
        if (ImperishableEnchantment.hasImperishable(getStack())) {
            if (ImperishableBlacklistsHandler.isItemProtected(getStack(), ImperishableBlacklistsHandler.ProtectionType.DESPAWN_PROTECTION)) {
                // Items with Imperishable don't despawn.
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

            // Items with Imperishable stop falling when they reach Y=0, and float up to Y=0 if their Y coordinate is below Y=0.
            if (ImperishableBlacklistsHandler.isItemProtected(getStack(), ImperishableBlacklistsHandler.ProtectionType.VOID_PROTECTION)) {
                if (getY() == 0.0D) {
                    setVelocity(Vec3d.ZERO);
                    setPosition(getX(), 0.0D, getZ());
                    onGround = true;
                } else if (getY() < 0.0D) {

                    Vec3d velocity = getVelocity();
                    setVelocity(velocity.x * 0.97D, velocity.y + velocity.y < 0.06D ? 0.5D : 0.0D, velocity.z * 0.97D);

                    double x = getX() + getVelocity().x;
                    double y = getY() + getVelocity().y;
                    double z = getZ() + getVelocity().z;

                    if (y >= 0.0D) {
                        setVelocity(Vec3d.ZERO);
                        y = 0.0D;
                        onGround = true;
                    }

                    setPosition(x, y, z);
                }

                // Set the Item Entity's Y position to Y=-64 when below Y=-64
                if (getY() < -64.0D) {
                    setPosition(getX(), -64.0D, getZ());
                }
            }
        }
    }

    // Items with Imperishable don't appear on fire when in fire or lava.
    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    private void imperishableFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if (ImperishableBlacklistsHandler.isItemProtected(getStack(), ImperishableBlacklistsHandler.ProtectionType.DAMAGE_PROTECTION)) {
            if (ImperishableEnchantment.hasImperishable(getStack())) {
                cir.setReturnValue(true);
            }
        }
    }

    // Items with Imperishable don't get removed when below Y=-64.
    @Override
    protected void imperishableInVoid(CallbackInfo ci) {
        if (ImperishableBlacklistsHandler.isItemProtected(getStack(), ImperishableBlacklistsHandler.ProtectionType.VOID_PROTECTION)) {
            if (ImperishableEnchantment.hasImperishable(getStack())) {
                ci.cancel();
            }
        }
    }
}
