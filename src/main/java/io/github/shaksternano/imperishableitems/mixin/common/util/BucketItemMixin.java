package io.github.shaksternano.imperishableitems.mixin.common.util;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(BucketItem.class)
abstract class BucketItemMixin extends Item {

    public BucketItemMixin(Settings settings) {
        super(settings);
    }

    // Buckets retain their enchantments when placing fluids.
    @Inject(method = "getEmptiedStack", at = @At("RETURN"), cancellable = true)
    private void placeTransferEnchantments(ItemStack stack, PlayerEntity player, CallbackInfoReturnable<ItemStack> cir) {
        if (ImperishableItems.getConfig().retainEnchantmentsMoreOften) {
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
}
