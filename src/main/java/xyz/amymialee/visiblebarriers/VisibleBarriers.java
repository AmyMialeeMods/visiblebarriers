package xyz.amymialee.visiblebarriers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.enums.PistonType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class VisibleBarriers implements ClientModInitializer {
    protected static boolean toggleVisible = false;
    protected static boolean toggleBarriers = false;
    protected static boolean toggleLights = false;
    protected static boolean toggleStructureVoids = false;
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
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BARRIER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STRUCTURE_VOID, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LIGHT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.AIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CAVE_AIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.VOID_AIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.MOVING_PISTON, RenderLayer.getTranslucent());
    }

    public static void sendFeedback(String translatable, Object... args) {
        if (VisibleConfig.shouldSendFeedback()) {
            PlayerEntity player = MinecraftClient.getInstance().player;
            if (player != null) {
                player.sendMessage(Text.translatable(translatable, args).formatted(Formatting.GRAY), true);
            }
        }
    }

    public static void booleanFeedback(String key, boolean value) {
        sendFeedback("[Visible Barriers] %s %s", Text.translatable(key), Text.translatable(value ? "visiblebarriers.enabled" : "visiblebarriers.disabled"));
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

    public static void setVisible (boolean visible) {
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

    public static Weather getWeather() {
        return setWeather;
    }

    public static void setWeather(Weather weather) {
        setWeather = weather;
        sendFeedback("visiblebarriers.command.weather", Text.translatable(setWeather.getTranslationKey()));
    }

    public static boolean isHoldingZoom() {
        return holdingZoom;
    }

    public static float getZoomModifier() {
        return (float) MathHelper.clamp(4f / Math.pow(zoomScroll, 2), 0.001f, 1);
    }

    public static void modifyZoomModifier(float amount) {
        zoomScroll -= amount;
        zoomScroll = MathHelper.clamp(zoomScroll, 0.01f, 1000);
        sendFeedback("visiblebarriers.feedback.zoom", "%.0f".formatted(10000f / (getZoomModifier() * 100)));
    }

    static {
        ModelPredicateProviderRegistry.register(VisibleBarriersCommon.MOVING_PISTON_BLOCK_ITEM, new Identifier("sticky"), (stack, world, entity, seed) -> {
            NbtCompound compound = stack.getSubNbt("BlockStateTag");
            if (compound != null && Objects.equals(compound.getString(PistonExtensionBlock.TYPE.getName()), String.valueOf(PistonType.STICKY))) {
                return 1.0F;
            }
            return 0.0F;
        });
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