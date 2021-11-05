package io.github.shakternano.imperishableitems.mixin.common;

import io.github.shakternano.imperishableitems.common.api.ImperishableProtection;
import io.github.shakternano.imperishableitems.common.enchantment.ImperishableEnchantment;
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
    private boolean imperishableDisarmTripwire(ItemStack getMainHandStack, Item item, World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (ImperishableProtection.isItemProtected(getMainHandStack, ImperishableProtection.ProtectionType.BREAK_PROTECTION)) {
            if (!player.isCreative()) {
                if (ImperishableEnchantment.isBrokenImperishable(getMainHandStack)) {
                    return false;
                }
            }
        }

        return getMainHandStack.isOf(item);
    }
}
