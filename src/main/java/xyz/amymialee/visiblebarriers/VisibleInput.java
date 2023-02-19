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
    private static KeyBinding keyBindingFullBright;
    private static KeyBinding keyBindingTime;
    private static KeyBinding keyBindingZoom;

    public static void initKeys() {
        keyBindingVisibility = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.bind",
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
                "key.visiblebarriers.structure_voids",
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.visiblebarriers"
        ));
        keyBindingFullBright = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.fullbright",
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.visiblebarriers"
        ));
        keyBindingTime = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.time",
                InputUtil.UNKNOWN_KEY.getCode(),
                "category.visiblebarriers"
        ));
        keyBindingZoom = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.zoom",
                InputUtil.UNKNOWN_KEY.getCode(),
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
            if (keyBindingFullBright.wasPressed()) {
                VisibleBarriers.toggleFullBright();
            }
            if (keyBindingTime.wasPressed()) {
                VisibleBarriers.toggleTime();
            }
            if (keyBindingZoom.isPressed()) {
                VisibleBarriers.holdingZoom = true;
                VisibleBarriers.sendFeedback("visiblebarriers.zoom.amount", false, "%.0f".formatted(100 / VisibleBarriers.zoomScroll));
            } else {
                if (VisibleBarriers.holdingZoom) {
                    VisibleBarriers.holdingZoom = false;
                    VisibleBarriers.sendFeedback("visiblebarriers.zoom.amount", false, "100");
                }
                VisibleBarriers.zoomScroll = VisibleConfig.getBaseZoom();
            }
        });
    }

    public static void initCommands() {
        ClientCommandRegistrationCallback.EVENT.register((commandDispatcher, commandRegistryAccess) -> {
            commandDispatcher.register(
                    ClientCommandManager.literal("visiblebarriers")
                            //Reload Config
                            .then(ClientCommandManager.literal("reload").executes(context -> {
                                VisibleConfig.loadConfig();
                                VisibleConfig.saveConfig();
                                VisibleBarriers.reloadWorldRenderer();
                                VisibleBarriers.sendFeedback("command.visiblebarriers.reload", false);
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
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.visible." + VisibleBarriers.toggleVisible, false);
                                        return 1;
                                    })))
                                    //Barriers
                                    .then(ClientCommandManager.literal("barriers").executes(context -> {
                                        VisibleBarriers.toggleBarriers();
                                        return 1;
                                    }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                        VisibleBarriers.toggleBarriers = BoolArgumentType.getBool(context, "visible");
                                        VisibleBarriers.reloadWorldRenderer();
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.barriers." + VisibleBarriers.toggleBarriers, false);
                                        return 1;
                                    })))
                                    //Lights
                                    .then(ClientCommandManager.literal("lights").executes(context -> {
                                        VisibleBarriers.toggleLights();
                                        return 1;
                                    }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                        VisibleBarriers.toggleLights = BoolArgumentType.getBool(context, "visible");
                                        VisibleBarriers.reloadWorldRenderer();
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.lights." + VisibleBarriers.toggleLights, false);
                                        return 1;
                                    })))
                                    //Structure Voids
                                    .then(ClientCommandManager.literal("structurevoids").executes(context -> {
                                        VisibleBarriers.toggleStructureVoids();
                                        return 1;
                                    }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                        VisibleBarriers.toggleStructureVoids = BoolArgumentType.getBool(context, "visible");
                                        VisibleBarriers.reloadWorldRenderer();
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.structurevoids." + VisibleBarriers.toggleStructureVoids, false);
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
                                VisibleBarriers.sendFeedback("command.visiblebarriers.fullbright." + VisibleBarriers.toggleFullBright, false);
                                return 1;
                            })))
                            //Set Time
                            .then(ClientCommandManager.literal("time")
                                    .then(ClientCommandManager.literal("enable").executes(context -> {
                                        if (MinecraftClient.getInstance().world != null) {
                                            MinecraftClient.getInstance().world.setTimeOfDay(VisibleConfig.getForcedTime());
                                        }
                                        VisibleBarriers.toggleTime = true;
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.time.true", false);
                                        return 0;
                                    }))
                                    .then(ClientCommandManager.literal("disable").executes(context -> {
                                        VisibleBarriers.toggleTime = false;
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.time.false", false);
                                        return 0;
                                    }))
                                    .then(ClientCommandManager.literal("set")
                                            .then(ClientCommandManager.literal("day").executes(context -> {
                                                VisibleConfig.setForcedTime(1000);
                                                VisibleBarriers.sendFeedback("command.visiblebarriers.time.day", false);
                                                return 0;
                                            }))
                                            .then(ClientCommandManager.literal("noon").executes(context -> {
                                                VisibleConfig.setForcedTime(6000);
                                                VisibleBarriers.sendFeedback("command.visiblebarriers.time.noon", false);
                                                return 0;
                                            }))
                                            .then(ClientCommandManager.literal("night").executes(context -> {
                                                VisibleConfig.setForcedTime(13000);
                                                VisibleBarriers.sendFeedback("command.visiblebarriers.time.night", false);
                                                return 0;
                                            }))
                                            .then(ClientCommandManager.literal("midnight").executes(context -> {
                                                VisibleConfig.setForcedTime(18000);
                                                VisibleBarriers.sendFeedback("command.visiblebarriers.time.midnight", false);
                                                return 0;
                                            }))
                                            .then(ClientCommandManager.argument("time", TimeArgumentType.time()).executes(context -> {
                                                int time = IntegerArgumentType.getInteger(context, "time");
                                                VisibleConfig.setForcedTime(time);
                                                VisibleBarriers.sendFeedback("command.visiblebarriers.time.custom", false, time);
                                                return 0;
                                            }))
                                    )
                            )
                            //Settings
                            .then(ClientCommandManager.literal("settings")
                                    //Visible Air
                                    .then(ClientCommandManager.literal("visibleair").executes(context -> {
                                        VisibleConfig.setVisibleAir(!VisibleConfig.isAirVisible());
                                        VisibleBarriers.reloadWorldRenderer();
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.visibleair." + VisibleConfig.isAirVisible(), false);
                                        return 1;
                                    }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                        VisibleConfig.setVisibleAir(BoolArgumentType.getBool(context, "visible"));
                                        VisibleBarriers.reloadWorldRenderer();
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.visibleair." + VisibleConfig.isAirVisible(), false);
                                        return 1;
                                    })))
                                    //Hide Particles
                                    .then(ClientCommandManager.literal("hiddenparticles").executes(context -> {
                                        VisibleConfig.setHideParticles(!VisibleConfig.shouldHideParticles());
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.hiddenparticles." + VisibleConfig.shouldHideParticles(), false);
                                        return 1;
                                    }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                        VisibleConfig.setHideParticles(BoolArgumentType.getBool(context, "visible"));
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.hiddenparticles." + VisibleConfig.shouldHideParticles(), false);
                                        return 1;
                                    })))
                                    //Send Feedback
                                    .then(ClientCommandManager.literal("sendfeedback").executes(context -> {
                                        VisibleConfig.setSendFeedback(!VisibleConfig.shouldSendFeedback());
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.sendfeedback." + VisibleConfig.shouldSendFeedback(), false);
                                        return 1;
                                    }).then(ClientCommandManager.argument("visible", BoolArgumentType.bool()).executes(context -> {
                                        VisibleConfig.setSendFeedback(BoolArgumentType.getBool(context, "visible"));
                                        VisibleBarriers.sendFeedback("command.visiblebarriers.sendfeedback." + VisibleConfig.shouldSendFeedback(), false);
                                        return 1;
                                    })))
                            )
            );
        });
    }
}
