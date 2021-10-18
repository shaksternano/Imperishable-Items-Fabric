package com.shaksternano.imperishableitems.mixin;

import com.shaksternano.imperishableitems.registry.ModEnchantments;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TripwireBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TripwireBlock.class)
public abstract class TripwireBlockMixin extends Block {

    private TripwireBlockMixin(Settings settings) {
        super(settings);
    }

    @Shadow @Final public static BooleanProperty DISARMED;

    @Inject(method = "onBreak", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", shift = At.Shift.AFTER))
    private void imperishableDisarmTripwire(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        if (!player.isCreative()) {
            ItemStack stack = player.getMainHandStack();

            if (stack.isDamageable()) {
                if (EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, stack) > 0) {
                    if (stack.getDamage() >= stack.getMaxDamage()) {
                        world.setBlockState(pos, state.with(DISARMED, false), 4);
                    }
                }
            }
        }
    }
}