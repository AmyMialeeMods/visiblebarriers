package xyz.amymialee.visiblebarriers.model;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.BlockStateModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TransparentBlockStateModel extends WrapperBlockStateModel {

    private final float transparency;

    protected TransparentBlockStateModel(BlockStateModel wrapped, float transparency) {
        super(wrapped);
        this.transparency = transparency;
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockRenderView blockView, BlockPos pos, BlockState state, Random random, Predicate<@Nullable Direction> cullTest) {
        emitter = new TransparentQuadEmitter(emitter, this.transparency);
        super.emitQuads(emitter, blockView, pos, state, random, cullTest);
    }

    public record Unbaked(
            BlockStateModel.Unbaked unbaked,
            float transparency
    ) implements CustomUnbakedBlockStateModel {

        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BlockStateModel.Unbaked.CODEC.fieldOf("model").forGetter(Unbaked::unbaked),
                Codec.FLOAT.optionalFieldOf("transparency", 0.5f).forGetter(Unbaked::transparency)
        ).apply(instance, Unbaked::new));

        @Override
        public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
            return CODEC;
        }

        @Override
        public BlockStateModel bake(Baker baker) {
            return new TransparentBlockStateModel(this.unbaked.bake(baker), this.transparency);
        }

        @Override
        public void resolve(Resolver resolver) {
            this.unbaked.resolve(resolver);
        }
    }
}
