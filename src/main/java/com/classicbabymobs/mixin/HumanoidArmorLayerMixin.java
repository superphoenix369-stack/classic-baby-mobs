package com.classicbabymobs.mixin;

import com.classicbabymobs.ClassicBabies;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Classic baby zombies, husks, drowned, zombie villagers, piglins and zombified piglins wear armor
 * shaped like 1.21.11: the adult armor shrunk with the old humanoid baby transform, using the normal
 * (not baby) armor textures.
 */
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {
    @ModifyExpressionValue(
            method = "getArmorModel",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;modelSet:Lnet/minecraft/client/renderer/entity/ArmorModelSet;")
    )
    private ArmorModelSet<?> classicBabyMobs$classicArmor(ArmorModelSet<?> original) {
        if (!ClassicBabies.inClassicLayer()) {
            return original;
        }
        ArmorModelSet<?> classic = ClassicBabies.armorFor(ClassicBabies.layerEntity());
        return classic != null ? classic : original;
    }
}
