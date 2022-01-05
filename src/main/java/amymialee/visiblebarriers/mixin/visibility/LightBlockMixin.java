package amymialee.visiblebarriers.mixin.visibility;

import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.block.*;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightBlock.class)
public class LightBlockMixin {
    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    public void getRenderType(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (VisibleBarriers.visible) {
            cir.setReturnValue(BlockRenderType.MODEL);
        }
    }

    @ModifyVariable(method = "<init>", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static AbstractBlock.Settings injected(AbstractBlock.Settings settings) {
        return settings.noCollision();
    }

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    public void getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (VisibleBarriers.visible) {
            cir.setReturnValue(VoxelShapes.fullCube());
        }
    }
}
