package xyz.amymialee.visiblebarriers.util;

public enum WeatherState {
    CLEAR(0, 0),
    RAIN(1, 0),
    THUNDER(1, 1);

    private final int rain;
    private final int thunder;

    WeatherState(int rain, int thunder) {
        this.rain = rain;
        this.thunder = thunder;
    }

    public int getRain() {
        return this.rain;
    }

    public int getThunder() {
        return this.thunder;
    }
}