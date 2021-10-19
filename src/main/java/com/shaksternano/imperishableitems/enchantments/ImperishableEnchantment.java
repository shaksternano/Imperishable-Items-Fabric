package com.shaksternano.imperishableitems.enchantments;

import com.shaksternano.imperishableitems.ImperishableItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ImperishableEnchantment extends Enchantment {

    public ImperishableEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.VANISHABLE, EquipmentSlot.values());
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinPower(int level) {
        return Math.max(ImperishableItems.config.imperishableMinPower, 0);
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 50;
    }

    @Override
    public boolean isTreasure() {
        return ImperishableItems.config.imperishableIsTreasure;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return ImperishableItems.config.imperishableSoldByVillagers;
    }
}