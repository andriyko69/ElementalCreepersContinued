package io.github.andriyko69.elementalcreeperscontinued.client.model;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CelebrationCreeperModel<T extends Entity> extends CreeperModel<T> {

    public CelebrationCreeperModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        CubeDeformation deformation = CubeDeformation.NONE;
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation)
                        .texOffs(24, 0).addBox(-1.0F, -14.0F, -1.0F, 2.0F, 6.0F, 2.0F, deformation)
                        .texOffs(32, 0).addBox(-0.5F, -15.0F, -0.5F, 1.0F, 1.0F, 1.0F, deformation),
                PartPose.offset(0.0F, 6.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(0.0F, 6.0F, 0.0F)
        );

        CubeListBuilder legBuilder = CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation);

        root.addOrReplaceChild("right_hind_leg", legBuilder, PartPose.offset(-2.0F, 18.0F, 4.0F));
        root.addOrReplaceChild("left_hind_leg", legBuilder, PartPose.offset(2.0F, 18.0F, 4.0F));
        root.addOrReplaceChild("right_front_leg", legBuilder, PartPose.offset(-2.0F, 18.0F, -4.0F));
        root.addOrReplaceChild("left_front_leg", legBuilder, PartPose.offset(2.0F, 18.0F, -4.0F));

        return LayerDefinition.create(meshDefinition, 64, 32);
    }
}