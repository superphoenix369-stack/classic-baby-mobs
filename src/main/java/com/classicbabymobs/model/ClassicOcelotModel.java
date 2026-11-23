package com.classicbabymobs.model;

import net.minecraft.client.model.animal.feline.AdultOcelotModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.FelineRenderState;

/** Baby ocelot built like 1.21.11. */
public class ClassicOcelotModel extends AdultOcelotModel {
    public ClassicOcelotModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(FelineRenderState state) {
        this.resetPose();
        ClassicFelineAnimation.setupAnim(state, this.head, this.body, this.tail1, this.tail2,
                this.leftHindLeg, this.rightHindLeg, this.leftFrontLeg, this.rightFrontLeg);
    }
}
