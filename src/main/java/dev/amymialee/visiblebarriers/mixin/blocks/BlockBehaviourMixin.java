package dev.amymialee.visiblebarriers.mixin.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Shadow public abstract Item asItem();

    @Inject(method = "propagatesSkylightDown", at = @At("HEAD"), cancellable = true)
    public void visiblebarriers$isTranslucent(BlockState state, CallbackInfoReturnable<Boolean> cir) {}

    @Inject(method = "skipRendering", at = @At("HEAD"), cancellable = true)
    public void visiblebarriers$isSideInvisible(BlockState state, BlockState neighborState, Direction direction, CallbackInfoReturnable<Boolean> cir) {}

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    public void visiblebarriers$getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {}
}