package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import io.github.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import io.github.shaksternano.imperishableitems.common.util.ImperishableProtection;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Wearable;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemDispenserBehavior.class)
abstract class ItemDispenserBehaviorMixin implements DispenserBehavior {

    private ItemDispenserBehaviorMixin() {}

    @Shadow protected abstract ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack);

    // Dispensing an item is cancelled if that item has Imperishable and is at 0 durability.
    @Redirect(method = "dispense", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/dispenser/ItemDispenserBehavior;dispenseSilently(Lnet/minecraft/util/math/BlockPointer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"))
    private ItemStack dispenseBrokenImperishable(ItemDispenserBehavior thisBehavior, BlockPointer pointer, ItemStack stack) {
        if (ImperishableProtection.isItemProtected(stack, ImperishableProtection.ProtectionType.BREAK_PROTECTION)) {
            // Still allow a wearable item to be dispensed even if the item is broken.
            if (!(stack.getItem() instanceof Wearable)) {
                if (ImperishableEnchantment.isBrokenImperishable(stack)) {
                    if (thisBehavior instanceof FallibleItemDispenserBehavior) {
                        ((FallibleItemDispenserBehavior) thisBehavior).setSuccess(false);
                    }

                    return stack;
                }
            }
        }

        return dispenseSilently(pointer, stack);
    }
}
