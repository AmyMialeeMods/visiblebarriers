package xyz.amymialee.visiblebarriers.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.mixin.boxing.EntityRendererMixin;

@Mixin(EmptyEntityRenderer.class)
public abstract class EmptyEntityRendererMixin<T extends Entity> extends EntityRendererMixin<T> {
    @Override
    protected void visibleBarriers$renderHead(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (VisibleBarriers.isVisibilityEnabled()) {
            if (entity instanceof AreaEffectCloudEntity cloud) {
                if (!this.floater.getItem().isOf(Items.LINGERING_POTION)) {
                    ItemStack potion = new ItemStack(Items.LINGERING_POTION);
                    NbtCompound tag = potion.getOrCreateNbt();
                    tag.putInt("CustomPotionColor", cloud.getColor());
                    this.floater.setItem(potion);
                }
                this.floater.render(entity, tickDelta, matrices, vertexConsumers, light);
            } else if (entity instanceof MarkerEntity) {
                if (!this.floater.getItem().isOf(Items.STRUCTURE_BLOCK)) {
                    this.floater.setItem(Items.STRUCTURE_BLOCK.getDefaultStack());
                }
                this.floater.render(entity, tickDelta, matrices, vertexConsumers, light);
            }
        }
    }
}