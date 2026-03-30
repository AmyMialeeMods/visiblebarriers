package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.access.EntityRenderStateAccess;
import xyz.amymialee.visiblebarriers.mixin.boxing.EntityRendererMixin;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;

@Mixin(NoopRenderer.class)
public abstract class NoopRendererMixin<T extends Entity, S extends EntityRenderState> extends EntityRendererMixin<T, S> {
    @Override
    protected void visibleBarriers$renderHead(S renderState, PoseStack matrices, SubmitNodeCollector queue, CameraRenderState cameraState, CallbackInfo ci) {
        if (VisibleBarriers.isVisibilityEnabled()) {
            var entity = ((EntityRenderStateAccess) renderState).visiblebarriers$getEntity();
            if (entity instanceof AreaEffectCloud cloud) {
                if (!this.floater.getItem().is(Items.LINGERING_POTION)) {
                    var potion = new ItemStack(Items.LINGERING_POTION);
                    potion.update(DataComponents.POTION_CONTENTS, PotionContents.EMPTY, c -> new PotionContents(c.potion(), cloud.potionContents.customColor(), c.customEffects(), Optional.empty()));
                    this.floater.setItem(potion);
                }
                this.floater.render(entity, matrices, queue, renderState.lightCoords);
            } else if (entity instanceof Marker) {
                if (!this.floater.getItem().is(Items.STRUCTURE_BLOCK)) {
                    this.floater.setItem(Items.STRUCTURE_BLOCK.getDefaultInstance());
                }
                this.floater.render(entity, matrices, queue, renderState.lightCoords);
            }
        }
    }
}