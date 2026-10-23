package io.github.andriyko69.elementalcreeperscontinued;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

@EventBusSubscriber(modid = ElementalCreepersContinued.MOD_ID)
public class Config {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final Map<String, String> LEGACY_KEYS = Map.ofEntries(
            Map.entry("cookieCreeperQuantity", "cookie_creeper.quantity"),
            Map.entry("darkCreeperExplosionRadius", "dark_creeper.explosionRadius"),
            Map.entry("darkCreeperDestroyBlocks", "dark_creeper.destroyBlocks"),
            Map.entry("earthCreeperExplosionRadius", "earth_creeper.explosionRadius"),
            Map.entry("electricCreeperExplosionRadius", "electric_creeper.explosionRadius"),
            Map.entry("fireCreeperExplosionRadius", "fire_creeper.explosionRadius"),
            Map.entry("ghostCreeperExplosionRadius", "ghost_creeper.explosionRadius"),
            Map.entry("ghostCreeperDamageMultiplier", "ghost_creeper.damageMultiplier"),
            Map.entry("ghoseCreeperSpawnChance", "ghost_creeper.spawnChanceOnDeath"),
            Map.entry("iceCreeperExplosionRadius", "ice_creeper.explosionRadius"),
            Map.entry("lightCreeperExplosionRadius", "light_creeper.explosionRadius"),
            Map.entry("magmaCreeperExplosionRadius", "magma_creeper.explosionRadius"),
            Map.entry("psychicCreeperExplosionRadius", "psychic_creeper.explosionRadius"),
            Map.entry("psychicCreeperLaunchMultiplier", "psychic_creeper.launchMultiplier"),
            Map.entry("reverseCreeperExplosionRadius", "reverse_creeper.explosionRadius"),
            Map.entry("spiderCreeperExplosionRadius", "spider_creeper.explosionRadius"),
            Map.entry("spiderCreeperPoisonTimeMedium", "spider_creeper.poisonTimeMedium"),
            Map.entry("waterCreeperExplosionRadius", "water_creeper.explosionRadius"),
            Map.entry("waterCreeperPermanentRadius", "water_creeper.permanentRadius")
    );

    private static final Map<String, ModConfigSpec.BooleanValue> SPAWN_TOGGLES = new HashMap<>();

    private static final ModConfigSpec.IntValue COOKIE_CREEPER_QUANTITY;

    private static final ModConfigSpec.DoubleValue DARK_CREEPER_EXPLOSION_RADIUS;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> DARK_CREEPER_DESTROY_BLOCKS;

    private static final ModConfigSpec.DoubleValue EARTH_CREEPER_EXPLOSION_RADIUS;

    private static final ModConfigSpec.DoubleValue ELECTRIC_CREEPER_EXPLOSION_RADIUS;

    private static final ModConfigSpec.DoubleValue FIRE_CREEPER_EXPLOSION_RADIUS;

    private static final ModConfigSpec.DoubleValue FRIENDLY_CREEPER_SPAWN_CHANCE;
    private static final ModConfigSpec.DoubleValue FRIENDLY_CREEPER_EXPLOSION_RADIUS;
    private static final ModConfigSpec.IntValue FRIENDLY_CREEPER_FUSE_TIME;
    private static final ModConfigSpec.IntValue FRIENDLY_CREEPER_COOLDOWN;
    private static final ModConfigSpec.IntValue FRIENDLY_CREEPER_HEAL_AMOUNT;

    private static final ModConfigSpec.DoubleValue GHOST_CREEPER_EXPLOSION_RADIUS;
    private static final ModConfigSpec.DoubleValue GHOST_CREEPER_DAMAGE_MULTIPLIER;
    private static final ModConfigSpec.DoubleValue GHOST_CREEPER_SPAWN_CHANCE;

    private static final ModConfigSpec.DoubleValue ICE_CREEPER_EXPLOSION_RADIUS;

    private static final ModConfigSpec.DoubleValue LIGHT_CREEPER_EXPLOSION_RADIUS;

