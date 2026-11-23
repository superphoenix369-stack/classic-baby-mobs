package com.classicbabymobs.mixin;

import com.classicbabymobs.ClassicBabies;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.layers.SheepWoolUndercoatLayer;
import net.minecraft.client.renderer.entity.state.SheepRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/** 1.21.11 drew the dyed undercoat on baby sheep too, using a baby-sized undercoat model. */
@Mixin(SheepWoolUndercoatLayer.class)
public abstract class SheepWoolUndercoatLayerMixin {
    @ModifyExpressionValue(
            method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/SheepRenderState;FF)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/SheepWoolUndercoatLayer;model:Lnet/minecraft/client/model/EntityModel;")
    )
    private EntityModel<SheepRenderState> classicBabyMobs$classicUndercoat(EntityModel<SheepRenderState> original) {
        return ClassicBabies.inClassicLayer() ? ClassicBabies.SHEEP_UNDERCOAT.get() : original;
    }
}
