package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import xyz.amymialee.visiblebarriers.VisibleOptions;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @WrapMethod(method = "applyFog")
    private static @Nullable Fog visiblebarriers$hide(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickDelta, Operation<Fog> original) {
        if (VisibleOptions.FOG_ENABLED.get()) return Fog.DUMMY;
        return original.call(camera, fogType, color, viewDistance, thickenFog, tickDelta);
    }
}