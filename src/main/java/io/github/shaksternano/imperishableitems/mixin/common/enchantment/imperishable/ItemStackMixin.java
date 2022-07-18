package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import io.github.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import io.github.shaksternano.imperishableitems.common.network.ModNetworking;
import io.github.shaksternano.imperishableitems.common.util.ImperishableBlacklistsHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean isDamageable();

    @Shadow
    public abstract int getDamage();

    @Shadow
    public abstract void setDamage(int damage);

    @Shadow
    public abstract int getMaxDamage();

    // Items don't break when they reach 0 durability.
    @Inject(method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"), cancellable = true)
    private void imperishableItems$imperishableDurability(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (ImperishableBlacklistsHandler.isItemProtected((ItemStack) (Object) this, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
            if (!(getItem() instanceof ElytraItem)) {
                if (isDamageable()) {
                    if (ImperishableEnchantment.hasImperishable((ItemStack) (Object) this) || !ImperishableItems.getConfig().enchantmentNeededToPreventBreaking) {
                        int newDamage = getDamage() + amount;
                        if (newDamage > getMaxDamage()) {
                            setDamage(getMaxDamage());
                        } else {
                            if (player != null) {
                                if (getDamage() < getMaxDamage() && newDamage == getMaxDamage()) {
                                    int itemId = Item.getRawId(getItem());
                                    PacketByteBuf buf = PacketByteBufs.create();
                                    buf.writeInt(itemId);
                                    ServerPlayNetworking.send(player, ModNetworking.EQUIPMENT_BREAK_EFFECTS, buf);
                                }
                            }

                            setDamage(newDamage);
                        }

                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }

    // Tool specific drops such cobblestone do not drop when mined by a tool with Imperishable at 0 durability.
    @Inject(method = "isSuitableFor", at = @At("HEAD"), cancellable = true)
    private void imperishableItems$imperishableSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (ImperishableBlacklistsHandler.isItemProtected((ItemStack) (Object) this, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
            if (ImperishableEnchantment.isBrokenImperishable((ItemStack) (Object) this)) {
                cir.setReturnValue(false);
            }
        }
    }

    // Tools with Imperishable do not have increased mining speed when at 0 durability.
    @Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
    private void imperishableItems$imperishableNoDurabilitySpeed(BlockState state, CallbackInfoReturnable<Float> cir) {
        if (ImperishableBlacklistsHandler.isItemProtected((ItemStack) (Object) this, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
            if (ImperishableEnchantment.isBrokenImperishable((ItemStack) (Object) this)) {
                cir.setReturnValue(1.0F);
            }
        }
    }

    // Items with Imperishable do not give bonus attributes such as attack damage on a sword when at 0 durability.
    @Inject(method = "getAttributeModifiers", at = @At("HEAD"), cancellable = true)
    private void imperishableItems$imperishableAttributeModifiers(EquipmentSlot equipmentSlot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
        if (ImperishableBlacklistsHandler.isItemProtected((ItemStack) (Object) this, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
            if (ImperishableEnchantment.isBrokenImperishable((ItemStack) (Object) this)) {
                cir.setReturnValue(ImmutableMultimap.of());
            }
        }
    }

    // Item specific right click block actions are cancelled if the item has Imperishable and is at 0 durability.
    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void imperishableItems$imperishableUseOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (ImperishableBlacklistsHandler.isItemProtected((ItemStack) (Object) this, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
            PlayerEntity player = context.getPlayer();
            boolean userIsCreative = false;
            if (player != null) {
                userIsCreative = player.isCreative();
            }

            if (!userIsCreative) {
                if (ImperishableEnchantment.isBrokenImperishable((ItemStack) (Object) this)) {
                    cir.setReturnValue(ActionResult.PASS);
                }
            }
        }
    }

    // Item specific right click entity are cancelled if the item has Imperishable and is at 0 durability.
    @Inject(method = "useOnEntity", at = @At("HEAD"), cancellable = true)
    private void imperishableItems$imperishableUseOnEntity(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (ImperishableBlacklistsHandler.isItemProtected((ItemStack) (Object) this, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
            if (!user.isCreative()) {
                if (ImperishableEnchantment.isBrokenImperishable((ItemStack) (Object) this)) {
                    cir.setReturnValue(ActionResult.PASS);
                }
            }
        }
    }

    // Adds "(Broken)" to the name of an item with Imperishable at 0 durability.
    @Inject(method = "getName", at = @At("RETURN"), cancellable = true)
    private void imperishableItems$imperishableBrokenName(CallbackInfoReturnable<Text> cir) {
        if (ImperishableBlacklistsHandler.isItemProtected((ItemStack) (Object) this, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
            if (ImperishableEnchantment.isBrokenImperishable((ItemStack) (Object) this)) {
                MutableText broken = Text.translatable("item.name." + ImperishableEnchantment.TRANSLATION_KEY + ".broken");
                broken.formatted(Formatting.RED);

                cir.setReturnValue(((MutableText) cir.getReturnValue()).append(broken));
            }
        }
    }
}
