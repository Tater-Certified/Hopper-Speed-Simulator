package io.github.qpcrummer.hopperspeedsimulator.mixins;

import io.github.qpcrummer.hopperspeedsimulator.Main;
import net.minecraft.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(HopperBlockEntity.class)
public abstract class HopperMixin {

    @ModifyArg(method = "insertAndExtract", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/HopperBlockEntity;setTransferCooldown(I)V"), index = 0)
    private static int inject(int x) {
        return Main.ticks;
    }
}
