package xyz.amymialee.visiblebarriers.mixin.boxing;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlockMixin {
    @Shadow public abstract BlockState getDefaultState();

    @Inject(method = "isTranslucent", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$isTranslucent(BlockState state, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {}

    @Inject(method = "getPlacementState", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {}
}