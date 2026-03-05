package io.github.andriyko69.elementalcreeperscontinued.client.model;

import io.github.andriyko69.elementalcreeperscontinued.entity.SpiderCreeper;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SpiderCreeperModel extends HierarchicalModel<SpiderCreeper> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightSideLeg;
    private final ModelPart leftSideLeg;

    public SpiderCreeperModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");

        this.rightSideLeg = root.hasChild("right_side_leg") ? root.getChild("right_side_leg") : null;
        this.leftSideLeg = root.hasChild("left_side_leg") ? root.getChild("left_side_leg") : null;
    }

    public static LayerDefinition createBodyLayer() {
        CubeDeformation deformation = CubeDeformation.NONE;
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        root.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation),
                PartPose.offset(0.0F, 6.0F, 0.0F)
        );

        root.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, deformation),
                PartPose.offset(0.0F, 6.0F, 0.0F)
        );

        CubeListBuilder legBuilder = CubeListBuilder.create()
                .texOffs(0, 16)
                .addBox(-2.0F, 0.0F, -2.0F, 4.0F, 6.0F, 4.0F, deformation);

        root.addOrReplaceChild("right_hind_leg", legBuilder, PartPose.offset(-2.0F, 18.0F, 4.0F));
        root.addOrReplaceChild("left_hind_leg", legBuilder, PartPose.offset(2.0F, 18.0F, 4.0F));
        root.addOrReplaceChild("right_front_leg", legBuilder, PartPose.offset(-2.0F, 18.0F, -4.0F));
        root.addOrReplaceChild("left_front_leg", legBuilder, PartPose.offset(2.0F, 18.0F, -4.0F));
        root.addOrReplaceChild("right_side_leg", legBuilder, PartPose.offset(-6.0F, 18.0F, 0.0F));
        root.addOrReplaceChild("left_side_leg", legBuilder, PartPose.offset(6.0F, 18.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public void setupAnim(@NotNull SpiderCreeper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);

        this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + 2F * (float) Math.PI / 3.0F) * 1.4F * limbSwingAmount;
        this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + 4F * (float) Math.PI / 3.0F) * 1.4F * limbSwingAmount;
        this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

        if (this.leftSideLeg != null && this.rightSideLeg != null) {
            this.leftSideLeg.xRot = Mth.cos(limbSwing * 0.6662F + 2F * (float) Math.PI / 3.0F) * 1.4F * limbSwingAmount;
            this.rightSideLeg.xRot = Mth.cos(limbSwing * 0.6662F + 4F * (float) Math.PI / 3.0F) * 1.4F * limbSwingAmount;
        }
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}