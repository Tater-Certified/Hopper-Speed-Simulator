package com.github.tatercertified.hopperspeedsimulator.mixins;

import com.github.tatercertified.hopperspeedsimulator.Main;
import net.minecraft.block.BlockState;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.HopperBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = HopperBehaviour.class, remap = false)
public class OmnihopperBehaviorMixin {
    /**
     * @author QPCrummer
     * @reason We have our own cooldown
     */
    @Overwrite
    public int getCooldown() {
        return Main.ticks;
    }

    /**
     * @author QPCrummer
     * @reason We have our own item count
     */
    @Overwrite
    public long getAmountPerActivation(BlockState targetState) {
        return Main.items;
    }
}
