package com.shaksternano.imperishableitems.common.enchantments;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import com.shaksternano.imperishableitems.common.registry.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

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
        return Math.max(ImperishableItems.getConfig().imperishableMinLevel, 0);
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + Math.max(ImperishableItems.getConfig().imperishableMaxLevelsAboveMin, 0);
    }

    @Override
    public boolean isTreasure() {
        return ImperishableItems.getConfig().imperishableIsTreasure;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return ImperishableItems.getConfig().imperishableSoldByVillagers;
    }

    // Returns true if an ItemStack has the Imperishable enchantment. Returns false otherwise.
    public static boolean hasImperishable(ItemStack stack) {
        return EnchantmentHelper.getLevel(ModEnchantments.IMPERISHABLE, stack) > 0;
    }

    // Returns true if an ItemStack is damageable, has the Imperishable enchantment, and the damage on it is more than the item's maximum damage. Returns false otherwise.
    public static boolean isBrokenImperishable(ItemStack stack) {
        return stack.isDamageable() && hasImperishable(stack) && stack.getDamage() >= stack.getMaxDamage();
    }

    // Removes the "(Broken)" string from the name of tools with Imperishable at 0 durability, so it doesn't mess with anvil renaming.
    public static String itemNameRemoveBroken(Text textName, ItemStack stack) {
        String trimmedName = textName.getString();

        if (isBrokenImperishable(stack)) {
            TranslatableText broken = new TranslatableText("item.name." + ImperishableItems.MOD_ID + ".imperishableBroken");
            int brokenLength = broken.getString().length();
            trimmedName = trimmedName.substring(0, trimmedName.length() - brokenLength);
        }

        return trimmedName;
    }
}