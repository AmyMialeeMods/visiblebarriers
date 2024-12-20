package xyz.amymialee.visiblebarriers;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.ModelIds;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.NotNull;
import xyz.amymialee.mialib.templates.MDataGen;
import xyz.amymialee.visiblebarriers.client.BlockHolderModelRenderer;

public class VisibleDataGen extends MDataGen {
    @Override
    protected void generateTranslations(MLanguageProvider provider, RegistryWrapper.WrapperLookup registryLookup, FabricLanguageProvider.@NotNull TranslationBuilder builder) {
        builder.add(VisibleBarriers.MOD_ID, "Visible Barriers");
        builder.add(VisibleBarriers.MOD_ID + ".existing_holders", "Blockstate Holders");
        builder.add(VisibleBarriers.MOD_ID + ".hidden_holders", "Extra Blockstate Holders");
        builder.add(VisibleClient.KEYBIND_VISIBILITY.getTranslationKey(), "Toggle Visibility");
        builder.add(VisibleClient.KEYBIND_WEATHER.getTranslationKey(), "Toggle Weather");
        builder.add(VisibleClient.KEYBIND_GAMMA.getTranslationKey(), "Toggle Gamma");
        builder.add(VisibleClient.KEYBIND_FOG.getTranslationKey(), "Toggle Fog");
        builder.add(VisibleClient.KEYBIND_TIME.getTranslationKey(), "Toggle Time");
        builder.add(VisibleOptions.VISIBILITY_ENABLED.getTranslationKey(), "Visibility Enabled");
        builder.add(VisibleOptions.VISIBILITY_ENABLED.getDescriptionTranslationKey(), "Toggles the visibility of invisible blocks.");
        builder.add(VisibleOptions.VISIBLE_BARRIERS.getTranslationKey(), "Visible Barriers");
        builder.add(VisibleOptions.VISIBLE_BARRIERS.getDescriptionTranslationKey(), "Should barriers be shown.");
        builder.add(VisibleOptions.VISIBLE_LIGHTS.getTranslationKey(), "Visible Lights");
        builder.add(VisibleOptions.VISIBLE_LIGHTS.getDescriptionTranslationKey(), "Should light blocks be shown.");
        builder.add(VisibleOptions.VISIBLE_STRUCTURE_VOIDS.getTranslationKey(), "Visible Structure Voids");
        builder.add(VisibleOptions.VISIBLE_STRUCTURE_VOIDS.getDescriptionTranslationKey(), "Should structure voids be shown.");
        builder.add(VisibleOptions.VISIBLE_PISTON_EXTENSIONS.getTranslationKey(), "Visible Piston Extensions");
        builder.add(VisibleOptions.VISIBLE_PISTON_EXTENSIONS.getDescriptionTranslationKey(), "Should piston extensions be shown.");
        builder.add(VisibleOptions.VISIBLE_BUBBLE_COLUMNS.getTranslationKey(), "Visible Bubble Columns");
        builder.add(VisibleOptions.VISIBLE_BUBBLE_COLUMNS.getDescriptionTranslationKey(), "Should bubble columns be shown.");
        builder.add(VisibleOptions.VISIBLE_VOID_AIR.getTranslationKey(), "Visible Void Air");
        builder.add(VisibleOptions.VISIBLE_VOID_AIR.getDescriptionTranslationKey(), "Should void air be shown.");
        builder.add(VisibleOptions.VISIBLE_CAVE_AIR.getTranslationKey(), "Visible Cave Air");
        builder.add(VisibleOptions.VISIBLE_CAVE_AIR.getDescriptionTranslationKey(), "Should cave air be shown.");
        builder.add(VisibleOptions.VISIBLE_AIR.getTranslationKey(), "Visible Air");
        builder.add(VisibleOptions.VISIBLE_AIR.getDescriptionTranslationKey(), "Should air be shown.");
        builder.add(VisibleOptions.WEATHER_ENABLED.getTranslationKey(), "Weather Enabled");
        builder.add(VisibleOptions.WEATHER_ENABLED.getDescriptionTranslationKey(), "Toggles the visibility of weather.");
        builder.add(VisibleOptions.WEATHER_STATE.getTranslationKey(), "Weather State");
        builder.add(VisibleOptions.WEATHER_STATE.getDescriptionTranslationKey(), "Weather to be shown.");
        builder.add(VisibleOptions.GAMMA_ENABLED.getTranslationKey(), "Gamma Enabled");
        builder.add(VisibleOptions.GAMMA_ENABLED.getDescriptionTranslationKey(), "Toggles the visibility of gamma.");
        builder.add(VisibleOptions.GAMMA_VALUE.getTranslationKey(), "Gamma Value");
        builder.add(VisibleOptions.GAMMA_VALUE.getDescriptionTranslationKey(), "Light level to be used while gamma is enabled.");
        builder.add(VisibleOptions.FOG_ENABLED.getTranslationKey(), "Fog Enabled");
        builder.add(VisibleOptions.FOG_ENABLED.getDescriptionTranslationKey(), "Toggles the visibility of fog.");
        builder.add(VisibleOptions.TIME_ENABLED.getTranslationKey(), "Time Enabled");
        builder.add(VisibleOptions.TIME_ENABLED.getDescriptionTranslationKey(), "Toggles the visibility of time.");
        builder.add(VisibleOptions.TIME_VALUE.getTranslationKey(), "Time Value");
        builder.add(VisibleOptions.TIME_VALUE.getDescriptionTranslationKey(), "Time to be used while time is enabled.");
        builder.add(VisibleOptions.HIDE_PARTICLES.getTranslationKey(), "Hide Particles");
        builder.add(VisibleOptions.HIDE_PARTICLES.getDescriptionTranslationKey(), "Hides particles shown by blocks such as barriers.");
        builder.add(VisibleBarriers.BLOCK_HOLDER_ITEM.getTranslationKey(), "Block Holder");
        builder.add(VisibleBarriers.WATER.getTranslationKey(), "Water");
        builder.add(VisibleBarriers.LAVA.getTranslationKey(), "Lava");
    }

    @Override
    protected void generateBlockStateModels(MModelProvider provider, @NotNull BlockStateModelGenerator generator) {
        generator.itemModelOutput.accept(VisibleBarriers.BLOCK_HOLDER_ITEM, ItemModels.special(ModelIds.getItemModelId(VisibleBarriers.BLOCK_HOLDER_ITEM), new BlockHolderModelRenderer.Unbaked()));
    }
}