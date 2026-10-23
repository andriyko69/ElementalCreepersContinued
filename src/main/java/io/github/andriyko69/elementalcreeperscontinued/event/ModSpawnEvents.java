package io.github.andriyko69.elementalcreeperscontinued.event;

import io.github.andriyko69.elementalcreeperscontinued.Config;
import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import io.github.andriyko69.elementalcreeperscontinued.registry.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

@EventBusSubscriber(modid = ElementalCreepersContinued.MOD_ID)
public final class ModSpawnEvents {

    private ModSpawnEvents() {
    }

    @SubscribeEvent
    public static void onSpawnPlacementCheck(MobSpawnEvent.SpawnPlacementCheck event) {
        MobSpawnType spawnType = event.getSpawnType();
        if (spawnType != MobSpawnType.NATURAL && spawnType != MobSpawnType.CHUNK_GENERATION) {
            return;
        }

        EntityType<?> entityType = event.getEntityType();
        if (!Config.isSpawningEnabled(entityType)) {
            event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
            return;
        }

        if (entityType == ModEntities.FRIENDLY_CREEPER.get() && event.getRandom().nextDouble() >= Config.friendlyCreeperSpawnChance) {
            event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
        }
    }
}