    private static final ModConfigSpec.DoubleValue MAGMA_CREEPER_EXPLOSION_RADIUS;

    private static final ModConfigSpec.DoubleValue PSYCHIC_CREEPER_EXPLOSION_RADIUS;
    private static final ModConfigSpec.DoubleValue PSYCHIC_CREEPER_LAUNCH_MULTIPLIER;

    private static final ModConfigSpec.DoubleValue REVERSE_CREEPER_EXPLOSION_RADIUS;

    private static final ModConfigSpec.DoubleValue SPIDER_CREEPER_EXPLOSION_RADIUS;
    private static final ModConfigSpec.DoubleValue SPIDER_CREEPER_POISON_TIME_MEDIUM;

    private static final ModConfigSpec.DoubleValue WATER_CREEPER_EXPLOSION_RADIUS;
    private static final ModConfigSpec.DoubleValue WATER_CREEPER_PERMANENT_RADIUS;

    static {
        pushVariant("celebration_creeper", "Celebration Creeper");
        BUILDER.pop();

        pushVariant("cookie_creeper", "Cookie Creeper");
        COOKIE_CREEPER_QUANTITY = BUILDER.comment("Cookie Creeper quantity").defineInRange("quantity", 25, 0, Integer.MAX_VALUE);
        BUILDER.pop();

        pushVariant("dark_creeper", "Dark Creeper");
        DARK_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Dark Creeper explosion radius").defineInRange("explosionRadius", 12.d, 0.d, Double.MAX_VALUE);
        DARK_CREEPER_DESTROY_BLOCKS = BUILDER.comment("A list of blocks which dark creepers destroy").defineList("destroyBlocks", List.of("minecraft:torch", "minecraft:redstone_torch", "minecraft:redstone_lamp", "minecraft:redstone_wall_torch", "minecraft:glowstone", "minecraft:lantern", "minecraft:sea_lantern", "minecraft:shroomlight", "minecraft:campfire", "minecraft:soul_campfire"), () -> "minecraft:torch", o -> o instanceof String s && Config.validateBlockName(s));
        BUILDER.pop();

        pushVariant("earth_creeper", "Earth Creeper");
        EARTH_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Earth Creeper explosion radius").defineInRange("explosionRadius", 8.d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();

        pushVariant("electric_creeper", "Electric Creeper");
        ELECTRIC_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Electric Creeper explosion radius").defineInRange("explosionRadius", 8.d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();

        pushVariant("fire_creeper", "Fire Creeper");
        FIRE_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Fire Creeper explosion radius").defineInRange("explosionRadius", 6.d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();

        pushVariant("friendly_creeper", "Friendly Creeper");
        FRIENDLY_CREEPER_SPAWN_CHANCE = BUILDER.comment("Spawn chance multiplier when selected by the spawner").defineInRange("spawnChance", 0.5d, 0.d, 1.d);
        FRIENDLY_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Friendly Creeper explosion radius").defineInRange("explosionRadius", 3.d, 0.d, 64.d);
        FRIENDLY_CREEPER_FUSE_TIME = BUILDER.comment("Friendly Creeper fuse time in ticks").defineInRange("fuseTime", 30, 3, 1200);
        FRIENDLY_CREEPER_COOLDOWN = BUILDER.comment("Friendly Creeper cooldown after an explosion in ticks").defineInRange("cooldown", 80, 0, 12000);
        FRIENDLY_CREEPER_HEAL_AMOUNT = BUILDER.comment("Health restored to a tamed Friendly Creeper when fed gunpowder").defineInRange("healAmount", 10, 0, 1024);
        BUILDER.pop();

        pushVariant("ghost_creeper", "Ghost Creeper");
        GHOST_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Ghost Creeper explosion radius").defineInRange("explosionRadius", 2.5d, 0.d, Double.MAX_VALUE);
        GHOST_CREEPER_DAMAGE_MULTIPLIER = BUILDER.comment("Ghost Creeper damage multiplier (compared to regular creeper)").defineInRange("damageMultiplier", 0.7d, 0.d, Double.MAX_VALUE);
        GHOST_CREEPER_SPAWN_CHANCE = BUILDER.comment("Ghost Creeper spawn chance when an elemental creeper dies").defineInRange("spawnChanceOnDeath", 0.01, 0, 1);
        BUILDER.pop();

        pushVariant("ice_creeper", "Ice Creeper");
        ICE_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Ice Creeper explosion radius").defineInRange("explosionRadius", 10.d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();

        pushVariant("illusion_creeper", "Illusion Creeper");
        BUILDER.pop();

        pushVariant("light_creeper", "Light Creeper");
        LIGHT_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Light Creeper explosion radius").defineInRange("explosionRadius", 4.d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();

        pushVariant("magma_creeper", "Magma Creeper");
        MAGMA_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Magma Creeper explosion radius").defineInRange("explosionRadius", 2d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();

        pushVariant("psychic_creeper", "Psychic Creeper");
        PSYCHIC_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Psychic Creeper explosion radius").defineInRange("explosionRadius", 3d, 0.d, Double.MAX_VALUE);
        PSYCHIC_CREEPER_LAUNCH_MULTIPLIER = BUILDER.comment("Psychic Creeper launch multiplier (compared to regular creeper)").defineInRange("launchMultiplier", 7d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();

        pushVariant("reverse_creeper", "Reverse Creeper");
        REVERSE_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Reverse Creeper explosion radius").defineInRange("explosionRadius", 6d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();

        pushVariant("spider_creeper", "Spider Creeper");
        SPIDER_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Spider Creeper explosion radius").defineInRange("explosionRadius", 7d, 0.d, Double.MAX_VALUE);
        SPIDER_CREEPER_POISON_TIME_MEDIUM = BUILDER.comment("Spider Creeper poison time on medium difficulty (easy = 0.66x, hard = 1.5x)").defineInRange("poisonTimeMedium", 8d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();

        pushVariant("water_creeper", "Water Creeper");
        WATER_CREEPER_EXPLOSION_RADIUS = BUILDER.comment("Water Creeper explosion radius").defineInRange("explosionRadius", 5.d, 0.d, Double.MAX_VALUE);
        WATER_CREEPER_PERMANENT_RADIUS = BUILDER.comment("Water Creeper permanent puddle radius").defineInRange("permanentRadius", 1.d, 0.d, Double.MAX_VALUE);
        BUILDER.pop();
    }

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int cookieCreeperQuantity;
    public static double darkCreeperExplosionRadius;
    public static double earthCreeperExplosionRadius;
    public static double electricCreeperExplosionRadius;
    public static double fireCreeperExplosionRadius;
    public static double iceCreeperExplosionRadius;
    public static double friendlyCreeperExplosionRadius;
    public static double ghostCreeperExplosionRadius;
    public static double lightCreeperExplosionRadius;
    public static double magmaCreeperExplosionRadius;
    public static double psychicCreeperExplosionRadius;
    public static double reverseCreeperExplosionRadius;
    public static double spiderCreeperExplosionRadius;
    public static double waterCreeperExplosionRadius;

    public static Set<Block> darkCreeperDestroyBlocks;
    public static double friendlyCreeperSpawnChance;
    public static int friendlyCreeperFuseTime;
    public static int friendlyCreeperCooldown;
    public static int friendlyCreeperHealAmount;
    public static double ghostCreeperDamageMultiplier;
    public static double psychicCreeperLaunchMultiplier;
    public static double spiderCreeperPoisonTimeMedium;
    public static double waterCreeperPermanentRadius;

    public static double ghostCreeperSpawnChance;

    private static Map<String, Boolean> spawningEnabled = Map.of();

    private static void pushVariant(String id, String displayName) {
        BUILDER.push(id);
        SPAWN_TOGGLES.put(id, BUILDER.comment("Whether " + displayName + "s can spawn naturally").define("enableSpawning", true));
    }

    public static boolean isSpawningEnabled(EntityType<?> entityType) {
        ResourceLocation key = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        if (!ElementalCreepersContinued.MOD_ID.equals(key.getNamespace())) {
            return true;
        }

        return spawningEnabled.getOrDefault(key.getPath(), true);
    }

    /**
     * Moves pre-1.1 flat keys into their variant sections. Must run before NeoForge loads the COMMON config,
     * since spec correction drops unknown keys and would silently reset customized values to defaults.
     */
    static void migrateLegacyKeys(Path configFile, Path defaultConfigFile) {
        try {
            if (!Files.isRegularFile(configFile)) {
                if (!Files.isRegularFile(defaultConfigFile)) {
                    return;
                }

                // NeoForge copies the defaultconfigs template only after this runs, so copy it here to migrate it first.
                Files.createDirectories(configFile.getParent());
                Files.copy(defaultConfigFile, configFile);
            }

            try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).sync().preserveInsertionOrder().writingMode(WritingMode.REPLACE_ATOMIC).build()) {
                config.load();
                boolean migrated = false;
                for (Map.Entry<String, String> legacyKey : LEGACY_KEYS.entrySet()) {
                    if (!config.contains(legacyKey.getKey())) {
                        continue;
                    }

                    Object value = config.remove(legacyKey.getKey());
                    if (!config.contains(legacyKey.getValue())) {
                        config.set(legacyKey.getValue(), value);
                    }
                    migrated = true;
                }

                if (migrated) {
                    Files.copy(configFile, configFile.resolveSibling(configFile.getFileName() + ".pre-migration.bak"), StandardCopyOption.REPLACE_EXISTING);
                    config.save();
                    LOGGER.info("Migrated legacy config keys in {}", configFile);
                }
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to migrate legacy config keys in {}", configFile, e);
        }
    }

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
        friendlyCreeperExplosionRadius = FRIENDLY_CREEPER_EXPLOSION_RADIUS.get();
        ghostCreeperExplosionRadius = GHOST_CREEPER_EXPLOSION_RADIUS.get();
        lightCreeperExplosionRadius = LIGHT_CREEPER_EXPLOSION_RADIUS.get();
        magmaCreeperExplosionRadius = MAGMA_CREEPER_EXPLOSION_RADIUS.get();
        psychicCreeperExplosionRadius = PSYCHIC_CREEPER_EXPLOSION_RADIUS.get();
        reverseCreeperExplosionRadius = REVERSE_CREEPER_EXPLOSION_RADIUS.get();
        spiderCreeperExplosionRadius = SPIDER_CREEPER_EXPLOSION_RADIUS.get();
        waterCreeperExplosionRadius = WATER_CREEPER_EXPLOSION_RADIUS.get();

        darkCreeperDestroyBlocks = DARK_CREEPER_DESTROY_BLOCKS.get().stream().map(blockName -> BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockName))).collect(Collectors.toSet());
        friendlyCreeperSpawnChance = FRIENDLY_CREEPER_SPAWN_CHANCE.get();
        friendlyCreeperFuseTime = FRIENDLY_CREEPER_FUSE_TIME.get();
        friendlyCreeperCooldown = FRIENDLY_CREEPER_COOLDOWN.get();
        friendlyCreeperHealAmount = FRIENDLY_CREEPER_HEAL_AMOUNT.get();
        ghostCreeperDamageMultiplier = GHOST_CREEPER_DAMAGE_MULTIPLIER.get();
        psychicCreeperLaunchMultiplier = PSYCHIC_CREEPER_LAUNCH_MULTIPLIER.get();
        spiderCreeperPoisonTimeMedium = SPIDER_CREEPER_POISON_TIME_MEDIUM.get();
        waterCreeperPermanentRadius = WATER_CREEPER_PERMANENT_RADIUS.get();

        ghostCreeperSpawnChance = GHOST_CREEPER_SPAWN_CHANCE.get();

        spawningEnabled = SPAWN_TOGGLES.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, entry -> entry.getValue().get()));
    }
}
