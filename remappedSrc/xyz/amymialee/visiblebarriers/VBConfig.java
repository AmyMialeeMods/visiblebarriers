package xyz.amymialee.visiblebarriers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import xyz.amymialee.visiblebarriers.util.HashPairMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

public class VBConfig {
    private final File optionsFile = new File(MinecraftClient.getInstance().runDirectory, "config/visiblebarriers.json");
    private boolean hideParticles = true;
    private boolean visibleAir = false;
    private boolean fullBright = false;
    private boolean solidLights = false;
    private int customKeys = 0;
    private final HashPairMap<Block, Integer, Integer> blockColors = new HashPairMap<>();
    private final HashPairMap<Item, Integer, Integer> itemColors = new HashPairMap<>();

    public void setHideParticles(boolean hideParticles) {
        this.hideParticles = hideParticles;
        this.saveConfig();
    }

    public void setVisibleAir(boolean visibleAir) {
        this.visibleAir = visibleAir;
        this.saveConfig();
    }

    public void setFullBright(boolean fullBright) {
        this.fullBright = fullBright;
        this.saveConfig();
    }

    public void addBlock(Block block, int color) {
        this.blockColors.put(block, color, -1);
        this.saveConfig();
    }

    public void addBlock(Block block, int color, int key) {
        this.blockColors.put(block, color, key);
        this.saveConfig();
    }

    public void addItem(Item item, int color) {
        this.itemColors.put(item, color, -1);
        this.saveConfig();
    }

    public void addItem(Item item, int color, int key) {
        this.itemColors.put(item, color, key);
        this.saveConfig();
    }

    public void removeBlock(Block block) {
        this.blockColors.remove(block);
        this.saveConfig();
    }

    public void removeItem(Item item) {
        this.itemColors.remove(item);
        this.saveConfig();
    }

    public boolean shouldHideParticles() {
        return this.hideParticles;
    }

    public boolean isAirVisible() {
        return this.visibleAir;
    }

    public boolean isFullBright() {
        return this.fullBright;
    }

    public boolean areLightsSolid() {
        return this.solidLights;
    }

    public boolean hasBlock(Block block) {
        return this.blockColors.containsKey(block);
    }

    public boolean hasItem(Item item) {
        return this.itemColors.containsKey(item);
    }

    public int getCustomKeyCount() {
        return this.customKeys;
    }

    public int getBlockColor(Block block) {
        Pair<Integer, Integer> pair = this.blockColors.get(block);
        if (pair == null || VisibleBarriers.spareKeyBindings.findOrDefault(pair.getRight(), false)) {
            return 0xFFFFFF;
        }
        return pair.getLeft();
    }

    public int getItemColor(Item item) {
        Pair<Integer, Integer> pair = this.itemColors.get(item);
        if (pair == null || VisibleBarriers.spareKeyBindings.findOrDefault(pair.getRight(), false)) {
            return 0xFFFFFF;
        }
        return pair.getLeft();
    }

    public VBConfig() {
        loadConfig(this);
    }

    private void addBlocks(int color, Block ... blocks) {
        for (Block block : blocks) {
            this.blockColors.put(block, color, -1);
        }
    }

    private void addItems(int color, Block ... items) {
        for (Block block : items) {
            this.itemColors.put(block.asItem(), color, -1);
        }
    }

    private void addItems(int color, Item ... items) {
        for (Item item : items) {
            this.itemColors.put(item, color, -1);
        }
    }

    public void saveConfig() {
        saveConfig(this);
    }

    protected static void saveConfig(VBConfig config) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject json = new JsonObject();
            json.addProperty("hideParticles", config.hideParticles);
            json.addProperty("visibleAir", config.visibleAir);
            json.addProperty("fullBright", config.fullBright);
            if (config.solidLights) json.addProperty("solidLights", true);
            if (config.customKeys > 0) json.addProperty("customKeys", config.customKeys);

            JsonArray blockColors = new JsonArray();
            for (Map.Entry<Block, Pair<Integer, Integer>> entry : config.blockColors.entrySet()) {
                JsonObject object = new JsonObject();
                object.addProperty("block", String.valueOf(Registry.BLOCK.getId(entry.getKey())));
                Pair<Integer, Integer> value = entry.getValue();
                object.addProperty("color", value.getLeft());
                if (value.getRight() != -1) {
                    object.addProperty("key", value.getRight() + 1);
                }
                blockColors.add(object);
            }
            json.add("blockColors", blockColors);

