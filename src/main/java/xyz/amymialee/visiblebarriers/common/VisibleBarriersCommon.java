package xyz.amymialee.visiblebarriers.common;

import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.PistonType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.visiblebarriers.cca.ModInstalledComponent;
import xyz.nucleoid.packettweaker.PacketContext;

import java.util.List;

public class VisibleBarriersCommon implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "visiblebarriers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // Custom Items
    public static final BlockItem MOVING_PISTON_BLOCK_ITEM = registerBlockItem(Blocks.MOVING_PISTON, "Moving Piston", "moving_piston");
    public static final BlockItem AIR_BLOCK_ITEM = registerBlockItem(Blocks.AIR, "Air", "air");
    public static final BlockItem CAVE_AIR_BLOCK_ITEM = registerBlockItem(Blocks.CAVE_AIR, "Cave Air", "cave_air");
    public static final BlockItem VOID_AIR_BLOCK_ITEM = registerBlockItem(Blocks.VOID_AIR, "Void Air", "void_air");
    public static final BlockItem END_PORTAL_BLOCK_ITEM = registerBlockItem(Blocks.END_PORTAL, "End Portal", "end_portal");
    public static final BlockItem END_GATEWAY_BLOCK_ITEM = registerBlockItem(Blocks.END_GATEWAY, "End Gateway", "end_gateway");
    public static final BlockItem BUBBLE_COLUMN_BLOCK_ITEM = registerBlockItem(Blocks.BUBBLE_COLUMN, "Bubble Column", "bubble_column");
    // Packet Identifiers
    public static final Identifier MOD_INSTALLED_PACKET = id("mod_installed");

    public record ModData(byte[] data) implements CustomPayload {
        public static final CustomPayload.Id<ModData> PACKET_ID = new CustomPayload.Id<>(MOD_INSTALLED_PACKET);
        public static final PacketCodec<PacketByteBuf, ModData> PACKET_CODEC = PacketCodec.of(ModData::write, ModData::new);

        public ModData(@NotNull PacketByteBuf buf) {
            this(new byte[buf.readableBytes()]);
            buf.readBytes(this.data);
        }

        public void write(@NotNull PacketByteBuf buf) {
            buf.writeBytes(this.data);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register(content -> {
            if (content.getContext().hasPermissions()) {
                for (var type : PistonType.values()) {
                    content.add(makeVariant(MOVING_PISTON_BLOCK_ITEM, Properties.PISTON_TYPE, type));
                }
                for (var item : List.of(AIR_BLOCK_ITEM, CAVE_AIR_BLOCK_ITEM, VOID_AIR_BLOCK_ITEM, END_PORTAL_BLOCK_ITEM, END_GATEWAY_BLOCK_ITEM)) {
                    content.add(new ItemStack(item));
                }
                content.add(makeVariant(BUBBLE_COLUMN_BLOCK_ITEM, Properties.DRAG, Boolean.TRUE));
                content.add(makeVariant(BUBBLE_COLUMN_BLOCK_ITEM, Properties.DRAG, Boolean.FALSE));
            }
        });
        PayloadTypeRegistry.playC2S().register(ModData.PACKET_ID, ModData.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(ModData.PACKET_ID, ModData.PACKET_CODEC);
        PayloadTypeRegistry.configurationC2S().register(ModData.PACKET_ID, ModData.PACKET_CODEC);
        PayloadTypeRegistry.configurationS2C().register(ModData.PACKET_ID, ModData.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ModData.PACKET_ID, (packet, context) -> {
            LOGGER.info("{} has mod Visible Barriers installed.", context.player().getNameForScoreboard());
            ServerPlayNetworking.send(context.player(), packet);
            ModInstalledComponent.KEY.get(context.player()).setInstalled(true);
        });
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, ModInstalledComponent.KEY).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end((a) -> new ModInstalledComponent());
    }

    private static <T extends Comparable<T>> @NotNull ItemStack makeVariant(Item item, Property<T> key, T value) {
        var stack = new ItemStack(item);
        stack.apply(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT, c -> c.with(key, value));
        return stack;
    }

    private static BlockItem registerBlockItem(Block block, String itemName, String... path) {
        var key = RegistryKey.of(RegistryKeys.ITEM, id(path));
        return Registry.register(Registries.ITEM, key.getValue(), new PolymerBlockItem(block, new Item.Settings().registryKey(key).rarity(Rarity.EPIC).translationKey(itemName), Items.STRUCTURE_BLOCK, true) {
            @Override
            public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context) {
                var can = false;
                if (context.getPlayer() != null) can = ModInstalledComponent.KEY.get(context.getPlayer()).isInstalled();
                return !can ? null : stack.get(DataComponentTypes.ITEM_MODEL);
            }

            @Override
            public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
                var can = false;
                if (context.getPlayer() != null) can = ModInstalledComponent.KEY.get(context.getPlayer()).isInstalled();
                return can ? this : super.getPolymerItem(itemStack, context);
            }
        });
    }

    public static @NotNull Identifier id(String... path) {
        return Identifier.of(MOD_ID, String.join(".", path));
    }
}