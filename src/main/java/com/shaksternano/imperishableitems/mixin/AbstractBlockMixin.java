package com.shaksternano.imperishableitems.mixin;

import com.shaksternano.imperishableitems.blockentity.BlockEntityHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin {

    private AbstractBlockMixin() {}

    @Inject(method = "getDroppedStacks", at = @At("TAIL"))
    private void setDroppedItemStackEnchantments(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (state.getBlock() instanceof BlockWithEntity) {
            List<ItemStack> stacks = cir.getReturnValue();

            for (ItemStack stack : stacks) {
                if (stack.getItem().equals(state.getBlock().asItem())) {
                    BlockEntity blockEntity = builder.get(LootContextParameters.BLOCK_ENTITY);
                    BlockEntityHelper.setDroppedItemStackEnchantments(blockEntity, stack);
                }
            }
        }
    }
}