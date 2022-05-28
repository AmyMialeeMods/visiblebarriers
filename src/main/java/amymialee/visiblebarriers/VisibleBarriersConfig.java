package amymialee.visiblebarriers;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = VisibleBarriers.MODID)
public final class VisibleBarriersConfig implements ConfigData {
    /// Decides if you can save items in the hotbar tab by dragging them in.
    public boolean allowHotbarQuickSaving = false;
}
