package xyz.amymialee.visiblebarriers.mixin.boxing;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviourMixin {
    @Shadow
    public abstract BlockState defaultBlockState();

    @Inject(method = "getStateForPlacement", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$getPlacementState(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
    }
}