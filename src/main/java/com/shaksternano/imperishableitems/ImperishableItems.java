package com.shaksternano.imperishableitems;

import com.shaksternano.imperishableitems.config.ModConfig;
import com.shaksternano.imperishableitems.registry.ModEnchantments;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class ImperishableItems implements ModInitializer {

    public static final String MOD_ID = "imperishableitems";
    public static ModConfig config;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ModEnchantments.registerEnchantments();
    }
}