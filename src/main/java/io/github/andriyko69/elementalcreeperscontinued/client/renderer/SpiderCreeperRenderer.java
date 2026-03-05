package io.github.andriyko69.elementalcreeperscontinued.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import io.github.andriyko69.elementalcreeperscontinued.client.model.SpiderCreeperModel;
import io.github.andriyko69.elementalcreeperscontinued.entity.SpiderCreeper;
import io.github.andriyko69.elementalcreeperscontinued.registry.client.ElementalCreepersClientRegistry;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SpiderCreeperRenderer extends MobRenderer<SpiderCreeper, SpiderCreeperModel> {
    private static final ResourceLocation SPIDER_CREEPER_LOCATION =
            ResourceLocation.fromNamespaceAndPath(
                    ElementalCreepersContinued.MOD_ID,
                    "textures/entity/spider_creeper.png"
            );

    public SpiderCreeperRenderer(EntityRendererProvider.Context context) {
        super(
                context,
                new SpiderCreeperModel(
                        context.bakeLayer(ElementalCreepersClientRegistry.SPIDER_CREEPER_MODEL_LAYER_LOCATION)
                ),
                0.5F
        );
        this.addLayer(new SpiderCreeperPowerLayer(this, context.getModelSet()));
    }

    @Override
    protected void scale(SpiderCreeper creeper, PoseStack poseStack, float partialTickTime) {
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
    protected float getWhiteOverlayProgress(SpiderCreeper creeper, float partialTicks) {
        float f = creeper.getSwelling(partialTicks);
        return (int) (f * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(f, 0.5F, 1.0F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SpiderCreeper creeper) {
        return SPIDER_CREEPER_LOCATION;
    }
}