package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.Builder.class)
public class EntityTypeBuilderMixin {
    @Shadow
    private int clientTrackingRange;

    @Inject(method = "clientTrackingRange", at = @At("RETURN"))
    public void visibleBarriers$minTrackingRange(int maxTrackingRange, CallbackInfoReturnable<EntityType.Builder<Entity>> cir) {
        if (this.clientTrackingRange == 0) {
            this.clientTrackingRange = 2;
        }
    }
}