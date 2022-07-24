package amymialee.visiblebarriers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
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

        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> VisibleBarriers.visible ? 11546150 : -1, Blocks.INFESTED_STONE, Blocks.INFESTED_DEEPSLATE,
                Blocks.INFESTED_COBBLESTONE, Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);

        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> VisibleBarriers.visible ? 11546150 : -1, Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER,
                Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER, Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER,
                Blocks.WAXED_OXIDIZED_CUT_COPPER, Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);

        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> VisibleBarriers.visible ? 11546150 : -1, Blocks.PETRIFIED_OAK_SLAB);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> VisibleBarriers.visible ? 11546150 : -1, Blocks.INFESTED_STONE, Blocks.INFESTED_DEEPSLATE,
                Blocks.INFESTED_COBBLESTONE, Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> VisibleBarriers.visible ? 11546150 : -1, Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER,
                Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER, Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER,
                Blocks.WAXED_OXIDIZED_CUT_COPPER, Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB,
                Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> VisibleBarriers.visible ? 11546150 : -1, Blocks.PETRIFIED_OAK_SLAB);

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