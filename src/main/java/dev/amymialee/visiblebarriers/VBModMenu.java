package dev.amymialee.visiblebarriers;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.amymialee.visiblebarriers.common.VisibleBarriersCommon;
import eu.midnightdust.lib.config.MidnightConfig;

public class VBModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> MidnightConfig.getScreen(parent, VisibleBarriersCommon.MOD_ID);
    }
}