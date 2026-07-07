package dev.amymialee.visiblebarriers;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import dev.amymialee.visiblebarriers.util.Weather;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.arguments.TimeArgument;
import org.lwjgl.glfw.GLFW;

public class VisibleInput {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(VisibleBarriers.id(VisibleBarriers.MOD_ID));
    private static KeyMapping keyBindingVisibility;
    private static KeyMapping keyBindingFullBright;
    private static KeyMapping keyBindingTime;
    private static KeyMapping keyBindingZoom;

    public static void init() {
        keyBindingVisibility = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.visiblebarriers.visible", GLFW.GLFW_KEY_B, CATEGORY));
        keyBindingFullBright = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.visiblebarriers.fullbright", InputConstants.KEY_M, CATEGORY));
        keyBindingTime = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.visiblebarriers.time", InputConstants.UNKNOWN.getValue(), CATEGORY));
        keyBindingZoom = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.visiblebarriers.zoom", InputConstants.KEY_Z, CATEGORY));

        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            if (keyBindingVisibility.consumeClick()) VisibleBarriers.toggleVisible();
            if (keyBindingFullBright.consumeClick()) VisibleBarriers.toggleFullBright();
            if (keyBindingTime.consumeClick()) VisibleBarriers.toggleTime();
            if (keyBindingZoom.isDown()) {
                VisibleBarriers.holdingZoom = true;
            } else {
                if (VisibleBarriers.holdingZoom) VisibleBarriers.holdingZoom = false;
                VisibleBarriers.zoomScroll = VisibleConfig.getBaseZoom();
            }
        });
        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, _) -> commandDispatcher.register(
                ClientCommands.literal("visiblebarriers")
                        .then(ClientCommands.literal("reload").executes(_ -> {
                            VisibleConfig.loadConfig();
                            VisibleConfig.saveConfig();
                            VisibleBarriers.reloadWorldRenderer();
                            return 1;
                        }))
                        .then(ClientCommands.literal("visibility").executes(_ -> {
                            VisibleBarriers.toggleVisible();
                            return 1;
                        }))
                        .then(ClientCommands.literal("fullbright").executes(_ -> {
                            VisibleBarriers.toggleFullBright();
                            return 1;
                        }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                            VisibleBarriers.toggleFullBright = BoolArgumentType.getBool(context, "visible");
                            VisibleBarriers.reloadWorldRenderer();
                            return 1;
                        })))
                        .then(ClientCommands.literal("time")
                                .then(ClientCommands.literal("enable").executes(_ -> {
                                    VisibleBarriers.toggleTime = true;
                                    return 0;
                                }))
                                .then(ClientCommands.literal("disable").executes(_ -> {
                                    VisibleBarriers.toggleTime = false;
                                    return 0;
                                }))
                                .then(ClientCommands.literal("set")
                                        .then(ClientCommands.literal("day").executes(_ -> {
                                            VisibleConfig.setForcedTime(1000);
                                            return 0;
                                        }))
                                        .then(ClientCommands.literal("noon").executes(_ -> {
                                            VisibleConfig.setForcedTime(6000);
                                            return 0;
                                        }))
                                        .then(ClientCommands.literal("night").executes(_ -> {
                                            VisibleConfig.setForcedTime(13000);
                                            return 0;
                                        }))
                                        .then(ClientCommands.literal("midnight").executes(_ -> {
                                            VisibleConfig.setForcedTime(18000);
                                            return 0;
                                        }))
                                        .then(ClientCommands.argument("time", TimeArgument.time()).executes(context -> {
                                            VisibleConfig.setForcedTime(IntegerArgumentType.getInteger(context, "time"));
                                            return 0;
                                        }))
                                )
                        )
                        .then(ClientCommands.literal("weather")
                                .then(ClientCommands.literal("default").executes(_ -> {
                                    VisibleBarriers.setWeather(Weather.DEFAULT);
                                    return 0;
                                }))
                                .then(ClientCommands.literal("clear").executes(_ -> {
                                    VisibleBarriers.setWeather(Weather.CLEAR);
                                    return 0;
                                }))
                                .then(ClientCommands.literal("rain").executes(_ -> {
                                    VisibleBarriers.setWeather(Weather.RAIN);
                                    return 0;
                                }))
                                .then(ClientCommands.literal("thunder").executes(_ -> {
                                    VisibleBarriers.setWeather(Weather.THUNDER);
                                    return 0;
                                }))
                        )
                        .then(ClientCommands.literal("settings")
                                .then(ClientCommands.literal("hiddenparticles").executes(_ -> {
                                    VisibleConfig.setHideParticles(!VisibleConfig.shouldHideParticles());
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleConfig.setHideParticles(BoolArgumentType.getBool(context, "visible"));
                                    return 1;
                                })))
                        )
        ));
    }
}