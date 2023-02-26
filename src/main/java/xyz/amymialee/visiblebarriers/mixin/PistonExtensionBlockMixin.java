package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.enums.PistonType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(PistonExtensionBlock.class)
public abstract class PistonExtensionBlockMixin extends BlockMixin {
    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        Block block = (Block) (Object) this;
        if (stateFrom.isOf(block)) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void visibleBarriers$getPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(this.getDefaultState().with(PistonExtensionBlock.FACING, ctx.getPlayerLookDirection().getOpposite()));
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        Item item = player.getStackInHand(hand).getItem();
        if (item == VisibleBarriersCommon.MOVING_PISTON_BLOCK_ITEM && state.contains(PistonExtensionBlock.TYPE)) {
            world.setBlockState(pos, state.with(PistonExtensionBlock.TYPE, state.get(PistonExtensionBlock.TYPE) == PistonType.DEFAULT ? PistonType.STICKY : PistonType.DEFAULT), Block.NOTIFY_LISTENERS);
            cir.setReturnValue(net.minecraft.util.ActionResult.SUCCESS);
        }
    }

    @Inject(method = "getPickStack", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$pickStack(BlockView world, BlockPos pos, BlockState state, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = new ItemStack(VisibleBarriersCommon.MOVING_PISTON_BLOCK_ITEM);
        NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString(PistonExtensionBlock.TYPE.getName(), String.valueOf(state.get(PistonExtensionBlock.TYPE)));
        stack.setSubNbt("BlockStateTag", nbtCompound);
        cir.setReturnValue(stack);
    }
}