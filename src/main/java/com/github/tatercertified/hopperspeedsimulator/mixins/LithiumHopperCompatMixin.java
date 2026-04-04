package com.github.tatercertified.hopperspeedsimulator.mixins;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.tatercertified.hopperspeedsimulator.Main;
import com.github.tatercertified.hopperspeedsimulator.compat.LithiumHopperHelperUtils;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.caffeinemc.mods.lithium.common.hopper.LithiumStackList;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@IfModLoaded(value = "lithium")
@IfModAbsent(value = "omnihopper")
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
    private static int injectedItem(int x) {
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
    private static Object captureItemStackLocal(LithiumStackList instance, int i) {
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
    private static int injectedItem1(int x) {
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
    private static boolean redirectToUseMultipleItems(Container inventory, ItemStack stack, Direction direction) {
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
            at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/lithium/common/hopper/HopperHelper;tryMoveSingleItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/Direction;)Z")
    )
    private static boolean redirectToUseMultipleItems1(Container inventory, ItemStack stack, Direction direction) {
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
