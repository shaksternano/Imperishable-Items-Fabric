package com.shaksternano.imperishableitems.client;

import com.shaksternano.imperishableitems.common.network.ModNetworkHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ImperishableItemsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModNetworkHandler.registerClientGlobalReceivers();
    }
}