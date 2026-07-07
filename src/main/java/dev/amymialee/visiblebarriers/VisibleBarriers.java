package dev.amymialee.visiblebarriers;

import dev.amymialee.visiblebarriers.util.TransparentBlockStateModel;
import dev.amymialee.visiblebarriers.util.Weather;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class VisibleBarriers implements ClientModInitializer {
    public static final String MOD_ID = "visiblebarriers";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    protected static boolean toggleVisible = false;
    protected static boolean toggleFullBright = false;
    protected static boolean toggleTime = false;
    protected static boolean holdingZoom = false;
    protected static Weather setWeather = Weather.DEFAULT;
    protected static float zoomScroll = 1.0F;

    @Override
    public void onInitializeClient() {
        VisibleInput.init();
        VisibleConfig.loadConfig();
        CustomUnbakedBlockStateModel.register(VisibleBarriers.id("transparent"), TransparentBlockStateModel.Unbaked.CODEC);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.OP_BLOCKS).register(content -> {
            if (!content.getContext().hasPermissions()) return;
            Consumer<ItemStack> consumer = (stack) -> {
                if (stack.getItem() == Items.AIR) return;
                var missing = true;
                for (var tab : CreativeModeTabs.tabs()) {
                    if (tab.getType() == CreativeModeTab.Type.SEARCH || !tab.contains(stack)) continue;
                    missing = false;
                    break;
                }
                if (missing) content.accept(stack);
            };
            for (var item : BuiltInRegistries.ITEM.stream().toList()) consumer.accept(new ItemStack(item));
            for (var block : BuiltInRegistries.BLOCK.stream().toList()) {
                var stack = new ItemStack(block);
                if (stack.getItem() != Items.AIR) {
                    consumer.accept(stack);
                } else if (Minecraft.getInstance().level != null) {
                    var itemStack = new ItemStack(Items.COMMAND_BLOCK);
                    var blockEntity = new CommandBlockEntity(BlockPos.ZERO, Blocks.COMMAND_BLOCK.defaultBlockState());
                    blockEntity.getCommandBlock().setCustomName(block.getName());
                    blockEntity.getCommandBlock().setCommand("setblock ~ ~ ~ " + BuiltInRegistries.BLOCK.getKey(block));
                    blockEntity.setAutomatic(true);
                    try (var reporter = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER)) {
                        var output = TagValueOutput.createWithContext(reporter, Minecraft.getInstance().level.registryAccess());
                        blockEntity.saveCustomOnly(output);
                        blockEntity.removeComponentsFromTag(output);
                        BlockItem.setBlockEntityData(itemStack, blockEntity.getType(), output);
                        itemStack.applyComponents(blockEntity.collectComponents());
                    }
                    itemStack.set(DataComponents.ITEM_NAME, block.getName());
                    consumer.accept(itemStack);
                }
            }
        });
    }

    public static void reloadWorldRenderer() {
        Minecraft.getInstance().levelRenderer.allChanged();
    }

    public static boolean isVisibilityEnabled() {
        return toggleVisible;
    }

    public static void toggleVisible() {
        setVisible(!toggleVisible);
    }

    public static void setVisible(boolean visible) {
        toggleVisible = visible;
        reloadWorldRenderer();
    }

    public static boolean isFullBrightEnabled() {
        return toggleFullBright;
    }

    public static void toggleFullBright() {
        setFullBright(!toggleFullBright);
    }

    public static void setFullBright(boolean fullBright) {
        toggleFullBright = fullBright;
    }

    public static boolean isTimeEnabled() {
        return toggleTime;
    }

    public static void toggleTime() {
        setTime(!toggleTime);
    }

    public static void setTime(boolean time) {
        toggleTime = time;
    }

    public static Weather getWeather() {
        return setWeather;
    }

    public static void setWeather(Weather weather) {
        setWeather = weather;
    }

    public static boolean isHoldingZoom() {
        return holdingZoom;
    }

    public static float getZoomModifier() {
        return (float) Mth.clamp(4f / Math.pow(zoomScroll, 2), 0.001f, 1);
    }

    public static void modifyZoomModifier(float amount) {
        zoomScroll -= amount;
        zoomScroll = Mth.clamp(zoomScroll, 0.01f, 1000);
    }

    public static @NotNull Identifier id(String... path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, String.join(".", path));
    }
}