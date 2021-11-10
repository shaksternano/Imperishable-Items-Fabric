package io.github.shaksternano.imperishableitems.common.access;

import net.minecraft.nbt.NbtElement;

public interface BlockEntityAccess {

    NbtElement getImperishableItemsEnchantments();

    void setImperishableItemsEnchantmentsEnchantments(NbtElement enchantments);

    Integer getImperishableItemsEnchantmentsRepairCost();

    void setImperishableItemsEnchantmentsRepairCost(Integer repairCost);
}
