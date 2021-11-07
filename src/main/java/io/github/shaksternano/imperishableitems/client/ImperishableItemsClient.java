package io.github.shaksternano.imperishableitems.client;

import io.github.shaksternano.imperishableitems.common.event.ModEvents;
import io.github.shaksternano.imperishableitems.common.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class ImperishableItemsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModNetworking.registerClientReceivers();
        ModEvents.registerClientEvents();
    }
}
