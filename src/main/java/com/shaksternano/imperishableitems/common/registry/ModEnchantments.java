package com.shaksternano.imperishableitems.common.registry;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import com.shaksternano.imperishableitems.common.enchantments.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModEnchantments {

    private ModEnchantments() {}

    public static final Enchantment IMPERISHABLE = new ImperishableEnchantment();

    public static void registerEnchantments() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(ImperishableItems.MOD_ID, "imperishable"), IMPERISHABLE);
    }
}