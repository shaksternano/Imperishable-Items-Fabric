package com.shaksternano.imperishableitems.common.blockentity;

import com.shaksternano.imperishableitems.common.access.BlockEntityAccess;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.Map;

public final class BlockEntityHelper {

    private BlockEntityHelper() {}

    /**
     * Copies the enchantments and repair cost of an {@link ItemStack} to a {@link BlockEntity}.
     */
    @SuppressWarnings("ConstantConditions")
    public static void setBlockEntityEnchantments(BlockEntity blockEntity, ItemStack stack) {
        if (stack.hasEnchantments()) {
            ((BlockEntityAccess) blockEntity).setEnchantments(stack.getEnchantments());
        }

        if (stack.hasNbt()) {
            if (stack.getNbt().contains("RepairCost", 3)) {
                ((BlockEntityAccess) blockEntity).setRepairCost(stack.getRepairCost());
            }
        }
    }

    /**
     * Copies the enchantments and repair cost of a {@link BlockEntity} to an {@link ItemStack}.
     */
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