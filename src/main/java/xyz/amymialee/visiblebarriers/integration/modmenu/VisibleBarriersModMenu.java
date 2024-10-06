package xyz.amymialee.visiblebarriers.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import xyz.amymialee.visiblebarriers.VisibleBarriers;
import xyz.amymialee.visiblebarriers.VisibleConfig;

public class VisibleBarriersModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("title.visiblebarriers.config"))
                    ;
            ConfigCategory generalCategory = builder.getOrCreateCategory(Text.translatable("category.visiblebarriers.general"));
            ConfigEntryBuilder configEntryBuilder = builder.entryBuilder();

            // Visible Barrier
            generalCategory.addEntry(configEntryBuilder.startBooleanToggle(Text.translatable("option.visiblebarriers.visible_barrier"), VisibleConfig.isBarrierVisible())
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("option.visiblebarriers.visible_barrier.tooltip"))
                    .setSaveConsumer(newValue -> {
                        VisibleConfig.setVisibleBarrier(newValue);
                        VisibleBarriers.setBarriers(newValue);
                    })
                    .build()
            );

            // Visible Air
            generalCategory.addEntry(configEntryBuilder.startBooleanToggle(Text.translatable("option.visiblebarriers.visible_air"), VisibleConfig.isAirVisible())
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("option.visiblebarriers.visible_air.tooltip"))
                    .setSaveConsumer(newValue -> {
                        VisibleConfig.setVisibleAir(newValue);
                        VisibleBarriers.reloadWorldRenderer();

                    })
                    .build()
            );

            // Hide Particles
            generalCategory.addEntry(configEntryBuilder.startBooleanToggle(Text.translatable("option.visiblebarriers.hide_particles"), VisibleConfig.shouldHideParticles())
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("option.visiblebarriers.hide_particles.tooltip"))
                    .setSaveConsumer(VisibleConfig::setHideParticles)
                    .build()
            );

            // Send Feedback
            generalCategory.addEntry(configEntryBuilder.startBooleanToggle(Text.translatable("option.visiblebarriers.send_feedback"), VisibleConfig.shouldSendFeedback())
                    .setDefaultValue(true)
                    .setTooltip(Text.translatable("option.visiblebarriers.send_feedback.tooltip"))
                    .setSaveConsumer(VisibleConfig::setSendFeedback)
                    .build()
            );

            // Solid Lights
            generalCategory.addEntry(configEntryBuilder.startBooleanToggle(Text.translatable("option.visiblebarriers.solid_lights"), VisibleConfig.areLightsSolid())
                    .setDefaultValue(false)
                    .setTooltip(Text.translatable("option.visiblebarriers.solid_lights.tooltip"))
                    .setSaveConsumer(VisibleConfig::setSolidLights)
                    .build()
            );

            // Base Zoom
            generalCategory.addEntry(configEntryBuilder.startFloatField(Text.translatable("option.visiblebarriers.base_zoom"),VisibleConfig.getBaseZoom())
                    .setDefaultValue(2.8f)
                    .setTooltip(Text.translatable("option.visiblebarriers.base_zoom.tooltip"))
                    .setSaveConsumer(VisibleConfig::setBaseZoom)
                    .build()
            );

            // Forced Time Value
            generalCategory.addEntry(configEntryBuilder.startLongField(Text.translatable("option.visiblebarriers.forced_time"),VisibleConfig.getForcedTime())
                    .setDefaultValue(6000)
                    .setTooltip(Text.translatable("option.visiblebarriers.forced_time.tooltip"))
                    .setSaveConsumer(VisibleConfig::setForcedTime)
                    .build()
            );

            return builder.build();

        };
    }
}
