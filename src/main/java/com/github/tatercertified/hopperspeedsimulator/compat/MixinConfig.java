package com.github.tatercertified.hopperspeedsimulator.compat;

import com.github.tatercertified.hopperspeedsimulator.Main;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinConfig implements IMixinConfigPlugin {
    private boolean lithiumPresent = false;
    private boolean omnihopperPresent = false;

    @Override
    public void onLoad(String mixinPackage) {
        this.lithiumPresent = FabricLoader.getInstance().isModLoaded("lithium");
        this.omnihopperPresent = FabricLoader.getInstance().isModLoaded("omnihopper");
        if (this.lithiumPresent) {
            Main.LOGGER.info("Lithium has been detected, deploying Mixins to save ourselves");
        }
        if (this.omnihopperPresent) {
            Main.LOGGER.info("Omnihopper has been detected, deploying Mixins to save ourselves");
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.contains("Lithium")) {
            return lithiumPresent && !omnihopperPresent;
        }
        if (mixinClassName.contains("Omnihopper")) {
            return omnihopperPresent;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
