package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow private float fovMultiplier;

    @Inject(method = "updateFovMultiplier", at = @At(value = "TAIL"))
    private void visibleBarriers$zoom(CallbackInfo ci) {
        if (VisibleBarriers.isHoldingZoom()) {
            this.fovMultiplier *= VisibleBarriers.getZoomModifier();
        }
    }
}