package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleConfig;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow public abstract Block getBlock();
    @Shadow public abstract boolean isAir();

    @Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
    private void visibleBarriers$invisibleModels(CallbackInfoReturnable<BlockRenderType> cir) {
        if (VisibleBarriers.isVisibilityEnabled()) {
            if (cir.getReturnValue() == BlockRenderType.INVISIBLE && (VisibleConfig.isAirVisible() || !this.isAir())) {
                cir.setReturnValue(BlockRenderType.MODEL);
            }
        } else {
            if (this.getBlock() == Blocks.BARRIER && VisibleBarriers.areBarriersEnabled()) {
                cir.setReturnValue(BlockRenderType.MODEL);
            } else if (this.getBlock() == Blocks.LIGHT && VisibleBarriers.areLightsEnabled()) {
                cir.setReturnValue(BlockRenderType.MODEL);
            } else if (this.getBlock() == Blocks.STRUCTURE_VOID && VisibleBarriers.areStructureVoidsEnabled()) {
                cir.setReturnValue(BlockRenderType.MODEL);
            }
        }
    }
}