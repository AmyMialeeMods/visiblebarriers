package xyz.amymialee.visiblebarriers.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.enums.PistonType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VisibleBarriersCommon implements ModInitializer {
    public final static String MOD_ID = "visiblebarriers";
    public final static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    //Custom Items
    public static final BlockItem MOVING_PISTON_BLOCK_ITEM = Registry.register(Registries.ITEM, id("moving_piston"), new BlockItem(Blocks.MOVING_PISTON, new FabricItemSettings().rarity(Rarity.EPIC)));
    public static final BlockItem AIR_BLOCK_ITEM = Registry.register(Registries.ITEM, id("air"), new BlockItem(Blocks.AIR, new FabricItemSettings().rarity(Rarity.EPIC)));
    public static final BlockItem CAVE_AIR_BLOCK_ITEM = Registry.register(Registries.ITEM, id("cave_air"), new BlockItem(Blocks.CAVE_AIR, new FabricItemSettings().rarity(Rarity.EPIC)));
    public static final BlockItem VOID_AIR_BLOCK_ITEM = Registry.register(Registries.ITEM, id("void_air"), new BlockItem(Blocks.VOID_AIR, new FabricItemSettings().rarity(Rarity.EPIC)));
    public static final BlockItem END_PORTAL_BLOCK_ITEM = Registry.register(Registries.ITEM, id("end_portal"), new BlockItem(Blocks.END_PORTAL, new FabricItemSettings().rarity(Rarity.EPIC)));
    public static final BlockItem END_GATEWAY_BLOCK_ITEM = Registry.register(Registries.ITEM, id("end_gateway"), new BlockItem(Blocks.END_GATEWAY, new FabricItemSettings().rarity(Rarity.EPIC)));
    //Packet Identifiers
    public static final Identifier MOD_INSTALLED_PACKET = id("mod_installed");

    @Override
    public void onInitialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.OPERATOR).register(content -> {
            for (PistonType type : PistonType.values()) {
                ItemStack stack = new ItemStack(VisibleBarriersCommon.MOVING_PISTON_BLOCK_ITEM);
                NbtCompound nbtCompound = new NbtCompound();
                nbtCompound.putString(PistonExtensionBlock.TYPE.getName(), String.valueOf(type));
                stack.setSubNbt("BlockStateTag", nbtCompound);
                content.add(stack);
            }
            for (BlockItem item : List.of(AIR_BLOCK_ITEM, CAVE_AIR_BLOCK_ITEM, VOID_AIR_BLOCK_ITEM, END_PORTAL_BLOCK_ITEM, END_GATEWAY_BLOCK_ITEM)) {
                content.add(new ItemStack(item));
            }
        });
        ServerPlayNetworking.registerGlobalReceiver(MOD_INSTALLED_PACKET, (server, player, handler, buf, responseSender) -> {
            LOGGER.info("{} has mod Visible Barriers installed.", player.getEntityName());
            ServerPlayNetworking.send(player, MOD_INSTALLED_PACKET, buf);
        });
    }

    public static Identifier id(String ... path) {
        return new Identifier(MOD_ID, String.join(".", path));
    }
}