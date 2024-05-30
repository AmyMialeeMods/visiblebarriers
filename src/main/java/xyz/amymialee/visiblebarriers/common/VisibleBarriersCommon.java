package xyz.amymialee.visiblebarriers.common;

import java.util.List;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.PistonType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisibleBarriersCommon implements ModInitializer {
    public final static String MOD_ID = "visiblebarriers";
    public final static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // Custom Items
    public static final BlockItem MOVING_PISTON_BLOCK_ITEM = Registry.register(Registries.ITEM, id("moving_piston"), new BlockItem(Blocks.MOVING_PISTON, new Item.Settings().rarity(Rarity.EPIC)));
    public static final BlockItem AIR_BLOCK_ITEM = Registry.register(Registries.ITEM, id("air"), new BlockItem(Blocks.AIR, new Item.Settings().rarity(Rarity.EPIC)));
    public static final BlockItem CAVE_AIR_BLOCK_ITEM = Registry.register(Registries.ITEM, id("cave_air"), new BlockItem(Blocks.CAVE_AIR, new Item.Settings().rarity(Rarity.EPIC)));
    public static final BlockItem VOID_AIR_BLOCK_ITEM = Registry.register(Registries.ITEM, id("void_air"), new BlockItem(Blocks.VOID_AIR, new Item.Settings().rarity(Rarity.EPIC)));
    public static final BlockItem END_PORTAL_BLOCK_ITEM = Registry.register(Registries.ITEM, id("end_portal"), new BlockItem(Blocks.END_PORTAL, new Item.Settings().rarity(Rarity.EPIC)));
    public static final BlockItem END_GATEWAY_BLOCK_ITEM = Registry.register(Registries.ITEM, id("end_gateway"), new BlockItem(Blocks.END_GATEWAY, new Item.Settings().rarity(Rarity.EPIC)));
    public static final BlockItem BUBBLE_COLUMN_BLOCK_ITEM = Registry.register(Registries.ITEM, id("bubble_column"), new BlockItem(Blocks.BUBBLE_COLUMN, new Item.Settings().rarity(Rarity.EPIC)));
    // Packet Identifiers
    public static final Identifier MOD_INSTALLED_PACKET = id("mod_installed");

    public static record ModData(byte[] data) implements CustomPayload {
        public static final CustomPayload.Id<ModData> PACKET_ID = new CustomPayload.Id<>(MOD_INSTALLED_PACKET);
        public static final PacketCodec<PacketByteBuf, ModData> PACKET_CODEC = PacketCodec.of(ModData::write, ModData::new);

        public ModData(PacketByteBuf buf) {
            this(new byte[buf.readableBytes()]);
            buf.readBytes(data);
        }

        public void write(PacketByteBuf buf) {
            buf.writeBytes(data);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register(content -> {
            for (PistonType type : PistonType.values()) {
                content.add(makeVariant(VisibleBarriersCommon.MOVING_PISTON_BLOCK_ITEM, Properties.PISTON_TYPE, type));
            }
            for (BlockItem item : List.of(AIR_BLOCK_ITEM, CAVE_AIR_BLOCK_ITEM, VOID_AIR_BLOCK_ITEM, END_PORTAL_BLOCK_ITEM, END_GATEWAY_BLOCK_ITEM)) {
                content.add(new ItemStack(item));
            }
            content.add(makeVariant(VisibleBarriersCommon.BUBBLE_COLUMN_BLOCK_ITEM, Properties.DRAG, Boolean.TRUE));
            content.add(makeVariant(VisibleBarriersCommon.BUBBLE_COLUMN_BLOCK_ITEM, Properties.DRAG, Boolean.TRUE));
        });
        PayloadTypeRegistry.playC2S().register(ModData.PACKET_ID, ModData.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ModData.PACKET_ID, ModData.PACKET_CODEC);
        PayloadTypeRegistry.configurationC2S().register(ModData.PACKET_ID, ModData.PACKET_CODEC);
        PayloadTypeRegistry.configurationS2C().register(ModData.PACKET_ID, ModData.PACKET_CODEC);

        ServerPlayNetworking.registerGlobalReceiver(ModData.PACKET_ID, (packet, context) -> {
            LOGGER.info("{} has mod Visible Barriers installed.", context.player().getNameForScoreboard());
            ServerPlayNetworking.send(context.player(), packet);
        });
    }

    private static <T extends Comparable<T>> ItemStack makeVariant(Item item, Property<T> key, T value) {
        ItemStack stack = new ItemStack(item);
        stack.apply(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT, c -> c.with(key, value));
        return stack;
    }

    public static Identifier id(String... path) {
        return new Identifier(MOD_ID, String.join(".", path));
    }
}