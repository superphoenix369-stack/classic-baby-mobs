package com.classicbabymobs.mixin;

import com.classicbabymobs.BabyMob;
import com.classicbabymobs.ClassicConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.animal.wolf.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Wolf#getTexture (only used by the wolf renderer) chooses between the adult and puppy texture.
 * Classic puppies use the adult texture, like in 1.21.11.
 */
@Mixin(Wolf.class)
public abstract class WolfMixin {
    @ModifyExpressionValue(
            method = "getTexture",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/animal/wolf/Wolf;isBaby()Z")
    )
    private boolean classicBabyMobs$adultWolfTexture(boolean isBaby) {
        return isBaby && !ClassicConfig.isClassic(BabyMob.WOLF);
    }
}
