package amymialee.visiblebarriers.mixin;

import amymialee.visiblebarriers.VisibleControls;
import net.minecraft.client.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At(value = "TAIL"))
    private void HalfDoors$DetectCustomKeys(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        VisibleControls.keyInput(key, scancode);
    }
}
