package io.github.andriyko69.elementalcreeperscontinued;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ElementalCreepersContinued.MOD_ID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ==================== Explosion radius/power ====================

    private static final ModConfigSpec.IntValue COOKIE_CREEPER_QUANTITY = BUILDER.comment("Cookie Creeper quantity").defineInRange("cookieCreeperQuantity", 25, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue DARK_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Dark Creeper explosion radius").defineInRange("darkCreeperExplosionRadius", 12.d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue EARTH_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Earth Creeper explosion radius").defineInRange("earthCreeperExplosionRadius", 8.d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue ELECTRIC_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Electric Creeper explosion radius").defineInRange("electricCreeperExplosionRadius", 8.d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue FIRE_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Fire Creeper explosion radius").defineInRange("fireCreeperExplosionRadius", 6.d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue ICE_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Ice Creeper explosion radius").defineInRange("iceCreeperExplosionRadius", 10.d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue GHOST_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Ghost Creeper explosion radius").defineInRange("ghostCreeperExplosionRadius", 2.5d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue LIGHT_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Light Creeper explosion radius").defineInRange("lightCreeperExplosionRadius", 4.d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue MAGMA_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Magma Creeper explosion radius").defineInRange("magmaCreeperExplosionRadius", 2d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue PSYCHIC_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Psychic Creeper explosion radius").defineInRange("psychicCreeperExplosionRadius", 3d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue REVERSE_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Reverse Creeper explosion radius").defineInRange("reverseCreeperExplosionRadius", 6d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue SPIDER_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Spider Creeper explosion radius").defineInRange("spiderCreeperExplosionRadius", 7d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue WATER_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Water Creeper explosion radius").defineInRange("waterCreeperExplosionRadius", 5.d, 0.d, Double.MAX_VALUE);

    // ==================== Explosion details ====================

    private static final ModConfigSpec.ConfigValue<List<? extends String>> DARK_CREEPER_DESTROY_BLOCKS = BUILDER.comment("A list of blocks which dark creepers destroy").defineList("darkCreeperDestroyBlocks", List.of("minecraft:torch", "minecraft:redstone_torch", "minecraft:redstone_lamp", "minecraft:redstone_wall_torch", "minecraft:glowstone", "minecraft:lantern", "minecraft:sea_lantern", "minecraft:shroomlight", "minecraft:campfire", "minecraft:soul_campfire"), () -> "minecraft:torch", o -> o instanceof String s && Config.validateBlockName(s));

    private static final ModConfigSpec.DoubleValue GHOST_CREEPER_DAMAGE_MULTIPLIER = BUILDER.comment("Ghost Creeper damage multiplier (compared to regular creeper)").defineInRange("ghostCreeperDamageMultiplier", 0.7d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue PSYCHIC_CREEPER_LAUNCH_MULTIPLIER = BUILDER.comment("Psychica Creeper launch multiplier (compared to regular creeper)").defineInRange("psychicCreeperLaunchMultiplier", 7d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue SPIDER_CREEPER_POISON_TIME_MEDIUM = BUILDER.comment("Spider Creeper poison time on medium difficulty (easy = 0.66x, hard = 1.5x)").defineInRange("spiderCreeperPoisonTimeMedium", 8d, 0.d, Double.MAX_VALUE);

    private static final ModConfigSpec.DoubleValue WATER_CREEPER_PERMANENT_RADIUS = BUILDER.comment("Water Creeper permanent puddle radius").defineInRange("waterCreeperPermanentRadius", 1.d, 0.d, Double.MAX_VALUE);

    // ==================== Misc ====================

    private static final ModConfigSpec.DoubleValue GHOST_CREEPER_SPAWN_CHANCE = BUILDER.comment("Ghost Creeper spawn chance when an elemental creeper dies").defineInRange("ghoseCreeperSpawnChance", 0.01, 0, 1);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int cookieCreeperQuantity;
    public static double darkCreeperExplosionRadius;
    public static double earthCreeperExplosionRadius;
    public static double electricCreeperExplosionRadius;
    public static double fireCreeperExplosionRadius;
    public static double iceCreeperExplosionRadius;
    public static double ghostCreeperExplosionRadius;
    public static double lightCreeperExplosionRadius;
    public static double magmaCreeperExplosionRadius;
    public static double psychicCreeperExplosionRadius;
    public static double reverseCreeperExplosionRadius;
    public static double spiderCreeperExplosionRadius;
    public static double waterCreeperExplosionRadius;

    public static Set<Block> darkCreeperDestroyBlocks;
    public static double ghostCreeperDamageMultiplier;
    public static double psychicCreeperLaunchMultiplier;
    public static double spiderCreeperPoisonTimeMedium;
    public static double waterCreeperPermanentRadius;

    public static double ghostCreeperSpawnChance;

    private static boolean validateBlockName(final Object obj) {
        if (obj instanceof String blockName) {
            return BuiltInRegistries.BLOCK.containsKey(ResourceLocation.parse(blockName));
        }

        return false;
    }

    @SubscribeEvent
    static void onConfigLoading(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() != SPEC) {
            return;
        }

        bake();
    }

    @SubscribeEvent
    static void onConfigReloading(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() != SPEC) {
            return;
        }

        bake();
    }

    private static void bake() {
        cookieCreeperQuantity = COOKIE_CREEPER_QUANTITY.get();
        darkCreeperExplosionRadius = DARK_CREEPER_EXPLOSION_RADIUS.get();
        earthCreeperExplosionRadius = EARTH_CREEPER_EXPLOSION_RADIUS.get();
        electricCreeperExplosionRadius = ELECTRIC_CREEPER_EXPLOSION_RADIUS.get();
        fireCreeperExplosionRadius = FIRE_CREEPER_EXPLOSION_RADIUS.get();
        iceCreeperExplosionRadius = ICE_CREEPER_EXPLOSION_RADIUS.get();
        ghostCreeperExplosionRadius = GHOST_CREEPER_EXPLOSION_RADIUS.get();
        lightCreeperExplosionRadius = LIGHT_CREEPER_EXPLOSION_RADIUS.get();
        magmaCreeperExplosionRadius = MAGMA_CREEPER_EXPLOSION_RADIUS.get();
        psychicCreeperExplosionRadius = PSYCHIC_CREEPER_EXPLOSION_RADIUS.get();
        reverseCreeperExplosionRadius = REVERSE_CREEPER_EXPLOSION_RADIUS.get();
        spiderCreeperExplosionRadius = SPIDER_CREEPER_EXPLOSION_RADIUS.get();
        waterCreeperExplosionRadius = WATER_CREEPER_EXPLOSION_RADIUS.get();

        darkCreeperDestroyBlocks = DARK_CREEPER_DESTROY_BLOCKS.get().stream().map(blockName -> BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockName))).collect(Collectors.toSet());
        ghostCreeperDamageMultiplier = GHOST_CREEPER_DAMAGE_MULTIPLIER.get();
        psychicCreeperLaunchMultiplier = PSYCHIC_CREEPER_LAUNCH_MULTIPLIER.get();
        spiderCreeperPoisonTimeMedium = SPIDER_CREEPER_POISON_TIME_MEDIUM.get();
        waterCreeperPermanentRadius = WATER_CREEPER_PERMANENT_RADIUS.get();

        ghostCreeperSpawnChance = GHOST_CREEPER_SPAWN_CHANCE.get();
    }
}
