package amymialee.visiblebarriers;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.Toml;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.io.File;
import java.io.IOException;

public class VisibleBarriersModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return VisibleBarriersModMenu::genConfig;
    }

    private static Screen genConfig(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(translatable("title"))
                .setSavingRunnable(VisibleBarriers.config::save);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        builder.getOrCreateCategory(translatable("category.general"))
                .addEntry(entryBuilder
                        .startBooleanToggle(
                                translatable("allowHotbarQuickSaving"),
                                VisibleBarriers.config.allowHotbarQuickSaving
                        )
                        .setSaveConsumer(value -> VisibleBarriers.config.allowHotbarQuickSaving = value)
                        .build()
                );
        return builder.build();
    }

    public static Text translatable(String text) {
        return new TranslatableText("config." + VisibleBarriers.MODID + "." + text);
    }
}
