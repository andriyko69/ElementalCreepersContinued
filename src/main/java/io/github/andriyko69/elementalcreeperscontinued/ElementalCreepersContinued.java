package io.github.andriyko69.elementalcreeperscontinued;

import io.github.andriyko69.elementalcreeperscontinued.entity.ElementalCreeper;
import io.github.andriyko69.elementalcreeperscontinued.registry.ModAttributes;
import io.github.andriyko69.elementalcreeperscontinued.registry.ModCreativeTabs;
import io.github.andriyko69.elementalcreeperscontinued.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import java.util.function.Supplier;

@Mod(ElementalCreepersContinued.MOD_ID)
public class ElementalCreepersContinued {

    public static final String MOD_ID = "elementalcreeperscontinued";

    public ElementalCreepersContinued(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModEntities.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        modEventBus.addListener(ModAttributes::onEntityAttributes);
        modEventBus.addListener(this::registerSpawnPlacements);
    }

    private void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        for (Supplier<? extends EntityType<? extends ElementalCreeper>> holder : ModEntities.CREEPERS) {
            event.register(
                    holder.get(),
                    SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Monster::checkMonsterSpawnRules,
                    RegisterSpawnPlacementsEvent.Operation.REPLACE
            );
        }
    }
}
