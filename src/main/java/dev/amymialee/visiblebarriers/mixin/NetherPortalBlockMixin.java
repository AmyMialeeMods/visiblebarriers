package dev.amymialee.visiblebarriers.mixin;

import dev.amymialee.visiblebarriers.mixin.boxing.BlockMixin;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin extends BlockMixin {
    @Shadow @Final public static EnumProperty<Direction.Axis> AXIS;

    @Override
    public void visibleBarriers$getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(this.defaultBlockState().setValue(AXIS, context.getHorizontalDirection().getOpposite().getClockWise().getAxis()));
    }
}