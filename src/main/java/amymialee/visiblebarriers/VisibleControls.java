package amymialee.visiblebarriers;

import amymialee.visiblebarriers.mixin.KeyBindingAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@SuppressWarnings("SameParameterValue")
public class VisibleControls {
    public static final KeyBinding VISIBILITY = createSafeKeyMapping("key.%s.visibility".formatted(VisibleBarriers.MOD_ID), GLFW.GLFW_KEY_B);

    public static void keyInput(int key, int scancode) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            if (VISIBILITY.matchesKey(key, scancode)) {
                VisibleBarriers.visible = !VisibleBarriers.visible;
                if (MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.isSneaking()) {
                    VisibleBarriers.visible_air = VisibleBarriers.visible;
                }
                if (!VisibleBarriers.visible) {
                    VisibleBarriers.visible_air = false;
                }
                MinecraftClient.getInstance().worldRenderer.reload();
            }
        }
    }

    private static KeyBinding createSafeKeyMapping(String description, int keycode) {
        InputUtil.Key key = InputUtil.Type.KEYSYM.createFromCode(keycode);
        KeyBinding oldMapping = KeyBindingAccessor.getKeyToBindings().get(key);
        KeyBinding keyMapping = new KeyBinding(description, keycode, "category.%s".formatted(VisibleBarriers.MOD_ID));
        KeyBindingAccessor.getKeyToBindings().put(key, oldMapping);
        KeyBindingAccessor.getKeysByID().remove(description);
        return keyMapping;
    }
}