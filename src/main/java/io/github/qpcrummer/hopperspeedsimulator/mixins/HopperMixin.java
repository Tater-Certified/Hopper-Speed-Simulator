package io.github.qpcrummer.hopperspeedsimulator.mixins;

import io.github.qpcrummer.hopperspeedsimulator.Main;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static io.github.qpcrummer.hopperspeedsimulator.Main.items;
import static io.github.qpcrummer.hopperspeedsimulator.Main.ticks;

@Mixin(HopperBlockEntity.class)
public abstract class HopperMixin {

    @ModifyArg(method = "insertAndExtract", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"), index = 0)
    private static int inject(int x) {
        return Main.ticks;
    }

    @ModifyArg(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;removeStack(II)Lnet/minecraft/item/ItemStack;"), index = 1)
        private static int inject2(int x) {
        return items;
    }

    @ModifyArg(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;removeStack(II)Lnet/minecraft/item/ItemStack;"), index = 1)
    private static int inject3(int x) {
        return items;
    }

    @ModifyArg(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"), index = 0)
    private static int inject4(int x) {
        return ticks;
    }
}
