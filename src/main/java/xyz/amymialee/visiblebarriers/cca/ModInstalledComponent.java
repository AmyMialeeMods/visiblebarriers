package xyz.amymialee.visiblebarriers.cca;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import org.jetbrains.annotations.NotNull;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import xyz.amymialee.visiblebarriers.common.VisibleBarriersCommon;

public class ModInstalledComponent implements Component {
    public static final ComponentKey<ModInstalledComponent> KEY = ComponentRegistry.getOrCreate(VisibleBarriersCommon.id("installed"), ModInstalledComponent.class);
    private boolean installed = false;

    public boolean isInstalled() {
        return this.installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    @Override
    public void readData(ReadView readView) {
        this.installed = readView.getBoolean("installed", false);
    }

    @Override
    public void writeData(WriteView writeView) {
        writeView.putBoolean("installed", this.installed);
    }
}