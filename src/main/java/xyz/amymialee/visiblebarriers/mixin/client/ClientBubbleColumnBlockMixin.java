package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(BubbleColumnBlock.class)
public abstract class ClientBubbleColumnBlockMixin extends BlockMixin {
    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, @NotNull BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (stateFrom.isOf((Block) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void visibleBarriers$isTranslucent(BlockState state, @NotNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
