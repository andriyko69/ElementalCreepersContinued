package io.github.andriyko69.elementalcreeperscontinued.client.renderer;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class GhostCreeperRenderer extends CreeperRenderer {

    public GhostCreeperRenderer(Context context) {
        super(context);
    }

    @Override
    protected RenderType getRenderType(@NotNull Creeper creeper, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucentEmissive(
                ResourceLocation.withDefaultNamespace("textures/entity/creeper/creeper.png")
        );
    }
}
