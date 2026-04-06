package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.VisibleConfig;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(LightBlock.class)
public abstract class LightBlockMixin extends BlockMixin {
    @WrapOperation(method = "getShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/CollisionContext;isHoldingItem(Lnet/minecraft/world/item/Item;)Z"))
    public boolean visibleBarriers$visibleOutlineShape(CollisionContext context, Item item, Operation<Boolean> original) {
        if (VisibleBarriers.isVisibilityEnabled() || VisibleBarriers.areLightsEnabled() || context == CollisionContext.empty()) {
            return true;
        }
        return original.call(context, item);
    }

    @Override
    public void visibleBarriers$getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        var isPlayer = context instanceof EntityCollisionContext entityContext && entityContext.getEntity() != null && entityContext.getEntity().isAlwaysTicking();
        var solidLights = VisibleConfig.areLightsSolid();
        if (!isPlayer || !solidLights) {
            cir.setReturnValue(Shapes.empty());
        }
        super.visibleBarriers$getCollisionShape(state, world, pos, context, cir);
    }

    @Override
    public void visibleBarriers$isSideInvisible(BlockState state, BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (VisibleBarriers.isVisibilityEnabled() || VisibleBarriers.areLightsEnabled()) {
            cir.setReturnValue(stateFrom.isSolidRender() || stateFrom.getBlock() == state.getBlock());
        }
    }

}