package amymialee.visiblebarriers.mixin.visibility;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MarkerEntity.class)
public abstract class MarkerEntityMixin extends Entity {
    public MarkerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "createSpawnPacket", at = @At("HEAD"), cancellable = true)
    public void createSpawnPacket(CallbackInfoReturnable<Packet<?>> cir) {
        cir.setReturnValue(new EntitySpawnS2CPacket((MarkerEntity)((Object)this)));
        cir.cancel();
    }
}
