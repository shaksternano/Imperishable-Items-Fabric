package com.shaksternano.imperishableitems.mixin;

import com.shaksternano.imperishableitems.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {

    private TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow private ItemStack tridentStack;

    @Inject(method = "tick", at = @At("TAIL"))
    private void checkTridentImperishable(CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, tridentStack) > 0) {
            if (getPos().y < 0.0D) {
                setVelocity(Vec3d.ZERO);
                setPosition(getX(), 0.0D, getZ());
                inGround = true;
                setNoClip(true);
            }
        }
    }

    @Inject(method = "age", at = @At("HEAD"), cancellable = true)
    private void imperishableAge(CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, tridentStack) > 0) {
            ci.cancel();
        }
    }
}