package xyz.amymialee.visiblebarriers;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.argument.TimeArgumentType;
import org.lwjgl.glfw.GLFW;

public class VisibleInput {
    private static KeyBinding keyBindingVisibility;
    private static KeyBinding keyBindingBarriers;
    private static KeyBinding keyBindingLights;
    private static KeyBinding keyBindingStructureVoids;
    private static KeyBinding keyBindingBubbleColumns;
    private static KeyBinding keyBindingFullBright;
    private static KeyBinding keyBindingTime;
    private static KeyBinding keyBindingZoom;

    public static void initKeys() {
        keyBindingVisibility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.visible",
                GLFW.GLFW_KEY_B,
                "category.visiblebarriers"
        ));
        keyBindingBarriers = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.barriers",
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.visiblebarriers"
        ));
        keyBindingLights = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.lights",
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.visiblebarriers"
        ));
        keyBindingStructureVoids = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.structurevoids",
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.visiblebarriers"
        ));
        keyBindingBubbleColumns = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.bubblecolumns",
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.visiblebarriers"
        ));
        keyBindingFullBright = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.fullbright",
                InputUtil.GLFW_KEY_M,
                "category.visiblebarriers"
        ));
        keyBindingTime = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.time",
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.visiblebarriers"
        ));
        keyBindingZoom = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.zoom",
                InputUtil.GLFW_KEY_Z,
                "category.visiblebarriers"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBindingVisibility.wasPressed()) {
                VisibleBarriers.toggleVisible();
            }
            if (keyBindingBarriers.wasPressed()) {
                VisibleBarriers.toggleBarriers();
            }
            if (keyBindingLights.wasPressed()) {
                VisibleBarriers.toggleLights();
            }
            if (keyBindingStructureVoids.wasPressed()) {
                VisibleBarriers.toggleStructureVoids();
            }
            if (keyBindingBubbleColumns.wasPressed()) {
                VisibleBarriers.toggleBubbleColumns();
            }
            if (keyBindingFullBright.wasPressed()) {
                VisibleBarriers.toggleFullBright();
            }
            if (keyBindingTime.wasPressed()) {
                VisibleBarriers.toggleTime();
            }
            if (keyBindingZoom.isPressed()) {
                VisibleBarriers.holdingZoom = true;
                VisibleBarriers.sendFeedback("visiblebarriers.feedback.zoom", "%.0f".formatted(10000f / (VisibleBarriers.getZoomModifier() * 100)));
            } else {
                if (VisibleBarriers.holdingZoom) {
                    VisibleBarriers.holdingZoom = false;
                    VisibleBarriers.sendFeedback("visiblebarriers.feedback.zoom", "100");
                }
                VisibleBarriers.zoomScroll = VisibleConfig.getBaseZoom();
            }
        });
    }

    public static void initCommands() {
        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess) -> commandDispatcher.register(
                ClientCommandManager.literal("visiblebarriers")
                        //Reload Config
                        .then(ClientCommandManager.literal("reload").executes(context -> {
                            VisibleConfig.loadConfig();
                            VisibleConfig.saveConfig();
                            VisibleBarriers.reloadWorldRenderer();
                            VisibleBarriers.sendFeedback("visiblebarriers.command.reload");
                            return 1;
                        }))
                        //Visibility
                        .then(ClientCommandManager.literal("visibility")
                                //Universal Visibility
                                .then(ClientCommandManager.literal("everything").executes(context -> {
                                    VisibleBarriers.toggleVisible();
                                    return 1;
                                }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleBarriers.toggleVisible = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.visibility", VisibleBarriers.toggleVisible);
                                    return 1;
                                })))
                                //Barriers
                                .then(ClientCommandManager.literal("barriers").executes(context -> {
                                    VisibleBarriers.toggleBarriers();
                                    return 1;
                                }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleBarriers.toggleBarriers = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.barriers", VisibleBarriers.toggleBarriers);
                                    return 1;
                                })))
                                //Lights
                                .then(ClientCommandManager.literal("lights").executes(context -> {
                                    VisibleBarriers.toggleLights();
                                    return 1;
                                }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleBarriers.toggleLights = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.lights", VisibleBarriers.toggleLights);
                                    return 1;
                                })))
                                //Structure Voids
                                .then(ClientCommandManager.literal("structurevoids").executes(context -> {
                                    VisibleBarriers.toggleStructureVoids();
                                    return 1;
                                }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleBarriers.toggleStructureVoids = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.structurevoids", VisibleBarriers.toggleStructureVoids);
                                    return 1;
                                })))
                                //Bubble columns
                                .then(ClientCommandManager.literal("bubblecolumns").executes(context -> {
                                    VisibleBarriers.toggleStructureVoids();
                                    return 1;
                                }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleBarriers.toggleBubbleColumns = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.bubblecolumns", VisibleBarriers.toggleBubbleColumns);
                                    return 1;
                                })))
                        )
                        //Fullbright
                        .then(ClientCommandManager.literal("fullbright").executes(context -> {
                            VisibleBarriers.toggleFullBright();
                            return 1;
                        }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                            VisibleBarriers.toggleFullBright = BoolArgumentType.getBool(context, "visible");
                            VisibleBarriers.reloadWorldRenderer();
                            VisibleBarriers.booleanFeedback("visiblebarriers.feedback.fullbright", VisibleBarriers.toggleFullBright);
                            return 1;
                        })))
                        //Set Time
                        .then(ClientCommandManager.literal("time")
                                .then(ClientCommandManager.literal("enable").executes(context -> {
                                    if (MinecraftClient.getInstance().world != null) {
                                        MinecraftClient.getInstance().world.setTimeOfDay(VisibleConfig.getForcedTime());
                                    }
                                    VisibleBarriers.toggleTime = true;
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.time", true);
                                    return 0;
                                }))
                                .then(ClientCommandManager.literal("disable").executes(context -> {
                                    VisibleBarriers.toggleTime = false;
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.time", false);
                                    return 0;
                                }))
                                .then(ClientCommandManager.literal("set")
                                        .then(ClientCommandManager.literal("day").executes(context -> {
                                            VisibleConfig.setForcedTime(1000);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.day");
                                            return 0;
                                        }))
                                        .then(ClientCommandManager.literal("noon").executes(context -> {
                                            VisibleConfig.setForcedTime(6000);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.noon");
                                            return 0;
                                        }))
                                        .then(ClientCommandManager.literal("night").executes(context -> {
                                            VisibleConfig.setForcedTime(13000);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.night");
                                            return 0;
                                        }))
                                        .then(ClientCommandManager.literal("midnight").executes(context -> {
                                            VisibleConfig.setForcedTime(18000);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.midnight");
                                            return 0;
                                        }))
                                        .then(ClientCommandManager.argument("time", TimeArgumentType.time()).executes(context -> {
                                            int time = IntegerArgumentType.getInteger(context, "time");
                                            VisibleConfig.setForcedTime(time);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.custom", time);
                                            return 0;
                                        }))
                                )
                        )
                        //Set Weather
                        .then(ClientCommandManager.literal("weather")
                                .then(ClientCommandManager.literal("default").executes(context -> {
                                    VisibleBarriers.setWeather(VisibleBarriers.Weather.DEFAULT);
                                    return 0;
                                }))
                                .then(ClientCommandManager.literal("clear").executes(context -> {
                                    VisibleBarriers.setWeather(VisibleBarriers.Weather.CLEAR);
                                    return 0;
                                }))
                                .then(ClientCommandManager.literal("rain").executes(context -> {
                                    VisibleBarriers.setWeather(VisibleBarriers.Weather.RAIN);
                                    return 0;
                                }))
                                .then(ClientCommandManager.literal("thunder").executes(context -> {
                                    VisibleBarriers.setWeather(VisibleBarriers.Weather.THUNDER);
                                    return 0;
                                }))
                        )
                        //Settings
                        .then(ClientCommandManager.literal("settings")
                                //Visible Air
                                .then(ClientCommandManager.literal("visibleair").executes(context -> {
                                    VisibleConfig.setVisibleAir(!VisibleConfig.isAirVisible());
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.visibleair", VisibleConfig.isAirVisible());
                                    return 1;
                                }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleConfig.setVisibleAir(BoolArgumentType.getBool(context, "visible"));
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.visibleair", VisibleConfig.isAirVisible());
                                    return 1;
                                })))
                                //Hide Particles
                                .then(ClientCommandManager.literal("hiddenparticles").executes(context -> {
                                    VisibleConfig.setHideParticles(!VisibleConfig.shouldHideParticles());
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.hiddenparticles", VisibleConfig.shouldHideParticles());
                                    return 1;
                                }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleConfig.setHideParticles(BoolArgumentType.getBool(context, "visible"));
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.hiddenparticles", VisibleConfig.shouldHideParticles());
                                    return 1;
                                })))
                                //Send Feedback
                                .then(ClientCommandManager.literal("sendfeedback").executes(context -> {
                                    VisibleConfig.setSendFeedback(!VisibleConfig.shouldSendFeedback());
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.sendfeedback", VisibleConfig.shouldSendFeedback());
                                    return 1;
                                }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleConfig.setSendFeedback(BoolArgumentType.getBool(context, "visible"));
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.sendfeedback", VisibleConfig.shouldSendFeedback());
                                    return 1;
                                })))
                        )
        ));
    }
}