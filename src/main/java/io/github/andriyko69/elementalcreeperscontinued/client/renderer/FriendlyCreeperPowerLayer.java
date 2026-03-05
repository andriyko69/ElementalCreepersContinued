package io.github.andriyko69.elementalcreeperscontinued.client.renderer;

import io.github.andriyko69.elementalcreeperscontinued.client.model.FriendlyCreeperModel;
import io.github.andriyko69.elementalcreeperscontinued.entity.FriendlyCreeper;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EnergySwirlLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.TamableAnimal;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FriendlyCreeperPowerLayer extends EnergySwirlLayer<FriendlyCreeper, FriendlyCreeperModel<FriendlyCreeper>> {
    private static final ResourceLocation POWER_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");
    private final FriendlyCreeperModel<TamableAnimal> model;

    public FriendlyCreeperPowerLayer(RenderLayerParent<FriendlyCreeper, FriendlyCreeperModel<FriendlyCreeper>> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new FriendlyCreeperModel<>(modelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
    }

    @Override
    protected float xOffset(float partialTicks) {
        return partialTicks * 0.01F;
    }

    @Override
    protected @NotNull ResourceLocation getTextureLocation() {
        return POWER_LOCATION;
    }

    protected @NotNull EntityModel<FriendlyCreeper> model() {
        return this.model;
    }
}
