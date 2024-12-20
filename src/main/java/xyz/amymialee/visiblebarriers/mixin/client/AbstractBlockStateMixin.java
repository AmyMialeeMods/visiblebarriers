package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.*;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.VisibleOptions;
import xyz.amymialee.visiblebarriers.items.BlockHolderItem;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Shadow public abstract boolean isOf(Block block);
    @Shadow public abstract Block getBlock();

    @WrapMethod(method = "getRenderType")
    private BlockRenderType visiblebarriers$model(@NotNull Operation<BlockRenderType> original) {
        var result = original.call();
        if (result == BlockRenderType.INVISIBLE && VisibleOptions.VISIBILITY_ENABLED.get()) {
            if (VisibleOptions.VISIBLE_BARRIERS.get() && this.isOf(Blocks.BARRIER)) return BlockRenderType.MODEL;
            if (VisibleOptions.VISIBLE_LIGHTS.get() && this.isOf(Blocks.LIGHT)) return BlockRenderType.MODEL;
            if (VisibleOptions.VISIBLE_STRUCTURE_VOIDS.get() && this.isOf(Blocks.STRUCTURE_VOID)) return BlockRenderType.MODEL;
            if (VisibleOptions.VISIBLE_PISTON_EXTENSIONS.get() && this.isOf(Blocks.MOVING_PISTON)) return BlockRenderType.MODEL;
            if (VisibleOptions.VISIBLE_BUBBLE_COLUMNS.get() && this.isOf(Blocks.BUBBLE_COLUMN)) return BlockRenderType.MODEL;
            if (VisibleOptions.VISIBLE_VOID_AIR.get() && this.isOf(Blocks.VOID_AIR)) return BlockRenderType.MODEL;
            if (VisibleOptions.VISIBLE_CAVE_AIR.get() && this.isOf(Blocks.CAVE_AIR)) return BlockRenderType.MODEL;
            if (VisibleOptions.VISIBLE_AIR.get() && this.isOf(Blocks.AIR)) return BlockRenderType.MODEL;
        }
        return result;
    }

    @WrapMethod(method = "isTransparent")
    public boolean visiblebarriers$transparent(Operation<Boolean> original) {
        if (VisibleOptions.VISIBILITY_ENABLED.get()) {
            if (VisibleOptions.VISIBLE_BARRIERS.get() && this.isOf(Blocks.BARRIER)) return true;
            if (VisibleOptions.VISIBLE_LIGHTS.get() && this.isOf(Blocks.LIGHT)) return true;
            if (VisibleOptions.VISIBLE_STRUCTURE_VOIDS.get() && this.isOf(Blocks.STRUCTURE_VOID)) return true;
            if (VisibleOptions.VISIBLE_PISTON_EXTENSIONS.get() && this.isOf(Blocks.MOVING_PISTON)) return true;
            if (VisibleOptions.VISIBLE_BUBBLE_COLUMNS.get() && this.isOf(Blocks.BUBBLE_COLUMN)) return true;
            if (VisibleOptions.VISIBLE_VOID_AIR.get() && this.isOf(Blocks.VOID_AIR)) return true;
            if (VisibleOptions.VISIBLE_CAVE_AIR.get() && this.isOf(Blocks.CAVE_AIR)) return true;
            if (VisibleOptions.VISIBLE_AIR.get() && this.isOf(Blocks.AIR)) return true;
        }
        return original.call();
    }

    @WrapMethod(method = "isSideInvisible")
    public boolean visiblebarriers$invisible(BlockState state, Direction direction, Operation<Boolean> original) {
        if (VisibleOptions.VISIBILITY_ENABLED.get() && this.getBlock() == state.getBlock()) {
            if (VisibleOptions.VISIBLE_BARRIERS.get() && this.isOf(Blocks.BARRIER)) return true;
            if (VisibleOptions.VISIBLE_LIGHTS.get() && this.isOf(Blocks.LIGHT)) return true;
            if (VisibleOptions.VISIBLE_STRUCTURE_VOIDS.get() && this.isOf(Blocks.STRUCTURE_VOID)) return true;
            if (VisibleOptions.VISIBLE_PISTON_EXTENSIONS.get() && this.isOf(Blocks.MOVING_PISTON)) return true;
            if (VisibleOptions.VISIBLE_BUBBLE_COLUMNS.get() && this.isOf(Blocks.BUBBLE_COLUMN)) return true;
            if (VisibleOptions.VISIBLE_VOID_AIR.get() && this.isOf(Blocks.VOID_AIR)) return true;
            if (VisibleOptions.VISIBLE_CAVE_AIR.get() && this.isOf(Blocks.CAVE_AIR)) return true;
            if (VisibleOptions.VISIBLE_AIR.get() && this.isOf(Blocks.AIR)) return true;
        }
        return original.call(state, direction);
    }

    @WrapMethod(method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;")
    public VoxelShape visiblebarriers$collision(BlockView world, BlockPos pos, ShapeContext context, Operation<VoxelShape> original) {
        if (VisibleOptions.VISIBILITY_ENABLED.get()) {
            if (this.isOf(Blocks.LIGHT)) return VoxelShapes.empty();
            if (this.isOf(Blocks.STRUCTURE_VOID)) return VoxelShapes.empty();
            if (this.isOf(Blocks.VOID_AIR)) return VoxelShapes.empty();
            if (this.isOf(Blocks.CAVE_AIR)) return VoxelShapes.empty();
            if (this.isOf(Blocks.AIR)) return VoxelShapes.empty();
        }
        return original.call(world, pos, context);
    }

    @WrapMethod(method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;")
    public VoxelShape visiblebarriers$outline(BlockView world, BlockPos pos, ShapeContext context, Operation<VoxelShape> original) {
        if (VisibleOptions.VISIBILITY_ENABLED.get()) {
            if (VisibleOptions.VISIBLE_LIGHTS.get() && this.isOf(Blocks.LIGHT)) return VoxelShapes.fullCube();
            if (VisibleOptions.VISIBLE_PISTON_EXTENSIONS.get() && this.isOf(Blocks.MOVING_PISTON)) return VoxelShapes.fullCube();
            if (VisibleOptions.VISIBLE_BUBBLE_COLUMNS.get() && this.isOf(Blocks.BUBBLE_COLUMN)) return VoxelShapes.fullCube();
            if (VisibleOptions.VISIBLE_VOID_AIR.get() && this.isOf(Blocks.VOID_AIR)) return VoxelShapes.fullCube();
            if (VisibleOptions.VISIBLE_CAVE_AIR.get() && this.isOf(Blocks.CAVE_AIR)) return VoxelShapes.fullCube();
        }
        return original.call(world, pos, context);
    }

    @WrapMethod(method = "getPickStack")
    private @Nullable ItemStack visiblebarriers$stack(WorldView world, BlockPos pos, boolean includeData, @NotNull Operation<ItemStack> original) {
        var result = original.call(world, pos, includeData);
        if (result.isEmpty()) return BlockHolderItem.from((BlockState) (Object) this);
        return result;
    }
}