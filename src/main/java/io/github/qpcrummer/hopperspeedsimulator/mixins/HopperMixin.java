package io.github.qpcrummer.hopperspeedsimulator.mixins;

import io.github.qpcrummer.hopperspeedsimulator.Main;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import static io.github.qpcrummer.hopperspeedsimulator.Main.*;

@Mixin(HopperBlockEntity.class)
public abstract class HopperMixin {

    @Shadow
    public static void serverTick(World world, BlockPos pos, BlockState state, HopperBlockEntity blockEntity) {
    }

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
    //TODO Actually make it work
    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;insertAndExtract(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/HopperBlockEntity;Ljava/util/function/BooleanSupplier;)Z"))
    private static boolean serverTickRedirect(World world, BlockPos pos, BlockState state, HopperBlockEntity blockEntity, BooleanSupplier booleanSupplier) {
        if (first) {
            if (unsafe) {
                first = false;
                update.scheduleAtFixedRate(() -> serverTick(world, pos, state, blockEntity), 0,1, TimeUnit.SECONDS);
            } else {
                update.shutdownNow();
                first = true;
            }
        } else {
            serverTick(world, pos, state, blockEntity);
        }
        return true;
    }
    private static final ScheduledExecutorService update = Executors.newScheduledThreadPool(1);
}
