package amymialee.visiblebarriers.mixin;

import amymialee.visiblebarriers.VisibleBarriers;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EmptyEntityRenderer.class)
public abstract class EmptyEntityRendererMixin<T extends Entity> extends EntityRenderer<T> {
    protected EmptyEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Unique private ItemRenderer itemRenderer;
    @Unique private final Random random = new Random();
    @Unique public float uniqueOffset;
    @Unique private ItemStack itemStack;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void EmptyEntityRenderer(EntityRendererFactory.Context context, CallbackInfo ci) {
        this.itemRenderer = context.getItemRenderer();
        this.uniqueOffset = this.random.nextFloat() * 3.1415927F * 2.0F;
    }

    @Unique
    public void render(T itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (VisibleBarriers.visible) {
            matrixStack.push();
            if (this.itemStack == null) {
                if (itemEntity instanceof AreaEffectCloudEntity) {
                    this.itemStack = Items.LINGERING_POTION.getDefaultStack().copy();
                    PotionUtil.setPotion(itemStack, ((AreaEffectCloudEntity) itemEntity).getPotion());
                }
                if (itemEntity instanceof MarkerEntity) {
                    this.itemStack = Items.STRUCTURE_BLOCK.getDefaultStack().copy();
                }
            }
            int j = itemStack.isEmpty() ? 187 : Item.getRawId(itemStack.getItem()) + itemStack.getDamage();
            this.random.setSeed(j);
            BakedModel bakedModel = this.itemRenderer.getHeldItemModel(itemStack, itemEntity.world, null, itemEntity.getId());
            boolean bl = bakedModel.hasDepth();
            int k = 1;
            float l = MathHelper.sin(((float) itemEntity.age * 8 + g) / 10.0F + this.uniqueOffset) * 0.1F + 0.1F;
            float m = bakedModel.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.getY();
            matrixStack.translate(0.0D, l + 0.25F * m, 0.0D);
            float n = (((float) itemEntity.age * 8 + g) / 20.0F + this.uniqueOffset);
            matrixStack.multiply(Vec3f.POSITIVE_Y.getRadialQuaternion(-n));
            float o = bakedModel.getTransformation().ground.scale.getX();
            float p = bakedModel.getTransformation().ground.scale.getY();
            float q = bakedModel.getTransformation().ground.scale.getZ();
            float v;
            float w;
            if (!bl) {
                float r = -0.0F * 0 * 0.5F * o;
                v = -0.0F * 0 * 0.5F * p;
                w = -0.09375F * 0 * 0.5F * q;
                matrixStack.translate(r, v, w);
            }
            for (int u = 0; u < k; ++u) {
                matrixStack.push();
                this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.GROUND, false, matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, bakedModel);
                matrixStack.pop();
                if (!bl) {
                    matrixStack.translate(0.0F * o, 0.0F * p, 0.09375F * q);
                }
            }
            matrixStack.pop();
            super.render(itemEntity, f, g, matrixStack, vertexConsumerProvider, i);
        }
    }
}
