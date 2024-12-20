package xyz.amymialee.visiblebarriers.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.cca.ModInstalledComponent;

public record ModInstalledPacket() implements CustomPayload {
    public static final Id<ModInstalledPacket> ID = new CustomPayload.Id<>(VisibleBarriers.id("mod_installed"));
    public static final PacketCodec<PacketByteBuf, ModInstalledPacket> CODEC = PacketCodec.unit(new ModInstalledPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<ModInstalledPacket> {
        @Override
        public void receive(@NotNull ModInstalledPacket payload, ServerPlayNetworking.@NotNull Context context) {
            VisibleBarriers.LOGGER.info("{} has mod Visible Barriers installed.", context.player().getNameForScoreboard());
            ModInstalledComponent.KEY.get(context.player()).setInstalled(true);
        }
    }
}