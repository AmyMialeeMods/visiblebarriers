package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.visiblebarriers.VisibleConfig;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @WrapOperation(method = "continueAttack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isAir()Z"))
    private boolean visibleBarriers$breakAir(BlockState state, Operation<Boolean> original) {
        if (VisibleConfig.isAirVisible()) return false;
        return original.call(state);
    }
}