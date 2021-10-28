package com.shaksternano.imperishableitems.common.access;

import net.minecraft.nbt.NbtElement;

public interface BlockEntityAccess {

    NbtElement getEnchantments();

    void setEnchantments(NbtElement enchantments);

    Integer getRepairCost();

    void setRepairCost(Integer repairCost);
}