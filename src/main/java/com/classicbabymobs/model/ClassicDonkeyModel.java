package com.classicbabymobs.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.DonkeyRenderState;

/** Baby donkey / mule as in 1.21.11. */
public class ClassicDonkeyModel extends ClassicEquineModel<DonkeyRenderState> {
    private final ModelPart leftChest;
    private final ModelPart rightChest;

    public ClassicDonkeyModel(ModelPart root) {
        super(root);
        this.leftChest = this.body.getChild("left_chest");
        this.rightChest = this.body.getChild("right_chest");
    }

    @Override
    public void setupAnim(DonkeyRenderState state) {
        super.setupAnim(state);
        this.leftChest.visible = state.hasChest;
        this.rightChest.visible = state.hasChest;
    }
}
