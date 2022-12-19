package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.LightBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(LightBlock.class)
public class LightBlockMixin extends BlockMixin {
    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$visibleOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.areLightsVisible()) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }

    @Override
    public void visibleBarriers$getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.areLightsVisible() && !VisibleBarriers.config.areLightsSolid()) {
            cir.setReturnValue(VoxelShapes.empty());
        }
        super.visibleBarriers$getCollisionShape(state, world, pos, context, cir);
    }
}