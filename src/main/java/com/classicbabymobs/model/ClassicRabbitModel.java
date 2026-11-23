package com.classicbabymobs.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.RabbitRenderState;
import net.minecraft.util.Mth;

/**
 * The 1.21.11 rabbit model. Tiny Takeover replaced the adult rabbit too, so the old rabbit is
 * restored completely here and drawn with the bundled 1.21.11 rabbit textures.
 */
public class ClassicRabbitModel extends EntityModel<RabbitRenderState> {
    private static final float REAR_JUMP_ANGLE = 50.0f;
    private static final float FRONT_JUMP_ANGLE = -40.0f;
    private static final float DEG_TO_RAD = (float) Math.PI / 180.0f;

    private final ModelPart leftHaunch;
    private final ModelPart rightHaunch;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart head;

    public ClassicRabbitModel(ModelPart root) {
        super(root);
        this.leftHaunch = root.getChild("left_haunch");
        this.rightHaunch = root.getChild("right_haunch");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.head = root.getChild("head");
    }

    @Override
    public void setupAnim(RabbitRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * DEG_TO_RAD;
        this.head.yRot = state.yRot * DEG_TO_RAD;
        float jumpRotation = Mth.sin(state.jumpCompletion * (float) Math.PI);
        this.leftHaunch.xRot += jumpRotation * REAR_JUMP_ANGLE * DEG_TO_RAD;
        this.rightHaunch.xRot += jumpRotation * REAR_JUMP_ANGLE * DEG_TO_RAD;
        this.leftFrontLeg.xRot += jumpRotation * FRONT_JUMP_ANGLE * DEG_TO_RAD;
        this.rightFrontLeg.xRot += jumpRotation * FRONT_JUMP_ANGLE * DEG_TO_RAD;
    }
}
