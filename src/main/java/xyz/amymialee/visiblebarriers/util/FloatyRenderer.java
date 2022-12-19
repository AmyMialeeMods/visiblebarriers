package xyz.amymialee.visiblebarriers.util;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.Random;

public class FloatyRenderer<T extends Entity> {
    private final Random random = new Random();
    public float uniqueOffset = this.random.nextFloat() * 3.1415927F * 2.0F;
    private final ItemRenderer renderer;
    private final ItemStack stack;

    public FloatyRenderer(ItemRenderer renderer, ItemStack stack) {
        this.renderer = renderer;
        this.stack = stack;
    }

    public void render(T entity, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.renderItem(this.stack, entity, g, matrixStack, vertexConsumerProvider, i);
    }

    public void renderItem(ItemStack stack, T entity, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        assert stack != null;
        int j = stack.isEmpty() ? 187 : Item.getRawId(stack.getItem()) + stack.getDamage();
        this.random.setSeed(j);
        BakedModel bakedModel = this.renderer.getModel(stack, entity.world, null, entity.getId());
        boolean bl = bakedModel.hasDepth();
        int k = 1;
        float l = MathHelper.sin(((float) entity.age * 8 + g) / 10.0F + this.uniqueOffset) * 0.1F + 0.1F;
        float m = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.getY();
        matrixStack.translate(0.0D, entity.getHeight() / 2, 0.0D);
        matrixStack.translate(0.0D, l + 0.25F * m, 0.0D);
        float n = (((float) entity.age * 8 + g) / 20.0F + this.uniqueOffset);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(-n));
        float o = bakedModel.getTransformation().ground.scale.getX();
        float p = bakedModel.getTransformation().ground.scale.getY();
        float q = bakedModel.getTransformation().ground.scale.getZ();
        float v;
        float w;
        if (!bl) {
            float r = -0.0F * 0 * 0.5F * o;
            v = -0.0F * 0 * 0.5F * p;
            w = -0.09375F * 0 * 0.5F * q;
            matrixStack.translate(r, v, w);
        }
        for (int u = 0; u < k; ++u) {
            matrixStack.push();
            this.renderer.renderItem(stack, ModelTransformation.Mode.GROUND, false, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, bakedModel);
            matrixStack.pop();
            if (!bl) {
                matrixStack.translate(0.0F * o, 0.0F * p, 0.09375F * q);
            }
        }
        matrixStack.pop();
    }
}