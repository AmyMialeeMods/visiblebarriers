package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Unique
    private double eventDeltaVerticalWheel = 0.0;

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void visibleBarriers$scroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (window == MinecraftClient.getInstance().getWindow().getHandle() && VisibleBarriers.isHoldingZoom()) {
            var d = (Boolean.TRUE.equals(this.client.options.getDiscreteMouseScroll().getValue()) ? Math.signum(vertical) : vertical) * this.client.options.getMouseWheelSensitivity().getValue();

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

    @WrapOperation(method = "updateMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;", ordinal = 0))
    private Object visibleBarriers$zoomSlow(SimpleOption<Double> option, Operation<Object> original) {
        double value = option.getValue();
        if (VisibleBarriers.isHoldingZoom()) {
            return value * VisibleBarriers.getZoomModifier();
        }
        return value;
    }
}