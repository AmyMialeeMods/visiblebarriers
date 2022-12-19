package xyz.amymialee.visiblebarriers.mixin.client;

import xyz.amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemColors.class)
public class ItemColorsMixin {
    @Inject(method = "getColor", at = @At("HEAD"), cancellable = true)
    private void visibleBarriers$itemColors(ItemStack item, int tintIndex, CallbackInfoReturnable<Integer> cir) {
        if (VisibleBarriers.areHighlightsEnabled()) {
            if (VisibleBarriers.config.hasItem(item.getItem())) {
                cir.setReturnValue(VisibleBarriers.config.getItemColor(item.getItem()));
            }
        }
    }
}