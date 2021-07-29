package amymialee.visiblebarriers.mixin;

import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightBlock.class)
public class LightBlockMixin {
    @Shadow @Final public static IntProperty LEVEL_15;

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0)
    private static AbstractBlock.Settings injected(AbstractBlock.Settings settings) {
        return settings.noCollision();
    }

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient && player.getStackInHand(hand).getItem() == Items.LIGHT) {
            world.setBlockState(pos, state.cycle(LEVEL_15), Block.NOTIFY_LISTENERS);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            cir.setReturnValue(ActionResult.CONSUME);
        }
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (VisibleBarriers.visible) {
            cir.setReturnValue(BlockRenderType.MODEL);
        }
    }

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.visible) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }
}
