package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.OptionInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Unique
    private double eventDeltaVerticalWheel = 0.0;

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void visibleBarriers$scroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window == Minecraft.getInstance().getWindow().handle() && VisibleBarriers.isHoldingZoom()) {
            var d = (this.minecraft.options.discreteMouseScroll().get() ? Math.signum(vertical) : vertical) * this.minecraft.options.mouseWheelSensitivity().get();

            if (this.eventDeltaVerticalWheel != 0.0 && Math.signum(d) != Math.signum(this.eventDeltaVerticalWheel)) {
                this.eventDeltaVerticalWheel = 0.0;
            }
            this.eventDeltaVerticalWheel += d;
            var i = (int) this.eventDeltaVerticalWheel;
            if (i == 0) {
                return;
            }
            this.eventDeltaVerticalWheel -= i;
            VisibleBarriers.modifyZoomModifier(-i);
            ci.cancel();
        }
    }

    @WrapOperation(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0))
    private Object visibleBarriers$zoomSlow(OptionInstance<Double> option, Operation<Object> original) {
        double value = option.get();
        if (VisibleBarriers.isHoldingZoom()) {
            return value * VisibleBarriers.getZoomModifier();
        }
        return value;
    }
}