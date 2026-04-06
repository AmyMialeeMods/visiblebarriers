package xyz.amymialee.visiblebarriers.mixin.boxing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Items;
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
    public void visibleBarriers$giveRenderer(@NotNull EntityRendererProvider.Context context, CallbackInfo ci) {
        this.floater = new FloatyRenderer(context.getItemModelResolver(), Items.BARRIER::getDefaultInstance);
    }

    @ModifyReturnValue(method = "createRenderState(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;", at = @At("TAIL"))
    private S visibleBarriers$getAndUpdateRenderStateTail(S original, @Local(argsOnly = true) T entity) {
        ((EntityRenderStateAccess) original).visiblebarriers$setEntity(entity);
        return original;
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void visibleBarriers$updateRenderStateTail(T entity, S state, float tickDelta, CallbackInfo ci) {
        ((EntityRenderStateAccess) state).visiblebarriers$setEntity(entity);
    }

    @Inject(method = "submit", at = @At("HEAD"))
    protected void visibleBarriers$renderHead(S renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState, CallbackInfo ci) {
        if (VisibleBarriers.isVisibilityEnabled() && renderState.isInvisible) {
            var entity = ((EntityRenderStateAccess) renderState).visiblebarriers$getEntity();
            if (entity == null) return; // This happens if a mod creates a render state without extracting it from an entity.

            if (entity.getPickResult() != null) {
                var stack = entity.getPickResult();
                if (!this.floater.getItem().is(stack.getItem())) {
                    this.floater.setItem(stack);
                }
                this.floater.render(entity, matrices, queue, renderState.lightCoords);
            } else {
                if (!this.floater.getItem().is(Items.STRUCTURE_VOID)) {
                    this.floater.setItem(Items.STRUCTURE_VOID.getDefaultInstance());
                }
                this.floater.render(entity, matrices, queue, renderState.lightCoords);
            }
        }
    }
}