package amymialee.visiblebarriers.mixin.entities;

import amymialee.visiblebarriers.VisibleBarriers;
import amymialee.visiblebarriers.util.FloatyRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmptyEntityRenderer.class)
public abstract class EmptyEntityRendererMixin<T extends Entity> extends EntityRenderer<T> {
    protected EmptyEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Unique private FloatyRenderer<T> floater;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void VisibleBarriers$GiveRenderer(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.floater = new FloatyRenderer<>(context.getItemRenderer(), Items.BARRIER.getDefaultStack());
    }

    @Unique
    public void render(T entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (VisibleBarriers.isVisible()) {
            if (entity instanceof AreaEffectCloudEntity potionEntity) {
                ItemStack potion = Items.LINGERING_POTION.getDefaultStack().copy();
                PotionUtil.setPotion(potion, potionEntity.getPotion());
                floater.renderItem(potion, entity, g, matrixStack, vertexConsumerProvider, i);
            } else if (entity instanceof MarkerEntity) {
                floater.renderItem(Items.STRUCTURE_BLOCK.getDefaultStack(), entity, g, matrixStack, vertexConsumerProvider, i);
            } else {
                floater.render(entity, g, matrixStack, vertexConsumerProvider, i);
            }
        }
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}