package xyz.amymialee.visiblebarriers.common;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import org.jetbrains.annotations.NotNull;

public class VisibleBarriersNetworking {

    private static final PacketContext.Key<@NotNull Unit> INSTALLED_PACKET_KEY = PacketContext.key(VisibleBarriersCommon.id("mod_installed"));

    public static void register() {
        PayloadTypeRegistry.serverboundPlay().register(ModInstalledPayload.TYPE, ModInstalledPayload.CODEC);
        PayloadTypeRegistry.clientboundPlay().register(ModInstalledPayload.TYPE, ModInstalledPayload.CODEC);
        PayloadTypeRegistry.serverboundConfiguration().register(ModInstalledPayload.TYPE, ModInstalledPayload.CODEC);
        PayloadTypeRegistry.clientboundConfiguration().register(ModInstalledPayload.TYPE, ModInstalledPayload.CODEC);

        ServerConfigurationConnectionEvents.BEFORE_CONFIGURE.register((listener, _) -> {
            if (ServerConfigurationNetworking.canSend(listener, ModInstalledPayload.TYPE)) {
                listener.getPacketContext().set(INSTALLED_PACKET_KEY, Unit.INSTANCE);
            }
        });

        ServerPlayConnectionEvents.JOIN.register((listener, _, _) -> {
            if (listener.getPacketContext().get(INSTALLED_PACKET_KEY) != null) {
                VisibleBarriersCommon.LOGGER.info("{} has mod Visible Barriers installed.", listener.getPlayer().getGameProfile().name());
            }
        });
    }

    public static boolean isModInstalled(PacketContext context) {
        return context.get(INSTALLED_PACKET_KEY) != null;
    }

    public static final class ModInstalledPayload implements CustomPacketPayload {

        public static final Identifier ID = VisibleBarriersCommon.id("mod_installed");
        public static final Type<@NotNull ModInstalledPayload> TYPE = new Type<>(ID);
        private static final ModInstalledPayload INSTANCE = new ModInstalledPayload();
        public static final StreamCodec<@NotNull FriendlyByteBuf, ModInstalledPayload> CODEC = StreamCodec.unit(ModInstalledPayload.INSTANCE);

        private ModInstalledPayload() {
        }

        @Override
        public @NotNull Type<? extends @NotNull CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
