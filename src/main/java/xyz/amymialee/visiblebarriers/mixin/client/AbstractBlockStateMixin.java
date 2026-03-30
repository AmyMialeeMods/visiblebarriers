package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.VisibleConfig;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
    @Shadow
    public abstract Block getBlock();

    @Shadow
    public abstract boolean isAir();

    @Shadow
    protected abstract BlockState asState();

    @Inject(method = "getRenderShape", at = @At("RETURN"), cancellable = true)
    private void visibleBarriers$invisibleModels(CallbackInfoReturnable<RenderShape> cir) {
        if (VisibleBarriers.isVisibilityEnabled()) {
            if (cir.getReturnValue() == RenderShape.INVISIBLE && (VisibleConfig.isAirVisible() || !this.isAir())) {
                cir.setReturnValue(RenderShape.MODEL);
            }
        } else {
            if (this.getBlock() == Blocks.BARRIER && VisibleBarriers.areBarriersEnabled()) {
                cir.setReturnValue(RenderShape.MODEL);
            } else if (this.getBlock() == Blocks.LIGHT && VisibleBarriers.areLightsEnabled()) {
                cir.setReturnValue(RenderShape.MODEL);
            } else if (this.getBlock() == Blocks.BUBBLE_COLUMN && VisibleBarriers.areBubbleColumnsEnabled()) {
                cir.setReturnValue(RenderShape.MODEL);
            } else if (this.getBlock() == Blocks.STRUCTURE_VOID && VisibleBarriers.areStructureVoidsEnabled()) {
                cir.setReturnValue(RenderShape.MODEL);
            } else {
                // Done like this to not make the walls visible unless the config is enabled.
                var state = this.asState();
                if (state.getBlock() instanceof WallBlock) {
                    var east = state.getValueOrElse(WallBlock.EAST, WallSide.LOW) == WallSide.NONE;
                    var west = state.getValueOrElse(WallBlock.WEST, WallSide.LOW) == WallSide.NONE;
                    var north = state.getValueOrElse(WallBlock.NORTH, WallSide.LOW) == WallSide.NONE;
                    var south = state.getValueOrElse(WallBlock.SOUTH, WallSide.LOW) == WallSide.NONE;

                    if (east && west && north && south && !state.getValueOrElse(WallBlock.UP, false)) {
                        cir.setReturnValue(RenderShape.INVISIBLE);
                    }
                }
            }
        }
    }
}