package dev.amymialee.visiblebarriers.mixin.client;

import dev.amymialee.visiblebarriers.VisibleConfig;
import dev.amymialee.visiblebarriers.mixin.boxing.BlockMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(WallBlock.class)
public abstract class ClientWallBlockMixin extends BlockMixin {
    @Shadow @Final private Function<BlockState, VoxelShape> shapes;

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$makeOutlineVisible(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (!VisibleConfig.visibility) return;
        var east = state.getValueOrElse(WallBlock.EAST, WallSide.LOW) == WallSide.NONE;
        var west = state.getValueOrElse(WallBlock.WEST, WallSide.LOW) == WallSide.NONE;
        var north = state.getValueOrElse(WallBlock.NORTH, WallSide.LOW) == WallSide.NONE;
        var south = state.getValueOrElse(WallBlock.SOUTH, WallSide.LOW) == WallSide.NONE;
        if (!east || !west || !north || !south || state.getValueOrElse(WallBlock.UP, false)) return;
        cir.setReturnValue(this.shapes.apply(state.trySetValue(WallBlock.UP, true)));
    }
}