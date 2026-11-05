package dev.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.amymialee.visiblebarriers.VisibleConfig;
import dev.amymialee.visiblebarriers.mixin.boxing.LevelMixin;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public class ClientLevelMixin extends LevelMixin {

    @WrapMethod(method = "tickTime")
    private void visibleBarriers$stopTime(Operation<Void> original) {
        if (!VisibleConfig.forcedTimeEnabled) original.call();
    }

    @WrapOperation(method = "doAnimateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    public void visibleBarriers$removeParticles(ClientLevel world, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> operation) {
        if (!VisibleConfig.hideParticles) operation.call(world, particle, x, y, z, xd, yd, zd);
    }

    @Override
    protected void visibleBarriers$setRain(float delta, CallbackInfoReturnable<Float> cir) {
        float rain = VisibleConfig.forcedWeather.getRain();
        if (rain >= 0.0F) {
            cir.setReturnValue(rain);
        }
    }

    @Override
    protected void visibleBarriers$setThunder(float delta, CallbackInfoReturnable<Float> cir) {
        float thunder = VisibleConfig.forcedWeather.getThunder();
        if (thunder >= 0.0F) {
            cir.setReturnValue(thunder);
        }
    }

    @Mixin(ClientLevel.ClientLevelData.class)
    static class ClientLevelDataMixin {
        @Inject(method = "getGameTime", at = @At("HEAD"), cancellable = true)
        private void visibleBarriers$forceTime(CallbackInfoReturnable<Long> cir) {
            if (VisibleConfig.forcedTimeEnabled) cir.setReturnValue((long) VisibleConfig.forcedTime);
        }
    }
}