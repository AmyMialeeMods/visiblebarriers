package dev.amymialee.visiblebarriers.util;

public enum Weather {
    DEFAULT(-1, -1, "visiblebarriers.weather.default"),
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
