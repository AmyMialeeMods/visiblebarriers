package amymialee.visiblebarriers.mixin.entities;

import amymialee.visiblebarriers.VisibleBarriers;
import amymialee.visiblebarriers.util.FloatyRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandEntityRenderer.class)
public abstract class ArmorStandEntityRendererMixin extends LivingEntityRenderer<ArmorStandEntity, ArmorStandArmorEntityModel> {
    public ArmorStandEntityRendererMixin(EntityRendererFactory.Context ctx, ArmorStandArmorEntityModel model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Unique private FloatyRenderer<ArmorStandEntity> floater;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void VisibleBarriers$GiveRenderer(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.floater = new FloatyRenderer<>(context.getItemRenderer(), Items.ARMOR_STAND.getDefaultStack());
    }

    @Unique
    public void render(ArmorStandEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (entity.isInvisible() && VisibleBarriers.isVisible()) {
            floater.render(entity, g, matrixStack, vertexConsumerProvider, i);
        }
        super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}