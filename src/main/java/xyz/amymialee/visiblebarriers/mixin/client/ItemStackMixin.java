package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.item.BlockPredicatesChecker;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import xyz.amymialee.visiblebarriers.VisibleBarriers;

import java.util.Objects;
import java.util.WeakHashMap;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    // // pre 1.20.5 code
    // @Inject(method = "isSectionVisible", at = @At(value = "HEAD"), cancellable = true)
    // private static void visibleBarriers$showDetails(int flags, ItemStack.TooltipSection tooltipSection, CallbackInfoReturnable<Boolean> cir) {
    //     if (VisibleBarriers.isVisibilityEnabled()) {
    //         cir.setReturnValue(true);
    //     }
    // }

    @Unique
    private static final WeakHashMap<Object, Object> visibleBarriers$tooltipCache = new WeakHashMap<>();

    @ModifyVariable(method = "getTooltip", index = 3, at = @At("HEAD"), argsOnly = true)
    private TooltipType visibleBarriers$showTooltip(TooltipType value) {
        return VisibleBarriers.isVisibilityEnabled() ? new TooltipType.Default(true, true) : value;
    }

    // this needs to be injected at the HIDE_ADDITIONAL_TOOLTIP one
    @Redirect(method = "getTooltip", at = @At(value = "INVOKE", ordinal = 2, target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"))
    private boolean visibleBarriers$showAdditionalTooltip(ItemStack instance, ComponentType<?> componentType) {
        return instance.contains(componentType) && !VisibleBarriers.isVisibilityEnabled();
    }

    @ModifyVariable(method = "appendTooltip", index = 5, at = @At(value = "LOAD", ordinal = 1))
    private <T extends TooltipAppender> TooltipAppender visibleBarriers$forceAppend(T value, @Local(argsOnly = true) ComponentType<T> componentType) {
        return convertComponent(componentType, value);
    }

    // don't implement these on inject showInTooltip method because that can cause consequences on other mods trying to read components.
//    @ModifyVariable(method = "getTooltip", index = 6, at = @At(value = "LOAD", ordinal = 1))
//    private BlockPredicatesChecker visibleBarriers$appendCanBreak(BlockPredicatesChecker component) {
//        return component.showInTooltip() ? component : convertComponent(DataComponentTypes.CAN_BREAK, component);
//    }

    @ModifyVariable(method = "getTooltip", index = 7, at = @At(value = "LOAD", ordinal = 1))
    private BlockPredicatesChecker visibleBarriers$appendCanPlaceOn(BlockPredicatesChecker component) {
        return component.showInTooltip() ? component : convertComponent(DataComponentTypes.CAN_PLACE_ON, component);
    }

    @ModifyVariable(method = "appendAttributeModifiersTooltip", index = 3, at = @At(value = "LOAD", ordinal = 0))
    private AttributeModifiersComponent visibleBarriers$appendAttributes(AttributeModifiersComponent component) {
        return component.showInTooltip() ? component : convertComponent(DataComponentTypes.ATTRIBUTE_MODIFIERS, component);
    }

    @Unique
    private static <T> T convertComponent(ComponentType<T> type, T value) {
        if (!VisibleBarriers.isVisibilityEnabled()) return value;

        // try to modify show_in_tooltip nbt value to true
        var computed = visibleBarriers$tooltipCache.computeIfAbsent(value, ins -> {
            var codec = type.getCodec();
            if (codec == null) return ins;
            World world = MinecraftClient.getInstance().world;
            if (world == null) return null;
            var ops = world.getRegistryManager().getOps(NbtOps.INSTANCE);
            return codec.encodeStart(ops, value).result().map(nbt -> {
                if (nbt.getType() == NbtElement.COMPOUND_TYPE) {
                    var comp = (NbtCompound) nbt;
                    final var key = "show_in_tooltip";
                    if (comp.contains(key, NbtElement.BYTE_TYPE) && comp.getByte(key) != (byte) 1) {
                        comp.putByte(key, (byte) 1);
                        return codec.parse(ops, comp).result().orElse(value);
                    }
                }
                return ins;
            }).orElse(ins);
        });

        //noinspection unchecked
        return (T) Objects.requireNonNullElse(computed, value);
    }
}