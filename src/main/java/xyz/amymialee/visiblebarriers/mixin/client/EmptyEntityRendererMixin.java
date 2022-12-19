package xyz.amymialee.visiblebarriers.mixin.client;

import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.mixin.boxing.EntityRendererMixin;
import xyz.amymialee.visiblebarriers.util.FloatyRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmptyEntityRenderer.class)
public abstract class EmptyEntityRendererMixin<T extends Entity> extends EntityRendererMixin<T> {
    @Unique private FloatyRenderer<T> floater;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void visibleBarriers$giveRenderer(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.floater = new FloatyRenderer<>(context.getItemRenderer(), Items.BARRIER.getDefaultStack());
    }

    @Override
    protected void visibleBarriers$renderHead(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        super.visibleBarriers$renderHead(entity, yaw, tickDelta, matrices, vertexConsumers, light, ci);
        if (VisibleBarriers.areEntitiesVisible()) {
            if (entity instanceof AreaEffectCloudEntity cloud) {
                ItemStack potion = new ItemStack(Items.LINGERING_POTION);
                NbtCompound tag = potion.getOrCreateNbt();
                tag.putInt("CustomPotionColor", cloud.getColor());
                this.floater.render(entity, tickDelta, matrices, vertexConsumers, light);
            } else if (entity instanceof MarkerEntity) {
                this.floater.renderItem(Items.STRUCTURE_BLOCK.getDefaultStack(), entity, tickDelta, matrices, vertexConsumers, light);
            }
        }
    }
}