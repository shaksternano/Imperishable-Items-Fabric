package io.github.shaksternano.imperishableitems.mixin.common.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Map;

@Mixin(BucketItem.class)
abstract class BucketItemMixin extends Item {

    private BucketItemMixin(Settings settings) {
        super(settings);
    }

    // Buckets retain their enchantments when picking up fluids.
    @ModifyArgs(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemUsage;method_30012(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private void pickupRetainEnchantments(Args args) {
        ItemStack stack = args.get(0);
        if (stack.hasEnchantments()) {
            ItemStack filledStack = args.get(2);
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
            EnchantmentHelper.set(enchantments, filledStack);
        }
    }


    // Buckets retain their enchantments when placing fluids.
    @Inject(method = "getEmptiedStack", at = @At("RETURN"), cancellable = true)
    private void placeRetainEnchantments(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (!player.abilities.creativeMode) {
            if (stack.hasEnchantments()) {
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(stack);
                ItemStack emptiedStack = cir.getReturnValue();
                EnchantmentHelper.set(enchantments, emptiedStack);
                cir.setReturnValue(emptiedStack);
            }
        }
    }
}
