package xyz.amymialee.visiblebarriers.common;

import eu.pb4.polymer.core.api.item.PolymerBlockItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.networking.v1.context.PacketContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VisibleBarriersCommon implements ModInitializer {
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

    @Override
    public void onInitialize() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS).register(content -> {
            if (content.getContext().hasPermissions()) {
                for (var type : PistonType.values()) {
                    content.accept(makeVariant(MOVING_PISTON_BLOCK_ITEM, BlockStateProperties.PISTON_TYPE, type));
                }
                for (var item : List.of(AIR_BLOCK_ITEM, CAVE_AIR_BLOCK_ITEM, VOID_AIR_BLOCK_ITEM, END_PORTAL_BLOCK_ITEM, END_GATEWAY_BLOCK_ITEM)) {
                    content.accept(new ItemStack(item));
                }
                content.accept(makeVariant(BUBBLE_COLUMN_BLOCK_ITEM, BlockStateProperties.DRAG, Boolean.TRUE));
                content.accept(makeVariant(BUBBLE_COLUMN_BLOCK_ITEM, BlockStateProperties.DRAG, Boolean.FALSE));
            }
        });
        VisibleBarriersNetworking.register();
    }

    private static <T extends Comparable<T>> @NotNull ItemStack makeVariant(Item item, Property<T> key, T value) {
        var stack = new ItemStack(item);
        stack.update(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY, c -> c.with(key, value));
        return stack;
    }

    private static BlockItem registerBlockItem(Block block, String itemName, String... path) {
        var key = ResourceKey.create(Registries.ITEM, id(path));
        return Registry.register(BuiltInRegistries.ITEM, key.identifier(), new PolymerBlockItem(block, new Item.Properties().setId(key).rarity(Rarity.EPIC).overrideDescription(itemName), Items.STRUCTURE_BLOCK, true) {

            @Override
            public @Nullable Identifier getPolymerItemModel(ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
                return VisibleBarriersNetworking.isModInstalled(context) ? stack.get(DataComponents.ITEM_MODEL) : null;
            }

            @Override
            public Item getPolymerItem(ItemStack itemStack, PacketContext context) {
                return VisibleBarriersNetworking.isModInstalled(context) ? this : super.getPolymerItem(itemStack, context);
            }

            @Override
            public void modifyBasePolymerItemStack(ItemStack out, ItemStack stack, PacketContext context, HolderLookup.Provider lookup) {
                if (stack.has(DataComponents.BLOCK_STATE)) {
                    out.set(DataComponents.BLOCK_STATE, stack.get(DataComponents.BLOCK_STATE));
                }
            }
        });
    }

    public static @NotNull Identifier id(String... path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, String.join(".", path));
    }
}