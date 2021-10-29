package com.shaksternano.imperishableitems.common.network;

import com.shaksternano.imperishableitems.common.ImperishableItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public final class ModNetworkHandler {

    private ModNetworkHandler() {}

    public static final Identifier EQUIPMENT_BREAK_EFFECTS = new Identifier(ImperishableItems.MOD_ID, "equipment_break_effects");

    @Environment(EnvType.CLIENT)
    public static void registerClientGlobalReceivers() {
        // Plays item break effects.
        ClientPlayNetworking.registerGlobalReceiver(EQUIPMENT_BREAK_EFFECTS, (client, handler, buf, responseSender) -> {
            if (client.player != null) {
                int itemId = buf.readInt();
                ItemStack stack = new ItemStack(Item.byRawId(itemId));

                client.execute(() -> client.player.playEquipmentBreakEffects(stack));
            }
        });
    }
}