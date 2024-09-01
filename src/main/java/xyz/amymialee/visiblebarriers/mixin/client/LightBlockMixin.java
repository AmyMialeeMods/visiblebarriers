package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.LightBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.VisibleConfig;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(LightBlock.class)
public abstract class LightBlockMixin extends BlockMixin {
    @WrapOperation(method = "getOutlineShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/ShapeContext;isHolding(Lnet/minecraft/item/Item;)Z"))
    public boolean visibleBarriers$visibleOutlineShape(ShapeContext context, Item item, Operation<Boolean> original) {
        if (VisibleBarriers.isVisibilityEnabled() || VisibleBarriers.areLightsEnabled() || context == ShapeContext.absent()) {
            return true;
        }
        return original.call(context, item);
    }

    @Override
    public void visibleBarriers$getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        boolean isPlayer = context instanceof EntityShapeContext entityContext && entityContext.getEntity() != null && entityContext.getEntity().isPlayer();
        boolean solidLights = VisibleConfig.areLightsSolid();
        if (!isPlayer || !solidLights) {
            cir.setReturnValue(VoxelShapes.empty());
        }
        super.visibleBarriers$getCollisionShape(state, world, pos, context, cir);
    }

    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (VisibleBarriers.isVisibilityEnabled() || VisibleBarriers.areLightsEnabled()) {
            cir.setReturnValue(stateFrom.isOpaqueFullCube(null, null) || stateFrom.getBlock() == state.getBlock());
        }
    }

}