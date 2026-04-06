package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Marker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "shouldRenderAtSqrDistance(D)Z", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$forceRender(double distance, CallbackInfoReturnable<Boolean> cir) {
        var this2 = (Entity) (Object) this;
        if (VisibleBarriers.isVisibilityEnabled() && this2 instanceof Marker) {
            cir.setReturnValue(true);
        }
    }
}