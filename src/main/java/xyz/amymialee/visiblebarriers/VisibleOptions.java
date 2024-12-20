package xyz.amymialee.visiblebarriers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import xyz.amymialee.mialib.mvalues.MValue;
import xyz.amymialee.mialib.mvalues.MValueEnum;
import xyz.amymialee.visiblebarriers.util.WeatherState;

public class VisibleOptions {
    public static final MValue<Boolean> VISIBILITY_ENABLED = MValue.of(VisibleBarriers.id("visibility_enabled"), MValue.BOOLEAN_FALSE).onChange((v) -> reloadWorldRenderer()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.ENDER_EYE).build();
    public static final MValue<Boolean> VISIBLE_BARRIERS = MValue.of(VisibleBarriers.id("visible_barriers"), MValue.BOOLEAN_TRUE).onChange((v) -> reloadWorldRenderer()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.BARRIER).build();
    public static final MValue<Boolean> VISIBLE_LIGHTS = MValue.of(VisibleBarriers.id("visible_lights"), MValue.BOOLEAN_TRUE).onChange((v) -> reloadWorldRenderer()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.LIGHT).build();
    public static final MValue<Boolean> VISIBLE_STRUCTURE_VOIDS = MValue.of(VisibleBarriers.id("visible_structure_voids"), MValue.BOOLEAN_TRUE).onChange((v) -> reloadWorldRenderer()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.STRUCTURE_VOID).build();
    public static final MValue<Boolean> VISIBLE_PISTON_EXTENSIONS = MValue.of(VisibleBarriers.id("visible_piston_extensions"), MValue.BOOLEAN_TRUE).onChange((v) -> reloadWorldRenderer()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.PISTON).build();
    public static final MValue<Boolean> VISIBLE_BUBBLE_COLUMNS = MValue.of(VisibleBarriers.id("visible_bubble_columns"), MValue.BOOLEAN_TRUE).onChange((v) -> reloadWorldRenderer()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.WATER_BUCKET).build();
    public static final MValue<Boolean> VISIBLE_VOID_AIR = MValue.of(VisibleBarriers.id("visible_void_air"), MValue.BOOLEAN_FALSE).onChange((v) -> reloadWorldRenderer()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.BLACK_STAINED_GLASS).build();
    public static final MValue<Boolean> VISIBLE_CAVE_AIR = MValue.of(VisibleBarriers.id("visible_cave_air"), MValue.BOOLEAN_FALSE).onChange((v) -> reloadWorldRenderer()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.GRAY_STAINED_GLASS).build();
    public static final MValue<Boolean> VISIBLE_AIR = MValue.of(VisibleBarriers.id("visible_air"), MValue.BOOLEAN_FALSE).onChange((v) -> reloadWorldRenderer()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.WHITE_STAINED_GLASS).build();
    public static final MValue<Boolean> WEATHER_ENABLED = MValue.of(VisibleBarriers.id("weather_enabled"), MValue.BOOLEAN_FALSE).onChange((v) -> updateWeather()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.WATER_BUCKET).build();
    public static final MValue<WeatherState> WEATHER_STATE = MValue.of(VisibleBarriers.id("weather_state"), new MValueEnum<>(WeatherState.CLEAR)).onChange((v) -> updateWeather()).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.WATER_BUCKET).build();
    public static final MValue<Boolean> GAMMA_ENABLED = MValue.of(VisibleBarriers.id("gamma_enabled"), MValue.BOOLEAN_FALSE).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.GLOWSTONE_DUST).build();
    public static final MValue<Float> GAMMA_VALUE = MValue.of(VisibleBarriers.id("gamma_value"), MValue.FLOAT.of(1f, -1f, 16f)).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.GLOWSTONE_DUST).build();
    public static final MValue<Boolean> FOG_ENABLED = MValue.of(VisibleBarriers.id("fog_enabled"), MValue.BOOLEAN_FALSE).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.GRAY_DYE).build();
    public static final MValue<Boolean> TIME_ENABLED = MValue.of(VisibleBarriers.id("time_enabled"), MValue.BOOLEAN_FALSE).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.CLOCK).build();
    public static final MValue<Integer> TIME_VALUE = MValue.of(VisibleBarriers.id("time_value"), MValue.INTEGER.of(6000, 0, 24000)).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.CLOCK).build();
    public static final MValue<Boolean> HIDE_PARTICLES = MValue.of(VisibleBarriers.id("hide_particles"), MValue.BOOLEAN_FALSE).category(MValue.DEFAULT_CATEGORY).clientSide().item(Items.FIREWORK_STAR).build();

    public static void reloadWorldRenderer() {
        if (MinecraftClient.getInstance().worldRenderer != null) MinecraftClient.getInstance().worldRenderer.reload();
    }

    public static void updateWeather() {
        var world = MinecraftClient.getInstance().world;
        if (world == null) return;
        var delta = MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true);
        world.setRainGradient(world.getRainGradient(delta));
        world.setThunderGradient(world.getThunderGradient(delta));
    }
}