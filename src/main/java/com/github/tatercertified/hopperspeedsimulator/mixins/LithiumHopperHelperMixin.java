package com.github.tatercertified.hopperspeedsimulator.mixins;

import com.github.tatercertified.hopperspeedsimulator.Main;
import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = HopperHelper.class, remap = false)
public class LithiumHopperHelperMixin {
    @ModifyArg(method = "tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;split(I)Lnet/minecraft/item/ItemStack;"), index = 0)
    private static int injectedItems(int x) {
        return Main.items;
    }

    @ModifyArg(method = "tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"), index = 0)
    private static int injectedItems1(int x) {
        return Main.items;
    }

    @ModifyArg(method = "tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/SidedInventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;increment(I)V"), index = 0)
    private static int injectedItems2(int x) {
        return Main.items;
    }
}
