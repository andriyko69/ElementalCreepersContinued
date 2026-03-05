package io.github.andriyko69.elementalcreeperscontinued.client.model;

import io.github.andriyko69.elementalcreeperscontinued.entity.FriendlyCreeper;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.TamableAnimal;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FriendlyCreeperModel<F extends TamableAnimal> extends HierarchicalModel<FriendlyCreeper> {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;

    public FriendlyCreeperModel(ModelPart root) {
        this.root = root;
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
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

        return LayerDefinition.create(meshDefinition, 64, 32);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }

    @Override
    public void setupAnim(FriendlyCreeper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float ageScale = entity.isBaby() ? 0.5F : 1.0F;

        if (entity.isInSittingPose()) {
            this.head.setPos(0.0F, 12.0F * ageScale, 0.0F);
            this.body.setPos(0.0F, 12.0F, 0.0F);
            this.leftHindLeg.setPos(2.0F, 22.0F, 2.0F);
            this.rightHindLeg.setPos(-2.0F, 22.0F, 2.0F);
            this.leftFrontLeg.setPos(2.0F, 22.0F, -2.0F);
            this.rightFrontLeg.setPos(-2.0F, 22.0F, -2.0F);

            this.leftHindLeg.xRot = (float) Math.PI / 2.0F;
            this.rightHindLeg.xRot = (float) Math.PI / 2.0F;
            this.leftFrontLeg.xRot = 3.0F * (float) Math.PI / 2.0F;
            this.rightFrontLeg.xRot = 3.0F * (float) Math.PI / 2.0F;

            this.leftHindLeg.yRot = (float) Math.PI / 18.0F;
            this.rightHindLeg.yRot = -(float) Math.PI / 18.0F;
            this.leftFrontLeg.yRot = -(float) Math.PI / 18.0F;
            this.rightFrontLeg.yRot = (float) Math.PI / 18.0F;
        } else {
            this.head.setPos(0.0F, 6.0F * ageScale, 0.0F);
            this.body.setPos(0.0F, 6.0F, 0.0F);
            this.leftHindLeg.setPos(2.0F, 18.0F, 4.0F);
            this.rightHindLeg.setPos(-2.0F, 18.0F, 4.0F);
            this.leftFrontLeg.setPos(2.0F, 18.0F, -4.0F);
            this.rightFrontLeg.setPos(-2.0F, 18.0F, -4.0F);

            this.leftHindLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.rightHindLeg.xRot = Mth.cos(limbSwing * 0.6662F + 2.0F * (float) Math.PI / 3.0F) * 1.4F * limbSwingAmount;
            this.leftFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F + 4.0F * (float) Math.PI / 3.0F) * 1.4F * limbSwingAmount;
            this.rightFrontLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

            this.leftHindLeg.yRot = 0.0F;
            this.rightHindLeg.yRot = 0.0F;
            this.leftFrontLeg.yRot = 0.0F;
            this.rightFrontLeg.yRot = 0.0F;
        }

        this.head.xRot = headPitch * ((float) Math.PI / 180.0F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180.0F);
    }
}