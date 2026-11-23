package com.classicbabymobs.mixin;

import com.classicbabymobs.ClassicBabies;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Villager biome outfits. Since 26.1 babies use separate "baby" outfit textures drawn for the tiny
 * model; classic babies use the regular outfit textures and a baby-sized "no hat" model, as in 1.21.11.
 * Babies still never show profession overlays, also as in 1.21.11.
 */
@Mixin(VillagerProfessionLayer.class)
public abstract class VillagerProfessionLayerMixin {
    private static final String SUBMIT = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;FF)V";

    @Unique
    private @Nullable EntityType<?> classicBabyMobs$classicType;

    @Inject(method = SUBMIT, at = @At("HEAD"))
    private void classicBabyMobs$check(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords,
                                       LivingEntityRenderState state, float yRot, float xRot, CallbackInfo ci) {
        this.classicBabyMobs$classicType = ClassicBabies.isClassic(state) ? state.entityType : null;
    }

    @Inject(method = SUBMIT, at = @At("RETURN"))
    private void classicBabyMobs$clear(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords,
                                       LivingEntityRenderState state, float yRot, float xRot, CallbackInfo ci) {
        this.classicBabyMobs$classicType = null;
    }

    /** The first isBaby check picks the "baby" or "type" outfit texture folder. */
    @ModifyExpressionValue(
            method = SUBMIT,
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;isBaby:Z", ordinal = 0)
    )
    private boolean classicBabyMobs$adultOutfitTexture(boolean isBaby) {
        return isBaby && this.classicBabyMobs$classicType == null;
    }

    @ModifyExpressionValue(
            method = SUBMIT,
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/VillagerProfessionLayer;noHatBabyModel:Lnet/minecraft/client/model/EntityModel;")
    )
    private EntityModel<?> classicBabyMobs$classicNoHatModel(EntityModel<?> original) {
        EntityType<?> type = this.classicBabyMobs$classicType;
        if (type == EntityTypes.VILLAGER) {
            return ClassicBabies.VILLAGER_NO_HAT.get();
        }
        if (type == EntityTypes.ZOMBIE_VILLAGER) {
            return ClassicBabies.ZOMBIE_VILLAGER_NO_HAT.get();
        }
        return original;
    }
}
