package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.access.EntityRenderStateAccess;
import xyz.amymialee.visiblebarriers.mixin.boxing.EntityRendererMixin;

import java.util.Optional;

@Mixin(EmptyEntityRenderer.class)
public abstract class EmptyEntityRendererMixin<T extends Entity, S extends EntityRenderState> extends EntityRendererMixin<T, S> {
    @Override
    protected void visibleBarriers$renderHead(S renderState, MatrixStack matrices, OrderedRenderCommandQueue queue, CameraRenderState cameraState, CallbackInfo ci) {
        if (VisibleBarriers.isVisibilityEnabled()) {
            var entity = ((EntityRenderStateAccess) renderState).visiblebarriers$getEntity();
            if (entity instanceof AreaEffectCloudEntity cloud) {
                if (!this.floater.getItem().isOf(Items.LINGERING_POTION)) {
                    var potion = new ItemStack(Items.LINGERING_POTION);
                    potion.apply(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT, c -> new PotionContentsComponent(c.potion(), cloud.potionContentsComponent.customColor(), c.customEffects(), Optional.empty()));
                    this.floater.setItem(potion);
                }
                this.floater.render(entity, matrices, queue, renderState.light);
            } else if (entity instanceof MarkerEntity) {
                if (!this.floater.getItem().isOf(Items.STRUCTURE_BLOCK)) {
                    this.floater.setItem(Items.STRUCTURE_BLOCK.getDefaultStack());
                }
                this.floater.render(entity, matrices, queue, renderState.light);
            }
        }
    }
}