package dev.amymialee.visiblebarriers.mixin.client;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.amymialee.visiblebarriers.VisibleBarriers;
import dev.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import dev.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(MovingPistonBlock.class)
public abstract class ClientMovingPistonBlockMixin extends BlockMixin {
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$makeOutlineVisible(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.isVisibilityEnabled() || context.isHoldingItem(VisibleBarriersCommon.MOVING_PISTON_BLOCK_ITEM)) {
            cir.setReturnValue(Shapes.block());
        }
    }
}