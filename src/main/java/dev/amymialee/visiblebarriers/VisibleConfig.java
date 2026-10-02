package dev.amymialee.visiblebarriers;

import dev.amymialee.visiblebarriers.util.MidnightLibExtras;
import dev.amymialee.visiblebarriers.util.PatreonButton;
import eu.midnightdust.lib.config.MidnightConfig;
import eu.midnightdust.lib.config.MidnightConfigListWidget;
import eu.midnightdust.lib.config.MidnightConfigScreen;

import java.util.Objects;

/**
 * Using midnightlib for the time being, might make a custom screen later.
 */
public class VisibleConfig extends MidnightConfig {
    public static final String MOD = "mod";
    public static final String KEYS = "keys";

    @Entry(category = MOD, name = "Visibility")
    public static boolean visibility = false;
    public static boolean prevVisibility = false;

    @Entry(category = MOD, name = "Hide Vanilla Barrier Particles")
    public static boolean hideParticles = true;

    @Entry(category = MOD, name = "Fullbright")
    public static boolean gamma = false;
    @Entry(category = MOD, name = "Fullbright Power", min = 0f, max = 255f, precision = 1)
    public static float gammaPower = 255f;

    @Entry(category = MOD, name = "Forced Time Enabled")
    public static boolean forcedTimeEnabled = false;
    @Entry(category = MOD, name = "Forced Time", isSlider = true, min = 0, max = 24000)
    public static int forcedTime = 6000;

    @Entry(category = MOD, name = "Forced Weather")
    public static Weather forcedWeather = Weather.UNCHANGED;

    @Entry(category = MOD, name = "Base Zoom Level", isSlider = true, min = 0.1f, max = 8f, precision = 2)
    public static float baseZoom = 2.8f;
    public static boolean holdingZoom = false;
    public static float zoomScroll = 1.0F;

    @Entry(category = MOD, name = "Show Actionbar Feedback Text")
    public static boolean sendFeedback = false;

    @Condition(requiredModId = "1234mod")
    @Comment(category = KEYS)
    public static Comment dummy;

    @Override
    public void onTabInit(String tabName, MidnightConfigListWidget list, MidnightConfigScreen screen) {
        list.addEntry(new PatreonButton(), list.defaultEntryHeight);
        if (!Objects.equals(tabName, KEYS)) return;
        MidnightLibExtras.KeybindButton.add(VisibleInput.keyBindingConfig, list, screen);
        MidnightLibExtras.KeybindButton.add(VisibleInput.keyBindingVisibility, list, screen);
        MidnightLibExtras.KeybindButton.add(VisibleInput.keyBindingFullBright, list, screen);
        MidnightLibExtras.KeybindButton.add(VisibleInput.keyBindingZoom, list, screen);
    }

    @Override
    public void writeChanges() {
        super.writeChanges();
        if (prevVisibility != visibility) {
            prevVisibility = visibility;
            VisibleBarriers.reloadWorldRenderer();
        }
    }

    public enum Weather {
        UNCHANGED(-1, -1, "visiblebarriers.weather.default"),
        CLEAR(0, 0, "visiblebarriers.weather.clear"),
        RAIN(1, 0, "visiblebarriers.weather.rain"),
        THUNDER(1, 1, "visiblebarriers.weather.thunder");

        private final int rain;
        private final int thunder;
        private final String translationKey;

        Weather(int rain, int thunder, String translationKey) {
            this.rain = rain;
            this.thunder = thunder;
            this.translationKey = translationKey;
        }

        public int getRain() {
            return this.rain;
        }

        public int getThunder() {
            return this.thunder;
        }

        public String getTranslationKey() {
            return this.translationKey;
        }
    }
}