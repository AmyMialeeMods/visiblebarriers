package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.WallBlock;
import net.minecraft.block.enums.WallShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

import java.util.function.Function;

@Mixin(WallBlock.class)
public abstract class ClientWallBlockMixin extends BlockMixin {
    @Shadow
    @Final
    private Function<BlockState, VoxelShape> outlineShapeFunction;

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$makeOutlineVisible(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.isVisibilityEnabled()) {
            var east = state.get(WallBlock.EAST_WALL_SHAPE, WallShape.LOW) == WallShape.NONE;
            var west = state.get(WallBlock.WEST_WALL_SHAPE, WallShape.LOW) == WallShape.NONE;
            var north = state.get(WallBlock.NORTH_WALL_SHAPE, WallShape.LOW) == WallShape.NONE;
            var south = state.get(WallBlock.SOUTH_WALL_SHAPE, WallShape.LOW) == WallShape.NONE;
            if (east && west && north && south && !state.get(WallBlock.UP, false)) {
                cir.setReturnValue(this.outlineShapeFunction.apply(state.withIfExists(WallBlock.UP, true)));
            }
        }
    }
}