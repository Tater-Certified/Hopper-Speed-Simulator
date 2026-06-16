/**
 * Copyright (c) 2026 QPCrummer
 * This project is Licensed under <a href="https://github.com/Tater-Certified/Hopper-Speed-Simulator/blob/main/LICENSE">MIT</a>
 */
package com.github.tatercertified.hopperspeedsimulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    public static int ticks;
    public static int items;

    private static final String CONFIG_VER = "1.3";
    public static final Properties PROPERTIES = new Properties();
    public static final Logger LOGGER = LoggerFactory.getLogger("Hopper Speed Simulator");
    public static final Path CONIFG = Path.of("").resolve("config/hopperspeedsim.properties");


    public static void onInitialize() {
        if (Files.notExists(CONIFG)) {
            mkfile();
            LOGGER.info("Creating Hopper Speed Simulator config");
        } else {
            loadcfg();
            if (!(Objects.equals(PROPERTIES.getProperty("config-version"), CONFIG_VER))) {
                mkfile();
                LOGGER.info("Updating Hopper Speed Simulator config");
            } else {
                parse();
            }
        }
    }

    public static void mkfile() {
        if (CONIFG.getParent() != null) {
            try {
                Files.createDirectories(CONIFG.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (OutputStream output = Files.newOutputStream(CONIFG)) {
            if (!PROPERTIES.contains("config-version")) {
                PROPERTIES.setProperty("config-version", CONFIG_VER);
            }
            if (!PROPERTIES.contains("ticks-per-transfer")) {
                PROPERTIES.setProperty("ticks-per-transfer", "8");
            }
            if (!PROPERTIES.contains("items-per-transfer")) {
                PROPERTIES.setProperty("items-per-transfer", "1");
            }
            PROPERTIES.store(output, null);
        } catch (IOException e) {
            LOGGER.warn("Failed to create config");
            throw new RuntimeException(e);
        }
        parse();
    }

    public static void loadcfg() {
        try (InputStream input = Files.newInputStream(CONIFG)) {
            PROPERTIES.load(input);
        } catch (IOException e) {
            LOGGER.warn("Failed to load config");
            throw new RuntimeException(e);
        }
    }

    public static void parse() {
        ticks = Integer.parseInt(PROPERTIES.getProperty("ticks-per-transfer"));
        items = Integer.parseInt(PROPERTIES.getProperty("items-per-transfer"));
    }
}
