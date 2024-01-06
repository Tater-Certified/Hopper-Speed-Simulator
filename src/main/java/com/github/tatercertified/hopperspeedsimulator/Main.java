package com.github.tatercertified.hopperspeedsimulator;

import com.github.tatercertified.hopperspeedsimulator.commands.Command;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;


public class Main implements ModInitializer {
    public static int ticks;
    public static int items;

    private final String configVer = "1.3";
    public static final Properties properties = new Properties();
    public static final Logger LOGGER = LoggerFactory.getLogger("Hopper Speed Sim");

    @Override
    public void onInitialize() {

        try {
            Command.registerCommand();
        } catch (FileNotFoundException e) {
            LOGGER.warn("Failed to load commands", e);
        }

        var path = FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties");

        if (Files.notExists(path)) {
            mkfile();
            LOGGER.info("Creating Hopper Speed Simulator config");
        } else {
            loadcfg();
            if (!(Objects.equals(properties.getProperty("config-version"), configVer))) {
                mkfile();
                LOGGER.info("Updating Hopper Speed Simulator config");
            } else {
                parse();
            }
        }
    }

    public void mkfile() {
        try (OutputStream output = Files.newOutputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
            if (!properties.contains("config-version")) {
                properties.setProperty("config-version", configVer);
            }
            if (!properties.contains("ticks-per-transfer")) {
                properties.setProperty("ticks-per-transfer", "8");
            }
            if (!properties.contains("items-per-transfer")) {
                properties.setProperty("items-per-transfer", "1");
            }
            properties.store(output, null);
        } catch (IOException e) {
            LOGGER.warn("Failed to create config");
            throw new RuntimeException(e);
        }
        parse();
    }

    public static void loadcfg() {
        try (InputStream input = Files.newInputStream(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties"))) {
            properties.load(input);
        } catch (IOException e) {
            LOGGER.warn("Failed to load config");
            throw new RuntimeException(e);
        }
    }

    public void parse() {
        ticks = Integer.parseInt(properties.getProperty("ticks-per-transfer"));
        items = Integer.parseInt(properties.getProperty("items-per-transfer"));
    }
}
