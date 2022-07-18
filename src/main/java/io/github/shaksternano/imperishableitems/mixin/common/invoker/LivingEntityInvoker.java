package io.github.shaksternano.imperishableitems.mixin.common.invoker;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityInvoker {

    @Invoker("playEquipmentBreakEffects")
    void imperishableItems$invokePlayEquipmentBreakEffects(ItemStack stack);
}
