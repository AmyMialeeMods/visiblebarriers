package dev.amymialee.visiblebarriers.mixin.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.amymialee.visiblebarriers.VisibleBarriers;
import dev.amymialee.visiblebarriers.util.FloatyRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(NoopRenderer.class)
public abstract class NoopRendererMixin<T extends Entity, S extends EntityRenderState> extends EntityRendererMixin<T, S> {
    @Unique private static final RenderStateDataKey<Entity> ENTITY_KEY = RenderStateDataKey.create(() -> "Entity");
    @Unique private FloatyRenderer floater;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void visiblebarriers$giveRenderer(@NotNull EntityRendererProvider.Context context, CallbackInfo ci) {
        this.floater = new FloatyRenderer(context.getItemModelResolver());
    }

    @Override
    protected void visiblebarriers$extractTail(T entity, @NonNull S state, float partialTicks, CallbackInfo ci) {
        state.setData(ENTITY_KEY, entity);
    }

    @Override
    protected void visiblebarriers$submitHead(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (!VisibleBarriers.isVisibilityEnabled()) return;
        var entity = state.getData(ENTITY_KEY);
        if (entity instanceof AreaEffectCloud cloud) {
            var potion = new ItemStack(Items.LINGERING_POTION);
            potion.update(DataComponents.POTION_CONTENTS, PotionContents.EMPTY, c -> new PotionContents(c.potion(), cloud.potionContents.customColor(), c.customEffects(), Optional.empty()));
            this.floater.setItem(potion);
            this.floater.submit(entity, state, poseStack, submitNodeCollector);
        } else if (entity instanceof Marker) {
            if (!this.floater.getItem().is(Items.STRUCTURE_BLOCK)) this.floater.setItem(Items.STRUCTURE_BLOCK.getDefaultInstance());
            this.floater.submit(entity, state, poseStack, submitNodeCollector);
        }
    }
}