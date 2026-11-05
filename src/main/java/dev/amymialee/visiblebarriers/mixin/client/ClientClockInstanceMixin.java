package dev.amymialee.visiblebarriers.mixin.client;

import dev.amymialee.visiblebarriers.VisibleConfig;
import net.minecraft.client.ClientClockManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientClockManager.ClientClockInstance.class)
public class ClientClockInstanceMixin {
    @Inject(method = "totalTicks", at = @At("HEAD"), cancellable = true)
    private void visibleBarriers$forceTime(CallbackInfoReturnable<Long> cir) {
        if (VisibleConfig.forcedTimeEnabled) cir.setReturnValue((long) VisibleConfig.forcedTime);
    }

    @Inject(method = "isPaused", at = @At("HEAD"), cancellable = true)
    private void visibleBarriers$forceTimePaused(CallbackInfoReturnable<Boolean> cir) {
        if (VisibleConfig.forcedTimeEnabled) cir.setReturnValue(true);
    }
}