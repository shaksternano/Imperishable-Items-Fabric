package io.github.shakternano.imperishableitems.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    EntityMixin() {}

    @Shadow public World world;
    @Shadow protected boolean onGround;

    @Shadow public abstract double getX();

    @Shadow public abstract double getY();

    @Shadow public abstract double getZ();

    @Shadow public abstract void setPosition(double x, double y, double z);

    @Shadow public abstract Vec3d getVelocity();

    @Shadow public abstract void setVelocity(Vec3d velocity);

    @Shadow public abstract void setVelocity(double x, double y, double z);

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    protected void damageImperishable(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {}

    @SuppressWarnings("CancellableInjectionUsage")
    @Inject(method = "tickInVoid", at = @At("HEAD"), cancellable = true)
    protected void imperishableInVoid(CallbackInfo ci) {}
}
