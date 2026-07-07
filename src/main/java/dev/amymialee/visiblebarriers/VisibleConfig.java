package dev.amymialee.visiblebarriers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VisibleConfig {
    private static final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("visiblebarriers.json");
    private static boolean hideParticles = true;
    private static float baseZoom = 2.8f;
    private static long forcedTime = 6000;

    public static void setHideParticles(boolean hideParticles) {
        VisibleConfig.hideParticles = hideParticles;
        saveConfig();
    }

    public static void setForcedTime(long forcedTime) {
        VisibleConfig.forcedTime = forcedTime;
        VisibleBarriers.setTime(true);
    }

    public static boolean shouldHideParticles() {
        return hideParticles;
    }

    public static float getBaseZoom() {
        return baseZoom;
    }

    public static long getForcedTime() {
        return forcedTime;
    }

    protected static void saveConfig() {
        try {
            var json = new JsonObject();
            json.addProperty("hideParticles", hideParticles);
            json.addProperty("baseZoom", baseZoom);
            Files.writeString(configFile, new GsonBuilder().setPrettyPrinting().create().toJson(json));
        } catch (Exception e) {
            VisibleBarriers.LOGGER.info(e.toString());
        }
    }

    protected static void loadConfig() {
        try {
            var data = new Gson().fromJson(Files.readString(configFile), JsonObject.class);
            if (data.has("hideParticles")) hideParticles = data.get("hideParticles").getAsBoolean();
            if (data.has("baseZoom")) baseZoom = data.get("baseZoom").getAsFloat();
        } catch (FileNotFoundException _) {
        } catch (Exception e) {
            VisibleBarriers.LOGGER.info("Error loading config data.");
            VisibleBarriers.LOGGER.info(e.toString());
        }
    }
}