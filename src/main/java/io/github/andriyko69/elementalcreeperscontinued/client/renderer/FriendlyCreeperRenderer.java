package io.github.andriyko69.elementalcreeperscontinued.client.renderer;

import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import io.github.andriyko69.elementalcreeperscontinued.client.model.FriendlyCreeperModel;
import io.github.andriyko69.elementalcreeperscontinued.entity.FriendlyCreeper;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class FriendlyCreeperRenderer extends MobRenderer<FriendlyCreeper, FriendlyCreeperModel<FriendlyCreeper>> {
    private static final ResourceLocation NORMAL_TEXTURE = ResourceLocation.fromNamespaceAndPath(ElementalCreepersContinued.MOD_ID,
            "textures/entity/friendly_creeper_0.png");
    private static final ResourceLocation TAME_TEXTURE = ResourceLocation.fromNamespaceAndPath(ElementalCreepersContinued.MOD_ID,
            "textures/entity/friendly_creeper_1.png");

    public FriendlyCreeperRenderer(EntityRendererProvider.Context p_173958_) {
        super(p_173958_, new FriendlyCreeperModel<>(p_173958_.bakeLayer(ModelLayers.CREEPER)), 0.5F);
        this.addLayer(new FriendlyCreeperPowerLayer(this, p_173958_.getModelSet()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FriendlyCreeper creeper) {
        return creeper.isTame() ? TAME_TEXTURE : NORMAL_TEXTURE;
    }

}
