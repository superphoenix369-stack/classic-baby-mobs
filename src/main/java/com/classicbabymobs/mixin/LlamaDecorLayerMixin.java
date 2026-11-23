package com.classicbabymobs.mixin;

import com.classicbabymobs.ClassicBabies;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.renderer.entity.layers.LlamaDecorLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Classic baby (trader) llamas wear the 1.21.11 baby decor model with the adult blanket texture. */
@Mixin(LlamaDecorLayer.class)
public abstract class LlamaDecorLayerMixin {
    @ModifyExpressionValue(
            method = "renderEquipment",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/LlamaDecorLayer;adultModel:Lnet/minecraft/client/model/animal/llama/LlamaModel;")
    )
    private LlamaModel classicBabyMobs$classicDecor(LlamaModel original) {
        return ClassicBabies.inClassicLayer() ? ClassicBabies.LLAMA_DECOR.get() : original;
    }
}
