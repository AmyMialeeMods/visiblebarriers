package dev.amymialee.visiblebarriers.mixin.client;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.amymialee.visiblebarriers.VisibleBarriers;
import dev.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(BarrierBlock.class)
public abstract class ClientBarrierBlockMixin extends BlockMixin {

    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (VisibleBarriers.isVisibilityEnabled() || VisibleBarriers.areBarriersEnabled()) {
            cir.setReturnValue(stateFrom.isSolidRender() || stateFrom.getBlock() == state.getBlock());
        }
    }

}