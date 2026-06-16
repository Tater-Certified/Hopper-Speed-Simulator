/**
 * Copyright (c) 2026 QPCrummer
 * This project is Licensed under <a href="https://github.com/Tater-Certified/Hopper-Speed-Simulator/blob/main/LICENSE">MIT</a>
 */
package com.github.tatercertified.hopperspeedsimulator.commands;

import static com.github.tatercertified.hopperspeedsimulator.Main.*;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.GameModeCommand;


public class Command {
    public static void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("hopperspeed")
                        .requires(Commands.hasPermission(GameModeCommand.PERMISSION_CHECK))
                        .executes(context -> {
                            context.getSource().sendSuccess(() -> Component.nullToEmpty("Current speed is "+ ticks +" ticks, with "+ items +" items per transfer"), true);
                            return 1;
                        })

                .then(literal("ticks-per-transfer")
                        .requires(Commands.hasPermission(GameModeCommand.PERMISSION_CHECK))
                        .then(argument("ticks-per-transfer", IntegerArgumentType.integer()).executes(context -> {
                    PROPERTIES.setProperty("ticks-per-transfer", String.valueOf(IntegerArgumentType.getInteger(context, "ticks-per-transfer")));
                    try (OutputStream output = Files.newOutputStream(CONIFG)) {
                        PROPERTIES.store(output, null);
                    } catch (IOException e) {
                        LOGGER.warn("Failed to save ticks-per-transfer configuration", e);
                    }
                    ticks = Integer.parseInt(PROPERTIES.getProperty("ticks-per-transfer"));
                    context.getSource().sendSuccess(() -> Component.nullToEmpty("Hoppers will now transfer items every "+ ticks +" ticks"), true);
                    return 1;
                })))

                .then(literal("default")
                        .requires(Commands.hasPermission(GameModeCommand.PERMISSION_CHECK))
                        .executes(context -> {
                    PROPERTIES.setProperty("ticks-per-transfer", "8");
                    PROPERTIES.setProperty("items-per-transfer", "1");
                    try (OutputStream output = Files.newOutputStream(CONIFG)) {
                        PROPERTIES.store(output, null);
                    } catch (IOException e) {
                        LOGGER.warn("Failed to save items-per-transfer configuration", e);
                    }
                    ticks = Integer.parseInt(PROPERTIES.getProperty("ticks-per-transfer"));
                    items = Integer.parseInt(PROPERTIES.getProperty("items-per-transfer"));
                    context.getSource().sendSuccess(() -> Component.nullToEmpty("Hoppers will now transfer normally at 1 item per 8 ticks"), true);
                    return 1;
                }))

                .then(literal("items-per-transfer")
                        .requires(Commands.hasPermission(GameModeCommand.PERMISSION_CHECK))
                        .then(argument("items-per-transfer", IntegerArgumentType.integer()).executes(context -> {
                    PROPERTIES.setProperty("items-per-transfer", String.valueOf(IntegerArgumentType.getInteger(context, "items-per-transfer")));
                            try (OutputStream output = Files.newOutputStream(CONIFG)) {
                                PROPERTIES.store(output, null);
                            } catch (IOException e) {
                                LOGGER.warn("Failed to save items-per-transfer configuration", e);
                            }
                            items = Integer.parseInt(PROPERTIES.getProperty("items-per-transfer"));
                            context.getSource().sendSuccess(() -> Component.nullToEmpty("Hoppers will now move " + items + " items per transfer"), true);
                    return 1;
                }))));
    }
}
