package io.github.shaksternano.imperishableitems.mixin.client;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import io.github.shaksternano.imperishableitems.common.network.ModNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.snooper.SnooperListener;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable> implements SnooperListener, WindowEventHandler {

    private MinecraftClientMixin(String string) {
        super(string);
    }

    @Shadow @Nullable public ClientPlayerEntity player;

    @SuppressWarnings("ConstantConditions")
    @Redirect(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD))
    private void debugModeKeypress(PlayerInventory getInventory, int i) {
        if (ImperishableItems.getConfig().debugMode) {
            PacketByteBuf buf;
            switch (i) {
                case 0:
                    // If debug mode is on and the hotbar slot 1 key is pressed, the item in the main hand will be enchanted with Imperishable. If the item is already enchanted with Imperishable, the Imperishable enchantment will be removed from the item.
                    buf = PacketByteBufs.create();
                    ClientPlayNetworking.send(ModNetworking.DEBUG_SET_IMPERISHABLE, buf);
                    break;
                case 1:
                    // If debug mode is on and the hotbar slot 2 key is pressed, the durability of the item in the main hand will be set to 1. If the durability is already 1, it will set it to full.
                    if (player.getMainHandStack().isDamageable()) {
                        buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(ModNetworking.DEBUG_SET_ONE_DURABILITY, buf);
                    }
                    break;
                case 2:
                    // If debug mode is on and the hotbar slot 3 key is pressed, the durability of the item in the main hand will be set to 0. If the durability is already 0, it will set it to full.
                    if (player.getMainHandStack().isDamageable()) {
                        buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(ModNetworking.DEBUG_SET_ZERO_DURABILITY, buf);
                    }
                    break;
                case 3:
                    // If debug mode is on and the hotbar slot 4 key is pressed, the durability of the item in the main hand will be decreased by 1 if the durability is not already 0.
                    if (player.getMainHandStack().isDamageable()) {
                        buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(ModNetworking.DEBUG_DECREMENT_DURABILITY, buf);
                    }
                    break;
                case 4:
                    // If debug mode is on and the hotbar slot 5 key is pressed, the durability of the item in the main hand will be increased by 1 if the durability is not already full.
                    if (player.getMainHandStack().isDamageable()) {
                        buf = PacketByteBufs.create();
                        ClientPlayNetworking.send(ModNetworking.DEBUG_INCREMENT_DURABILITY, buf);
                    }
                    break;
                }
        } else {
            getInventory.selectedSlot = i;
        }
    }
}
