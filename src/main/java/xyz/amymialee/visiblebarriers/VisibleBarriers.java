package xyz.amymialee.visiblebarriers;

import eu.pb4.polymer.core.api.block.SimplePolymerBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.state.property.Property;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.amymialee.mialib.templates.MRegistry;
import xyz.amymialee.visiblebarriers.cca.ModInstalledComponent;
import xyz.amymialee.visiblebarriers.items.BlockDataComponent;
import xyz.amymialee.visiblebarriers.items.BlockHolderItem;
import xyz.amymialee.visiblebarriers.networking.ModInstalledPacket;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class VisibleBarriers implements ModInitializer, EntityComponentInitializer {
    public static final String MOD_ID = "visiblebarriers";
    public static final MRegistry REGISTRY = new MRegistry(MOD_ID);
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final ComponentType<Block> BLOCK = REGISTRY.register("block", ComponentType.<Block>builder().codec(BlockDataComponent.CODEC).packetCodec(BlockDataComponent.PACKET_CODEC).build());
    public static final ComponentType<ItemStack> STACK = REGISTRY.register("stack", ComponentType.<ItemStack>builder().codec(ItemStack.CODEC).packetCodec(ItemStack.PACKET_CODEC).build());
    public static final Item BLOCK_HOLDER_ITEM = REGISTRY.register("block_holder", new Item.Settings(), BlockHolderItem::new);
    public static final Block WATER = REGISTRY.register("water", AbstractBlock.Settings.copy(Blocks.WATER), Block::new);
    public static final Block LAVA = REGISTRY.register("lava", AbstractBlock.Settings.copy(Blocks.LAVA), Block::new);

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP, id("existing_holders"), FabricItemGroup.builder().displayName(Text.translatable(MOD_ID + ".existing_holders")).icon(() -> BLOCK_HOLDER_ITEM.getDefaultStack().mialib$set(BLOCK, Blocks.BARRIER)).entries(((displayContext, entries) -> generateHolderTab(entries, true))).build());
        Registry.register(Registries.ITEM_GROUP, id("hidden_holders"), FabricItemGroup.builder().displayName(Text.translatable(MOD_ID + ".hidden_holders")).icon(() -> BLOCK_HOLDER_ITEM.getDefaultStack().mialib$set(BLOCK, Blocks.PISTON_HEAD)).entries(((displayContext, entries) -> generateHolderTab(entries, false))).build());
        PayloadTypeRegistry.playC2S().register(ModInstalledPacket.ID, ModInstalledPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ModInstalledPacket.ID, new ModInstalledPacket.Receiver());
    }

    @Override
    public void registerEntityComponentFactories(@NotNull EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, ModInstalledComponent.KEY).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end((a) -> new ModInstalledComponent());
    }

    public static Block registerBlock(String name, AbstractBlock.@NotNull Settings settings, @NotNull Function<AbstractBlock.Settings, Block> block) {
        return REGISTRY.register(name, block.apply(settings.registryKey(RegistryKey.of(RegistryKeys.BLOCK, id(name)))));
    }

    private static void generateHolderTab(ItemGroup.Entries entries, boolean hasItems) {
        var list = new ArrayList<Block>();
        for (var entry : Registries.BLOCK.getEntrySet()) {
            if (hasItems == Item.BLOCK_ITEMS.containsKey(entry.getValue())) list.add(entry.getValue());
        }
        list.sort(Comparator.comparing(Registries.BLOCK::getRawId));
        for (var entry : list) {
            var properties = entry.getDefaultState().getProperties();
            if (properties.isEmpty()) {
                entries.add(BLOCK_HOLDER_ITEM.getDefaultStack().mialib$set(BLOCK, entry));
            } else {
                generatePropertyCombinations(entries, entry, entry.getDefaultState(), new ArrayList<>(properties), 0);
            }
        }
    }

    private static void generatePropertyCombinations(ItemGroup.Entries entries, Block block, BlockState state, @NotNull List<Property<?>> properties, int index) {
        if (index < properties.size()) {
            var property = properties.get(index);
            for (var value : property.getValues()) {
                generatePropertyCombinations(entries, block, getBlockState(state, property, value), properties, index + 1);
            }
        } else {
            var blockStateComponent = BlockStateComponent.DEFAULT;
            for (var property : properties) blockStateComponent = blockStateComponent.with(property, state);
            entries.add(BLOCK_HOLDER_ITEM.getDefaultStack().mialib$set(BLOCK, block).mialib$set(DataComponentTypes.BLOCK_STATE, blockStateComponent));
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>, V extends T> BlockState getBlockState(BlockState state, Property<?> property, Comparable<?> value) {
        state = state.with((Property<T>) property, (V) value);
        return state;
    }

    public static @NotNull Identifier id(String... path) {
        return Identifier.of(MOD_ID, String.join(".", path));
    }
}