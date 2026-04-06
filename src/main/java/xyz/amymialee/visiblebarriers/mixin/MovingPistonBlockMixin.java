package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(MovingPistonBlock.class)
public abstract class MovingPistonBlockMixin extends BlockMixin {
    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        var block = (Block) (Object) this;
        if (stateFrom.is(block)) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void visibleBarriers$getPlacementState(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(this.defaultBlockState().setValue(MovingPistonBlock.FACING, ctx.getNearestLookingDirection().getOpposite()));
    }

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$onUse(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        var item = player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        if (item == VisibleBarriersCommon.MOVING_PISTON_BLOCK_ITEM && state.hasProperty(MovingPistonBlock.TYPE)) {
            world.setBlock(pos, state.setValue(MovingPistonBlock.TYPE, state.getValue(MovingPistonBlock.TYPE) == PistonType.DEFAULT ? PistonType.STICKY : PistonType.DEFAULT), Block.UPDATE_CLIENTS);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }

    @Inject(method = "getCloneItemStack", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$pickStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData, @NotNull CallbackInfoReturnable<ItemStack> cir) {
        var stack = new ItemStack(VisibleBarriersCommon.MOVING_PISTON_BLOCK_ITEM);
        stack.update(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY, c -> c.with(BlockStateProperties.PISTON_TYPE, state.getValue(MovingPistonBlock.TYPE)));
        cir.setReturnValue(stack);
    }
}