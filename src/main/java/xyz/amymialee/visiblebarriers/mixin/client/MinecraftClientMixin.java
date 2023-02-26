package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.amymialee.visiblebarriers.VisibleConfig;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @WrapOperation(method = "handleBlockBreaking", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isAir()Z"))
    private boolean visibleBarriers$breakAir(BlockState state, Operation<Boolean> original) {
        if (VisibleConfig.isAirVisible()) {
            return false;
        }
        return original.call(state);
    }
}