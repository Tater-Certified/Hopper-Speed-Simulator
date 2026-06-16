/**
 * Copyright (c) 2026 QPCrummer
 * This project is Licensed under <a href="https://github.com/Tater-Certified/Hopper-Speed-Simulator/blob/main/LICENSE">MIT</a>
 */
package com.github.tatercertified.hopperspeedsimulator.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.tatercertified.hopperspeedsimulator.Main;
import com.github.tatercertified.hopperspeedsimulator.compat.LithiumHopperHelperUtils;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.caffeinemc.mods.lithium.common.hopper.LithiumStackList;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@IfModLoaded(value = "lithium")
@Mixin(value = HopperBlockEntity.class, priority = 1500)
public abstract class LithiumHopperCompatMixin {
    private static int transferAmount;
    private static final ThreadLocal<ItemStack> STACK = ThreadLocal.withInitial(() -> ItemStack.EMPTY);

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumExtract"
    )
    @ModifyArg(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"),
            index = 0
    )
    private static int hopperspeedsim$injectedItem(int x) {
        return transferAmount;
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumExtract"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/lithium/common/hopper/LithiumStackList;get(I)Ljava/lang/Object;", ordinal = 0)
    )
    private static Object hopperspeedsim$captureItemStackLocal(LithiumStackList instance, int i) {
        ItemStack stack1 = instance.get(i);
        STACK.set(stack1);
        return stack1;
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumExtract"
    )
    @ModifyArg(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;removeItem(II)Lnet/minecraft/world/item/ItemStack;"),
            index = 1
    )
    private static int hopperspeedsim$injectedItem1(int x) {
        transferAmount = Math.min(STACK.get().getCount(), Main.items);
        STACK.remove();
        return transferAmount;
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumExtract"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/lithium/common/hopper/HopperHelper;tryMoveSingleItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Z")
    )
    private static boolean hopperspeedsim$redirectToUseMultipleItems(Container inventory, ItemStack stack, Direction direction) {
        return LithiumHopperHelperUtils.tryMoveMultipleItems(inventory, stack, direction, transferAmount);
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumInsert",
            prefix = "handler"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z")
    )
    private static boolean hopperspeedsim$redirectIsEmpty(ItemStack stack) {
        if (!stack.isEmpty()) {
            transferAmount = Math.min(stack.getCount(), Main.items);
            return false;
        } else {
            return true;
        }
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumInsert",
            prefix = "handler"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/lithium/common/hopper/HopperHelper;tryMoveSingleItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Z")
    )
    private static boolean hopperspeedsim$redirectToUseMultipleItems1(Container inventory, ItemStack stack, Direction direction) {
        return LithiumHopperHelperUtils.tryMoveMultipleItems(inventory, stack, direction, transferAmount);
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumInsert",
            prefix = "handler"
    )
    @ModifyConstant(
            method = "@MixinSquared:Handler",
            constant = @Constant(intValue = 8) )
    private static int hopperspeedsim$redirectFullValue(int x) {
        return Main.ticks;
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumInsert",
            prefix = "handler"
    )
    @ModifyConstant(
            method = "@MixinSquared:Handler",
            constant = @Constant(intValue = 7) )
    private static int hopperspeedsim$redirectLessValue(int x) {
        return Main.ticks - 1;
    }
}
