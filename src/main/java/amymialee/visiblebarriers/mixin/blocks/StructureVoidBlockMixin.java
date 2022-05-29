package amymialee.visiblebarriers.mixin.blocks;

import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.StructureVoidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StructureVoidBlock.class)
public class StructureVoidBlockMixin {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void VisibleBarriers$MakeVisible(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (VisibleBarriers.isVisible()) {
            cir.setReturnValue(BlockRenderType.MODEL);
        }
    }
}