/**
 * Copyright (c) 2026 QPCrummer
 * This project is Licensed under <a href="https://github.com/Tater-Certified/Hopper-Speed-Simulator/blob/main/LICENSE">MIT</a>
 */
package com.github.tatercertified.hopperspeedsimulator.mixin;

import com.github.tatercertified.hopperspeedsimulator.Main;
import net.minecraft.server.MinecraftServer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ModInitMixin {
    @Inject(method = "<clinit>", at = @At(value = "FIELD", target = "Lnet/minecraft/server/MinecraftServer;OVERLOADED_THRESHOLD_NANOS:J", opcode = Opcodes.PUTSTATIC))
    private static void hopperspeedsim$initMod(CallbackInfo ci) {
        Main.onInitialize();
    }
}
