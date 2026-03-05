package io.github.andriyko69.elementalcreeperscontinued.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import io.github.andriyko69.elementalcreeperscontinued.client.model.CelebrationCreeperModel;
import io.github.andriyko69.elementalcreeperscontinued.entity.CelebrationCreeper;
import io.github.andriyko69.elementalcreeperscontinued.registry.client.ElementalCreepersClientRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;


public class CelebrationCreeperRenderer extends MobRenderer<CelebrationCreeper, CelebrationCreeperModel<CelebrationCreeper>> {
    private static final ResourceLocation CREEPER_LOCATION = ResourceLocation.fromNamespaceAndPath(ElementalCreepersContinued.MOD_ID, "textures/entity/celebration_creeper.png");

    public CelebrationCreeperRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new CelebrationCreeperModel<>(
                        context.bakeLayer(ElementalCreepersClientRegistry.CELEBRATION_CREEPER_MODEL_LAYER_LOCATION)
                ),
                0.5F
        );

        this.addLayer(new CelebrationCreeperPowerLayer(this, context.getModelSet()));
    }

    @Override
    protected void scale(CelebrationCreeper creeper, PoseStack poseStack, float partialTickTime) {
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
    protected float getWhiteOverlayProgress(CelebrationCreeper creeper, float partialTickTime) {
        float f = creeper.getSwelling(partialTickTime);
        return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CelebrationCreeper creeper) {
        return CREEPER_LOCATION;
    }

}
