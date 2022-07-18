package io.github.shaksternano.imperishableitems.mixin.common.util;

import io.github.shaksternano.imperishableitems.common.ImperishableItems;
import io.github.shaksternano.imperishableitems.common.access.BlockEntityAccess;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
abstract class BlockEntityMixin implements BlockEntityAccess {

    @Unique
    private static final String IMPERISHABLE_ITEMS$ENCHANTMENTS_NBT_KEY = "Enchantments";
    @Unique
    private static final String IMPERISHABLE_ITEMS$REPAIR_COST_NBT_KEY = "RepairCost";

    private NbtElement imperishableItemsEnchantments;
    private Integer imperishableItemsRepairCost;

    // Adds enchantments and repair cost to NBT, for example this will allow the enchantments and repair cost to be shown when the /data command is used.
    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void imperishableItems$getEnchantmentsForNbt(NbtCompound nbt, CallbackInfo ci) {
        if (ImperishableItems.getConfig().blockEntitiesStoreEnchantments) {
            if (imperishableItemsEnchantments != null) {
                nbt.put(IMPERISHABLE_ITEMS$ENCHANTMENTS_NBT_KEY, imperishableItemsEnchantments);
            }

            if (imperishableItemsRepairCost != null) {
                nbt.putInt(IMPERISHABLE_ITEMS$REPAIR_COST_NBT_KEY, imperishableItemsRepairCost);
            }
        }
    }

    // Sets the enchantments and repair cost from NBT, for example when the enchantments and repair cost are specified when using the /setblock command.
    @Inject(method = "readNbt", at = @At("HEAD"))
    private void imperishableItems$setEnchantmentsFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (ImperishableItems.getConfig().blockEntitiesStoreEnchantments) {
            if (nbt.contains(IMPERISHABLE_ITEMS$ENCHANTMENTS_NBT_KEY)) {
                NbtElement enchantments = nbt.get(IMPERISHABLE_ITEMS$ENCHANTMENTS_NBT_KEY);
                if (enchantments != null) {
                    imperishableItemsEnchantments = enchantments.copy();
                }
            }

            if (nbt.contains(IMPERISHABLE_ITEMS$REPAIR_COST_NBT_KEY, NbtElement.INT_TYPE)) {
                imperishableItemsRepairCost = nbt.getInt(IMPERISHABLE_ITEMS$REPAIR_COST_NBT_KEY);
            }
        }
    }

    @Unique
    @Override
    public NbtElement imperishableItems$getImperishableItemsEnchantments() {
        return imperishableItemsEnchantments;
    }

    @Unique
    @Override
    public void imperishableItems$setImperishableItemsEnchantments(NbtElement enchantments) {
        this.imperishableItemsEnchantments = enchantments.copy();
    }

    @Unique
    @Override
    public Integer imperishableItems$getImperishableItemsRepairCost() {
        return imperishableItemsRepairCost;
    }

    @Unique
    @Override
    public void imperishableItems$setImperishableItemsRepairCost(Integer repairCost) {
        this.imperishableItemsRepairCost = repairCost;
    }
}
