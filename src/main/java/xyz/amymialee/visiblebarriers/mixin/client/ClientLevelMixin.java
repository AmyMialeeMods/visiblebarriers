package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.VisibleConfig;
import xyz.amymialee.visiblebarriers.mixin.boxing.LevelMixin;

@Mixin(ClientLevel.class)
public class ClientLevelMixin extends LevelMixin {

    @WrapOperation(method = "addBreakingBlockEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;shouldSpawnTerrainParticles()Z"))
    private boolean visibleBarriers$removeWaterBreakParticles(BlockState state, Operation<Boolean> operation) {
        if (state.getBlock() instanceof LiquidBlock) {
            return false;
        }
        return operation.call(state);
    }

    @WrapMethod(method = "tickTime")
    private void visibleBarriers$stopTime(Operation<Void> original) {
        if (!VisibleBarriers.isTimeEnabled()) {
            original.call();
        }
    }

    @WrapOperation(method = "doAnimateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V", ordinal = 0))
    public void visibleBarriers$removeParticles(ClientLevel world, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> operation) {
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

    @Mixin(ClientLevel.ClientLevelData.class)
    static class ClientLevelDataMixin {
        @Inject(method = "getGameTime", at = @At("HEAD"), cancellable = true)
        private void visibleBarriers$forceTime(CallbackInfoReturnable<Long> cir) {
            if (VisibleBarriers.isTimeEnabled()) {
                cir.setReturnValue(VisibleConfig.getForcedTime());
            }
        }
    }
}