package xyz.amymialee.visiblebarriers.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Marker.class)
public abstract class MarkerMixin extends Entity {
    public MarkerMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "getAddEntityPacket", at = @At("HEAD"), cancellable = true)
    public void visibleBarriers$forcePacket(CallbackInfoReturnable<Packet<?>> cir) {
        var entityTrackerEntry = new ServerEntity((ServerLevel) this.level(), (Marker) ((Object) this), 0, false, null);
        cir.setReturnValue(new ClientboundAddEntityPacket(this, entityTrackerEntry, 0));
    }

}