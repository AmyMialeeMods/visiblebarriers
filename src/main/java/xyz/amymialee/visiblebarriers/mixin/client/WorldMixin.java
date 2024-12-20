package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import xyz.amymialee.visiblebarriers.VisibleOptions;

@Mixin(World.class)
public class WorldMixin {
    @WrapMethod(method = "getRainGradient")
    private float visiblebarriers$rain(float delta, Operation<Float> original) {
        if (VisibleOptions.WEATHER_ENABLED.get()) return VisibleOptions.WEATHER_STATE.get().getRain();
        return original.call(delta);
    }

    @WrapMethod(method = "getThunderGradient")
    private float visiblebarriers$thunder(float delta, Operation<Float> original) {
        if (VisibleOptions.WEATHER_ENABLED.get()) return VisibleOptions.WEATHER_STATE.get().getThunder();
        return original.call(delta);
    }
}