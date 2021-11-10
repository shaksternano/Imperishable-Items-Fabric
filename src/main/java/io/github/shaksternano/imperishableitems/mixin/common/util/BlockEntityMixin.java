package io.github.shaksternano.imperishableitems.mixin.common.util;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import io.github.shaksternano.imperishableitems.common.access.BlockEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
abstract class BlockEntityMixin implements BlockEntityAccess {

    private NbtElement imperishableItemsEnchantments;
    private Integer imperishableItemsRepairCost;

    // Adds enchantments and repair cost to NBT, for example this will allow the enchantments and repair cost to be shown when the /data command is used.
    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void getEnchantmentsForNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (ImperishableItems.getConfig().blockEntitiesStoreEnchantments) {
            if (imperishableItemsEnchantments != null) {
                nbt.put("Enchantments", imperishableItemsEnchantments);
            }

            if (imperishableItemsRepairCost != null) {
                nbt.putInt("RepairCost", imperishableItemsRepairCost);
            }
        }
    }

    // Sets the enchantments and repair cost from NBT, for example when the enchantments and repair cost are specified when using the /setblock command.
    @Inject(method = "fromTag", at = @At("TAIL"))
    private void setEnchantmentsFromNbt(BlockState state, NbtCompound tag, CallbackInfo ci) {
        if (ImperishableItems.getConfig().blockEntitiesStoreEnchantments) {
            NbtElement enchantments = tag.get("Enchantments");
            if (enchantments != null) {
                this.imperishableItemsEnchantments = enchantments.copy();
            }

            if (tag.contains("RepairCost", 3)) {
                imperishableItemsRepairCost = tag.getInt("RepairCost");
            }
        }
    }

    @Override
    public NbtElement getImperishableItemsEnchantments() {
        return imperishableItemsEnchantments;
    }

    @Override
    public void setImperishableItemsEnchantments(NbtElement enchantments) {
        this.imperishableItemsEnchantments = enchantments.copy();
    }

    @Override
    public Integer getImperishableItemsRepairCost() {
        return imperishableItemsRepairCost;
    }

    @Override
    public void setImperishableItemsRepairCost(Integer repairCost) {
        this.imperishableItemsRepairCost = repairCost;
    }
}
