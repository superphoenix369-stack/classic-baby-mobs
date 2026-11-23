package com.classicbabymobs.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.FelineRenderState;
import net.minecraft.util.Mth;

/** The 1.21.11 FelineModel#setupAnim, shared by the classic cat and ocelot models. */
final class ClassicFelineAnimation {
    private ClassicFelineAnimation() {
    }

    static void setupAnim(FelineRenderState state, ModelPart head, ModelPart body, ModelPart tail1, ModelPart tail2,
                          ModelPart leftHindLeg, ModelPart rightHindLeg, ModelPart leftFrontLeg, ModelPart rightFrontLeg) {
        float ageScale = state.ageScale;
        if (state.isCrouching) {
            body.y += 1.0f * ageScale;
            head.y += 2.0f * ageScale;
            tail1.y += 1.0f * ageScale;
            tail2.y += -4.0f * ageScale;
            tail2.z += 2.0f * ageScale;
            tail1.xRot = (float) Math.PI / 2.0f;
            tail2.xRot = (float) Math.PI / 2.0f;
        } else if (state.isSprinting) {
            tail2.y = tail1.y;
            tail2.z += 2.0f * ageScale;
            tail1.xRot = (float) Math.PI / 2.0f;
            tail2.xRot = (float) Math.PI / 2.0f;
        }

        head.xRot = state.xRot * ((float) Math.PI / 180.0f);
        head.yRot = state.yRot * ((float) Math.PI / 180.0f);
        if (!state.isSitting) {
            body.xRot = (float) Math.PI / 2.0f;
            float animationSpeed = state.walkAnimationSpeed;
            float animationPos = state.walkAnimationPos;
            if (state.isSprinting) {
                leftHindLeg.xRot = Mth.cos(animationPos * 0.6662f) * animationSpeed;
                rightHindLeg.xRot = Mth.cos(animationPos * 0.6662f + 0.3f) * animationSpeed;
                leftFrontLeg.xRot = Mth.cos(animationPos * 0.6662f + (float) Math.PI + 0.3f) * animationSpeed;
                rightFrontLeg.xRot = Mth.cos(animationPos * 0.6662f + (float) Math.PI) * animationSpeed;
                tail2.xRot = 1.7278761f + ((float) Math.PI / 10.0f) * Mth.cos(animationPos) * animationSpeed;
            } else {
                leftHindLeg.xRot = Mth.cos(animationPos * 0.6662f) * animationSpeed;
                rightHindLeg.xRot = Mth.cos(animationPos * 0.6662f + (float) Math.PI) * animationSpeed;
                leftFrontLeg.xRot = Mth.cos(animationPos * 0.6662f + (float) Math.PI) * animationSpeed;
                rightFrontLeg.xRot = Mth.cos(animationPos * 0.6662f) * animationSpeed;
                if (!state.isCrouching) {
                    tail2.xRot = 1.7278761f + ((float) Math.PI / 4.0f) * Mth.cos(animationPos) * animationSpeed;
                } else {
                    tail2.xRot = 1.7278761f + 0.47123894f * Mth.cos(animationPos) * animationSpeed;
                }
            }
        }

        if (state.isSitting) {
            body.xRot = (float) Math.PI / 4.0f;
            body.y += -4.0f * ageScale;
            body.z += 5.0f * ageScale;
            head.y += -3.3f * ageScale;
            head.z += 1.0f * ageScale;
            tail1.y += 8.0f * ageScale;
            tail1.z += -2.0f * ageScale;
            tail2.y += 2.0f * ageScale;
            tail2.z += -0.8f * ageScale;
            tail1.xRot = 1.7278761f;
            tail2.xRot = 2.670354f;
            leftFrontLeg.xRot = -0.15707964f;
            leftFrontLeg.y += 2.0f * ageScale;
            leftFrontLeg.z -= 2.0f * ageScale;
            rightFrontLeg.xRot = -0.15707964f;
            rightFrontLeg.y += 2.0f * ageScale;
            rightFrontLeg.z -= 2.0f * ageScale;
            leftHindLeg.xRot = -(float) Math.PI / 2.0f;
            leftHindLeg.y += 3.0f * ageScale;
            leftHindLeg.z -= 4.0f * ageScale;
            rightHindLeg.xRot = -(float) Math.PI / 2.0f;
            rightHindLeg.y += 3.0f * ageScale;
            rightHindLeg.z -= 4.0f * ageScale;
        }

        if (state.lieDownAmount > 0.0f) {
            head.zRot = Mth.rotLerp(state.lieDownAmount, head.zRot, -1.2707963f);
            head.yRot = Mth.rotLerp(state.lieDownAmount, head.yRot, 1.2707963f);
            leftFrontLeg.xRot = -1.2707963f;
            rightFrontLeg.xRot = -0.47079635f;
            rightFrontLeg.zRot = -0.2f;
            rightFrontLeg.x += ageScale;
            leftHindLeg.xRot = -0.4f;
            rightHindLeg.xRot = 0.5f;
            rightHindLeg.zRot = -0.5f;
            rightHindLeg.x += 0.8f * ageScale;
            rightHindLeg.y += 2.0f * ageScale;
            tail1.xRot = Mth.rotLerp(state.lieDownAmountTail, tail1.xRot, 0.8f);
            tail2.xRot = Mth.rotLerp(state.lieDownAmountTail, tail2.xRot, -0.4f);
        }

        if (state.relaxStateOneAmount > 0.0f) {
            head.xRot = Mth.rotLerp(state.relaxStateOneAmount, head.xRot, -0.58177644f);
        }
    }
}
