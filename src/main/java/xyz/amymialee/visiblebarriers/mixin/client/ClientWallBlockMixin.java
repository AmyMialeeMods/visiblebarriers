package xyz.amymialee.visiblebarriers.mixin.client;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@Mixin(WallBlock.class)
public abstract class ClientWallBlockMixin extends BlockMixin {
    @Shadow
    @Final
    private Function<BlockState, VoxelShape> shapes;

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$makeOutlineVisible(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.isVisibilityEnabled()) {
            var east = state.getValueOrElse(WallBlock.EAST, WallSide.LOW) == WallSide.NONE;
            var west = state.getValueOrElse(WallBlock.WEST, WallSide.LOW) == WallSide.NONE;
            var north = state.getValueOrElse(WallBlock.NORTH, WallSide.LOW) == WallSide.NONE;
            var south = state.getValueOrElse(WallBlock.SOUTH, WallSide.LOW) == WallSide.NONE;
            if (east && west && north && south && !state.getValueOrElse(WallBlock.UP, false)) {
                cir.setReturnValue(this.shapes.apply(state.trySetValue(WallBlock.UP, true)));
            }
        }
    }
}