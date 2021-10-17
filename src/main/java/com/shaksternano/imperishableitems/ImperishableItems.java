package com.shaksternano.imperishableitems;

import com.shaksternano.imperishableitems.registry.ModEnchantments;
import net.fabricmc.api.ModInitializer;

public class ImperishableItems implements ModInitializer {

    public static final String MOD_ID = "imperishableitems";

    @Override
    public void onInitialize() {
        ModEnchantments.registerEnchantments();
    }
}