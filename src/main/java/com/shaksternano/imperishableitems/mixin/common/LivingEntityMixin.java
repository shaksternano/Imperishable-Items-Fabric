package com.shaksternano.imperishableitems.mixin.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {

    LivingEntityMixin() {}

    @Shadow public abstract ItemStack getMainHandStack();
}
