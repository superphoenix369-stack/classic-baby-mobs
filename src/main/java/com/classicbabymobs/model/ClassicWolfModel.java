package com.classicbabymobs.model;

import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.WolfRenderState;

/**
 * Baby wolf as in 1.21.11. The 26.x adult wolf is identical except that, when sitting, its upper body
 * is raised by a fixed 2 pixels; 1.21.11 scaled that by the age scale.
 */
public class ClassicWolfModel extends AdultWolfModel {
    private final ModelPart classicUpperBody;

    public ClassicWolfModel(ModelPart root) {
        super(root);
        this.classicUpperBody = root.getChild("upper_body");
    }

    @Override
    protected void setSittingPose(WolfRenderState state) {
        super.setSittingPose(state);
        this.classicUpperBody.y += 2.0f * state.ageScale - 2.0f;
    }
}
