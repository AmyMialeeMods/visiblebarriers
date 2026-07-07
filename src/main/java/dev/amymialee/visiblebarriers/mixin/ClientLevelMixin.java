package dev.amymialee.visiblebarriers.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.amymialee.visiblebarriers.VisibleBarriers;
import dev.amymialee.visiblebarriers.VisibleConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public class ClientLevelMixin extends LevelMixin {

    @WrapOperation(method = "addBreakingBlockEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;shouldSpawnTerrainParticles()Z"))
    private boolean visiblebarriers$removeWaterBreakParticles(@NonNull BlockState state, Operation<Boolean> operation) {
        if (state.getBlock() instanceof LiquidBlock) return false;
        return operation.call(state);
    }

    @WrapMethod(method = "tickTime")
    private void visiblebarriers$stopTime(Operation<Void> original) {
        if (!VisibleBarriers.isTimeEnabled()) original.call();
    }

    @WrapOperation(method = "doAnimateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    public void visiblebarriers$removeParticles(ClientLevel world, ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd, Operation<Void> operation) {
        if (!VisibleConfig.shouldHideParticles()) operation.call(world, particle, x, y, z, xd, yd, zd);
    }

    @Override
    protected void visiblebarriers$setRain(float delta, CallbackInfoReturnable<Float> cir) {
        float rain = VisibleBarriers.getWeather().getRain();
        if (rain >= 0.0F) cir.setReturnValue(rain);
    }

    @Override
    protected void visiblebarriers$setThunder(float delta, CallbackInfoReturnable<Float> cir) {
        float thunder = VisibleBarriers.getWeather().getThunder();
        if (thunder >= 0.0F) cir.setReturnValue(thunder);
    }

    @Mixin(ClientLevel.ClientLevelData.class)
    static class ClientLevelDataMixin {
        @Inject(method = "getGameTime", at = @At("HEAD"), cancellable = true)
        private void visiblebarriers$forceTime(CallbackInfoReturnable<Long> cir) {
            if (VisibleBarriers.isTimeEnabled()) cir.setReturnValue(VisibleConfig.getForcedTime());
        }
    }
}