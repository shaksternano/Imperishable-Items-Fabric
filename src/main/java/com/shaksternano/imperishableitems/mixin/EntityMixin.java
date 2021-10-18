package com.shaksternano.imperishableitems.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    protected EntityMixin() {}

    @Shadow public boolean removed;

    @Shadow public World world;

    @Shadow public abstract Vec3d getPos();

    @Shadow public abstract Vec3d getVelocity();

    @Shadow public abstract void setVelocity(Vec3d velocity);

    @Shadow public abstract void setPosition(double x, double y, double z);

    @Shadow public abstract double getX();

    @Shadow public abstract double getZ();

    @Shadow protected boolean onGround;

    @Shadow public abstract double getY();

    @Shadow public abstract void setVelocity(double x, double y, double z);

    @Inject(method = "tickInVoid", at = @At("HEAD"), cancellable = true)
    protected void imperishableInVoid(CallbackInfo ci) {}
}