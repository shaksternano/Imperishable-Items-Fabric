package com.shaksternano.imperishableitems.mixin;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends EntityMixin {

    protected PersistentProjectileEntityMixin() {}

    @Shadow protected boolean inGround;

    @Shadow public abstract void setNoClip(boolean noClip);
}