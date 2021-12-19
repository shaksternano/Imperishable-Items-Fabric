package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import io.github.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import io.github.shaksternano.imperishableitems.common.util.ImperishableBlacklistsHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.TripwireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TripwireBlock.class)
abstract class TripwireBlockMixin {

    // Shears with Imperishable at 0 durability can't disarm tripwires.
    @Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private boolean imperishableDisarmTripwire(ItemStack getMainHandStack, World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.isCreative()) {
            if (ImperishableBlacklistsHandler.isItemProtected(getMainHandStack, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
                if (ImperishableEnchantment.isBrokenImperishable(getMainHandStack)) {
                    return true;
                }
            }
        }

        return getMainHandStack.isEmpty();
    }
}
