package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
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
    public void visibleBarriers$forcePacket(CallbackInfoReturnable<Packet<?>> cir) {
        var entityTrackerEntry = new EntityTrackerEntry((ServerWorld) this.getEntityWorld(), (MarkerEntity) ((Object) this), 0, false, null);
        cir.setReturnValue(new EntitySpawnS2CPacket(this, entityTrackerEntry, 0));
    }

}