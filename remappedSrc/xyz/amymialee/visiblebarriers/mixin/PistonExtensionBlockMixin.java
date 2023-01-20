package xyz.amymialee.visiblebarriers.mixin;

import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.PistonType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonExtensionBlock.class)
public abstract class PistonExtensionBlockMixin extends BlockMixin {
    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (stateFrom.isOf((Block) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$makeOutlineVisible(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.isVisible()) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }

    @Inject(method = "getPickStack", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$pickStack(BlockView world, BlockPos pos, BlockState state, CallbackInfoReturnable<ItemStack> cir) {
        if (VisibleBarriers.isVisible()) {
            if (state.contains(PistonExtensionBlock.TYPE) && state.get(PistonExtensionBlock.TYPE) == PistonType.DEFAULT) {
                cir.setReturnValue(new ItemStack(Blocks.PISTON));
            } else {
                cir.setReturnValue(new ItemStack(Blocks.STICKY_PISTON));
            }
        }
    }
}