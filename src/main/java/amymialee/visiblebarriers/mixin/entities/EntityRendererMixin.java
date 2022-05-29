package amymialee.visiblebarriers.mixin.entities;

import amymialee.visiblebarriers.VisibleBarriers;
import amymialee.visiblebarriers.util.FloatyRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Unique private FloatyRenderer<T> floater;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void VisibleBarriers$GiveRenderer(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.floater = new FloatyRenderer<>(context.getItemRenderer(), Items.SILVERFISH_SPAWN_EGG.getDefaultStack());
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void VisibleBarriers$UseRenderer(T entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (VisibleBarriers.isVisible() && entity.isInvisible()) {
            if (entity.getPickBlockStack() != null) {
                floater.renderItem(entity.getPickBlockStack(), entity, g, matrixStack, vertexConsumerProvider, i);
            } else {
                floater.render(entity, g, matrixStack, vertexConsumerProvider, i);
            }
        }
    }
}