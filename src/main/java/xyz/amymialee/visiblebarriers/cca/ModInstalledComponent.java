package xyz.amymialee.visiblebarriers.cca;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
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
    public void readFromNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        this.installed = tag.getBoolean("installed");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag, RegistryWrapper.WrapperLookup lookup) {
        tag.putBoolean("installed", this.installed);
    }
}