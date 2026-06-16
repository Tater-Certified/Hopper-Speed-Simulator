/**
 * Copyright (c) 2026 QPCrummer
 * This project is Licensed under <a href="https://github.com/Tater-Certified/Hopper-Speed-Simulator/blob/main/LICENSE">MIT</a>
 */
package com.github.tatercertified.hopperspeedsimulator.mixin;

import static com.github.tatercertified.hopperspeedsimulator.Main.ticks;
import static net.minecraft.world.level.block.entity.HopperBlockEntity.addItem;

import com.github.tatercertified.hopperspeedsimulator.Main;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperMixin {

    @Shadow private int cooldownTime;

    @ModifyArg(method = "tryMoveItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;setCooldown(I)V"), index = 0)
    private static int hopperspeedsim$inject(int x) {
        return Main.ticks;
    }

    @ModifyArg(method = "tryTakeInItemFromSlot(Lnet/minecraft/world/level/block/entity/Hopper;Lnet/minecraft/world/Container;ILnet/minecraft/core/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;removeItem(II)Lnet/minecraft/world/item/ItemStack;"), index = 1)
        private static int hopperspeedsim$inject2(int x) {
        return Main.items;
    }

    @ModifyArg(method = "ejectItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;removeItem(II)Lnet/minecraft/world/item/ItemStack;"), index = 1)
    private static int hopperspeedsim$inject3(int x) {
        return Main.items;
    }

    @Inject(method = "ejectItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;getItem(I)Lnet/minecraft/world/item/ItemStack;"), cancellable = true)
    private static void hopperspeedsim$preventDuplication(Level level, BlockPos blockPos, HopperBlockEntity self, CallbackInfoReturnable<Boolean> cir, @Local(name = "slot") int slot, @Local(name = "container") Container container, @Local(name = "direction") Direction direction) {
        if (Main.items > 1) {
            ItemStack toMove = self.getItem(slot).split(Main.items);
            ItemStack leftovers = addItem(self, container, toMove, direction);

            if (!leftovers.isEmpty()) {
                self.getItem(slot).grow(leftovers.getCount());
                cir.setReturnValue(false);
            } else {
                container.setChanged();
                cir.setReturnValue(true);
            }
        }
    }

    @ModifyArg(method = "tryMoveInItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;setCooldown(I)V"), index = 0)
    private static int hopperspeedsim$inject4(int x) {
        return Main.ticks;
    }

    /**
     * @author QPCrummer
     * @reason If you decide to use Hopper Slow Simulator, we need to make sure it works
     */
    @Overwrite
    private boolean isOnCustomCooldown() {
        return this.cooldownTime > ticks;
    }
}
