package io.github.andriyko69.elementalcreeperscontinued.client.renderer;

import io.github.andriyko69.elementalcreeperscontinued.client.model.SpiderCreeperModel;
import io.github.andriyko69.elementalcreeperscontinued.entity.SpiderCreeper;
import io.github.andriyko69.elementalcreeperscontinued.registry.client.ElementalCreepersClientRegistry;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SpiderCreeperPowerLayer extends EnergySwirlLayer<SpiderCreeper, SpiderCreeperModel> {
    private static final ResourceLocation POWER_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");

    private final SpiderCreeperModel model;

    public SpiderCreeperPowerLayer(RenderLayerParent<SpiderCreeper, SpiderCreeperModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new SpiderCreeperModel(
                modelSet.bakeLayer(ElementalCreepersClientRegistry.SPIDER_CREEPER_MODEL_LAYER_LOCATION)
        );
    }

    @Override
    protected float xOffset(float partialTicks) {
        return partialTicks * 0.01F;
    }

    @Override
    protected @NotNull ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected @NotNull SpiderCreeperModel model() {
        return this.model;
    }
}