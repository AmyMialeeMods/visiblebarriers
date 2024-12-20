package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import xyz.amymialee.visiblebarriers.VisibleOptions;

@Mixin(ClientWorld.Properties.class)
public class ClientWorldPropertiesMixin {
    @WrapMethod(method = "getTimeOfDay")
    private long visiblebarriers$time(Operation<Long> original) {
        if (VisibleOptions.TIME_ENABLED.get()) return VisibleOptions.TIME_VALUE.get();
        return original.call();
    }
}