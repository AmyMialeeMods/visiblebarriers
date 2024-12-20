package xyz.amymialee.visiblebarriers.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MarkerEntity.class)
public abstract class MarkerEntityMixin extends Entity {
    public MarkerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @WrapMethod(method = "createSpawnPacket")
    public Packet<ClientPlayPacketListener> visiblebarriers$packet(EntityTrackerEntry entityTrackerEntry, Operation<Packet<ClientPlayPacketListener>> original) {
        return super.createSpawnPacket(entityTrackerEntry);
    }
}