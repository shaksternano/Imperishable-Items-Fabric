package com.shaksternano.imperishableitems.mixin.common;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import com.shaksternano.imperishableitems.common.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntityMixin {

    private TridentEntityMixin() {}

    @Shadow private ItemStack tridentStack;

    // Tridents with Imperishable stop falling when they reach the world's minimum Y.
    @Inject(method = "tick", at = @At("TAIL"))
    private void checkTridentImperishable(CallbackInfo ci) {
        if (ImperishableItems.getConfig().imperishableProtectsFromVoid) {
            if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, tridentStack) > 0) {
                if (!isNoClip()) {
                    if (getPos().y < world.getBottomY()) {
                        setVelocity(Vec3d.ZERO);
                        setPosition(getX(), world.getBottomY(), getZ());
                        inGround = true;
                        setNoClip(true);
                    }
                }
            }
        }
    }

    // Tridents with Imperishable don't despawn.
    @Inject(method = "age", at = @At("HEAD"), cancellable = true)
    private void imperishableAge(CallbackInfo ci) {
        if (ImperishableItems.getConfig().imperishablePreventsDespawn) {
            if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, tridentStack) > 0) {
                ci.cancel();
            }
        }
    }

    // Tridents with Imperishable don't get removed when 64 blocks below the world's minimum Y position.
    @Override
    protected void imperishableInVoid(CallbackInfo ci) {
        if (ImperishableItems.getConfig().imperishableProtectsFromVoid) {
            if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, tridentStack) > 0) {
                ci.cancel();
            }
        }
    }
}