package io.github.shakternano.imperishableitems.client;

import io.github.shakternano.imperishableitems.common.event.ModEvents;
import io.github.shakternano.imperishableitems.common.network.ModNetworking;
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
