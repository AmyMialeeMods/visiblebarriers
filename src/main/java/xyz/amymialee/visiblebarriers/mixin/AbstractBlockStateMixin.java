package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow public abstract Block getBlock();
    @Shadow public abstract boolean isAir();

    @Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
    private void visibleBarriers$invisibleModels(CallbackInfoReturnable<BlockRenderType> cir) {
        if (VisibleBarriers.isVisible()) {
            if (cir.getReturnValue() == BlockRenderType.INVISIBLE && (VisibleBarriers.config.isAirVisible() || !this.isAir())) {
                cir.setReturnValue(BlockRenderType.MODEL);
            }
        } else {
            if (this.getBlock() == Blocks.BARRIER && VisibleBarriers.areBarriersVisible()) {
                cir.setReturnValue(BlockRenderType.MODEL);
            } else if (this.getBlock() == Blocks.LIGHT && VisibleBarriers.areLightsVisible()) {
                cir.setReturnValue(BlockRenderType.MODEL);
            } else if (this.getBlock() == Blocks.STRUCTURE_VOID && VisibleBarriers.areStructureVoidsVisible()) {
                cir.setReturnValue(BlockRenderType.MODEL);
            }
        }
    }
}