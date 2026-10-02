package dev.amymialee.visiblebarriers.util;

import eu.midnightdust.lib.config.ButtonEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.MultiLineTextWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.jspecify.annotations.NonNull;

import java.net.URI;
import java.util.List;

public class PatreonButton extends ButtonEntry {
    private static final Font textRenderer = Minecraft.getInstance().font;
    public final Component text;
    public final URI uri = URI.create("https://www.patreon.com/c/amymialee");
    public MultiLineTextWidget title;

    public PatreonButton() {
        super(List.of(), null, null);
        this.text = Component.translatable("visiblebarriers.patreon").withColor(TextColor.GOLD);
        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        title = new MultiLineTextWidget(12, 0, text.copy(), textRenderer).setCentered(true);
        title.setMaxWidth(scaledWidth - 24);
        title.setX(scaledWidth / 2 - (title.getWidth() / 2));
    }

    @Override
    public void extractContent(@NonNull GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        int y = this.getY();
        if (title == null) return;
        title.setY(y + 5);
        title.extractRenderState(context, mouseX, mouseY, tickDelta);
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent click, boolean doubled) {
        ConfirmLinkScreen.confirmLinkNow(Minecraft.getInstance().gui.screen(), uri, true);
        return super.mouseClicked(click, doubled);
    }

    @Override
    public @NonNull List<? extends GuiEventListener> children() {
        return List.of();
    }

    @Override
    public @NonNull List<? extends NarratableEntry> narratables() {
        return List.of();
    }
}