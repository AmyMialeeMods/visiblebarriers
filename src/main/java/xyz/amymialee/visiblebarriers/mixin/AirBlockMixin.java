package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(AirBlock.class)
public abstract class AirBlockMixin extends BlockMixin {
    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, @NonNull BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (stateFrom.is((Block) (Object) this)) cir.setReturnValue(true);
    }

    @Override
    public void visibleBarriers$isTranslucent(BlockState state, @NonNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}