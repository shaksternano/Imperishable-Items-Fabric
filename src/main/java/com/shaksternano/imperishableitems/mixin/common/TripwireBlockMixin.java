package com.shaksternano.imperishableitems.mixin.common;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import com.shaksternano.imperishableitems.common.enchantments.ImperishableEnchantment;
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

    // Shears with Imperishable at 0 durability can't disarm tripwires.
    @Redirect(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean imperishableDisarmTripwire(ItemStack stack, Item item, World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (ImperishableItems.getConfig().imperishablePreventsBreaking) {
            if (!player.isCreative()) {
                if (ImperishableEnchantment.isBrokenImperishable(stack)) {
                    return false;
                }
            }
        }

        return stack.isOf(item);
    }
}