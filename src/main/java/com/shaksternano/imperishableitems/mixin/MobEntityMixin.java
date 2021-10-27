package com.shaksternano.imperishableitems.mixin;

import com.shaksternano.imperishableitems.ImperishableItems;
import com.shaksternano.imperishableitems.registry.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Shearable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {

    private MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    private void imperishableShearMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (ImperishableItems.config.imperishablePreventsBreaking) {
            if (!player.isCreative()) {
                ItemStack stack = player.getStackInHand(hand);

                if (stack.getItem() instanceof ShearsItem) {
                    if (this instanceof Shearable) {
                        if (stack.isDamageable()) {
                            if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, stack) > 0) {
                                if (stack.getDamage() >= stack.getMaxDamage()) {
                                    cir.setReturnValue(ActionResult.PASS);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}