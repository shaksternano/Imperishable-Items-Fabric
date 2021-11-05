package io.github.shakternano.imperishableitems.mixin.common;

import io.github.shakternano.imperishableitems.common.api.ImperishableProtection;
import io.github.shakternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {

    private AbstractBlockStateMixin() {}

    @Shadow public abstract Block getBlock();

    // Shears with Imperishable at 0 durability have shear specific right click block actions cancelled.
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    private void imperishableShearsUseOnBlock(World world, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack stack = player.getStackInHand(hand);

        if (ImperishableProtection.isItemProtected(stack, ImperishableProtection.ProtectionType.BREAK_PROTECTION)) {
            if (!player.isCreative()) {
                if (stack.getItem() instanceof ShearsItem) {
                    if (getBlock() instanceof BeehiveBlock || getBlock() instanceof PumpkinBlock) {
                        if (ImperishableEnchantment.isBrokenImperishable(stack)) {
                            cir.setReturnValue(ActionResult.PASS);
                        }
                    }
                }
            }
        }
    }
}
