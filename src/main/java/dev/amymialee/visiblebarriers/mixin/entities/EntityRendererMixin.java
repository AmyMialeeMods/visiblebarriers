package dev.amymialee.visiblebarriers.mixin.entities;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @WrapMethod(method = "shouldShowName")
    private boolean visiblebarriers$showName(T entity, double distanceToCameraSq, Operation<Boolean> original) {
        if (VisibleBarriers.isVisibilityEnabled()) return false;
        return original.call(entity, distanceToCameraSq);
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    protected void visiblebarriers$extractTail(T entity, @NonNull S state, float partialTicks, CallbackInfo ci) {}

    @Inject(method = "submit", at = @At("HEAD"))
    protected void visiblebarriers$submitHead(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {}
}