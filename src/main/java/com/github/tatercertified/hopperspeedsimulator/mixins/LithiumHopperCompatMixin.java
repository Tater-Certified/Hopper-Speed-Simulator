package com.github.tatercertified.hopperspeedsimulator.mixins;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.tatercertified.hopperspeedsimulator.Main;
import com.github.tatercertified.hopperspeedsimulator.compat.LithiumHopperHelperUtils;
import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@IfModLoaded(value = "lithium")
@IfModAbsent(value = "omnihopper")
@Mixin(value = HopperBlockEntity.class, priority = 1500)
public abstract class LithiumHopperCompatMixin {
    private static int transferAmount;
    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumExtract"
    )
    @ModifyArg(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;increment(I)V"),
            index = 0
    )
    private static int injectedItem(int x) {
        return transferAmount;
    }


    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumExtract"
    )
    @ModifyArg(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;removeStack(II)Lnet/minecraft/item/ItemStack;"),
            index = 1
    )
    private static int injectedItem1(int x, @Local(ordinal = 0) ItemStack itemStack) {
        transferAmount = Math.min(itemStack.getCount(), Main.items);
        return transferAmount;
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumExtract"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/lithium/common/hopper/HopperHelper;tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Direction;)Z")
    )
    private static boolean redirectToUseMultipleItems(Inventory inventory, ItemStack stack, Direction direction) {
        return LithiumHopperHelperUtils.tryMoveMultipleItems(inventory, stack, direction, transferAmount);
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumInsert",
            prefix = "handler"
    )
    @Redirect(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z")
    )
    private static boolean redirectIsEmpty(ItemStack stack) {
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
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/lithium/common/hopper/HopperHelper;tryMoveSingleItem(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Direction;)Z")
    )
    private static boolean redirectToUseMultipleItems1(Inventory inventory, ItemStack stack, Direction direction) {
        return LithiumHopperHelperUtils.tryMoveMultipleItems(inventory, stack, direction, transferAmount);
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumInsert",
            prefix = "handler"
    )
    @ModifyConstant(
            method = "@MixinSquared:Handler",
            constant = @Constant(intValue = 8)
    )
    private static int redirectFullValue(int x) {
        return Main.ticks;
    }

    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.block.hopper.HopperBlockEntityMixin",
            name = "lithiumInsert",
            prefix = "handler"
    )
    @ModifyConstant(
            method = "@MixinSquared:Handler",
            constant = @Constant(intValue = 7)
    )
    private static int redirectLessValue(int x) {
        return Main.ticks - 1;
    }
}
