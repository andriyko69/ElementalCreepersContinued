package io.github.andriyko69.elementalcreeperscontinued.event;

import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import io.github.andriyko69.elementalcreeperscontinued.entity.SpiderCreeper;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = ElementalCreepersContinued.MOD_ID)
public final class ModMobEffectEvents {

    private ModMobEffectEvents() {
    }

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof SpiderCreeper && event.getEffectInstance().is(MobEffects.POISON)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}