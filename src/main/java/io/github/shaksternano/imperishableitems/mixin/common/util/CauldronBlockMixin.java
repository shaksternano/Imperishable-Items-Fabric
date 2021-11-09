package io.github.shaksternano.imperishableitems.mixin.common.util;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Map;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin extends Block {

    public CauldronBlockMixin(Settings settings) {
        super(settings);
    }

    // Fluid containers retain their enchantments when placing or picking up fluids from a cauldron.
    @ModifyArgs(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setStackInHand(Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;)V"))
    private void setStackTransferEnchantments(Args args, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (ImperishableItems.getConfig().retainEnchantmentsMoreOften) {
            ItemStack inputStack = player.getStackInHand(hand);
            ItemStack outputStack = args.get(1);

            transferEnchantments(inputStack, outputStack);
        }
    }

    // Checks if the new ItemStack that has enchantments can be inserted into the player's inventory.
    @ModifyArgs(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"))
    private void insertStackTransferEnchantments(Args args, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (ImperishableItems.getConfig().retainEnchantmentsMoreOften) {
            ItemStack inputStack = player.getStackInHand(hand);
            ItemStack outputStack = args.get(0);

            transferEnchantments(inputStack, outputStack);
        }
    }

    // Dropped fluid containers retain their enchantments when placing or picking up fluids from a cauldron.
    @ModifyArgs(method = "onUse", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;"))
    private void dropItemTransferEnchantments(Args args, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (ImperishableItems.getConfig().retainEnchantmentsMoreOften) {
            ItemStack inputStack = player.getStackInHand(hand);
            ItemStack outputStack = args.get(0);

            transferEnchantments(inputStack, outputStack);
        }
    }

    // Transfers the enchantments from one item to another if the receiving item is a bucket, potion, or a bottle.
    private static void transferEnchantments(ItemStack inputStack, ItemStack outputStack) {
        if (ImperishableItems.getConfig().retainEnchantmentsMoreOften) {
            if (inputStack.hasEnchantments()) {
                if (outputStack.getItem() instanceof BucketItem || outputStack.getItem() instanceof PotionItem || outputStack.getItem() instanceof GlassBottleItem) {
                    Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(inputStack);
                    EnchantmentHelper.set(enchantments, outputStack);
                }
            }
        }
    }
}
