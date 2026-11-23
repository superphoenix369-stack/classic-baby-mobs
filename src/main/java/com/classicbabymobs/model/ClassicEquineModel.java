package com.classicbabymobs.model;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.util.Mth;

/** Baby horse / skeleton horse / zombie horse with the 1.21.11 AbstractEquineModel#setupAnim. */
public class ClassicEquineModel<S extends EquineRenderState> extends EntityModel<S> {
    private static final float DEG_125 = 2.1816616f;
    private static final float DEG_60 = (float) Math.PI / 3.0f;
    private static final float DEG_45 = (float) Math.PI / 4.0f;
    private static final float DEG_30 = (float) Math.PI / 6.0f;
    private static final float DEG_15 = 0.2617994f;

    protected final ModelPart body;
    protected final ModelPart headParts;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart tail;

    public ClassicEquineModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.headParts = root.getChild("head_parts");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.tail = this.body.getChild("tail");
    }

    @Override
    public void setupAnim(S state) {
        super.setupAnim(state);
        float clampedYRot = Mth.clamp(state.yRot, -20.0f, 20.0f);
        float headRotXRad = state.xRot * ((float) Math.PI / 180.0f);
        float animationSpeed = state.walkAnimationSpeed;
        float animationPos = state.walkAnimationPos;
        if (animationSpeed > 0.2f) {
            headRotXRad += Mth.cos(animationPos * 0.8f) * 0.15f * animationSpeed;
        }

        float eating = state.eatAnimation;
        float standing = state.standAnimation;
        float iStanding = 1.0f - standing;
        float feedingAnim = state.feedingAnimation;
        boolean animateTail = state.animateTail;
        this.headParts.xRot = DEG_30 + headRotXRad;
        this.headParts.yRot = clampedYRot * ((float) Math.PI / 180.0f);
        float waterMultiplier = state.isInWater ? 0.2f : 1.0f;
        float legAnim1 = Mth.cos(waterMultiplier * animationPos * 0.6662f + (float) Math.PI);
        float legXRotAnim = legAnim1 * 0.8f * animationSpeed;
        float baseHeadAngle = (1.0f - Math.max(standing, eating)) * (DEG_30 + headRotXRad + feedingAnim * Mth.sin(state.ageInTicks) * 0.05f);
        this.headParts.xRot = standing * (DEG_15 + headRotXRad) + eating * (DEG_125 + Mth.sin(state.ageInTicks) * 0.05f) + baseHeadAngle;
        this.headParts.yRot = standing * clampedYRot * ((float) Math.PI / 180.0f) + (1.0f - Math.max(standing, eating)) * this.headParts.yRot;
        float ageScale = state.ageScale;
        this.headParts.y += Mth.lerp(eating, Mth.lerp(standing, 0.0f, -8.0f * ageScale), 7.0f * ageScale);
        this.headParts.z = Mth.lerp(standing, this.headParts.z, -4.0f * ageScale);
        this.body.xRot = standing * -DEG_45 + iStanding * this.body.xRot;
        float standAngle = DEG_15 * standing;
        float bobValue = Mth.cos(state.ageInTicks * 0.6f + (float) Math.PI);
        this.leftFrontLeg.y -= 12.0f * ageScale * standing;
        this.leftFrontLeg.z += 4.0f * ageScale * standing;
        this.rightFrontLeg.y = this.leftFrontLeg.y;
        this.rightFrontLeg.z = this.leftFrontLeg.z;
        float rlegRot = (-DEG_60 + bobValue) * standing + legXRotAnim * iStanding;
        float llegRot = (-DEG_60 - bobValue) * standing - legXRotAnim * iStanding;
        this.leftHindLeg.xRot = standAngle - legAnim1 * 0.5f * animationSpeed * iStanding;
        this.rightHindLeg.xRot = standAngle + legAnim1 * 0.5f * animationSpeed * iStanding;
        this.leftFrontLeg.xRot = rlegRot;
        this.rightFrontLeg.xRot = llegRot;
        this.tail.xRot = DEG_30 + animationSpeed * 0.75f;
        this.tail.y += animationSpeed * ageScale;
        this.tail.z += animationSpeed * 2.0f * ageScale;
        if (animateTail) {
            this.tail.yRot = Mth.cos(state.ageInTicks * 0.7f);
        } else {
            this.tail.yRot = 0.0f;
        }
    }
}
