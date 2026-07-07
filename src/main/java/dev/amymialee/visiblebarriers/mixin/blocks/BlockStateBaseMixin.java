package dev.amymialee.visiblebarriers.mixin.blocks;

import dev.amymialee.visiblebarriers.VisibleBarriers;
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
public abstract class BlockStateBaseMixin {
    @Shadow public abstract Block getBlock();

    @Inject(method = "getRenderShape", at = @At("RETURN"), cancellable = true)
    private void visiblebarriers$invisibleModels(CallbackInfoReturnable<RenderShape> cir) {
        if (VisibleBarriers.isVisibilityEnabled() && cir.getReturnValue() == RenderShape.INVISIBLE && this.getBlock() != Blocks.AIR) cir.setReturnValue(RenderShape.MODEL);
    }
}