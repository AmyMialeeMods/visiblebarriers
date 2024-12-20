package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.visiblebarriers.VisibleOptions;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @WrapOperation(method = "tickTime", at = @At(value = "FIELD", target = "Lnet/minecraft/client/world/ClientWorld;shouldTickTimeOfDay:Z"))
    private boolean visiblebarriers$stay(ClientWorld instance, Operation<Boolean> original) {
        if (VisibleOptions.TIME_ENABLED.get()) return false;
        return original.call(instance);
    }

    @WrapOperation(method = "randomBlockDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private void visiblebarriers$particles(ClientWorld world, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Operation<Void> operation) {
        if (VisibleOptions.HIDE_PARTICLES.get()) return;
        operation.call(world, parameters, x, y, z, velocityX, velocityY, velocityZ);
    }
}