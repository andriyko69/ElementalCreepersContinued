package io.github.andriyko69.elementalcreeperscontinued.registry.client;

import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import io.github.andriyko69.elementalcreeperscontinued.client.model.CelebrationCreeperModel;
import io.github.andriyko69.elementalcreeperscontinued.client.model.SpiderCreeperModel;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.CelebrationCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.CookieCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.DarkCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.EarthCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.ElectricCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.FireCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.FriendlyCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.GhostCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.IceCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.IllusionCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.LightCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.MagmaCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.PsychicCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.ReverseCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.SpiderCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.client.renderer.WaterCreeperRenderer;
import io.github.andriyko69.elementalcreeperscontinued.registry.ModEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(
        modid = ElementalCreepersContinued.MOD_ID,
        value = Dist.CLIENT
)
public final class ElementalCreepersClientRegistry {
    public static final ModelLayerLocation SPIDER_CREEPER_MODEL_LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(
                            ElementalCreepersContinued.MOD_ID,
                            "spider_creeper"
                    ),
                    "main"
            );

    public static final ModelLayerLocation CELEBRATION_CREEPER_MODEL_LAYER_LOCATION =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(
                            ElementalCreepersContinued.MOD_ID,
                            "celebration_creeper"
                    ),
                    "main"
            );

    private ElementalCreepersClientRegistry() {
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CELEBRATION_CREEPER.get(), CelebrationCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.COOKIE_CREEPER.get(), CookieCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.DARK_CREEPER.get(), DarkCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.EARTH_CREEPER.get(), EarthCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.ELECTRIC_CREEPER.get(), ElectricCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.FAKE_ILLUSION_CREEPER.get(), IllusionCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.FIRE_CREEPER.get(), FireCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.ICE_CREEPER.get(), IceCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.ILLUSION_CREEPER.get(), IllusionCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.GHOST_CREEPER.get(), GhostCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.LIGHT_CREEPER.get(), LightCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.MAGMA_CREEPER.get(), MagmaCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.PSYCHIC_CREEPER.get(), PsychicCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.REVERSE_CREEPER.get(), ReverseCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.SPIDER_CREEPER.get(), SpiderCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.WATER_CREEPER.get(), WaterCreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.FRIENDLY_CREEPER.get(), FriendlyCreeperRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SPIDER_CREEPER_MODEL_LAYER_LOCATION, SpiderCreeperModel::createBodyLayer);
        event.registerLayerDefinition(CELEBRATION_CREEPER_MODEL_LAYER_LOCATION, CelebrationCreeperModel::createBodyLayer);
    }
}