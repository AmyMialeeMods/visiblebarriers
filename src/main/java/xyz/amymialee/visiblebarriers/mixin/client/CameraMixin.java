package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    private float fovModifier;

    @Inject(method = "tickFov", at = @At(value = "TAIL"))
    private void visibleBarriers$zoom(CallbackInfo ci) {
        if (VisibleBarriers.isHoldingZoom()) {
            this.fovModifier *= VisibleBarriers.getZoomModifier();
        }
    }
}