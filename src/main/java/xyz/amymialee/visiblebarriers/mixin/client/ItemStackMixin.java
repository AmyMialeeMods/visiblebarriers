package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    // @Inject(method = "isSectionVisible", at = @At(value = "HEAD"), cancellable = true)
    // private static void visibleBarriers$showDetails(int flags, ItemStack.TooltipSection tooltipSection, CallbackInfoReturnable<Boolean> cir) {
    // if (VisibleBarriers.isVisibilityEnabled()) {
    // cir.setReturnValue(true);
    // }
    // }
}