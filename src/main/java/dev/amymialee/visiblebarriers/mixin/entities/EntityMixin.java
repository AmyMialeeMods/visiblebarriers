package dev.amymialee.visiblebarriers.mixin.entities;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "shouldRenderAtSqrDistance(D)Z", at = @At("HEAD"), cancellable = true)
    public void visiblebarriers$anyDistance(double distance, CallbackInfoReturnable<Boolean> cir) {
        var this2 = (Entity) (Object) this;
        if (VisibleBarriers.isVisibilityEnabled() && this2 instanceof Marker) cir.setReturnValue(true);
    }

    @WrapMethod(method = "isInvisibleTo")
    public boolean visiblebarriers$invisibility(Player player, Operation<Boolean> original) {
        if (VisibleBarriers.isVisibilityEnabled()) return false;
        return original.call(player);
    }
}