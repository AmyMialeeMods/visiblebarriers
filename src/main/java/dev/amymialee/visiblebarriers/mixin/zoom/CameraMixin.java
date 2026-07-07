package dev.amymialee.visiblebarriers.mixin.zoom;

import dev.amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow private float fovModifier;

    @Inject(method = "tickFov", at = @At(value = "TAIL"))
    private void visiblebarriers$zoom(CallbackInfo ci) {
        if (VisibleBarriers.isHoldingZoom()) this.fovModifier *= VisibleBarriers.getZoomModifier();
    }
}