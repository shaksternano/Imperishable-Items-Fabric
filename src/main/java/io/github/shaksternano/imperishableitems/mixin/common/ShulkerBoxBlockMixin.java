package io.github.shaksternano.imperishableitems.mixin.common;

import io.github.shaksternano.imperishableitems.common.api.BlockEntityHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin extends BlockWithEntity {

    private ShulkerBoxBlockMixin(Settings settings) {
        super(settings);
    }

    // Sets the enchantments and repair cost of the shulker box item dropped when a shulker box with enchantments and repair cost is broken by a player in creative mode.
    @Inject(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void setDroppedShulkerStackEnchantments(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci, BlockEntity blockEntity, ShulkerBoxBlockEntity shulkerBoxBlockEntity, ItemStack itemStack) {
        BlockEntityHelper.setDroppedItemStackEnchantments(blockEntity, itemStack);
    }
}
