package xyz.amymialee.visiblebarriers.mixin.client;

import xyz.amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.client.render.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BakedQuad.class)
public class BakedQuadMixin {
    @Inject(method = "hasColor", at = @At("HEAD"), cancellable = true)
    private void visibleBarriers$hasColors(CallbackInfoReturnable<Boolean> cir) {
        if (VisibleBarriers.areHighlightsEnabled()) {
            cir.setReturnValue(true);
        }
    }
}