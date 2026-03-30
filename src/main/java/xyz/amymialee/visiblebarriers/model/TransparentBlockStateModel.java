//package xyz.amymialee.visiblebarriers.model;
//
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.MapCodec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
//import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
//import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
//import net.minecraft.client.renderer.block.model.BlockStateModel;
//import net.minecraft.client.resources.model.ModelBaker;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.level.BlockAndTintGetter;
//import net.minecraft.world.level.block.state.BlockState;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.function.Predicate;
//
//public class TransparentBlockStateModel extends WrapperBlockStateModel {
//
//    private final float transparency;
//
//    protected TransparentBlockStateModel(BlockStateModel wrapped, float transparency) {
//        super(wrapped);
//        this.transparency = transparency;
//    }
//
//    @Override
//    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter blockView, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
//        emitter = new TransparentQuadEmitter(emitter, this.transparency);
//        super.emitQuads(emitter, blockView, pos, state, random, cullTest);
//    }
//
//    public record Unbaked(
//            BlockStateModel.Unbaked unbaked,
//            float transparency
//    ) implements CustomUnbakedBlockStateModel {
//
//        public static final MapCodec<xyz.amymialee.visiblebarriers.model.TransparentBlockStateModel.Unbaked> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
//                BlockStateModel.Unbaked.CODEC.fieldOf("model").forGetter(xyz.amymialee.visiblebarriers.model.TransparentBlockStateModel.Unbaked::unbaked),
//                Codec.FLOAT.optionalFieldOf("transparency", 0.5f).forGetter(xyz.amymialee.visiblebarriers.model.TransparentBlockStateModel.Unbaked::transparency)
//        ).apply(instance, xyz.amymialee.visiblebarriers.model.TransparentBlockStateModel.Unbaked::new));
//
//        @Override
//        public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
//            return CODEC;
//        }
//
//        @Override
//        public BlockStateModel bake(ModelBaker baker) {
//            return new TransparentBlockStateModel(this.unbaked.bake(baker), this.transparency);
//        }
//
//        @Override
//        public void resolveDependencies(Resolver resolver) {
//            this.unbaked.resolveDependencies(resolver);
//        }
//    }
//}
