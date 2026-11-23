package com.classicbabymobs.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.hoglin.HoglinModel;

/** Baby hoglin/zoglin as in 1.21.11, where babies also lifted their head while headbutting. */
public class ClassicHoglinModel extends HoglinModel {
    public ClassicHoglinModel(ModelPart root) {
        super(root);
    }

    @Override
    protected void animateHeadbutt(float headbuttLerpFactor) {
        super.animateHeadbutt(headbuttLerpFactor);
        this.head.y += headbuttLerpFactor * 2.5f;
    }
}
