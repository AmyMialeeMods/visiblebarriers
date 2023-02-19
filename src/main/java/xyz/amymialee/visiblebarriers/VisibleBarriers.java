package xyz.amymialee.visiblebarriers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class VisibleBarriers implements ClientModInitializer {
    public final static String MOD_ID = "visiblebarriers";
    public final static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    protected static boolean toggleVisible = false;
    protected static boolean toggleBarriers = false;
    protected static boolean toggleLights = false;
    protected static boolean toggleStructureVoids = false;
    protected static boolean toggleFullBright = false;
    protected static boolean toggleTime = false;
    protected static boolean holdingZoom = false;
    protected static float zoomScroll = 1.0F;

    @Override
    public void onInitializeClient() {
        VisibleInput.initKeys();
        VisibleInput.initCommands();
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BARRIER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STRUCTURE_VOID, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LIGHT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.AIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CAVE_AIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.VOID_AIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MOVING_PISTON, RenderLayer.getTranslucent());
    }

    public static void sendFeedback(String translatable, boolean chat, Object... args) {
        if (VisibleConfig.shouldSendFeedback()) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                player.sendMessage(Text.translatable(translatable, args).formatted(Formatting.GRAY), !chat);
            }
        }
    }

    public static void reloadWorldRenderer() {
        MinecraftClient.getInstance().worldRenderer.reload();
    }

    public static boolean isVisibilityEnabled () {
        return toggleVisible;
    }

    public static void toggleVisible () {
        setVisible(!toggleVisible);
    }

    public static void setVisible ( boolean visible){
        toggleVisible = visible;
        sendFeedback("visiblebarriers.visible." + (toggleVisible ? "enabled" : "disabled"), true);
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
        sendFeedback("visiblebarriers.fullbright." + (toggleFullBright ? "enabled" : "disabled"), true);
    }

    public static boolean isTimeEnabled() {
        return toggleTime;
    }

    public static void toggleTime() {
        setTime(!toggleTime);
    }

    public static void setTime(boolean time) {
        toggleTime = time;
        sendFeedback("visiblebarriers.time." + (toggleTime ? "enabled" : "disabled"), true);
    }

    public static boolean areBarriersEnabled() {
        return toggleBarriers;
    }

    public static void toggleBarriers() {
        setBarriers(!toggleBarriers);
    }

    public static void setBarriers(boolean barriers) {
        toggleBarriers = barriers;
        sendFeedback("visiblebarriers.barriers." + (toggleBarriers ? "enabled" : "disabled"), true);
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
        sendFeedback("visiblebarriers.lights." + (toggleLights ? "enabled" : "disabled"), true);
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
        sendFeedback("visiblebarriers.structurevoids." + (toggleStructureVoids ? "enabled" : "disabled"), true);
        reloadWorldRenderer();
    }

    public static boolean isHoldingZoom() {
        return holdingZoom;
    }

    public static float getZoomModifier() {
        return zoomScroll;
    }

    public static void modifyZoomModifier(float amount) {
        zoomScroll += (amount * 0.08f);
        zoomScroll = MathHelper.clamp(zoomScroll, 0.05f, 1.25f);
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.translatable("visiblebarriers.zoom.amount", "%.0f".formatted(100 / zoomScroll)).formatted(Formatting.GRAY), true);
        }
    }
}