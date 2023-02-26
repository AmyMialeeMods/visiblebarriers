package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleConfig;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.mixin.boxing.WorldMixin;

@Mixin(ClientWorld.class)
public class ClientWorldMixin extends WorldMixin {
    @Inject(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"), cancellable = true)
    private void visibleBarriers$stopTime(CallbackInfo ci) {
        if (VisibleBarriers.isTimeEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method = "doRandomBlockDisplayTicks", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$removeParticles(int centerX, int centerY, int centerZ, CallbackInfo ci) {
        if (VisibleConfig.shouldHideParticles()) {
            ci.cancel();
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