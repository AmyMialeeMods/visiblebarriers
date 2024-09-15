package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
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
    private static final WeakHashMap<TooltipAppender, TooltipAppender> visibleBarriers$tooltipCache = new WeakHashMap<>();

    @ModifyVariable(method = "getTooltip", index = 3, at = @At("HEAD"), argsOnly = true)
    private TooltipType visibleBarriers$showTooltip(TooltipType value) {
        return VisibleBarriers.isVisibilityEnabled() ? new TooltipType.Default(true, true) : value;
    }

    // this needs to be injected at the HIDE_ADDITIONAL_TOOLTIP one
    @Redirect(method = "getTooltip", at = @At(value = "INVOKE", ordinal = 3, target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"))
    private boolean visibleBarriers$showAdditionalTooltip(ItemStack instance, ComponentType<?> componentType) {
        return instance.contains(componentType) && !VisibleBarriers.isVisibilityEnabled();
    }

    @ModifyVariable(method = "appendTooltip", index = 5, at = @At(value = "LOAD", ordinal = 1))
    private <T extends TooltipAppender> TooltipAppender visibleBarriers$forceAppend(T value, @Local(argsOnly = true) ComponentType<T> componentType) {
        if (!VisibleBarriers.isVisibilityEnabled()) return value;

        TooltipAppender computed = visibleBarriers$tooltipCache.computeIfAbsent(value, ins -> {
            Codec<T> codec = componentType.getCodec();
            if (codec == null) return ins;
            World world = MinecraftClient.getInstance().world;
            if (world == null) return null;
            RegistryOps<NbtElement> ops = world.getRegistryManager().getOps(NbtOps.INSTANCE);
            return codec.encodeStart(ops, value).result().map(nbt -> {
                if (nbt.getType() == NbtElement.COMPOUND_TYPE) {
                    NbtCompound comp = (NbtCompound) nbt;
                    final String key = "show_in_tooltip";
                    if (comp.contains(key, NbtElement.BYTE_TYPE) && comp.getByte(key) != (byte) 1) {
                        comp.putByte(key, (byte) 1);
                        return codec.parse(ops, comp).result().orElse(value);
                    }
                }
                return ins;
            }).orElse(ins);
        });

        return Objects.requireNonNullElse(computed, value);
    }
}