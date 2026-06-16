# Hopper-Speed-Simulator
*Storage tech is my passion*

This mod allows you to easily change the speed of hoppers with the simple command /hopperspeed

Commands:
```
/hopperspeed                    - gives the current speed
/hopperspeed default            - defaults to the vanilla speed
/hopperspeed ticks-per-transfer - sets the transfer delay to the specified number of ticks
/hopperspeed items-per-transfer - sets the number of items per transfer attempt
```

Compatible with Lithium, FabricAutoCrafter, and many other mods

This mod is *not* compatible with mods that add hoppers with their own item moving logic

![](example_images/fasthopper.gif)

# Supported Platforms (As of v2.0.0)
- Fabric/Quilt (1.14.3 - 26.2)
- Forge (26.2 - )
- NeoForge (26.2 - )
- PaperMC/Spigot/Folia (26.2 - )
- Sponge (26.2 - )

# Installation
## Fabric, Quilt, Forge, NeoForge
Simply put the mod in the mods folder
## Sponge
Simply put the plugin in the plugins folder
## Spigot/PaperMC
1. Install the [Ignite](https://github.com/vectrix-space/ignite) Mixin loader
2. Run the ignite jar alongside the paper/spigot jar
3. Put the mod in the mods folder and restart
## Folia
1. Install the [Ignite](https://github.com/vectrix-space/ignite) Mixin loader
2. Rename the Folia jar to "paper.jar". Alternatively, you can launch the game with the following JVM args: `-Dignite.locator=paper -Dignite.jar=./folia.jar`
3. Run the ignite jar alongside the folia jar
4. Put the mod in the mods folder and restart
