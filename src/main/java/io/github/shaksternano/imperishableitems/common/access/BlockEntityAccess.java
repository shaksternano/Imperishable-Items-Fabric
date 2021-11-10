package io.github.shaksternano.imperishableitems.common.access;

import net.minecraft.nbt.NbtElement;

public interface BlockEntityAccess {

    NbtElement getImperishableItemsEnchantments();

    void setImperishableItemsEnchantments(NbtElement enchantments);

    Integer getImperishableItemsRepairCost();

    void setImperishableItemsRepairCost(Integer repairCost);
}
