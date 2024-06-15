package xyz.amymialee.visiblebarriers.mixin.boxing;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
    @Inject(method = "getRainGradient", at = @At("HEAD"), cancellable = true)
    protected void visibleBarriers$setRain(float delta, CallbackInfoReturnable<Float> cir) {
    }

    @Inject(method = "getThunderGradient", at = @At("HEAD"), cancellable = true)
    protected void visibleBarriers$setThunder(float delta, CallbackInfoReturnable<Float> cir) {
    }
}