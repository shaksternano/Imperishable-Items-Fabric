package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import io.github.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import io.github.shaksternano.imperishableitems.common.util.ImperishableProtection;
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
abstract class MobEntityMixin extends LivingEntity {

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // Shears with Imperishable at 0 durability have shear specific right click mob actions cancelled.
    @Inject(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;"), cancellable = true)
    private void imperishableShearMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);

        if (ImperishableProtection.isItemProtected(stack, ImperishableProtection.ProtectionType.BREAK_PROTECTION)) {
            if (!player.isCreative()) {
                if (stack.getItem() instanceof ShearsItem) {
                    if (this instanceof Shearable) {
                        if (ImperishableEnchantment.isBrokenImperishable(stack)) {
                            cir.setReturnValue(ActionResult.PASS);
                        }
                    }
                }
            }
        }
    }
}
