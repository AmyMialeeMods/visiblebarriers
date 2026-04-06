package xyz.amymialee.visiblebarriers;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.commands.arguments.TimeArgument;
import org.lwjgl.glfw.GLFW;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;

public class VisibleInput {

    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(VisibleBarriersCommon.id(VisibleBarriersCommon.MOD_ID));

    private static KeyMapping keyBindingVisibility;
    private static KeyMapping keyBindingBarriers;
    private static KeyMapping keyBindingLights;
    private static KeyMapping keyBindingStructureVoids;
    private static KeyMapping keyBindingBubbleColumns;
    private static KeyMapping keyBindingFullBright;
    private static KeyMapping keyBindingTime;
    private static KeyMapping keyBindingZoom;

    public static void initKeys() {
        keyBindingVisibility = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.visible",
                GLFW.GLFW_KEY_B,
                CATEGORY
        ));
        keyBindingBarriers = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.barriers",
                InputConstants.UNKNOWN.getValue(),
                CATEGORY
        ));
        keyBindingLights = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.lights",
                InputConstants.UNKNOWN.getValue(),
                CATEGORY
        ));
        keyBindingStructureVoids = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.structurevoids",
                InputConstants.UNKNOWN.getValue(),
                CATEGORY
        ));
        keyBindingBubbleColumns = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.bubblecolumns",
                InputConstants.UNKNOWN.getValue(),
                CATEGORY
        ));
        keyBindingFullBright = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.fullbright",
                InputConstants.KEY_M,
                CATEGORY
        ));
        keyBindingTime = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.time",
                InputConstants.UNKNOWN.getValue(),
                CATEGORY
        ));
        keyBindingZoom = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.visiblebarriers.zoom",
                InputConstants.KEY_Z,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(_ -> {
            if (keyBindingVisibility.consumeClick()) {
                VisibleBarriers.toggleVisible();
                //if vis is determined to be false/off and barriers are still true/on, turn the barriers off again
                if (!VisibleBarriers.isVisibilityEnabled() && VisibleBarriers.areBarriersEnabled()) {
                    VisibleBarriers.toggleBarriers(); //toggle barriers back off again
                }

                //if vis is true make sure to double check that config has barriers as true
                if (VisibleBarriers.isVisibilityEnabled()) {
                    VisibleConfig.setVisibleBarrier(true);
                }
                //if vis is false make sure to double check that config has barriers as false
                if (!VisibleBarriers.isVisibilityEnabled()) {
                    VisibleConfig.setVisibleBarrier(false);
                }
            }
            if (keyBindingBarriers.consumeClick()) {
                VisibleBarriers.toggleBarriers();
            }
            if (keyBindingLights.consumeClick()) {
                VisibleBarriers.toggleLights();
            }
            if (keyBindingStructureVoids.consumeClick()) {
                VisibleBarriers.toggleStructureVoids();
            }
            if (keyBindingBubbleColumns.consumeClick()) {
                VisibleBarriers.toggleBubbleColumns();
            }
            if (keyBindingFullBright.consumeClick()) {
                VisibleBarriers.toggleFullBright();
            }
            if (keyBindingTime.consumeClick()) {
                VisibleBarriers.toggleTime();
            }
            if (keyBindingZoom.isDown()) {
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
        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, _) -> commandDispatcher.register(
                ClientCommands.literal("visiblebarriers")
                        //Reload Config
                        .then(ClientCommands.literal("reload").executes(_ -> {
                            VisibleConfig.loadConfig();
                            VisibleConfig.saveConfig();
                            VisibleBarriers.reloadWorldRenderer();
                            VisibleBarriers.sendFeedback("visiblebarriers.command.reload");
                            return 1;
                        }))
                        //Visibility
                        .then(ClientCommands.literal("visibility")
                                //Universal Visibility
                                .then(ClientCommands.literal("everything").executes(_ -> {
                                    VisibleBarriers.toggleVisible();
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleBarriers.toggleVisible = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.visibility", VisibleBarriers.toggleVisible);
                                    return 1;
                                })))
                                //Barriers
                                .then(ClientCommands.literal("barriers").executes(_ -> {
                                    VisibleConfig.setVisibleBarrier(!VisibleConfig.isBarrierVisible());
                                    VisibleBarriers.toggleBarriers();
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleConfig.setVisibleBarrier(BoolArgumentType.getBool(context, "visible"));
                                    VisibleBarriers.toggleBarriers = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.barriers", VisibleBarriers.toggleBarriers);
                                    return 1;
                                })))
                                //Lights
                                .then(ClientCommands.literal("lights").executes(_ -> {
                                    VisibleBarriers.toggleLights();
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleBarriers.toggleLights = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.lights", VisibleBarriers.toggleLights);
                                    return 1;
                                })))
                                //Structure Voids
                                .then(ClientCommands.literal("structurevoids").executes(_ -> {
                                    VisibleBarriers.toggleStructureVoids();
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleBarriers.toggleStructureVoids = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.structurevoids", VisibleBarriers.toggleStructureVoids);
                                    return 1;
                                })))
                                //Bubble columns
                                .then(ClientCommands.literal("bubblecolumns").executes(_ -> {
                                    VisibleBarriers.toggleBubbleColumns();
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleBarriers.toggleBubbleColumns = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.bubblecolumns", VisibleBarriers.toggleBubbleColumns);
                                    return 1;
                                })))
                        )
                        //Fullbright
                        .then(ClientCommands.literal("fullbright").executes(_ -> {
                            VisibleBarriers.toggleFullBright();
                            return 1;
                        }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                            VisibleBarriers.toggleFullBright = BoolArgumentType.getBool(context, "visible");
                            VisibleBarriers.reloadWorldRenderer();
                            VisibleBarriers.booleanFeedback("visiblebarriers.feedback.fullbright", VisibleBarriers.toggleFullBright);
                            return 1;
                        })))
                        //Set Time
                        .then(ClientCommands.literal("time")
                                .then(ClientCommands.literal("enable").executes(_ -> {
                                    VisibleBarriers.toggleTime = true;
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.time", true);
                                    return 0;
                                }))
                                .then(ClientCommands.literal("disable").executes(_ -> {
                                    VisibleBarriers.toggleTime = false;
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.time", false);
                                    return 0;
                                }))
                                .then(ClientCommands.literal("set")
                                        .then(ClientCommands.literal("day").executes(_ -> {
                                            VisibleConfig.setForcedTime(1000);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.day");
                                            return 0;
                                        }))
                                        .then(ClientCommands.literal("noon").executes(_ -> {
                                            VisibleConfig.setForcedTime(6000);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.noon");
                                            return 0;
                                        }))
                                        .then(ClientCommands.literal("night").executes(_ -> {
                                            VisibleConfig.setForcedTime(13000);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.night");
                                            return 0;
                                        }))
                                        .then(ClientCommands.literal("midnight").executes(_ -> {
                                            VisibleConfig.setForcedTime(18000);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.midnight");
                                            return 0;
                                        }))
                                        .then(ClientCommands.argument("time", TimeArgument.time()).executes(context -> {
                                            var time = IntegerArgumentType.getInteger(context, "time");
                                            VisibleConfig.setForcedTime(time);
                                            VisibleBarriers.sendFeedback("visiblebarriers.command.time.custom", time);
                                            return 0;
                                        }))
                                )
                        )
                        //Set Weather
                        .then(ClientCommands.literal("weather")
                                .then(ClientCommands.literal("default").executes(_ -> {
                                    VisibleBarriers.setWeather(VisibleBarriers.Weather.DEFAULT);
                                    return 0;
                                }))
                                .then(ClientCommands.literal("clear").executes(_ -> {
                                    VisibleBarriers.setWeather(VisibleBarriers.Weather.CLEAR);
                                    return 0;
                                }))
                                .then(ClientCommands.literal("rain").executes(_ -> {
                                    VisibleBarriers.setWeather(VisibleBarriers.Weather.RAIN);
                                    return 0;
                                }))
                                .then(ClientCommands.literal("thunder").executes(_ -> {
                                    VisibleBarriers.setWeather(VisibleBarriers.Weather.THUNDER);
                                    return 0;
                                }))
                        )
                        //Settings
                        .then(ClientCommands.literal("settings")
                                //Persist Visible Barriers
                                .then(ClientCommands.literal("visiblebarrier").executes(_ -> {
                                    VisibleConfig.setVisibleBarrier(!VisibleConfig.isBarrierVisible());
                                    VisibleBarriers.toggleBarriers();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.barriers", VisibleBarriers.toggleBarriers);
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleConfig.setVisibleBarrier(BoolArgumentType.getBool(context, "visible"));
                                    VisibleBarriers.toggleBarriers = BoolArgumentType.getBool(context, "visible");
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.feedback.barriers", VisibleBarriers.toggleBarriers);
                                    return 1;
                                })))
                                //Visible Air
                                .then(ClientCommands.literal("visibleair").executes(_ -> {
                                    VisibleConfig.setVisibleAir(!VisibleConfig.isAirVisible());
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.visibleair", VisibleConfig.isAirVisible());
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleConfig.setVisibleAir(BoolArgumentType.getBool(context, "visible"));
                                    VisibleBarriers.reloadWorldRenderer();
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.visibleair", VisibleConfig.isAirVisible());
                                    return 1;
                                })))
                                //Hide Particles
                                .then(ClientCommands.literal("hiddenparticles").executes(_ -> {
                                    VisibleConfig.setHideParticles(!VisibleConfig.shouldHideParticles());
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.hiddenparticles", VisibleConfig.shouldHideParticles());
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleConfig.setHideParticles(BoolArgumentType.getBool(context, "visible"));
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.hiddenparticles", VisibleConfig.shouldHideParticles());
                                    return 1;
                                })))
                                //Send Feedback
                                .then(ClientCommands.literal("sendfeedback").executes(_ -> {
                                    VisibleConfig.setSendFeedback(!VisibleConfig.shouldSendFeedback());
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.sendfeedback", VisibleConfig.shouldSendFeedback());
                                    return 1;
                                }).then(ClientCommands.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                    VisibleConfig.setSendFeedback(BoolArgumentType.getBool(context, "visible"));
                                    VisibleBarriers.booleanFeedback("visiblebarriers.settings.sendfeedback", VisibleConfig.shouldSendFeedback());
                                    return 1;
                                })))
                        )
        ));
    }
}