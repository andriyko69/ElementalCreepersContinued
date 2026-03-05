package io.github.andriyko69.elementalcreeperscontinued.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import io.github.andriyko69.elementalcreeperscontinued.client.model.FriendlyCreeperModel;
import io.github.andriyko69.elementalcreeperscontinued.entity.CelebrationCreeper;
import io.github.andriyko69.elementalcreeperscontinued.entity.FriendlyCreeper;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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
    protected void scale(FriendlyCreeper creeper, PoseStack poseStack, float partialTickTime) {
        float f = creeper.getSwelling(partialTickTime);
        float f1 = 1.0F + Mth.sin(f * 100.0F) * f * 0.01F;
        f = Mth.clamp(f, 0.0F, 1.0F);
        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        poseStack.scale(f2, f3, f2);
    }

    @Override
    protected float getWhiteOverlayProgress(FriendlyCreeper creeper, float partialTickTime) {
        float f = creeper.getSwelling(partialTickTime);
        return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull FriendlyCreeper creeper) {
        return creeper.isTame() ? TAME_TEXTURE : NORMAL_TEXTURE;
    }

}
