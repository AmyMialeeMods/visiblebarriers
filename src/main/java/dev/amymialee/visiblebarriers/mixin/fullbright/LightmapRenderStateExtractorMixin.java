package dev.amymialee.visiblebarriers.mixin.fullbright;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightmapRenderStateExtractor.class)
public class LightmapRenderStateExtractorMixin {
    @WrapOperation(method = "extract", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 0))
    private float visiblebarriers$fullBright(Double number, Operation<Float> original) {
        return VisibleBarriers.isFullBrightEnabled() ? 255f : original.call(number);
    }
}
