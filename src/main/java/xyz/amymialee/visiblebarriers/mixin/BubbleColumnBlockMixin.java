package xyz.amymialee.visiblebarriers.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.BlockPickInteractionAware;
import net.minecraft.block.BlockState;
import net.minecraft.block.BubbleColumnBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(BubbleColumnBlock.class)
public abstract class BubbleColumnBlockMixin extends BlockMixin implements BlockPickInteractionAware {
    @Override
    public void visibleBarriers$getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        ItemStack stack = ctx.getStack();
        BlockStateComponent blockState = stack.getComponents().get(DataComponentTypes.BLOCK_STATE);
        boolean drag = true;
        if (blockState != null) {
            drag = blockState.getValue(Properties.DRAG) == Boolean.TRUE;
        }

        cir.setReturnValue(this.getDefaultState().with(BubbleColumnBlock.DRAG, drag));
    }

    @Override
    public ItemStack getPickedStack(BlockState state, BlockView view, BlockPos pos, PlayerEntity player, HitResult result) {
        ItemStack stack = new ItemStack(VisibleBarriersCommon.BUBBLE_COLUMN_BLOCK_ITEM);
        stack.apply(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT, c -> c.with(Properties.DRAG, Boolean.TRUE));
        return stack;
    }

    @Environment(EnvType.CLIENT)
    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$visibleOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.isVisibilityEnabled() || VisibleBarriers.areBubbleColumnsEnabled() || context == ShapeContext.absent()) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }
}
