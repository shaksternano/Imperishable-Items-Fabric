package com.shaksternano.imperishableitems.mixin.client;

import com.mojang.authlib.GameProfile;
import com.shaksternano.imperishableitems.common.ImperishableItems;
import com.shaksternano.imperishableitems.common.network.ModNetworking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

    private ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    // If debug mode is on, the item in the main hand will be enchanted with Imperishable instead of it being dropped when the drop item is pressed. If the item already has Imperishable, the Imperishable enchantment will be removed from the item.
    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    private void debugDropSetImperishable(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        if (ImperishableItems.getConfig().debugMode) {
            PacketByteBuf buf = PacketByteBufs.create();
            ClientPlayNetworking.send(ModNetworking.DEBUG_DROP_SET_IMPERISHABLE, buf);
            cir.setReturnValue(true);
        }
    }
}
