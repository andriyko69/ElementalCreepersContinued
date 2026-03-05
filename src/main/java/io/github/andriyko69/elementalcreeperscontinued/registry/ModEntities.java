package io.github.andriyko69.elementalcreeperscontinued.registry;

import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import io.github.andriyko69.elementalcreeperscontinued.entity.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ElementalCreepersContinued.MOD_ID);

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(ElementalCreepersContinued.MOD_ID);

    public static final List<DeferredItem<? extends Item>> EGG_ITEMS = new ArrayList<>();
    public static final List<Supplier<? extends EntityType<? extends ElementalCreeper>>> CREEPERS = new ArrayList<>();

    public static final DeferredHolder<EntityType<?>, EntityType<CelebrationCreeper>> CELEBRATION_CREEPER =
            registerCreeper(CelebrationCreeper::new, "celebration_creeper", new Color(98, 182, 24));

    public static final DeferredHolder<EntityType<?>, EntityType<CookieCreeper>> COOKIE_CREEPER =
            registerCreeper(CookieCreeper::new, "cookie_creeper", new Color(202, 147, 98));

    public static final DeferredHolder<EntityType<?>, EntityType<DarkCreeper>> DARK_CREEPER =
            registerCreeper(DarkCreeper::new, "dark_creeper", new Color(50, 50, 50));

    public static final DeferredHolder<EntityType<?>, EntityType<EarthCreeper>> EARTH_CREEPER =
            registerCreeper(EarthCreeper::new, "earth_creeper", new Color(93, 50, 0));

    public static final DeferredHolder<EntityType<?>, EntityType<ElectricCreeper>> ELECTRIC_CREEPER =
            registerCreeper(ElectricCreeper::new, "electric_creeper", new Color(251, 234, 57));

    public static final DeferredHolder<EntityType<?>, EntityType<FakeIllusionCreeper>> FAKE_ILLUSION_CREEPER =
            registerCreeper(FakeIllusionCreeper::new, "fake_illusion_creeper", null);

    public static final DeferredHolder<EntityType<?>, EntityType<FireCreeper>> FIRE_CREEPER =
            registerCreeper(FireCreeper::new, "fire_creeper", new Color(227, 111, 24));

    public static final DeferredHolder<EntityType<?>, EntityType<IceCreeper>> ICE_CREEPER =
            registerCreeper(IceCreeper::new, "ice_creeper", Color.WHITE);

    public static final DeferredHolder<EntityType<?>, EntityType<IllusionCreeper>> ILLUSION_CREEPER =
            registerCreeper(IllusionCreeper::new, "illusion_creeper", new Color(158, 158, 158));

    public static final DeferredHolder<EntityType<?>, EntityType<GhostCreeper>> GHOST_CREEPER =
            registerCreeper(GhostCreeper::new, "ghost_creeper", null, null);

    public static final DeferredHolder<EntityType<?>, EntityType<LightCreeper>> LIGHT_CREEPER =
            registerCreeper(LightCreeper::new, "light_creeper", new Color(255, 244, 125));

    public static final DeferredHolder<EntityType<?>, EntityType<MagmaCreeper>> MAGMA_CREEPER =
            registerCreeper(MagmaCreeper::new, "magma_creeper", new Color(165, 0, 16));

    public static final DeferredHolder<EntityType<?>, EntityType<PsychicCreeper>> PSYCHIC_CREEPER =
            registerCreeper(PsychicCreeper::new, "psychic_creeper", new Color(121, 51, 142));

    public static final DeferredHolder<EntityType<?>, EntityType<ReverseCreeper>> REVERSE_CREEPER =
            registerCreeper(ReverseCreeper::new, "reverse_creeper", new Color(894731), Color.BLACK);

    public static final DeferredHolder<EntityType<?>, EntityType<SpiderCreeper>> SPIDER_CREEPER =
            registerCreeper(SpiderCreeper::new, "spider_creeper", Color.RED);

    public static final DeferredHolder<EntityType<?>, EntityType<WaterCreeper>> WATER_CREEPER =
            registerCreeper(WaterCreeper::new, "water_creeper", new Color(59, 115, 205));

    public static final DeferredHolder<EntityType<?>, EntityType<FriendlyCreeper>> FRIENDLY_CREEPER = ENTITIES.register(
            "friendly_creeper",
            () -> EntityType.Builder.of(FriendlyCreeper::new, net.minecraft.world.entity.MobCategory.CREATURE)
                    .sized(0.6f, 1.7f).clientTrackingRange(10)
                    .build("friendly_creeper")
    );
    public static final DeferredItem<DeferredSpawnEggItem> FRIENDLY_CREEPER_SPAWN_EGG = ITEMS.registerItem(
            "friendly_creeper_spawn_egg",
            props -> new DeferredSpawnEggItem(
                    FRIENDLY_CREEPER,
                    new Color(0, 255, 0).getRGB() & 0xFFFFFF,
                    new Color(0, 128, 0).getRGB() & 0xFFFFFF,
                    props
            )
    );

    public static <T extends ElementalCreeper> DeferredHolder<EntityType<?>, EntityType<T>> registerCreeper(
            EntityFactory<T> creeperFactory,
            String name,
            @Nullable Color colour
    ) {
        return registerCreeper(creeperFactory, name, colour, Color.BLACK);
    }

    public static <T extends ElementalCreeper> DeferredHolder<EntityType<?>, EntityType<T>> registerCreeper(
            EntityFactory<T> creeperFactory,
            String name,
            @Nullable Color colour,
            @Nullable Color colour2
    ) {
        DeferredHolder<EntityType<?>, EntityType<T>> entity = ENTITIES.register(
                name,
                () -> EntityType.Builder.of(creeperFactory, MobCategory.MONSTER)
                        .sized(0.6F, 1.7F)
                        .clientTrackingRange(8)
                        .build(name)
        );
        CREEPERS.add(entity);

        if (colour != null && colour2 != null) {
            DeferredItem<DeferredSpawnEggItem> egg = ITEMS.registerItem(
                    name + "_spawn_egg",
                    props -> new DeferredSpawnEggItem(
                            entity,
                            colour.getRGB() & 0xFFFFFF,
                            colour2.getRGB() & 0xFFFFFF,
                            props
                    )
            );
            EGG_ITEMS.add(egg);
        }
        return entity;
    }

    public static void register(IEventBus modEventBus) {
        EGG_ITEMS.add(FRIENDLY_CREEPER_SPAWN_EGG);
        ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
    }
}