package amymialee.visiblebarriers.mixin.blocks;

import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("deprecation")
@Mixin(AirBlock.class)
public abstract class AirBlockMixin extends Block {
    public AirBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void VisibleBarriers$MakeVisible(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (VisibleBarriers.isVisibleAir()) {
            cir.setReturnValue(BlockRenderType.MODEL);
        }
    }

    @Unique
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
    }

    @Unique
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }
}