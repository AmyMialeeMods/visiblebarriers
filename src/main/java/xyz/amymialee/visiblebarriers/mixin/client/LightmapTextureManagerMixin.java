package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float visibleBarriers$fullBright(Double number, Operation<Float> original) {
        if (VisibleBarriers.config.isFullBright()) {
            return 255f;
        }
        return original.call(number);
    }
}
