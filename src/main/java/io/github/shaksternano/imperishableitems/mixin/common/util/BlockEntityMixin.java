package io.github.shaksternano.imperishableitems.mixin.common.util;

import io.github.shaksternano.imperishableitems.common.access.BlockEntityAccess;
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

    private BlockEntityMixin() {}

    private NbtElement enchantments;
    private Integer repairCost;

    // Adds enchantments and repair cost to NBT, for example this will allow the enchantments and repair cost to be shown when the /data command is used.
    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void getEnchantmentsForNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (enchantments != null) {
            nbt.put("Enchantments", enchantments);
        }

        if (repairCost != null) {
            nbt.putInt("RepairCost", repairCost);
        }
    }

    // Sets the enchantments and repair cost from NBT, for example when the enchantments and repair cost are specified when using the /setblock command.
    @Inject(method = "readNbt", at = @At("TAIL"))
    private void setEnchantmentsFromNbt(NbtCompound nbt, CallbackInfo ci) {
        NbtElement enchantments = nbt.get("Enchantments");
        if (enchantments != null) {
            this.enchantments = enchantments.copy();
        }

        if (nbt.contains("RepairCost", 3)) {
            repairCost = nbt.getInt("RepairCost");
        }
    }

    @Override
    public NbtElement getEnchantments() {
        return enchantments;
    }

    @Override
    public void setEnchantments(NbtElement enchantments) {
        this.enchantments = enchantments.copy();
    }

    @Override
    public Integer getRepairCost() {
        return repairCost;
    }

    @Override
    public void setRepairCost(Integer repairCost) {
        this.repairCost = repairCost;
    }
}
