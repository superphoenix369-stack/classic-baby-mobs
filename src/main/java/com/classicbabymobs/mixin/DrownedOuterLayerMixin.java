package com.classicbabymobs.mixin;

import com.classicbabymobs.ClassicBabies;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.monster.zombie.DrownedModel;
import net.minecraft.client.renderer.entity.layers.DrownedOuterLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Classic baby drowned use the 1.21.11 baby outer layer with the adult outer-layer texture. */
@Mixin(DrownedOuterLayer.class)
public abstract class DrownedOuterLayerMixin {
    @ModifyExpressionValue(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/ZombieRenderState;FF)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/DrownedOuterLayer;model:Lnet/minecraft/client/model/monster/zombie/DrownedModel;")
    )
    private DrownedModel classicBabyMobs$classicOuterLayer(DrownedModel original) {
        return ClassicBabies.inClassicLayer() ? ClassicBabies.DROWNED_OUTER.get() : original;
    }
}
