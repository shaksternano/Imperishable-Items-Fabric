package io.github.shakternano.imperishableitems.mixin.common;

import io.github.shakternano.imperishableitems.common.api.BlockEntityHelper;
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
public abstract class BlockItemMixin extends Item {

    private BlockItemMixin(Settings settings) {
        super(settings);
    }

    // Sets a block entity's enchantments and repair cost when it's placed.
    @Inject(method = "writeTagToBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getSubNbt(Ljava/lang/String;)Lnet/minecraft/nbt/NbtCompound;"), cancellable = true)
    private static void setBlockEntityEnchantments(World world, PlayerEntity player, BlockPos pos, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
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