            JsonArray itemColors = new JsonArray();
            for (Map.Entry<Item, Pair<Integer, Integer>> entry : config.itemColors.entrySet()) {
                JsonObject object = new JsonObject();
                object.addProperty("item", String.valueOf(Registry.ITEM.getId(entry.getKey())));
                Pair<Integer, Integer> value = entry.getValue();
                object.addProperty("color", value.getLeft());
                if (value.getRight() != -1) {
                    object.addProperty("key", value.getRight());
                }
                itemColors.add(object);
            }
            json.add("itemColors", itemColors);
            String jsonData = gson.toJson(json);
            FileWriter writer = new FileWriter(config.optionsFile);
            writer.write(jsonData);
            writer.close();
        } catch (Exception e) {
            VisibleBarriers.logger.info(e.toString());
        }
    }

    public void loadConfig() {
        loadConfig(this);
    }

    protected static void loadConfig(VBConfig config) {
        try {
            Gson gson = new Gson();
            FileReader reader = new FileReader(config.optionsFile);
            JsonObject data = gson.fromJson(reader, JsonObject.class);
            if (data.has("hideParticles")) {
                config.hideParticles = data.get("hideParticles").getAsBoolean();
            }
            if (data.has("visibleAir")) {
                config.visibleAir = data.get("visibleAir").getAsBoolean();
            }
            if (data.has("fullBright")) {
                config.fullBright = data.get("fullBright").getAsBoolean();
            }
            if (data.has("solidLights")) {
                config.solidLights = data.get("solidLights").getAsBoolean();
            }
            if (data.has("customKeys")) {
                config.customKeys = data.get("customKeys").getAsInt();
            }
            if (data.has("blockColors")) {
                config.blockColors.clear();
                JsonArray blockColors = data.get("blockColors").getAsJsonArray();
                for (JsonElement element : blockColors) {
                    JsonObject object = element.getAsJsonObject();
                    String blockName = object.get("block").getAsString();
                    int color = object.get("color").getAsInt();
                    Block block = Registry.BLOCK.get(new Identifier(blockName));
                    if (block != Blocks.AIR) {
                        if (object.has("key")) {
                            int key = object.get("key").getAsInt();
                            config.blockColors.put(block, color, key - 1);
                        } else {
                            config.blockColors.put(block, color, -1);
                        }
                    }
                }
            } else {
                addDefaultBlocks(config);
            }
            if (data.has("itemColors")) {
                config.itemColors.clear();
                JsonArray itemColors = data.get("itemColors").getAsJsonArray();
                for (JsonElement element : itemColors) {
                    JsonObject object = element.getAsJsonObject();
                    String itemName = object.get("item").getAsString();
                    int color = object.get("color").getAsInt();
                    Item item = Registry.ITEM.get(new Identifier(itemName));
                    if (item != Items.AIR) {
                        if (object.has("key")) {
                            int key = object.get("key").getAsInt();
                            config.itemColors.put(item, color, key);
                        } else {
                            config.itemColors.put(item, color, -1);
                        }
                    }
                }
            } else {
                addDefaultItems(config);
            }
        } catch (FileNotFoundException e) {
            VisibleBarriers.logger.info("Config data not found.");
            addDefaultBlocks(config);
            addDefaultItems(config);
        } catch (Exception e) {
            VisibleBarriers.logger.info("Error loading config data.");
            VisibleBarriers.logger.info(e.toString());
        }
    }

    private static void addDefaultBlocks(VBConfig config) {
        config.addBlocks(11546150, Blocks.INFESTED_STONE, Blocks.INFESTED_DEEPSLATE, Blocks.INFESTED_COBBLESTONE, Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
        config.addBlocks(11546150, Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER, Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER, Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
        config.addBlocks(11546150, Blocks.PETRIFIED_OAK_SLAB);
        config.addBlocks(11546150, Blocks.INFESTED_STONE, Blocks.INFESTED_DEEPSLATE, Blocks.INFESTED_COBBLESTONE, Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
        config.addBlocks(500, Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB, Blocks.OAK_SLAB, Blocks.SPRUCE_SLAB, Blocks.BIRCH_SLAB, Blocks.JUNGLE_SLAB, Blocks.ACACIA_SLAB, Blocks.DARK_OAK_SLAB, Blocks.MANGROVE_SLAB, Blocks.CRIMSON_SLAB, Blocks.WARPED_SLAB, Blocks.STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SANDSTONE_SLAB, Blocks.CUT_SANDSTONE_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.BRICK_SLAB, Blocks.STONE_BRICK_SLAB, Blocks.MUD_BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, Blocks.QUARTZ_SLAB, Blocks.RED_SANDSTONE_SLAB, Blocks.CUT_RED_SANDSTONE_SLAB, Blocks.PURPUR_SLAB, Blocks.PRISMARINE_SLAB, Blocks.PRISMARINE_BRICK_SLAB, Blocks.DARK_PRISMARINE_SLAB, Blocks.POLISHED_GRANITE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_DIORITE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.SMOOTH_QUARTZ_SLAB, Blocks.GRANITE_SLAB, Blocks.ANDESITE_SLAB, Blocks.RED_NETHER_BRICK_SLAB, Blocks.POLISHED_ANDESITE_SLAB, Blocks.DIORITE_SLAB, Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.DEEPSLATE_TILE_SLAB, Blocks.BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        saveConfig(config);
    }

    private static void addDefaultItems(VBConfig config) {
        config.addItems(11546150, Blocks.INFESTED_STONE, Blocks.INFESTED_DEEPSLATE, Blocks.INFESTED_COBBLESTONE, Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
        config.addItems(11546150, Blocks.WAXED_COPPER_BLOCK, Blocks.WAXED_EXPOSED_COPPER, Blocks.WAXED_WEATHERED_COPPER, Blocks.WAXED_OXIDIZED_COPPER, Blocks.WAXED_CUT_COPPER, Blocks.WAXED_EXPOSED_CUT_COPPER, Blocks.WAXED_WEATHERED_CUT_COPPER, Blocks.WAXED_OXIDIZED_CUT_COPPER, Blocks.WAXED_CUT_COPPER_SLAB, Blocks.WAXED_EXPOSED_CUT_COPPER_SLAB, Blocks.WAXED_WEATHERED_CUT_COPPER_SLAB, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, Blocks.WAXED_CUT_COPPER_STAIRS, Blocks.WAXED_EXPOSED_CUT_COPPER_STAIRS, Blocks.WAXED_WEATHERED_CUT_COPPER_STAIRS, Blocks.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
        config.addItems(11546150, Blocks.PETRIFIED_OAK_SLAB);
        config.addItems(11546150, Blocks.INFESTED_STONE, Blocks.INFESTED_DEEPSLATE, Blocks.INFESTED_COBBLESTONE, Blocks.INFESTED_STONE_BRICKS, Blocks.INFESTED_MOSSY_STONE_BRICKS, Blocks.INFESTED_CRACKED_STONE_BRICKS, Blocks.INFESTED_CHISELED_STONE_BRICKS);
        config.addItems(500, Blocks.CUT_COPPER_SLAB, Blocks.EXPOSED_CUT_COPPER_SLAB, Blocks.WEATHERED_CUT_COPPER_SLAB, Blocks.OXIDIZED_CUT_COPPER_SLAB, Blocks.OAK_SLAB, Blocks.SPRUCE_SLAB, Blocks.BIRCH_SLAB, Blocks.JUNGLE_SLAB, Blocks.ACACIA_SLAB, Blocks.DARK_OAK_SLAB, Blocks.MANGROVE_SLAB, Blocks.CRIMSON_SLAB, Blocks.WARPED_SLAB, Blocks.STONE_SLAB, Blocks.SMOOTH_STONE_SLAB, Blocks.SANDSTONE_SLAB, Blocks.CUT_SANDSTONE_SLAB, Blocks.COBBLESTONE_SLAB, Blocks.BRICK_SLAB, Blocks.STONE_BRICK_SLAB, Blocks.MUD_BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, Blocks.QUARTZ_SLAB, Blocks.RED_SANDSTONE_SLAB, Blocks.CUT_RED_SANDSTONE_SLAB, Blocks.PURPUR_SLAB, Blocks.PRISMARINE_SLAB, Blocks.PRISMARINE_BRICK_SLAB, Blocks.DARK_PRISMARINE_SLAB, Blocks.POLISHED_GRANITE_SLAB, Blocks.SMOOTH_RED_SANDSTONE_SLAB, Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_DIORITE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.END_STONE_BRICK_SLAB, Blocks.SMOOTH_SANDSTONE_SLAB, Blocks.SMOOTH_QUARTZ_SLAB, Blocks.GRANITE_SLAB, Blocks.ANDESITE_SLAB, Blocks.RED_NETHER_BRICK_SLAB, Blocks.POLISHED_ANDESITE_SLAB, Blocks.DIORITE_SLAB, Blocks.COBBLED_DEEPSLATE_SLAB, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.DEEPSLATE_TILE_SLAB, Blocks.BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        saveConfig(config);
    }
}