package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.block.*;
import net.minecraft.block.enums.WallShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.VisibleConfig;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract Block getBlock();

    @Shadow
    public abstract boolean isAir();

    @Shadow
    protected abstract BlockState asBlockState();

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
            } else if (this.getBlock() == Blocks.BUBBLE_COLUMN && VisibleBarriers.areBubbleColumnsEnabled()) {
                cir.setReturnValue(BlockRenderType.MODEL);
            } else if (this.getBlock() == Blocks.STRUCTURE_VOID && VisibleBarriers.areStructureVoidsEnabled()) {
                cir.setReturnValue(BlockRenderType.MODEL);
            } else {
                // Done like this to not make the walls visible unless the config is enabled.
                var state = this.asBlockState();
                if (state.getBlock() instanceof WallBlock) {
                    var east = state.get(WallBlock.EAST_WALL_SHAPE, WallShape.LOW) == WallShape.NONE;
                    var west = state.get(WallBlock.WEST_WALL_SHAPE, WallShape.LOW) == WallShape.NONE;
                    var north = state.get(WallBlock.NORTH_WALL_SHAPE, WallShape.LOW) == WallShape.NONE;
                    var south = state.get(WallBlock.SOUTH_WALL_SHAPE, WallShape.LOW) == WallShape.NONE;

                    if (east && west && north && south && !state.get(WallBlock.UP, false)) {
                        cir.setReturnValue(BlockRenderType.INVISIBLE);
                    }
                }
            }
        }
    }
}