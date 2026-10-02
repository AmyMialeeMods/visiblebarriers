package dev.amymialee.visiblebarriers.mixin.client;

import dev.amymialee.visiblebarriers.VisibleConfig;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
    @Shadow public abstract Block getBlock();

    @Inject(method = "getRenderShape", at = @At("RETURN"), cancellable = true)
    private void visibleBarriers$invisibleModels(CallbackInfoReturnable<RenderShape> cir) {
        if (!VisibleConfig.visibility) return;
        if (cir.getReturnValue() != RenderShape.INVISIBLE || this.getBlock() == Blocks.AIR) return;
        cir.setReturnValue(RenderShape.MODEL);

        // Done like this to not make the walls visible unless the config is enabled.
//            var state = this.asState();
//            if (state.getBlock() instanceof WallBlock) {
//                var east = state.getValueOrElse(WallBlock.EAST, WallSide.LOW) == WallSide.NONE;
//                var west = state.getValueOrElse(WallBlock.WEST, WallSide.LOW) == WallSide.NONE;
//                var north = state.getValueOrElse(WallBlock.NORTH, WallSide.LOW) == WallSide.NONE;
//                var south = state.getValueOrElse(WallBlock.SOUTH, WallSide.LOW) == WallSide.NONE;
//
//                if (east && west && north && south && !state.getValueOrElse(WallBlock.UP, false)) {
//                    cir.setReturnValue(RenderShape.INVISIBLE);
//                }
//            }
    }
}