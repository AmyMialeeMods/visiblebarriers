package dev.amymialee.visiblebarriers.mixin.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MovingPistonBlock.class)
public abstract class MovingPistonBlockMixin extends BlockMixin {
    @Override
    public void visiblebarriers$isSideInvisible(BlockState state, @NonNull BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (stateFrom.is((Block) (Object) this)) cir.setReturnValue(true);
    }

    @Override
    public void visiblebarriers$isTranslucent(BlockState state, @NonNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}