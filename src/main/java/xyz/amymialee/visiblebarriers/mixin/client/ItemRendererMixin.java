package xyz.amymialee.visiblebarriers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    @WrapMethod(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V")
    private void visiblebarriers$holder(LivingEntity entity, @NotNull ItemStack stack, ModelTransformationMode transformationMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, int seed, @NotNull Operation<Void> original) {
        original.call(entity, stack, transformationMode, leftHanded, matrices, vertexConsumers, world, light, overlay, seed);
    }
}