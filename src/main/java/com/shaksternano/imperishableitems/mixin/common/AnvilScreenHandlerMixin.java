package com.shaksternano.imperishableitems.mixin.common;

import com.shaksternano.imperishableitems.common.api.ImperishableProtection;
import com.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    private AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    // Removing "(Broken)" at the end of the name of an item with Imperishable at 0 durability in an anvil will not register as renamed.
    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;getString()Ljava/lang/String;"))
    private String imperishableBrokenUpdateResult(Text getName) {
        String trimmedName = getName.getString();
        ItemStack stack = input.getStack(0);

        if (ImperishableProtection.isItemProtected(stack, ImperishableProtection.ProtectionType.BREAK_PROTECTION)) {
            trimmedName = ImperishableEnchantment.itemNameRemoveBroken(getName, stack);
        }

        return trimmedName;
    }
}
