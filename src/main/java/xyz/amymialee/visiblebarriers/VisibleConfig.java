package xyz.amymialee.visiblebarriers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VisibleConfig {
    private static final Path configFile = FabricLoader.getInstance().getConfigDir().resolve("visiblebarriers.json");
    private static boolean visibleBarrier = false;
    private static boolean visibleAir = false;
    private static boolean hideParticles = true;
    private static boolean sendFeedback = true;
    private static boolean solidLights = false;
    private static float baseZoom = 2.8f;
    private static long forcedTime = 6000;

    public static void setVisibleBarrier(boolean visibleBarrier) {
        VisibleConfig.visibleBarrier = visibleBarrier;
        saveConfig();
    }

    public static void setVisibleAir(boolean visibleAir) {
        VisibleConfig.visibleAir = visibleAir;
        saveConfig();
    }

    public static void setHideParticles(boolean hideParticles) {
        VisibleConfig.hideParticles = hideParticles;
        saveConfig();
    }

    public static void setSendFeedback(boolean sendFeedback) {
        VisibleConfig.sendFeedback = sendFeedback;
        saveConfig();
    }

    public static void setForcedTime(long forcedTime) {
        VisibleConfig.forcedTime = forcedTime;
        VisibleBarriers.setTime(true);
        if (MinecraftClient.getInstance().world != null) {
            MinecraftClient.getInstance().world.setTimeOfDay(VisibleConfig.getForcedTime());
        }
    }

    public static boolean isBarrierVisible() {
        return visibleBarrier;
    }

    public static boolean isAirVisible() {
        return visibleAir;
    }

    public static boolean shouldHideParticles() {
        return hideParticles;
    }

    public static boolean shouldSendFeedback() {
        return sendFeedback;
    }

    public static float getBaseZoom() {
        return baseZoom;
    }

    public static long getForcedTime() {
        return forcedTime;
    }

    public static boolean areLightsSolid() {
        return solidLights;
    }

    protected static void saveConfig() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject json = new JsonObject();
            json.addProperty("visibleBarrier", visibleBarrier);
            json.addProperty("visibleAir", visibleAir);
            json.addProperty("hideParticles", hideParticles);
            json.addProperty("sendFeedback", sendFeedback);
            json.addProperty("baseZoom", baseZoom);
            if (solidLights) json.addProperty("solidLights", true);
            String jsonData = gson.toJson(json);
            Files.writeString(configFile, jsonData);
        } catch (Exception e) {
            VisibleBarriersCommon.LOGGER.info(e.toString());
        }
    }

    protected static void loadConfig() {
        try {
            Gson gson = new Gson();
            String reader = Files.readString(configFile);
            JsonObject data = gson.fromJson(reader, JsonObject.class);
            if (data.has("visibleBarrier")) {
                visibleBarrier = data.get("visibleBarrier").getAsBoolean();
                if (visibleBarrier) {
                    VisibleBarriers.toggleBarriers();
                }
            }
            if (data.has("visibleAir")) {
                visibleAir = data.get("visibleAir").getAsBoolean();
            }
            if (data.has("hideParticles")) {
                hideParticles = data.get("hideParticles").getAsBoolean();
            }
            if (data.has("sendFeedback")) {
                sendFeedback = data.get("sendFeedback").getAsBoolean();
            }
            if (data.has("baseZoom")) {
                baseZoom = data.get("baseZoom").getAsFloat();
            }
            if (data.has("solidLights")) {
                solidLights = data.get("solidLights").getAsBoolean();
            }
        } catch (FileNotFoundException e) {
            VisibleBarriersCommon.LOGGER.info("Config data not found.");
        } catch (Exception e) {
            VisibleBarriersCommon.LOGGER.info("Error loading config data.");
            VisibleBarriersCommon.LOGGER.info(e.toString());
        }
    }
}