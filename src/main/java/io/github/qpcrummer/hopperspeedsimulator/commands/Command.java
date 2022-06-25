package io.github.qpcrummer.hopperspeedsimulator.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static io.github.qpcrummer.hopperspeedsimulator.Main.properties;
import static io.github.qpcrummer.hopperspeedsimulator.Main.ticks;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class Command {

    public static void registerCommand() throws FileNotFoundException {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> dispatcher.register(CommandManager.literal("hopperspeed")
                        .executes(context -> {
                            ServerPlayerEntity player = context.getSource().getPlayer();
                            assert player != null;
                            player.sendMessage(Text.of("Current speed is "+ ticks +" ticks per transfer"));
                            return 1;
                        })
                .then(argument("ticks-per-item", IntegerArgumentType.integer()) .executes(context -> {
                    int ticks2 = IntegerArgumentType.getInteger(context, "ticks-per-item");
                    properties.setProperty("ticks-per-transfer", String.valueOf(ticks2));
                    try (OutputStream output = new FileOutputStream(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties")))) {
                        properties.store(output, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ticks = Integer.parseInt(properties.getProperty("ticks-per-transfer"));
                    ServerPlayerEntity player = context.getSource().getPlayer();
                            assert player != null;
                            player.sendMessage(Text.of("Hoppers will now transfer items every "+ ticks +" ticks"));
                    return 1;
                }))
                .then(literal("default").executes(context -> {
                    properties.setProperty("ticks-per-transfer", "8");
                    try (OutputStream output = new FileOutputStream(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties")))) {
                        properties.store(output, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ticks = Integer.parseInt(properties.getProperty("ticks-per-transfer"));
                    ServerPlayerEntity player = context.getSource().getPlayer();
                           assert player != null;
                           player.sendMessage(Text.of("Hoppers will now transfer normally at 8 ticks per item"));
                    return 1;
                        }
                ))));
    }
}
