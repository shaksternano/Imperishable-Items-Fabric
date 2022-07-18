package io.github.shaksternano.imperishableitems.mixin.common.enchantment.imperishable;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.shaksternano.imperishableitems.common.enchantment.ImperishableEnchantment;
import io.github.shaksternano.imperishableitems.common.util.ImperishableBlacklistsHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(AnvilScreenHandler.class)
abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    protected AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    // Removing "(Broken)" at the end of the name of an item with Imperishable at 0 durability in an anvil will not register as renamed.
    @ModifyExpressionValue(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;getString()Ljava/lang/String;"))
    private String imperishableBrokenUpdateResult(String name) {
        ItemStack stack = input.getStack(0);
        if (ImperishableBlacklistsHandler.isItemProtected(stack, ImperishableBlacklistsHandler.ProtectionType.BREAK_PROTECTION)) {
            return ImperishableEnchantment.itemNameRemoveBroken(name, stack);
        } else {
            return name;
        }
    }
}
