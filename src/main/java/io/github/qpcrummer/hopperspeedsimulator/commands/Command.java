package io.github.qpcrummer.hopperspeedsimulator.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import static io.github.qpcrummer.hopperspeedsimulator.Main.*;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class Command {

    public static void registerCommand() throws FileNotFoundException {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> dispatcher.register(CommandManager.literal("hopperspeed")
                        .executes(context -> {
                            context.getSource().sendFeedback(Text.of("Current speed is "+ ticks +" ticks, with "+ items +" items per transfer"), true);
                            return 1;
                        })

                .then(literal("ticks-per-item").then(argument("ticks-per-item", IntegerArgumentType.integer()).executes(context -> {
                    properties.setProperty("ticks-per-transfer", String.valueOf(IntegerArgumentType.getInteger(context, "ticks-per-item")));
                    try (OutputStream output = Files.newOutputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
                        properties.store(output, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ticks = Integer.parseInt(properties.getProperty("ticks-per-transfer"));
                    context.getSource().sendFeedback(Text.of("Hoppers will now transfer items every "+ ticks +" ticks"), true);
                    return 1;
                })))

                .then(literal("default").executes(context -> {
                    properties.setProperty("ticks-per-transfer", "8");
                    properties.setProperty("items-per-transfer", "1");
                    try (OutputStream output = Files.newOutputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
                        properties.store(output, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ticks = Integer.parseInt(properties.getProperty("ticks-per-transfer"));
                    items = Integer.parseInt(properties.getProperty("items-per-transfer"));
                    context.getSource().sendFeedback(Text.of("Hoppers will now transfer normally at 1 item per 8 ticks"), true);
                    return 1;
                }))

                .then(literal("items-per-transfer").then(argument("items-per-transfer", IntegerArgumentType.integer()).executes(context -> {
                    properties.setProperty("items-per-transfer", String.valueOf(IntegerArgumentType.getInteger(context, "items-per-transfer")));
                            try (OutputStream output = Files.newOutputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
                                properties.store(output, null);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            items = Integer.parseInt(properties.getProperty("items-per-transfer"));
                            context.getSource().sendFeedback(Text.of("Hoppers will now move " + items + " items per transfer"), true);
                    return 1;
                }
                )))));
    }
}
