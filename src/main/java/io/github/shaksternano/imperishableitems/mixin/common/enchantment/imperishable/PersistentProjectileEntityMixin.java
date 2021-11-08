package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PersistentProjectileEntity.class)
abstract class PersistentProjectileEntityMixin extends EntityMixin {

    PersistentProjectileEntityMixin() {}

    @Shadow protected boolean inGround;

    @Shadow public abstract boolean isNoClip();

    @Shadow public abstract void setNoClip(boolean noClip);
}
