package dev.amymialee.visiblebarriers;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;

public class VisibleInput {
    public static KeyMapping keyBindingVisibility;
    public static KeyMapping keyBindingFullBright;
    public static KeyMapping keyBindingConfig;
    public static KeyMapping keyBindingZoom;

    public static void initKeys() {
        var category = KeyMapping.Category.register(VisibleBarriersCommon.id(VisibleBarriersCommon.MOD_ID));
        keyBindingVisibility = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.visible", InputConstants.KEY_B, category));
        keyBindingFullBright = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.fullbright", InputConstants.KEY_N, category));
        keyBindingConfig = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.config", InputConstants.KEY_K, category));
        keyBindingZoom = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.zoom", InputConstants.KEY_Z, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBindingVisibility.consumeClick()) {
                VisibleConfig.visibility = !VisibleConfig.visibility;
                VisibleBarriers.booleanFeedback("visiblebarriers.feedback.visible", VisibleConfig.visibility);
                VisibleBarriers.reloadWorldRenderer();
            }
            if (keyBindingFullBright.consumeClick()) {
                VisibleConfig.gamma = !VisibleConfig.gamma;
                VisibleBarriers.booleanFeedback("visiblebarriers.feedback.fullbright", VisibleConfig.gamma);
            }
            if (keyBindingConfig.consumeClick()) {
                client.setScreenAndShow(MidnightConfig.getScreen(null, VisibleBarriersCommon.MOD_ID));
            }
            if (keyBindingZoom.isDown()) {
                VisibleConfig.holdingZoom = true;
                VisibleBarriers.sendFeedback("visiblebarriers.feedback.zoom", "%.0f".formatted(10000f / (VisibleBarriers.getZoomModifier() * 100)));
            } else {
                if (VisibleConfig.holdingZoom) {
                    VisibleConfig.holdingZoom = false;
                    VisibleBarriers.sendFeedback("visiblebarriers.feedback.zoom", "100");
                }
                VisibleConfig.zoomScroll = VisibleConfig.baseZoom;
            }
        });
    }

    public static void initCommands() {
        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, _) -> {
            commandDispatcher.register(ClientCommands.literal("vbtime")
                    .then(ClientCommands.literal("enable").executes(_ -> {
                        VisibleConfig.forcedTimeEnabled = true;
                        VisibleBarriers.booleanFeedback("visiblebarriers.feedback.time", true);
                        return 0;
                    }))
                    .then(ClientCommands.literal("disable").executes(_ -> {
                        VisibleConfig.forcedTimeEnabled = false;
                        VisibleBarriers.booleanFeedback("visiblebarriers.feedback.time", false);
                        return 0;
                    }))
                    .then(ClientCommands.literal("set")
                            .then(ClientCommands.literal("day").executes(_ -> {
                                VisibleConfig.forcedTime = 1000;
                                VisibleBarriers.sendFeedback("visiblebarriers.command.time.day");
                                return 0;
                            }))
                            .then(ClientCommands.literal("noon").executes(_ -> {
                                VisibleConfig.forcedTime = 6000;
                                VisibleBarriers.sendFeedback("visiblebarriers.command.time.noon");
                                return 0;
                            }))
                            .then(ClientCommands.literal("night").executes(_ -> {
                                VisibleConfig.forcedTime = 13000;
                                VisibleBarriers.sendFeedback("visiblebarriers.command.time.night");
                                return 0;
                            }))
                            .then(ClientCommands.literal("midnight").executes(_ -> {
                                VisibleConfig.forcedTime = 18000;
                                VisibleBarriers.sendFeedback("visiblebarriers.command.time.midnight");
                                return 0;
                            }))
                            .then(ClientCommands.argument("time", TimeArgument.time()).executes(context -> {
                                var time = IntegerArgumentType.getInteger(context, "time");
                                VisibleConfig.forcedTime = time;
                                VisibleBarriers.sendFeedback("visiblebarriers.command.time.custom", time);
                                return 0;
                            }))
                    )
            );
            commandDispatcher.register(ClientCommands.literal("vbweather")
                    .then(ClientCommands.literal("unchanged").executes(_ -> {
                        VisibleConfig.forcedWeather = VisibleConfig.Weather.UNCHANGED;
                        VisibleBarriers.sendFeedback("visiblebarriers.command.weather", Component.translatable(VisibleConfig.forcedWeather.getTranslationKey()));
                        return 0;
                    }))
                    .then(ClientCommands.literal("clear").executes(_ -> {
                        VisibleConfig.forcedWeather = VisibleConfig.Weather.CLEAR;
                        VisibleBarriers.sendFeedback("visiblebarriers.command.weather", Component.translatable(VisibleConfig.forcedWeather.getTranslationKey()));
                        return 0;
                    }))
                    .then(ClientCommands.literal("rain").executes(_ -> {
                        VisibleConfig.forcedWeather = VisibleConfig.Weather.RAIN;
                        VisibleBarriers.sendFeedback("visiblebarriers.command.weather", Component.translatable(VisibleConfig.forcedWeather.getTranslationKey()));
                        return 0;
                    }))
                    .then(ClientCommands.literal("thunder").executes(_ -> {
                        VisibleConfig.forcedWeather = VisibleConfig.Weather.THUNDER;
                        VisibleBarriers.sendFeedback("visiblebarriers.command.weather", Component.translatable(VisibleConfig.forcedWeather.getTranslationKey()));
                        return 0;
                    }))
            );
        });
    }
}