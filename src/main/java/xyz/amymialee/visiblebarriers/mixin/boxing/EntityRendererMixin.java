package xyz.amymialee.visiblebarriers.mixin.boxing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.access.EntityRenderStateAccess;
import xyz.amymialee.visiblebarriers.util.FloatyRenderer;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Unique
    protected FloatyRenderer floater;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void visibleBarriers$giveRenderer(EntityRendererFactory.@NotNull Context context, CallbackInfo ci) {
        this.floater = new FloatyRenderer(context.getItemModelManager(), Items.BARRIER.getDefaultStack());
    }

    @ModifyReturnValue(method = "getAndUpdateRenderState", at = @At("TAIL"))
    private S visibleBarriers$getAndUpdateRenderStateTail(S original, @Local(argsOnly = true) T entity) {
        ((EntityRenderStateAccess) original).visiblebarriers$setEntity(entity);
        return original;
    }

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void visibleBarriers$updateRenderStateTail(T entity, S state, float tickDelta, CallbackInfo ci) {
        ((EntityRenderStateAccess) state).visiblebarriers$setEntity(entity);
    }

    @Inject(method = "render", at = @At("HEAD"))
    protected void visibleBarriers$renderHead(S renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState, CallbackInfo ci) {
        if (VisibleBarriers.isVisibilityEnabled() && renderState.invisible) {
            var entity = ((EntityRenderStateAccess) renderState).visiblebarriers$getEntity();
            if (entity == null) return; // This happens if a mod creates a render state without extracting it from an entity.

            if (entity.getPickBlockStack() != null) {
                var stack = entity.getPickBlockStack();
                if (!this.floater.getItem().isOf(stack.getItem())) {
                    this.floater.setItem(stack);
                }
                this.floater.render(entity, matrices, queue, renderState.light);
            } else {
                if (!this.floater.getItem().isOf(Items.STRUCTURE_VOID)) {
                    this.floater.setItem(Items.STRUCTURE_VOID.getDefaultStack());
                }
                this.floater.render(entity, matrices, queue, renderState.light);
            }
        }
    }
}