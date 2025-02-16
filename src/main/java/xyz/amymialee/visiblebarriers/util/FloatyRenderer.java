package xyz.amymialee.visiblebarriers.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.entity.state.ItemEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.NotNull;

public class FloatyRenderer {
    private final ItemModelManager itemModelManager;
    private final Random random = Random.create();
    private ItemStack stack;

    public FloatyRenderer(ItemModelManager itemModelManager, ItemStack stack) {
        this.itemModelManager = itemModelManager;
        this.stack = stack;
    }

    public void render(Entity entity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        this.renderItem(this.stack, entity, matrixStack, vertexConsumerProvider, light);
    }

    private void renderItem(ItemStack stack, @NotNull Entity entity, @NotNull MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        if (MinecraftClient.getInstance().cameraEntity == null) return;
        var state = new ItemEntityRenderState();
        var tickDelta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
        state.x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
        state.y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
        state.z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
        state.age = (float)entity.age + tickDelta;
        state.width = entity.getWidth();
        state.height = entity.getHeight();
        state.standingEyeHeight = entity.getStandingEyeHeight();
        state.positionOffset = null;
        state.squaredDistanceToCamera = MinecraftClient.getInstance().cameraEntity.squaredDistanceTo(entity);
        this.itemModelManager.update(state.itemRenderState, stack, ModelTransformationMode.GROUND, false, entity.getWorld(), null, entity.getId());
        state.renderedAmount = 1;
        matrixStack.push();
        matrixStack.translate(0.0D, entity.getHeight() / 2, 0.0D);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(-((entity.age + tickDelta) * 8) / 20.0f));
        ItemEntityRenderer.renderStack(matrixStack, vertexConsumerProvider, light, state, this.random);
        matrixStack.pop();
    }

    public ItemStack getItem() {
        return this.stack;
    }

    public void setItem(ItemStack stack) {
        this.stack = stack;
    }
}