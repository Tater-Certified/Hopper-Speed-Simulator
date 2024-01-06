package com.github.tatercertified.hopperspeedsimulator.mixins;

import com.github.tatercertified.hopperspeedsimulator.Main;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.github.tatercertified.hopperspeedsimulator.Main.items;
import static com.github.tatercertified.hopperspeedsimulator.Main.ticks;

@Mixin(HopperBlockEntity.class)
public abstract class HopperMixin {

    @Shadow private int transferCooldown;

    @ModifyArg(method = "insertAndExtract", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"), index = 0)
    private static int inject(int x) {
        return Main.ticks;
    }

    @ModifyArg(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;removeStack(II)Lnet/minecraft/item/ItemStack;"), index = 1)
        private static int inject2(int x) {
        return Main.items;
    }

    @ModifyArg(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;removeStack(II)Lnet/minecraft/item/ItemStack;"), index = 1)
    private static int inject3(int x) {
        return Main.items;
    }

    @ModifyArg(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"), index = 0)
    private static int inject4(int x) {
        return Main.ticks;
    }

    /**
     * @author QPCrummer
     * @reason If you decide to use Hopper Slow Simulator, we need to make sure it works
     */
    @Overwrite
    private boolean isDisabled() {
        return this.transferCooldown > ticks;
    }
}
