package dev.amymialee.visiblebarriers.mixin.zoom;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.OptionInstance;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Unique private double eventDeltaVerticalWheel = 0.0;

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void visiblebarriers$scroll(long handle, double xoffset, double yoffset, CallbackInfo ci) {
        if (handle != Minecraft.getInstance().getWindow().handle() || !VisibleBarriers.isHoldingZoom()) return;
        var d = (this.minecraft.options.discreteMouseScroll().get() ? Math.signum(yoffset) : yoffset) * this.minecraft.options.mouseWheelSensitivity().get();
        if (this.eventDeltaVerticalWheel != 0.0 && Math.signum(d) != Math.signum(this.eventDeltaVerticalWheel)) this.eventDeltaVerticalWheel = 0.0;
        this.eventDeltaVerticalWheel += d;
        var i = (int) this.eventDeltaVerticalWheel;
        if (i == 0) return;
        this.eventDeltaVerticalWheel -= i;
        VisibleBarriers.modifyZoomModifier(-i);
        ci.cancel();
    }

    @WrapOperation(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0))
    private @NonNull Object visiblebarriers$zoomSlow(@NonNull OptionInstance<Double> option, Operation<Object> original) {
        if (VisibleBarriers.isHoldingZoom()) return option.get() * VisibleBarriers.getZoomModifier();
        return option.get();
    }
}