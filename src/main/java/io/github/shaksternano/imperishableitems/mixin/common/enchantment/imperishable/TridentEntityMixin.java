package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import io.github.shaksternano.imperishableitems.common.util.ImperishableBlacklistsHandler;
import io.github.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
abstract class TridentEntityMixin extends PersistentProjectileEntityMixin {

    @Shadow private ItemStack tridentStack;

    // Tridents with Imperishable stop falling when they reach Y=0.
    @Inject(method = "tick", at = @At("TAIL"))
    private void checkTridentImperishable(CallbackInfo ci) {
        if (ImperishableBlacklistsHandler.isItemProtected(tridentStack, ImperishableBlacklistsHandler.ProtectionType.VOID_PROTECTION)) {
            if (ImperishableEnchantment.hasImperishable(tridentStack)) {
                if (!isNoClip()) {
                    if (getY() < 0.0D) {
                        setVelocity(Vec3d.ZERO);
                        setPosition(getX(), 0.0D, getZ());
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
        if (ImperishableBlacklistsHandler.isItemProtected(tridentStack, ImperishableBlacklistsHandler.ProtectionType.DESPAWN_PROTECTION)) {
            if (ImperishableEnchantment.hasImperishable(tridentStack)) {
                ci.cancel();
            }
        }
    }

    // Tridents with Imperishable don't get removed when below Y=-64.
    @Override
    protected void imperishableInVoid(CallbackInfo ci) {
        if (ImperishableBlacklistsHandler.isItemProtected(tridentStack, ImperishableBlacklistsHandler.ProtectionType.VOID_PROTECTION)) {
            if (ImperishableEnchantment.hasImperishable(tridentStack)) {
                ci.cancel();
            }
        }
    }
}
