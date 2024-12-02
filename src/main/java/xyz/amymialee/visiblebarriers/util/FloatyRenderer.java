package xyz.amymialee.visiblebarriers.util;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.RotationAxis;

public class FloatyRenderer {
    private final ItemRenderer renderer;
    private ItemStack stack;

    public FloatyRenderer(ItemRenderer renderer, ItemStack stack) {
        this.renderer = renderer;
        this.stack = stack;
    }

    public void render(Entity entity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        this.renderItem(this.stack, entity, matrixStack, vertexConsumerProvider, light);
    }

    private void renderItem(ItemStack stack, Entity entity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        matrixStack.push();
        BakedModel bakedModel = this.renderer.getModel(stack, entity.getWorld(), null, entity.getId());
        matrixStack.translate(0.0D, entity.getHeight() / 2, 0.0D);
        float tickDelta = (entity.age % 20) / 20.0f;
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(-((entity.age + tickDelta) * 8) / 20.0f));
        this.renderer.renderItem(stack, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumerProvider, light, 0, bakedModel);
        matrixStack.pop();
    }

    public ItemStack getItem() {
        return this.stack;
    }

    public void setItem(ItemStack stack) {
        this.stack = stack;
    }
}