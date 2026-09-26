package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateBaseMixin {
    @Shadow
    public abstract Block getBlock();

    @ModifyReturnValue(method = "shouldSpawnTerrainParticles", at = @At("RETURN"))
    private boolean visibleBarriers$removeWaterBreakParticles(boolean original) {
        return original && !(this.getBlock() instanceof LiquidBlock);
    }
}
