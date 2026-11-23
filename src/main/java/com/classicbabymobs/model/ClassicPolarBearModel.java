package com.classicbabymobs.model;

import net.minecraft.client.model.animal.polarbear.PolarBearModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.PolarBearRenderState;

/**
 * Baby polar bear as in 1.21.11, where the standing-up head motion was scaled down for babies
 * (head offset factor 0.444 instead of 1).
 */
public class ClassicPolarBearModel extends PolarBearModel {
    private static final float BABY_HEAD_AGE_SCALE = 0.44444445f;

    public ClassicPolarBearModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(PolarBearRenderState state) {
        super.setupAnim(state);
        // 26.x moves the head by the full amount; undo the part that 1.21.11 did not apply to babies.
        float standScale = state.standScale * state.standScale;
        float missing = 1.0f - BABY_HEAD_AGE_SCALE;
        this.head.y += standScale * 24.0f * missing;
        this.head.z -= standScale * 13.0f * missing;
    }
}
