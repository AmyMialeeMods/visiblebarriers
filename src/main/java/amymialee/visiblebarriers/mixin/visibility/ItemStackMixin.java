package amymialee.visiblebarriers.mixin.visibility;

import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Inject(method = "isSectionVisible", at = @At(value = "HEAD"), cancellable = true)
    private static void isSectionVisible(int flags, ItemStack.TooltipSection tooltipSection, CallbackInfoReturnable<Boolean> cir) {
        if (VisibleBarriers.isVisible()) {
            cir.setReturnValue(true);
        }
    }
}
