package xyz.amymialee.visiblebarriers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersNetworking;
import xyz.amymialee.visiblebarriers.model.TransparentBlockStateModel;

@Environment(EnvType.CLIENT)
public class VisibleBarriers implements ClientModInitializer {
    protected static boolean toggleVisible = false;
    protected static boolean toggleBarriers = false;
    protected static boolean toggleLights = false;
    protected static boolean toggleStructureVoids = false;
    protected static boolean toggleBubbleColumns = false;
    protected static boolean toggleFullBright = false;
    protected static boolean toggleTime = false;
    protected static boolean holdingZoom = false;
    protected static Weather setWeather = Weather.DEFAULT;
    protected static float zoomScroll = 1.0F;

    @Override
    public void onInitializeClient() {
        VisibleInput.initKeys();
        VisibleInput.initCommands();
        VisibleConfig.loadConfig();
        ClientConfigurationNetworking.registerGlobalReceiver(VisibleBarriersNetworking.ModInstalledPayload.TYPE, (_, _) -> {});
        CustomUnbakedBlockStateModel.register(VisibleBarriersCommon.id("transparent"), TransparentBlockStateModel.Unbaked.CODEC);
    }

    public static void sendFeedback(String translatable, Object... args) {
        if (VisibleConfig.shouldSendFeedback()) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                player.sendOverlayMessage(Component.translatable(translatable, args).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public static void booleanFeedback(String key, boolean value) {
        sendFeedback("[Visible Barriers] %s %s", Component.translatable(key), Component.translatable(value ? "visiblebarriers.enabled" : "visiblebarriers.disabled"));
    }

    public static void reloadWorldRenderer() {
        var mc = Minecraft.getInstance();
        if (mc.levelRenderer != null && mc.level != null) {
            mc.levelRenderer.invalidateCompiledGeometry(mc.level, mc.options, mc.gameRenderer.mainCamera(), mc.getBlockColors());
        }
    }

    public static boolean isVisibilityEnabled() {
        return toggleVisible;
    }

    public static void toggleVisible() {
        setVisible(!toggleVisible);
    }

    public static void setVisible(boolean visible) {
        toggleVisible = visible;
        booleanFeedback("visiblebarriers.feedback.visible", toggleVisible);
        reloadWorldRenderer();
    }

    public static boolean isFullBrightEnabled() {
        return toggleFullBright;
    }

    public static void toggleFullBright() {
        setFullBright(!toggleFullBright);
    }

    public static void setFullBright(boolean fullBright) {
        toggleFullBright = fullBright;
        booleanFeedback("visiblebarriers.feedback.fullbright", toggleFullBright);
    }

    public static boolean isTimeEnabled() {
        return toggleTime;
    }

    public static void toggleTime() {
        setTime(!toggleTime);
    }

    public static void setTime(boolean time) {
        toggleTime = time;
        booleanFeedback("visiblebarriers.feedback.time", toggleTime);
    }

    public static boolean areBarriersEnabled() {
        return toggleBarriers;
    }

    public static void toggleBarriers() {
        setBarriers(!toggleBarriers);
    }

    public static void setBarriers(boolean barriers) {
        toggleBarriers = barriers;
        booleanFeedback("visiblebarriers.feedback.barriers", toggleBarriers);
        reloadWorldRenderer();
    }

    public static boolean areLightsEnabled() {
        return toggleLights;
    }

    public static void toggleLights() {
        setLights(!toggleLights);
    }

    public static void setLights(boolean lights) {
        toggleLights = lights;
        booleanFeedback("visiblebarriers.feedback.lights", toggleLights);
        reloadWorldRenderer();
    }

    public static boolean areStructureVoidsEnabled() {
        return toggleStructureVoids;
    }

    public static void toggleStructureVoids() {
        setStructureVoids(!toggleStructureVoids);
    }

    public static void setStructureVoids(boolean structureVoids) {
        toggleStructureVoids = structureVoids;
        booleanFeedback("visiblebarriers.feedback.structurevoids", toggleStructureVoids);
        reloadWorldRenderer();
    }

    public static boolean areBubbleColumnsEnabled() {
        return toggleBubbleColumns;
    }

    public static void toggleBubbleColumns() {
        setBubbleColumns(!toggleBubbleColumns);
    }

    public static void setBubbleColumns(boolean bubbleColumns) {
        toggleBubbleColumns = bubbleColumns;
        booleanFeedback("visiblebarriers.feedback.bubblecolumns", toggleBubbleColumns);
        reloadWorldRenderer();
    }

    public static Weather getWeather() {
        return setWeather;
    }

    public static void setWeather(Weather weather) {
        setWeather = weather;
        sendFeedback("visiblebarriers.command.weather", Component.translatable(setWeather.getTranslationKey()));
    }

    public static boolean isHoldingZoom() {
        return holdingZoom;
    }

    public static float getZoomModifier() {
        return (float) Mth.clamp(4f / Math.pow(zoomScroll, 2), 0.001f, 1);
    }

    public static void modifyZoomModifier(float amount) {
        zoomScroll -= amount;
        zoomScroll = Mth.clamp(zoomScroll, 0.01f, 1000);
        sendFeedback("visiblebarriers.feedback.zoom", "%.0f".formatted(10000f / (getZoomModifier() * 100)));
    }

    public enum Weather {
        DEFAULT(-1, -1, "visiblebarriers.weather.default"),
        CLEAR(0, 0, "visiblebarriers.weather.clear"),
        RAIN(1, 0, "visiblebarriers.weather.rain"),
        THUNDER(1, 1, "visiblebarriers.weather.thunder");

        private final int rain;
        private final int thunder;
        private final String translationKey;

        Weather(int rain, int thunder, String translationKey) {
            this.rain = rain;
            this.thunder = thunder;
            this.translationKey = translationKey;
        }

        public int getRain() {
            return this.rain;
        }

        public int getThunder() {
            return this.thunder;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }
    }
}