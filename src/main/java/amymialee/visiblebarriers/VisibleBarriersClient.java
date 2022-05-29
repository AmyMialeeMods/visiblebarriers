package amymialee.visiblebarriers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class VisibleBarriersClient implements ClientModInitializer {
    private static KeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.BARRIER, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.STRUCTURE_VOID, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.LIGHT, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.AIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.CAVE_AIR, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(Blocks.VOID_AIR, RenderLayer.getTranslucent());

        keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.visiblebarriers.bind",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.visiblebarriers.bind"
        ));
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
                VisibleBarriers.visible = !VisibleBarriers.visible;
                if (Screen.hasShiftDown()) {
                    if (VisibleBarriers.visible) {
                        VisibleBarriers.visibleAir = true;
                    }
                }
                if (!VisibleBarriers.visible) {
                    VisibleBarriers.visibleAir = false;
                }
                client.worldRenderer.reload();
            }
        });
    }
}