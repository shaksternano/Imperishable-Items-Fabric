package com.shaksternano.imperishableitems.mixin;

import com.shaksternano.imperishableitems.ImperishableItems;
import com.shaksternano.imperishableitems.enchantments.ImperishableEnchantment;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    private AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    // A tool with imperishable at 0 durability in an anvil will not have "(Broken)" at the end if its name.
    @Redirect(method = "onSlotUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;getString()Ljava/lang/String;"))
    private String imperishableBrokenOnSlotUpdate(Text getName, ScreenHandler handler, int slotId, ItemStack stack) {
        String trimmedName = getName.getString();

        if (ImperishableItems.getConfig().imperishablePreventsBreaking) {
            trimmedName = ImperishableEnchantment.itemNameRemoveBroken(getName, stack);
        }

        return trimmedName;
    }

    // Putting "(Broken)" at the end of the name of a tool with Imperishable at 0 durability will register as a new name.
    @Redirect(method = "onRenamed", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;getString()Ljava/lang/String;"))
    private String imperishableBrokenOnRenamed(Text getName) {
        String trimmedName = getName.getString();

        if (ImperishableItems.getConfig().imperishablePreventsBreaking) {
            Slot slot = handler.getSlot(0);

            if (slot != null && slot.hasStack()) {
                ItemStack stack = slot.getStack();
                trimmedName = ImperishableEnchantment.itemNameRemoveBroken(getName, stack);
            }

        }

        return trimmedName;
    }
}