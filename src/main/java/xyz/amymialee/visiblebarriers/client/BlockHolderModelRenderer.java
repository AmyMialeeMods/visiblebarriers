package xyz.amymialee.visiblebarriers.client;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.impl.client.rendering.fluid.FluidRenderHandlerInfo;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.FluidRenderer;
import net.minecraft.client.render.block.entity.LoadedBlockEntityModels;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Environment(EnvType.CLIENT)
public class BlockHolderModelRenderer implements SpecialModelRenderer<BlockState> {
    private final BlockRenderManager blockRenderer;

    public BlockHolderModelRenderer(BlockRenderManager blockRenderer) {
        this.blockRenderer = blockRenderer;
    }

    @Override
    public @Nullable BlockState getData(@NotNull ItemStack itemStack) {
        var state = itemStack.getOrDefault(VisibleBarriers.BLOCK, Blocks.AIR).getDefaultState();
        return itemStack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT).applyToState(state);
    }

    @Override
    public void render(@Nullable BlockState state, ModelTransformationMode modelTransformationMode, @NotNull MatrixStack matrices, VertexConsumerProvider provider, int light, int overlay, boolean bl) {
        matrices.push();
        var player = MinecraftClient.getInstance().player;
        if (player != null) {
            matrices.translate(0.5F, 0.5F, 0.5F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(2 * (player.age + MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true))));
            matrices.translate(-0.5F, -0.5F, -0.5F);
        }
        if (state != null) this.renderBlockAsEntity(state, matrices, provider, 0xFFFFFFFF, overlay);
        matrices.pop();
    }

    public void renderBlockAsEntity(BlockState state, @NotNull MatrixStack matrices, @NotNull VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var bakedModel = this.blockRenderer.getModel(state);
        var color = this.blockRenderer.blockColors.getColor(state, null, null, 0);
        var r = (float)(color >> 16 & 0xFF) / 255.0F;
        var g = (float)(color >> 8 & 0xFF) / 255.0F;
        var b = (float)(color & 0xFF) / 255.0F;
        this.blockRenderer.blockModelRenderer.render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(state)), state, bakedModel, r, g, b, light, overlay);
        this.blockRenderer.blockEntityModelsGetter.get().render(state.getBlock(), ModelTransformationMode.NONE, matrices, vertexConsumers, light, overlay);
        if (state.isOf(Blocks.WATER) || state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) {
            var client = MinecraftClient.getInstance();
            var world = client.world;
            var player = client.player;
            if (world == null || player == null) return;
            var pos = player.getBlockPos();
            color = BiomeColors.getWaterColor(world, pos);
            r = (float)(color >> 16 & 0xFF) / 255.0F;
            g = (float)(color >> 8 & 0xFF) / 255.0F;
            b = (float)(color & 0xFF) / 255.0F;
            var water = VisibleBarriers.WATER.getDefaultState();
            var waterModel = this.blockRenderer.getModel(water);
            this.blockRenderer.blockModelRenderer.render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(water)), water, waterModel, r, g, b, light, overlay);
        } else if (state.isOf(Blocks.LAVA)) {
            var lava = VisibleBarriers.LAVA.getDefaultState();
            var lavaModel = this.blockRenderer.getModel(lava);
            this.blockRenderer.blockModelRenderer.render(matrices.peek(), vertexConsumers.getBuffer(RenderLayers.getEntityBlockLayer(lava)), lava, lavaModel, r, g, b, light, overlay);
        }
    }

    @Environment(EnvType.CLIENT)
    public record Unbaked() implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<BlockHolderModelRenderer.Unbaked> CODEC = MapCodec.unit(new BlockHolderModelRenderer.Unbaked());

        @Override
        public MapCodec<BlockHolderModelRenderer.Unbaked> getCodec() {
            return CODEC;
        }

        @Override
        public @NotNull SpecialModelRenderer<?> bake(LoadedEntityModels entityModels) {
            return new BlockHolderModelRenderer(MinecraftClient.getInstance().getBlockRenderManager());
        }
    }
}