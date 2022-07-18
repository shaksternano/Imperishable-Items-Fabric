package io.github.shaksternano.imperishableitems.common.access;

import net.minecraft.nbt.NbtElement;

public interface BlockEntityAccess {

    NbtElement imperishableItems$getImperishableItemsEnchantments();

    void imperishableItems$setImperishableItemsEnchantments(NbtElement enchantments);

    Integer imperishableItems$getImperishableItemsRepairCost();

    void imperishableItems$setImperishableItemsRepairCost(Integer repairCost);
}
