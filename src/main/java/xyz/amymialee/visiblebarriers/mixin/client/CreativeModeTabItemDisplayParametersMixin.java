package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeModeTab.ItemDisplayParameters.class)
public class CreativeModeTabItemDisplayParametersMixin {
    @Inject(method = "hasPermissions", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$overridePermission(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
        cir.cancel();
    }
}