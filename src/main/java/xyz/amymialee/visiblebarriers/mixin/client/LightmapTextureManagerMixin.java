package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.visiblebarriers.VisibleOptions;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {
    @WrapOperation(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float visiblebarriers$gamma(Double number, Operation<Float> original) {
        if (VisibleOptions.GAMMA_ENABLED.get()) return VisibleOptions.GAMMA_VALUE.get();
        return original.call(number);
    }
}