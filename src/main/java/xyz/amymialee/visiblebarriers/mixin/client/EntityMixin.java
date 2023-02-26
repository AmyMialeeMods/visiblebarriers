package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MarkerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "shouldRender(D)Z", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$forceRender(double distance, CallbackInfoReturnable<Boolean> cir) {
        Entity this2 = (Entity) (Object) this;
        if (VisibleBarriers.isVisibilityEnabled() && this2 instanceof MarkerEntity) {
            cir.setReturnValue(true);
        }
    }
}