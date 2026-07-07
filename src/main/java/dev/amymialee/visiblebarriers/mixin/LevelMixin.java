package dev.amymialee.visiblebarriers.mixin;

import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {
    @Inject(method = "getRainLevel", at = @At("HEAD"), cancellable = true)
    protected void visiblebarriers$setRain(float a, CallbackInfoReturnable<Float> cir) {}

    @Inject(method = "getThunderLevel", at = @At("HEAD"), cancellable = true)
    protected void visiblebarriers$setThunder(float a, CallbackInfoReturnable<Float> cir) {}
}