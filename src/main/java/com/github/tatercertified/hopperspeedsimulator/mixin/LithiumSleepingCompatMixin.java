/**
 * Copyright (c) 2026 QPCrummer
 * This project is Licensed under <a href="https://github.com/Tater-Certified/Hopper-Speed-Simulator/blob/main/LICENSE">MIT</a>
 */
package com.github.tatercertified.hopperspeedsimulator.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.tatercertified.hopperspeedsimulator.Main;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@IfModLoaded(value = "lithium")
@Mixin(value = HopperBlockEntity.class, priority = 1500, remap = false)
public abstract class LithiumSleepingCompatMixin {
    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.world.block_entity_ticking.sleeping.hopper.HopperBlockEntityMixin",
            name = "wakeUpOnCooldownSet",
            prefix = "handler"
    )
    @ModifyConstant(
            method = "@MixinSquared:Handler",
            constant = @Constant(intValue = 7) )
    private int hopperspeedsim$injected(int value) {
        int newInt = Main.ticks - 1;
        return Math.max(newInt, 0);
    }
}
