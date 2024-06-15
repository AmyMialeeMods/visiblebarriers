package xyz.amymialee.visiblebarriers.mixin.boxing;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.util.FloatyRenderer;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Unique
    protected FloatyRenderer<T> floater;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void visibleBarriers$giveRenderer(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.floater = new FloatyRenderer<>(context.getItemRenderer(), Items.BARRIER.getDefaultStack());
    }

    @Inject(method = "render", at = @At("HEAD"))
    protected void visibleBarriers$renderHead(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (VisibleBarriers.isVisibilityEnabled() && entity.isInvisible()) {
            if (entity.getPickBlockStack() != null) {
                ItemStack stack = entity.getPickBlockStack();
                if (!this.floater.getItem().isOf(stack.getItem())) {
                    this.floater.setItem(stack);
                }
                this.floater.render(entity, tickDelta, matrices, vertexConsumers, light);
            } else {
                if (!this.floater.getItem().isOf(Items.STRUCTURE_VOID)) {
                    this.floater.setItem(Items.STRUCTURE_VOID.getDefaultStack());
                }
                this.floater.render(entity, tickDelta, matrices, vertexConsumers, light);
            }
        }
    }
}