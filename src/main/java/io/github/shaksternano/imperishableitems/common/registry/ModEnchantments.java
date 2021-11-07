package io.github.shaksternano.imperishableitems.common.registry;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import io.github.shaksternano.imperishableitems.common.enchantment.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ModEnchantments {

    private ModEnchantments() {}

    public static final Enchantment IMPERISHABLE = new ImperishableEnchantment();

    public static void registerEnchantments() {
        Registry.register(Registry.ENCHANTMENT, new Identifier(ImperishableItems.MOD_ID, ImperishableEnchantment.ENCHANTMENT_ID), IMPERISHABLE);
    }
}
