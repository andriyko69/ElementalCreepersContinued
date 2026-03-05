package io.github.andriyko69.elementalcreeperscontinued.registry;

import io.github.andriyko69.elementalcreeperscontinued.entity.*;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import static io.github.andriyko69.elementalcreeperscontinued.registry.ModEntities.*;

public class ModAttributes {
    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(CELEBRATION_CREEPER.get(), CelebrationCreeper.createAttributes().build());
        event.put(COOKIE_CREEPER.get(), CookieCreeper.createAttributes().build());
        event.put(DARK_CREEPER.get(), DarkCreeper.createAttributes().build());
        event.put(EARTH_CREEPER.get(), EarthCreeper.createAttributes().build());
        event.put(ELECTRIC_CREEPER.get(), ElectricCreeper.createAttributes().build());
        event.put(FAKE_ILLUSION_CREEPER.get(), FakeIllusionCreeper.createAttributes().build());
        event.put(FIRE_CREEPER.get(), FireCreeper.createAttributes().build());
        event.put(ICE_CREEPER.get(), IceCreeper.createAttributes().build());
        event.put(ILLUSION_CREEPER.get(), IllusionCreeper.createAttributes().build());
        event.put(GHOST_CREEPER.get(), GhostCreeper.createAttributes().build());
        event.put(LIGHT_CREEPER.get(), LightCreeper.createAttributes().build());
        event.put(MAGMA_CREEPER.get(), MagmaCreeper.createAttributes().build());
        event.put(PSYCHIC_CREEPER.get(), PsychicCreeper.createAttributes().build());
        event.put(REVERSE_CREEPER.get(), ReverseCreeper.createAttributes().build());
        event.put(SPIDER_CREEPER.get(), SpiderCreeper.createAttributes().build());
        event.put(WATER_CREEPER.get(), WaterCreeper.createAttributes().build());

        event.put(FRIENDLY_CREEPER.get(), FriendlyCreeper.createAttributes().build());
    }
}
