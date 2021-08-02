package amymialee.visiblebarriers;

import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.Toml;
import me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml.TomlWriter;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;

public final class VisibleBarriersConfig {
    private transient File file;
    /// Decides if you can save items in the hotbar tab by dragging them in.
    public boolean allowHotbarQuickSaving = false;

    private VisibleBarriersConfig() {}

    public static VisibleBarriersConfig load() {
        File file = new File(
                FabricLoader.getInstance().getConfigDir().toString(),
                VisibleBarriers.MODID + ".toml"
        );
        VisibleBarriersConfig config;
        if (file.exists()) {
            Toml configTOML = new Toml().read(file);
            config = configTOML.to(VisibleBarriersConfig.class);
            config.file = file;
        } else {
            config = new VisibleBarriersConfig();
            config.file = file;
            config.save();
        }
        return config;
    }

    public void save() {
        TomlWriter writer = new TomlWriter();
        try {
            writer.write(this, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
