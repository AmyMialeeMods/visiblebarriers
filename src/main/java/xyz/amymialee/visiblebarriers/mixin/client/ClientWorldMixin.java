package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.VisibleConfig;
import xyz.amymialee.visiblebarriers.mixin.boxing.WorldMixin;

@Mixin(ClientWorld.class)
public class ClientWorldMixin extends WorldMixin {
    @Inject(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"), cancellable = true)
    private void visibleBarriers$stopTime(CallbackInfo ci) {
        if (VisibleBarriers.isTimeEnabled()) {
            ci.cancel();
        }
    }

    @WrapOperation(method = "randomBlockDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
    public void visibleBarriers$removeParticles(ClientWorld world, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> operation) {
        if (!VisibleConfig.shouldHideParticles()) {
            operation.call(world, parameters, x, y, z, velocityX, velocityY, velocityZ);
        }
    }

    @Override
    protected void visibleBarriers$setRain(float delta, CallbackInfoReturnable<Float> cir) {
        float rain = VisibleBarriers.getWeather().getRain();
        if (rain >= 0.0F) {
            cir.setReturnValue(rain);
        }
    }

    @Override
    protected void visibleBarriers$setThunder(float delta, CallbackInfoReturnable<Float> cir) {
        float thunder = VisibleBarriers.getWeather().getThunder();
        if (thunder >= 0.0F) {
            cir.setReturnValue(thunder);
        }
    }

    @Mixin(ClientWorld.Properties.class)
    static class ClientWorldPropertiesMixin {
        @Inject(method = "getTimeOfDay", at = @At("HEAD"), cancellable = true)
        private void visibleBarriers$forceTime(CallbackInfoReturnable<Long> cir) {
            if (VisibleBarriers.isTimeEnabled()) {
                cir.setReturnValue(VisibleConfig.getForcedTime());
            }
        }
    }
}