package xyz.amymialee.visiblebarriers.util;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

public class FloatyRenderer<T extends Entity> {
    private final ItemRenderer renderer;
    private ItemStack stack;

    public FloatyRenderer(ItemRenderer renderer, ItemStack stack) {
        this.renderer = renderer;
        this.stack = stack;
    }

    public void render(T entity, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.renderItem(this.stack, entity, g, matrixStack, vertexConsumerProvider, i);
    }

    public void renderItem(ItemStack stack, T entity, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        BakedModel bakedModel = this.renderer.getModel(stack, entity.world, null, entity.getId());
        matrixStack.translate(0.0D, entity.getHeight() / 2, 0.0D);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(-((entity.age + g) * 8) / 20.0f));
        this.renderer.renderItem(stack, ModelTransformationMode.GROUND, false, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, bakedModel);
        matrixStack.pop();
    }

    public ItemStack getItem() {
        return this.stack;
    }

    public void setItem(ItemStack stack) {
        this.stack = stack;
    }
}