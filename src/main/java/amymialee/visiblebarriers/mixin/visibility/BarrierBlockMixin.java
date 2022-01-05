package amymialee.visiblebarriers.mixin.visibility;

import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.block.BarrierBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BarrierBlock.class)
public class BarrierBlockMixin {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (VisibleBarriers.visible) {
            cir.setReturnValue(BlockRenderType.MODEL);
        }
    }
}
