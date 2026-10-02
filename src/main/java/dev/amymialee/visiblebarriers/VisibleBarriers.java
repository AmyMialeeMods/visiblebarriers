package dev.amymialee.visiblebarriers;

import dev.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import dev.amymialee.visiblebarriers.common.VisibleBarriersNetworking;
import dev.amymialee.visiblebarriers.model.TransparentBlockStateModel;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class VisibleBarriers implements ClientModInitializer {
    /**
     * This is incredibly jank and awful, but I don't have the time to replace it with a better system atm.
     */
    public static final RenderStateDataKey<Entity> ENTITY = RenderStateDataKey.create(() -> "Visible Barriers Entity");

    @Override
    public void onInitializeClient() {
        MidnightConfig.init(VisibleBarriersCommon.MOD_ID, VisibleConfig.class);
        VisibleInput.initKeys();
        VisibleInput.initCommands();
        ClientConfigurationNetworking.registerGlobalReceiver(VisibleBarriersNetworking.ModInstalledPayload.TYPE, (_, _) -> {});
        CustomUnbakedBlockStateModel.register(VisibleBarriersCommon.id("transparent"), TransparentBlockStateModel.Unbaked.CODEC);
    }

    public static void sendFeedback(String translatable, Object... args) {
        if (!VisibleConfig.sendFeedback) return;
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        player.sendOverlayMessage(Component.translatable(translatable, args).withStyle(ChatFormatting.GRAY));
    }

    public static void booleanFeedback(String key, boolean value) {
        sendFeedback("[Visible Barriers] %s %s", Component.translatable(key), Component.translatable(value ? "visiblebarriers.enabled" : "visiblebarriers.disabled"));
    }

    public static void reloadWorldRenderer() {
        Minecraft.getInstance().levelExtractor.allChanged();
    }

    public static float getZoomModifier() {
        return (float) Mth.clamp(4f / Math.pow(VisibleConfig.zoomScroll, 2), 0.001f, 1);
    }

    public static void modifyZoomModifier(float amount) {
        VisibleConfig.zoomScroll -= amount;
        VisibleConfig.zoomScroll = Mth.clamp(VisibleConfig.zoomScroll, 0.01f, 1000);
        sendFeedback("visiblebarriers.feedback.zoom", "%.0f".formatted(10000f / (getZoomModifier() * 100)));
    }
}