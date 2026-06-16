/**
 * Copyright (c) 2026 QPCrummer
 * This project is Licensed under <a href="https://github.com/Tater-Certified/Hopper-Speed-Simulator/blob/main/LICENSE">MIT</a>
 */
package com.github.tatercertified.hopperspeedsimulator;

import com.bawnorton.mixinsquared.MixinSquaredBootstrap;
import com.moulberry.mixinconstraints.MixinConstraints;
import com.moulberry.mixinconstraints.mixin.MixinConstraintsBootstrap;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinPluginConfig implements IMixinConfigPlugin {

    private String mixinPackage;

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (this.mixinPackage != null && !mixinClassName.startsWith(this.mixinPackage)) {
            return true;
        }
        return MixinConstraints.shouldApplyMixin(mixinClassName);
    }

    @Override
    public void onLoad(String mixinPackage) {
        this.mixinPackage = mixinPackage;
        MixinSquaredBootstrap.init();
        MixinConstraintsBootstrap.init(mixinPackage);
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }


    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        MixinSquaredBootstrap.reOrderExtensions();
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
