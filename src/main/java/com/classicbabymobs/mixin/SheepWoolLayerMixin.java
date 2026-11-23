package com.classicbabymobs.mixin;

import com.classicbabymobs.ClassicBabies;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.SheepWoolLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** Classic baby sheep wear the 1.21.11 baby wool model with the adult wool texture. */
@Mixin(SheepWoolLayer.class)
public abstract class SheepWoolLayerMixin {
    @ModifyExpressionValue(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SheepRenderState;FF)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/SheepWoolLayer;adultModel:Lnet/minecraft/client/model/EntityModel;")
    )
    private EntityModel<SheepRenderState> classicBabyMobs$classicWool(EntityModel<SheepRenderState> original) {
        return ClassicBabies.inClassicLayer() ? ClassicBabies.SHEEP_WOOL.get() : original;
    }
}
