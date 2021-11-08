package io.github.shaksternano.imperishableitems.mixin.common.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(BucketItem.class)
abstract class BucketItemMixin extends Item implements FluidModificationItem {

    private BucketItemMixin(Settings settings) {
        super(settings);
    }

    // Buckets retain their enchantments when picking up fluids.
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemUsage;exchangeStack(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void pickupRetainEnchantments(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, ItemStack itemStack, BlockHitResult blockHitResult, BlockPos blockPos, BlockPos blockPos2, BlockState blockState, FluidDrainable fluidDrainable, ItemStack itemStack2) {
        if (itemStack.hasEnchantments()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack);
            EnchantmentHelper.set(enchantments, itemStack2);
        }
    }

    // Buckets retain their enchantments when placing fluids.
    @Inject(method = "getEmptiedStack", at = @At("RETURN"), cancellable = true)
    private static void placeRetainEnchantments(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (!player.getAbilities().creativeMode) {
            if (stack.hasEnchantments()) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
                ItemStack emptiedStack = cir.getReturnValue();
                EnchantmentHelper.set(enchantments, emptiedStack);
                cir.setReturnValue(emptiedStack);
            }
        }
    }
}
