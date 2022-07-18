package io.github.shaksternano.imperishableitems.mixin.client.util;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import io.github.shaksternano.imperishableitems.common.network.ModNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(value = MinecraftClient.class, priority = 0)
abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @SuppressWarnings("ConstantConditions")
    @Redirect(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD), require = 0)
    private void debugModeKeypress(PlayerInventory getInventory, int i) {
        if (ImperishableItems.getConfig().debugMode) {
            switch (i) {
                case 0 -> {
                    // If debug mode is on and the hotbar slot 1 key is pressed, the item in the main hand will be enchanted with Imperishable instead of it being dropped. If the item already has Imperishable, the Imperishable enchantment will be removed from the item.
                    PacketByteBuf buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(ModNetworking.DEBUG_SET_IMPERISHABLE, buf);
                }
                case 1 -> {
                    // If debug mode is on and the hotbar slot 2 key is pressed, the durability of the item in the main hand will be set to 1. If the durability is already 1, it will set it to full.
                    if (player.getMainHandStack().isDamageable()) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(ModNetworking.DEBUG_SET_ONE_DURABILITY, buf);
                    }
                }
                case 2 -> {
                    // If debug mode is on and the hotbar slot 3 key is pressed, the durability of the item in the main hand will be set to 0. If the durability is already 0, it will set it to full.
                    if (player.getMainHandStack().isDamageable()) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(ModNetworking.DEBUG_SET_ZERO_DURABILITY, buf);
                    }
                }
                case 3 -> {
                    // If debug mode is on and the hotbar slot 4 key is pressed, the durability of the item in the main hand will be decreased by 1 if the durability is not already 0.
                    if (player.getMainHandStack().isDamageable()) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(ModNetworking.DEBUG_DECREMENT_DURABILITY, buf);
                    }
                }
                case 4 -> {
                    // If debug mode is on and the hotbar slot 5 key is pressed, the durability of the item in the main hand will be increased by 1 if the durability is not already full.
                    if (player.getMainHandStack().isDamageable()) {
                        PacketByteBuf buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(ModNetworking.DEBUG_INCREMENT_DURABILITY, buf);
                    }
                }
            }
        } else {
            getInventory.selectedSlot = i;
        }
    }
}
