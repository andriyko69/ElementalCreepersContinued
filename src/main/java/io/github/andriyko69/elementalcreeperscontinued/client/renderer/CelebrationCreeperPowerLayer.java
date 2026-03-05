package io.github.andriyko69.elementalcreeperscontinued.client.renderer;

import io.github.andriyko69.elementalcreeperscontinued.client.model.CelebrationCreeperModel;
import io.github.andriyko69.elementalcreeperscontinued.entity.CelebrationCreeper;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class CelebrationCreeperPowerLayer
        extends EnergySwirlLayer<CelebrationCreeper, CelebrationCreeperModel<CelebrationCreeper>> {

    private static final ResourceLocation POWER_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");

    private final CelebrationCreeperModel<CelebrationCreeper> model;

    public CelebrationCreeperPowerLayer(
            RenderLayerParent<CelebrationCreeper, CelebrationCreeperModel<CelebrationCreeper>> renderer,
            EntityModelSet modelSet
    ) {
        super(renderer);
        this.model = new CelebrationCreeperModel<>(modelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
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
    protected @NotNull CelebrationCreeperModel<CelebrationCreeper> model() {
        return this.model;
    }
}