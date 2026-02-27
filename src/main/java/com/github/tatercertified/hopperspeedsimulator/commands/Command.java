package com.github.tatercertified.hopperspeedsimulator.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.function.Predicate;

import static com.github.tatercertified.hopperspeedsimulator.Main.*;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class Command {
    private static final String PERMISSION_PREFIX = "hopperspeed.command.";
    private static final int PERMISSION_DEFAULT_OP_LEVEL_USE = 0;
    private static final int PERMISSION_DEFAULT_OP_LEVEL_EDIT = 2;

    public static void registerCommand() throws FileNotFoundException {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> dispatcher.register(CommandManager.literal("hopperspeed")
                        .requires(perm("use", PERMISSION_DEFAULT_OP_LEVEL_USE))
                        .executes(context -> {
                            context.getSource().sendFeedback(() -> Text.of("Current speed is "+ ticks +" ticks, with "+ items +" items per transfer"), true);
                            return 1;
                        })

                .then(literal("ticks-per-transfer")
                        .requires(perm("ticks-per-transfer", PERMISSION_DEFAULT_OP_LEVEL_EDIT))
                        .then(argument("ticks-per-transfer", IntegerArgumentType.integer()).executes(context -> {
                    properties.setProperty("ticks-per-transfer", String.valueOf(IntegerArgumentType.getInteger(context, "ticks-per-transfer")));
                    try (OutputStream output = Files.newOutputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
                        properties.store(output, null);
                    } catch (IOException e) {
                        LOGGER.warn("Failed to save ticks-per-transfer configuration", e);
                    }
                    ticks = Integer.parseInt(properties.getProperty("ticks-per-transfer"));
                    context.getSource().sendFeedback(() -> Text.of("Hoppers will now transfer items every "+ ticks +" ticks"), true);
                    return 1;
                })))

                .then(literal("default")
                        .requires(perm("default", PERMISSION_DEFAULT_OP_LEVEL_EDIT))
                        .executes(context -> {
                    properties.setProperty("ticks-per-transfer", "8");
                    properties.setProperty("items-per-transfer", "1");
                    try (OutputStream output = Files.newOutputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
                        properties.store(output, null);
                    } catch (IOException e) {
                        LOGGER.warn("Failed to save items-per-transfer configuration", e);
                    }
                    ticks = Integer.parseInt(properties.getProperty("ticks-per-transfer"));
                    items = Integer.parseInt(properties.getProperty("items-per-transfer"));
                    context.getSource().sendFeedback(() -> Text.of("Hoppers will now transfer normally at 1 item per 8 ticks"), true);
                    return 1;
                }))

                .then(literal("items-per-transfer")
                        .requires(perm("items-per-transfer", PERMISSION_DEFAULT_OP_LEVEL_EDIT))
                        .then(argument("items-per-transfer", IntegerArgumentType.integer()).executes(context -> {
                    properties.setProperty("items-per-transfer", String.valueOf(IntegerArgumentType.getInteger(context, "items-per-transfer")));
                            try (OutputStream output = Files.newOutputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
                                properties.store(output, null);
                            } catch (IOException e) {
                                LOGGER.warn("Failed to save items-per-transfer configuration", e);
                            }
                            items = Integer.parseInt(properties.getProperty("items-per-transfer"));
                            context.getSource().sendFeedback(() -> Text.of("Hoppers will now move " + items + " items per transfer"), true);
                    return 1;
                }
                )))));
    }

    private static Predicate<ServerCommandSource> perm(String nodeSuffix, int defaultOpLevel) {
        return Permissions.require(PERMISSION_PREFIX + nodeSuffix, defaultOpLevel);
    }
}
