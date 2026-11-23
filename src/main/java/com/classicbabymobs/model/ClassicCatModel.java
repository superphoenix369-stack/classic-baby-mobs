package com.classicbabymobs.model;

import net.minecraft.client.model.animal.feline.AdultCatModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.CatRenderState;

/** Baby cat built like 1.21.11: adult cat mesh with the old baby transform and the old feline animation. */
public class ClassicCatModel extends AdultCatModel {
    public ClassicCatModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(CatRenderState state) {
        this.resetPose();
        ClassicFelineAnimation.setupAnim(state, this.head, this.body, this.tail1, this.tail2,
                this.leftHindLeg, this.rightHindLeg, this.leftFrontLeg, this.rightFrontLeg);
    }
}
