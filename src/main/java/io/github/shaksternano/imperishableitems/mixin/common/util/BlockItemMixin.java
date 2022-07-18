package io.github.shaksternano.imperishableitems.mixin.common.util;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import io.github.shaksternano.imperishableitems.common.util.BlockEntityHelper;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
abstract class BlockItemMixin extends Item {

    @SuppressWarnings("unused")
    protected BlockItemMixin(Settings settings) {
        super(settings);
    }

    // Sets a block entity's enchantments and repair cost when it's placed.
    @Inject(method = "writeNbtToBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;getBlockEntityNbt(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/nbt/NbtCompound;"), cancellable = true)
    private static void imperishableItems$setBlockEntityEnchantments(World world, PlayerEntity player, BlockPos pos, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (ImperishableItems.getConfig().blockEntitiesStoreEnchantments) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                if (!world.isClient && blockEntity.copyItemDataRequiresOperator() && (player == null || !player.isCreativeLevelTwoOp())) {
                    cir.setReturnValue(false);
                } else {
                    BlockEntityHelper.setBlockEntityEnchantments(blockEntity, stack);
                }
            }
        }
    }
}
