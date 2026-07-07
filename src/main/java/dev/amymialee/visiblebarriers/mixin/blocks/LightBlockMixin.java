package dev.amymialee.visiblebarriers.mixin.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.amymialee.visiblebarriers.VisibleBarriers;
import dev.amymialee.visiblebarriers.VisibleConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightBlock.class)
public abstract class LightBlockMixin extends BlockMixin {
    @WrapOperation(method = "getShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/CollisionContext;isHoldingItem(Lnet/minecraft/world/item/Item;)Z"))
    public boolean visiblebarriers$visibleOutlineShape(CollisionContext context, Item item, Operation<Boolean> original) {
        if (VisibleBarriers.isVisibilityEnabled() || context == CollisionContext.empty()) return true;
        return original.call(context, item);
    }

    @Override
    public void visiblebarriers$getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, @NonNull CallbackInfoReturnable<VoxelShape> cir) {
        cir.setReturnValue(Shapes.empty());
    }

    @Override
    public void visiblebarriers$isSideInvisible(BlockState state, @NonNull BlockState stateFrom, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (stateFrom.is((Block) (Object) this)) cir.setReturnValue(true);
    }

    @Override
    public void visiblebarriers$isTranslucent(BlockState state, @NonNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}