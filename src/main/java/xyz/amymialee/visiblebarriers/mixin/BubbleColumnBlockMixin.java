package xyz.amymialee.visiblebarriers.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.PlayerPickItemEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(BubbleColumnBlock.class)
public abstract class BubbleColumnBlockMixin extends BlockMixin implements PlayerPickItemEvents.PickItemFromBlock {
    @Override
    public void visibleBarriers$getPlacementState(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        var stack = ctx.getItemInHand();
        var blockState = stack.getComponents().get(DataComponents.BLOCK_STATE);
        var drag = true;
        if (blockState != null) {
            drag = blockState.get(BlockStateProperties.DRAG) == Boolean.TRUE;
        }

        cir.setReturnValue(this.defaultBlockState().setValue(BubbleColumnBlock.DRAG_DOWN, drag));
    }

    @Override
    public @Nullable ItemStack onPickItemFromBlock(@NonNull ServerPlayer player, @NonNull BlockPos pos, @NonNull BlockState state, boolean requestIncludeData) {
        var stack = new ItemStack(VisibleBarriersCommon.BUBBLE_COLUMN_BLOCK_ITEM);
        stack.update(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY, c -> c.with(BlockStateProperties.DRAG, Boolean.TRUE));
        return stack;
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$visibleOutlineShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.isVisibilityEnabled() || VisibleBarriers.areBubbleColumnsEnabled() || context == CollisionContext.empty()) {
            cir.setReturnValue(Shapes.block());
        }
    }
}
