package amymialee.visiblebarriers.mixin.blocks;

import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightBlock.class)
public class LightBlockMixin {
    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void VisibleBarriers$AdventureBan(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient) {
            if (!player.canModifyBlocks()) {
                cir.setReturnValue(ActionResult.SUCCESS);
            }
        }
    }

    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void VisibleBarriers$MakeVisible(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (VisibleBarriers.isVisible()) {
            cir.setReturnValue(BlockRenderType.MODEL);
        }
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static AbstractBlock.Settings VisibleBarriers$NoCollision(AbstractBlock.Settings settings) {
        return settings.noCollision();
    }

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void VisibleBarriers$MakeOutlineVisible(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.isVisible()) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }
}