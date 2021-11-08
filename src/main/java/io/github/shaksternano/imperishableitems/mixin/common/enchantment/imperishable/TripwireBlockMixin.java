package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import io.github.shaksternano.imperishableitems.common.util.ImperishableProtection;
import io.github.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TripwireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TripwireBlock.class)
abstract class TripwireBlockMixin extends Block {

    public TripwireBlockMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private Item imperishableDisarmTripwire(ItemStack getMainHandStack, World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (ImperishableProtection.isItemProtected(getMainHandStack, ImperishableProtection.ProtectionType.BREAK_PROTECTION)) {
            if (!player.isCreative()) {
                if (ImperishableEnchantment.isBrokenImperishable(getMainHandStack)) {
                    return null;
                }
            }
        }

        return getMainHandStack.getItem();
    }
}
