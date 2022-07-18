package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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

@SuppressWarnings("unused")
@Mixin(TripwireBlock.class)
abstract class TripwireBlockMixin {

    // Shears with Imperishable at 0 durability can't disarm tripwires.
    @ModifyExpressionValue(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean imperishableItems$imperishableDisarmTripwire(boolean isShears, World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!player.isCreative()) {
            ItemStack mainHandStack = player.getMainHandStack();
            if (ImperishableBlacklistsHandler.isItemProtected(mainHandStack, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
                if (ImperishableEnchantment.isBrokenImperishable(mainHandStack)) {
                    return false;
                }
            }
        }
        return isShears;
    }
}
