package com.classicbabymobs.model;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.animation.definitions.SnifferAnimation;
import net.minecraft.client.model.animal.sniffer.SnifferModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.SnifferRenderState;

/** Snifflet as in 1.21.11: half-size sniffer with the old static "baby" head pose (bigger head). */
public class ClassicSnifferModel extends SnifferModel {
    private final KeyframeAnimation babyTransform;

    public ClassicSnifferModel(ModelPart root) {
        super(root);
        this.babyTransform = SnifferAnimation.BABY_TRANSFORM.bake(root);
    }

    @Override
    public void setupAnim(SnifferRenderState state) {
        super.setupAnim(state);
        if (state.isBaby) {
            this.babyTransform.applyStatic();
        }
    }
}
