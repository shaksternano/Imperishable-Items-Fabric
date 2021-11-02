package com.shaksternano.imperishableitems.mixin.common;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import com.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
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
public abstract class TripwireBlockMixin extends Block {

    private TripwireBlockMixin(Settings settings) {
        super(settings);
    }

    @Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private Item imperishableDisarmTripwire(ItemStack stack, World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (ImperishableItems.getConfig().imperishablePreventsBreaking) {
            if (!player.isCreative()) {
                if (ImperishableEnchantment.isBrokenImperishable(stack)) {
                    return null;
                }
            }
        }

        return stack.getItem();
    }
}
