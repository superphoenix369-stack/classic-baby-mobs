package com.classicbabymobs.mixin;

import com.classicbabymobs.BabyMob;
import com.classicbabymobs.ClassicConfig;
import net.minecraft.client.renderer.entity.CatRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/** The cat renderer picks its texture while building the render state; classic kittens get the adult one. */
@Mixin(CatRenderer.class)
public abstract class CatRendererMixin {
    @ModifyArg(
            method = "extractRenderState(Lnet/minecraft/world/entity/animal/feline/Cat;Lnet/minecraft/client/renderer/entity/state/CatRenderState;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/feline/CatVariant;assetInfo(Z)Lnet/minecraft/core/ClientAsset$ResourceTexture;")
    )
    private boolean classicBabyMobs$adultCatTexture(boolean isBaby) {
        return isBaby && !ClassicConfig.isClassic(BabyMob.CAT);
    }
}
