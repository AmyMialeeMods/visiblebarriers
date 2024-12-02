package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.block.*;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(AirBlock.class)
public abstract class AirBlockMixin extends BlockMixin {
    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (stateFrom.isOf((Block) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    @Override
    public void visibleBarriers$isTranslucent(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "getOutlineShape", at = @At(value = "HEAD"), cancellable = true)
    public void visibleBarriers$visibleOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if ((this.asItem() != Items.AIR && context.isHolding(this.asItem())) || context == ShapeContext.absent()) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }

    @Override
    public void visibleBarriers$getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        boolean isPlayer = context instanceof EntityShapeContext entityContext && entityContext.getEntity() != null && entityContext.getEntity().isPlayer();
        if (!isPlayer) {
            cir.setReturnValue(VoxelShapes.empty());
        }
        super.visibleBarriers$getCollisionShape(state, world, pos, context, cir);
    }
}