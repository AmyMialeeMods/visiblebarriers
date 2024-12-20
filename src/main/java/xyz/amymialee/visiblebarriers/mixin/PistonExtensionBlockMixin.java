package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.item.ItemPlacementContext;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonExtensionBlock.class)
public abstract class PistonExtensionBlockMixin extends BlockMixin {
    @Override
    public void visibleBarriers$getPlacementState(@NotNull ItemPlacementContext ctx, @NotNull CallbackInfoReturnable<BlockState> cir) {
        cir.setReturnValue(this.getDefaultState().with(PistonExtensionBlock.FACING, ctx.getPlayerLookDirection().getOpposite()));
    }
}