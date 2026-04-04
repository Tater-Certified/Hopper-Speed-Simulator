package com.github.tatercertified.hopperspeedsimulator.mixins;

import com.github.tatercertified.hopperspeedsimulator.Main;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static com.github.tatercertified.hopperspeedsimulator.Main.ticks;

@Mixin(HopperBlockEntity.class)
public abstract class HopperMixin {

    @Shadow private int cooldownTime;

    @ModifyArg(method = "tryMoveItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;setCooldown(I)V"), index = 0)
    private static int inject(int x) {
        return Main.ticks;
    }

    @ModifyArg(method = "tryTakeInItemFromSlot(Lnet/minecraft/world/level/block/entity/Hopper;Lnet/minecraft/world/Container;ILnet/minecraft/core/Direction;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;removeItem(II)Lnet/minecraft/world/item/ItemStack;"), index = 1)
        private static int inject2(int x) {
        return Main.items;
    }

    @ModifyArg(method = "ejectItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;removeItem(II)Lnet/minecraft/world/item/ItemStack;"), index = 1)
    private static int inject3(int x) {
        return Main.items;
    }

    @ModifyArg(method = "tryMoveInItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;setCooldown(I)V"), index = 0)
    private static int inject4(int x) {
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
