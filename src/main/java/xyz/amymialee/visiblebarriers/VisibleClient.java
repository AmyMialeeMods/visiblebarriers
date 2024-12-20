package xyz.amymialee.visiblebarriers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import org.lwjgl.glfw.GLFW;
import xyz.amymialee.visiblebarriers.networking.ModInstalledPacket;

@Environment(EnvType.CLIENT)
public class VisibleClient implements ClientModInitializer {
    public static final KeyBinding KEYBIND_VISIBILITY = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.visiblebarriers.visible", GLFW.GLFW_KEY_B, VisibleBarriers.MOD_ID));
    public static final KeyBinding KEYBIND_WEATHER = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.visiblebarriers.weather", GLFW.GLFW_KEY_UNKNOWN, VisibleBarriers.MOD_ID));
    public static final KeyBinding KEYBIND_GAMMA = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.visiblebarriers.gamma", GLFW.GLFW_KEY_UNKNOWN, VisibleBarriers.MOD_ID));
    public static final KeyBinding KEYBIND_FOG = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.visiblebarriers.fog", GLFW.GLFW_KEY_UNKNOWN, VisibleBarriers.MOD_ID));
    public static final KeyBinding KEYBIND_TIME = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.visiblebarriers.time", GLFW.GLFW_KEY_UNKNOWN, VisibleBarriers.MOD_ID));

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> ClientPlayNetworking.send(new ModInstalledPacket()));
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), VisibleBarriers.WATER, VisibleBarriers.LAVA, Blocks.BARRIER, Blocks.LIGHT, Blocks.STRUCTURE_VOID, Blocks.MOVING_PISTON, Blocks.BUBBLE_COLUMN, Blocks.VOID_AIR, Blocks.CAVE_AIR, Blocks.AIR);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KEYBIND_VISIBILITY.wasPressed()) VisibleOptions.VISIBILITY_ENABLED.send(!VisibleOptions.VISIBILITY_ENABLED.get());
            if (KEYBIND_WEATHER.wasPressed()) VisibleOptions.WEATHER_ENABLED.send(!VisibleOptions.WEATHER_ENABLED.get());
            if (KEYBIND_GAMMA.wasPressed()) VisibleOptions.GAMMA_ENABLED.send(!VisibleOptions.GAMMA_ENABLED.get());
            if (KEYBIND_FOG.wasPressed()) VisibleOptions.FOG_ENABLED.send(!VisibleOptions.FOG_ENABLED.get());
            if (KEYBIND_TIME.wasPressed()) VisibleOptions.TIME_ENABLED.send(!VisibleOptions.TIME_ENABLED.get());
        });
    }
}