package com.shaksternano.imperishableitems.blockentity;

import com.shaksternano.imperishableitems.access.BlockEntityAccess;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class BlockEntityHelper {

    private BlockEntityHelper() {}

    @SuppressWarnings("ConstantConditions")
    public static void setBlockEntityEnchantments(@Nullable BlockEntity blockEntity, ItemStack stack) {
        if (blockEntity != null) {
            if (stack.hasEnchantments()) {
                ((BlockEntityAccess) blockEntity).setEnchantments(stack.getEnchantments());
            }

            if (stack.hasTag()) {
                if (stack.getTag().contains("RepairCost", 3)) {
                    ((BlockEntityAccess) blockEntity).setRepairCost(stack.getRepairCost());
                }
            }
        }
    }

    public static void setDroppedItemStackEnchantments(BlockEntity blockEntity, ItemStack stack) {
        NbtElement enchantmentsNbt = ((BlockEntityAccess) blockEntity).getEnchantments();
        if (enchantmentsNbt != null) {
            Map<Enchantment, Integer> enchantmentsMap = EnchantmentHelper.fromNbt((NbtList) enchantmentsNbt);
            EnchantmentHelper.set(enchantmentsMap, stack);
        }

        Integer repairCost = ((BlockEntityAccess) blockEntity).getRepairCost();
        if (repairCost != null) {
            stack.setRepairCost(repairCost);
        }
    }
}