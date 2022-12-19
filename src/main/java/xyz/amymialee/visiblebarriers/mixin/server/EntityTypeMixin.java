package xyz.amymialee.visiblebarriers.mixin.server;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.Builder.class)
public class EntityTypeMixin {
    @Shadow private int maxTrackingRange;

    @Inject(method = "maxTrackingRange", at = @At("RETURN"))
    public void visibleBarriers$minTrackingRange(int maxTrackingRange, CallbackInfoReturnable<EntityType.Builder<Entity>> cir) {
        if (this.maxTrackingRange == 0) {
            this.maxTrackingRange = 4;
        }
    }
}