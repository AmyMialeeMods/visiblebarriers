package amymialee.visiblebarriers.mixin;

import amymialee.visiblebarriers.VisibleBarriersConfig;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.option.HotbarStorageEntry;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
    @Shadow private static int selectedTab;

    public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (VisibleBarriersConfig.load().allowHotbarQuickSaving) {
            if (selectedTab == ItemGroup.HOTBAR.getIndex() && !((CreativeInventoryScreen) ((Object) this)).getScreenHandler().getCursorStack().isEmpty() &&
                    slot != null) {
                int barSlot = slotId % 9;
                int barRow = slotId / 9;
                HotbarStorageEntry entry = this.client.getCreativeHotbarStorage().getSavedHotbar(barRow);
                entry.set(barSlot, ((CreativeInventoryScreen) ((Object) this)).getScreenHandler().getCursorStack());
                this.client.getCreativeHotbarStorage().save();
                slot.setStack(((CreativeInventoryScreen) ((Object) this)).getScreenHandler().getCursorStack());
                ((CreativeInventoryScreen) ((Object) this)).getScreenHandler().setCursorStack(ItemStack.EMPTY);
                ci.cancel();
            }
        }
    }
}
