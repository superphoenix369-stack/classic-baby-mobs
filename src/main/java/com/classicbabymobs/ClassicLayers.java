package com.classicbabymobs;

import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer;
import net.minecraft.client.renderer.entity.layers.WolfCollarLayer;

/**
 * Render layers that, for a classic baby, should be drawn the way an adult's would be (adult texture,
 * 1.21.11 rules) but with the classic baby layer model, which the layer mixins supply.
 * <p>
 * Layers not listed here (held items, mooshroom mushrooms, the fox's mouth item, villager outfits...)
 * keep their normal baby behaviour, which already matches 1.21.11 or is handled by its own mixin.
 */
public final class ClassicLayers {
    private ClassicLayers() {
    }

    public static boolean drawsAsAdult(RenderLayer<?, ?> layer) {
        return layer instanceof SheepWoolLayer
                || layer instanceof SheepWoolUndercoatLayer
                || layer instanceof CatCollarLayer
                || layer instanceof WolfCollarLayer
                || layer instanceof HorseMarkingLayer
                || layer instanceof LlamaDecorLayer
                || layer instanceof DrownedOuterLayer
                || layer instanceof HumanoidArmorLayer;
    }
}
