package io.github.qpcrummer.hopperspeedsimulator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

import static io.github.qpcrummer.hopperspeedsimulator.commands.Command.registerCommand;


public class Main implements ModInitializer {

    public static String cfgver;
    public static int ticks;

    public static Properties properties = new Properties();

    @Override
    public void onInitialize() {

        try {
            registerCommand();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        var path = FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties");

        if (Files.notExists(path)) {
            try {
                mkfile();
                System.out.println("Creating Hopper Speed Simulator config");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                loadcfg();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cfgver = properties.getProperty("config-version");
            if (!(Objects.equals(cfgver, "1.0"))) {
                try {
                    mkfile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Updating Hopper Speed Simulator config");
            } else {
                parse();
            }
        }
    }

    public void mkfile() throws IOException {
        OutputStream output = new FileOutputStream(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties")));
        if (!properties.contains("config-version")) {properties.setProperty("config-version", "1.0");}
        if (!properties.contains("ticks-per-transfer")) {properties.setProperty("ticks-per-transfer", "8");}
        properties.store(output, null);
        parse();
    }

    public static void loadcfg() throws IOException {
        InputStream input = new FileInputStream(String.valueOf(FabricLoader.getInstance().getConfigDir().resolve("hopperspeedsim.properties")));
        properties.load(input);
    }

    public void parse() {
        cfgver = properties.getProperty("config-version");
        ticks = Integer.parseInt(properties.getProperty("ticks-per-transfer"));
    }
}
