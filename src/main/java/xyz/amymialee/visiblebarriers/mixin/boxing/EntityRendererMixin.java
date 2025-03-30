package xyz.amymialee.visiblebarriers.mixin.boxing;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.access.EntityRenderStateAccess;
import xyz.amymialee.visiblebarriers.util.FloatyRenderer;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Shadow
    @Final
    private S state;

    @Unique
    protected FloatyRenderer floater;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void visibleBarriers$giveRenderer(EntityRendererFactory.@NotNull Context context, CallbackInfo ci) {
        this.floater = new FloatyRenderer(context.getItemModelManager(), Items.BARRIER.getDefaultStack());
    }

    @Inject(method = "getAndUpdateRenderState", at = @At("TAIL"))
    private void visibleBarriers$getAndUpdateRenderStateTail(T entity, float tickDelta, CallbackInfoReturnable<S> cir) {
        ((EntityRenderStateAccess) this.state).visiblebarriers$setEntity(entity);
    }

    @Inject(method = "updateRenderState", at = @At("TAIL"))
    private void visibleBarriers$updateRenderStateTail(T entity, S state, float tickDelta, CallbackInfo ci) {
        ((EntityRenderStateAccess) state).visiblebarriers$setEntity(entity);
    }

    @Inject(method = "render", at = @At("HEAD"))
    protected void visibleBarriers$renderHead(EntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (VisibleBarriers.isVisibilityEnabled() && state.invisible) {
            var entity = ((EntityRenderStateAccess) state).visiblebarriers$getEntity();
            if (entity.getPickBlockStack() != null) {
                var stack = entity.getPickBlockStack();
                if (!this.floater.getItem().isOf(stack.getItem())) {
                    this.floater.setItem(stack);
                }
                this.floater.render(entity, matrices, vertexConsumers, light);
            } else {
                if (!this.floater.getItem().isOf(Items.STRUCTURE_VOID)) {
                    this.floater.setItem(Items.STRUCTURE_VOID.getDefaultStack());
                }
                this.floater.render(entity, matrices, vertexConsumers, light);
            }
        }
    }
}