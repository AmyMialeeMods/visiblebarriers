package xyz.amymialee.visiblebarriers.mixin.server;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LightBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.amymialee.visiblebarriers.mixin.boxing.BlockMixin;

@Mixin(LightBlock.class)
public class LightBlockMixin extends BlockMixin {
    @Shadow @Final public static IntProperty LEVEL_15;

    @Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$downwardLight(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!world.isClient && player.isSneaking()) {
            int level = state.get(LEVEL_15) - 1;
            world.setBlockState(pos, state.with(LEVEL_15, level < 0 ? 15 : level), Block.NOTIFY_LISTENERS);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }
}