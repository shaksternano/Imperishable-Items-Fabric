package com.shaksternano.imperishableitems.common;

import com.shaksternano.imperishableitems.common.config.ModConfig;
import com.shaksternano.imperishableitems.common.event.ModEvents;
import com.shaksternano.imperishableitems.common.network.ModNetworkHandler;
import com.shaksternano.imperishableitems.common.registry.ModEnchantments;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public final class ImperishableItems implements ModInitializer {

    public static final String MOD_ID = "imperishableitems";
    private static ModConfig config;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        ModEnchantments.registerEnchantments();
        ModNetworkHandler.registerGlobalReceivers();
        ModEvents.registerEvents();
    }

    public static ModConfig getConfig() {
        return config;
    }
}
