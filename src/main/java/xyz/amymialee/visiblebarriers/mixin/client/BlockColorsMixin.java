package xyz.amymialee.visiblebarriers.mixin.client;

import xyz.amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
    @Inject(method = "getParticleColor", at = @At("HEAD"), cancellable = true)
    private void visibleBarriers$particleColors(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (VisibleBarriers.areHighlightsEnabled()) {
            if (VisibleBarriers.config.hasBlock(state.getBlock())) {
                cir.setReturnValue(VisibleBarriers.config.getBlockColor(state.getBlock()));
            }
        }
    }

    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void visibleBarriers$blockColors(BlockState state, BlockRenderView world, BlockPos pos, int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (VisibleBarriers.areHighlightsEnabled()) {
            if (VisibleBarriers.config.hasBlock(state.getBlock())) {
                cir.setReturnValue(VisibleBarriers.config.getBlockColor(state.getBlock()));
            }
        }
    }
}