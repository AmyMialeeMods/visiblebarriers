package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.block.PistonExtensionBlock;
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
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(PistonExtensionBlock.class)
public abstract class ClientPistonExtensionBlockMixin extends BlockMixin {
    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$makeOutlineVisible(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.isVisibilityEnabled() || context.isHolding(VisibleBarriersCommon.MOVING_PISTON_BLOCK_ITEM)) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }
}