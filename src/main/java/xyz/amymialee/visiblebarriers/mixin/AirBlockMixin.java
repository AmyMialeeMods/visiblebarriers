package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(AirBlock.class)
public abstract class AirBlockMixin extends BlockMixin {
    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (stateFrom.isOf((Block) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void visibleBarriers$isTranslucent(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}