package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import xyz.amymialee.visiblebarriers.VisibleConfig;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.mixin.boxing.WorldMixin;

@Debug(export = true)
@Mixin(ClientWorld.class)
public class ClientWorldMixin extends WorldMixin {
    @Inject(method = "tickTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"), cancellable = true)
    private void visibleBarriers$stopTime(CallbackInfo ci) {
        if (VisibleBarriers.isTimeEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method = "randomBlockDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;randomDisplayTick(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    public void visibleBarriers$disableSpecificParticles(int centerX, int centerY, int centerZ, int radius, Random random, Block arg5, BlockPos.Mutable pos, CallbackInfo ci, int i, int j, int k, BlockState blockState) {
        if (VisibleConfig.shouldHideParticles() && (blockState.isOf(Blocks.BARRIER) || blockState.isOf(Blocks.LIGHT))) {
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