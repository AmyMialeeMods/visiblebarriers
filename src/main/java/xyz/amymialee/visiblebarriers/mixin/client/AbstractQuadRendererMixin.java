package xyz.amymialee.visiblebarriers.mixin.client;

import xyz.amymialee.visiblebarriers.VisibleBarriers;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.MutableQuadViewImpl;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.AbstractQuadRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractQuadRenderer.class)
public abstract class AbstractQuadRendererMixin {
    @Shadow protected abstract void colorizeQuad(MutableQuadViewImpl q, int blockColorIndex);

    @Inject(method = "colorizeQuad", at = @At("HEAD"), cancellable = true, remap = false)
    private void visibleBarriers$colorizing(MutableQuadViewImpl q, int blockColorIndex, CallbackInfo ci) {
        if (VisibleBarriers.areHighlightsEnabled() && blockColorIndex == -1) {
            this.colorizeQuad(q, 255);
            ci.cancel();
        }
    }
}