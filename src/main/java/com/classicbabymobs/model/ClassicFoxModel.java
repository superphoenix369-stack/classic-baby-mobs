package com.classicbabymobs.model;

import net.minecraft.client.model.animal.fox.FoxModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.FoxRenderState;
import net.minecraft.util.Mth;

/**
 * Baby fox with the 1.21.11 FoxModel#setupAnim (including its baby-only sleeping and sitting offsets).
 * Extends the 26.x FoxModel because the held-item layer expects one.
 */
public class ClassicFoxModel extends FoxModel {
    private float legMotionPos;

    public ClassicFoxModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(FoxRenderState state) {
        this.resetPose();
        float animationSpeed = state.walkAnimationSpeed;
        float animationPos = state.walkAnimationPos;
        this.rightHindLeg.xRot = Mth.cos(animationPos * 0.6662f) * 1.4f * animationSpeed;
        this.leftHindLeg.xRot = Mth.cos(animationPos * 0.6662f + (float) Math.PI) * 1.4f * animationSpeed;
        this.rightFrontLeg.xRot = Mth.cos(animationPos * 0.6662f + (float) Math.PI) * 1.4f * animationSpeed;
        this.leftFrontLeg.xRot = Mth.cos(animationPos * 0.6662f) * 1.4f * animationSpeed;
        this.head.zRot = state.headRollAngle;
        this.rightHindLeg.visible = true;
        this.leftHindLeg.visible = true;
        this.rightFrontLeg.visible = true;
        this.leftFrontLeg.visible = true;
        float ageScale = state.ageScale;
        if (state.isCrouching) {
            this.body.xRot += 0.10471976f;
            float crouch = state.crouchAmount;
            this.body.y += crouch * ageScale;
            this.head.y += crouch * ageScale;
        } else if (state.isSleeping) {
            this.body.zRot = -(float) Math.PI / 2.0f;
            this.body.y += 5.0f * ageScale;
            this.tail.xRot = -2.6179938f;
            if (state.isBaby) {
                this.tail.xRot = -2.1816616f;
                this.body.z += 2.0f;
            }
            this.head.x += 2.0f * ageScale;
            this.head.y += 2.99f * ageScale;
            this.head.yRot = -2.0943952f;
            this.head.zRot = 0.0f;
            this.rightHindLeg.visible = false;
            this.leftHindLeg.visible = false;
            this.rightFrontLeg.visible = false;
            this.leftFrontLeg.visible = false;
        } else if (state.isSitting) {
            this.body.xRot = (float) Math.PI / 6.0f;
            this.body.y -= 7.0f * ageScale;
            this.body.z += 3.0f * ageScale;
            this.tail.xRot = (float) Math.PI / 4.0f;
            this.tail.z -= 1.0f * ageScale;
            this.head.xRot = 0.0f;
            this.head.yRot = 0.0f;
            if (state.isBaby) {
                this.head.y -= 1.0f;
                this.head.z -= 0.375f;
            } else {
                this.head.y -= 6.5f;
                this.head.z += 2.75f;
            }
            this.rightHindLeg.xRot = -1.3089969f;
            this.rightHindLeg.y += 4.0f * ageScale;
            this.rightHindLeg.z -= 0.25f * ageScale;
            this.leftHindLeg.xRot = -1.3089969f;
            this.leftHindLeg.y += 4.0f * ageScale;
            this.leftHindLeg.z -= 0.25f * ageScale;
            this.rightFrontLeg.xRot = -0.2617994f;
            this.leftFrontLeg.xRot = -0.2617994f;
        }

        if (!state.isSleeping && !state.isFaceplanted && !state.isCrouching) {
            this.head.xRot = state.xRot * ((float) Math.PI / 180.0f);
            this.head.yRot = state.yRot * ((float) Math.PI / 180.0f);
        }

        if (state.isSleeping) {
            this.head.xRot = 0.0f;
            this.head.yRot = -2.0943952f;
            this.head.zRot = Mth.cos(state.ageInTicks * 0.027f) / 22.0f;
        }

        if (state.isCrouching) {
            float wiggleAmount = Mth.cos(state.ageInTicks) * 0.01f;
            this.body.yRot = wiggleAmount;
            this.rightHindLeg.zRot = wiggleAmount;
            this.leftHindLeg.zRot = wiggleAmount;
            this.rightFrontLeg.zRot = wiggleAmount / 2.0f;
            this.leftFrontLeg.zRot = wiggleAmount / 2.0f;
        }

        if (state.isFaceplanted) {
            this.legMotionPos += 0.67f;
            this.rightHindLeg.xRot = Mth.cos(this.legMotionPos * 0.4662f) * 0.1f;
            this.leftHindLeg.xRot = Mth.cos(this.legMotionPos * 0.4662f + (float) Math.PI) * 0.1f;
            this.rightFrontLeg.xRot = Mth.cos(this.legMotionPos * 0.4662f + (float) Math.PI) * 0.1f;
            this.leftFrontLeg.xRot = Mth.cos(this.legMotionPos * 0.4662f) * 0.1f;
        }
    }
}
