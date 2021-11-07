package io.github.shaksternano.imperishableitems.common;

import io.github.shaksternano.imperishableitems.common.config.ImperishableBlacklistsConfig;
import io.github.shaksternano.imperishableitems.common.config.ModConfig;
import io.github.shaksternano.imperishableitems.common.event.ModEvents;
import io.github.shaksternano.imperishableitems.common.network.ModNetworking;
import io.github.shaksternano.imperishableitems.common.registry.ModEnchantments;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public final class ImperishableItems implements ModInitializer {

    public static final String MOD_ID = "imperishableitems";
    private static ModConfig config;
    private static ImperishableBlacklistsConfig blacklists;

    @Override
    public void onInitialize() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
        config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        AutoConfig.register(ImperishableBlacklistsConfig.class, JanksonConfigSerializer::new);
        blacklists = AutoConfig.getConfigHolder(ImperishableBlacklistsConfig.class).getConfig();

        ModEnchantments.registerEnchantments();
        ModNetworking.registerServerReceivers();
        ModEvents.registerEvents();
    }

    public static ModConfig getConfig() {
        return config;
    }

    public static ImperishableBlacklistsConfig getBlacklists() {
        return blacklists;
    }
}
