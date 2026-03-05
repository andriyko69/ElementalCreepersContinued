package io.github.andriyko69.elementalcreeperscontinued.client.renderer;

import io.github.andriyko69.elementalcreeperscontinued.ElementalCreepersContinued;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LightCreeperRenderer extends CreeperRenderer {
    private static final ResourceLocation CREEPER_LOCATION = ResourceLocation.fromNamespaceAndPath(ElementalCreepersContinued.MOD_ID, "textures/entity/light_creeper.png");

    public LightCreeperRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Creeper entity) {
        return CREEPER_LOCATION;
    }

    @Override
    protected RenderType getRenderType(@NotNull Creeper creeper, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucentEmissive(
                getTextureLocation(creeper)
        );
    }
}
