package xyz.amymialee.visiblebarriers.mixin.client;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.client.BlockHolderModelRenderer;

@Mixin(SpecialModelTypes.class)
public class SpecialModelTypesMixin {
    @Shadow @Final public static Codecs.IdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked>> ID_MAPPER;

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void visiblebarriers$model(CallbackInfo ci) {
        ID_MAPPER.put(VisibleBarriers.id("block_holder"), BlockHolderModelRenderer.Unbaked.CODEC);
    }
}