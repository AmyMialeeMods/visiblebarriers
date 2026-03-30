package xyz.amymialee.visiblebarriers.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FloatyRenderer {
    private final ItemModelResolver itemModelManager;
    private final RandomSource random = RandomSource.create();
    private final Supplier<ItemStack> fallback;
    private ItemStack stack = null;

    public FloatyRenderer(ItemModelResolver itemModelManager, Supplier<ItemStack> fallback) {
        this.itemModelManager = itemModelManager;
        this.fallback = fallback;
    }

    public void render(Entity entity, PoseStack matrixStack, SubmitNodeCollector queue, int light) {
        this.renderItem(this.getItem(), entity, matrixStack, queue, light);
    }

    private void renderItem(ItemStack stack, @NotNull Entity entity, @NotNull PoseStack matrixStack, SubmitNodeCollector queue, int light) {
        if (Minecraft.getInstance().getCameraEntity() == null) return;
        var state = new ItemEntityRenderState();
        var tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        state.x = Mth.lerp(tickDelta, entity.xOld, entity.getX());
        state.y = Mth.lerp(tickDelta, entity.yOld, entity.getY());
        state.z = Mth.lerp(tickDelta, entity.zOld, entity.getZ());
        state.ageInTicks = entity.tickCount + tickDelta;
        state.boundingBoxWidth = entity.getBbWidth();
        state.boundingBoxHeight = entity.getBbHeight();
        state.eyeHeight = entity.getEyeHeight();
        state.passengerOffset = null;
        state.distanceToCameraSq = Minecraft.getInstance().getCameraEntity().distanceToSqr(entity);
        this.itemModelManager.updateForTopItem(state.item, stack, ItemDisplayContext.GROUND, entity.level(), entity instanceof LivingEntity living ? living : null, entity.getId());
        state.count = 1;
        matrixStack.pushPose();
        matrixStack.translate(0.0D, entity.getBbHeight() / 2, 0.0D);
        matrixStack.mulPose(Axis.YP.rotation(-((entity.tickCount + tickDelta) * 8) / 20.0f));
        ItemEntityRenderer.renderMultipleFromCount(matrixStack, queue, light, state, this.random);
        matrixStack.popPose();
    }

    public ItemStack getItem() {
        if (this.stack == null) {
            this.stack = this.fallback.get();
        }
        return this.stack;
    }

    public void setItem(ItemStack stack) {
        this.stack = stack;
    }
}