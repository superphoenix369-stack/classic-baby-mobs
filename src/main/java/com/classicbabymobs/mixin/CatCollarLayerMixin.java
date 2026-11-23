package com.classicbabymobs.mixin;

import com.classicbabymobs.ClassicBabies;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.animal.feline.AdultCatModel;
import net.minecraft.client.renderer.entity.layers.CatCollarLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Classic baby cats wear the 1.21.11 baby collar model with the adult collar texture. */
@Mixin(CatCollarLayer.class)
public abstract class CatCollarLayerMixin {
    @ModifyExpressionValue(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/CatRenderState;FF)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/CatCollarLayer;adultModel:Lnet/minecraft/client/model/animal/feline/AdultCatModel;")
    )
    private AdultCatModel classicBabyMobs$classicCollar(AdultCatModel original) {
        return ClassicBabies.inClassicLayer() ? ClassicBabies.CAT_COLLAR.get() : original;
    }
}
