package com.classicbabymobs.mixin;

import com.classicbabymobs.ClassicBabies;
import com.classicbabymobs.ClassicLayers;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * The heart of the mod. When a baby whose classic look is switched on is drawn:
 * <ul>
 *   <li>its model is swapped for the 1.21.11-style baby model for the duration of the draw,</li>
 *   <li>its texture is looked up as if it were an adult (1.21.11 babies used the adult texture),</li>
 *   <li>render layers that have their own baby model are told to use the classic one instead.</li>
 * </ul>
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {
    private static final String SUBMIT = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V";

    @Shadow
    protected M model;

    @Unique
    private @Nullable M classicBabyMobs$vanillaModel;

    @Inject(method = SUBMIT, at = @At("HEAD"))
    @SuppressWarnings("unchecked")
    private void classicBabyMobs$useClassicModel(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        ClassicBabies.Handler handler = ClassicBabies.active(state);
        if (handler == null) {
            return;
        }
        EntityModel<?> classic = ClassicBabies.bodyModel(handler, state);
        if (classic == null) {
            return;
        }
        this.classicBabyMobs$vanillaModel = this.model;
        this.model = (M) classic;
    }

    @Inject(method = SUBMIT, at = @At("RETURN"))
    private void classicBabyMobs$restoreModel(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (this.classicBabyMobs$vanillaModel != null) {
            this.model = this.classicBabyMobs$vanillaModel;
            this.classicBabyMobs$vanillaModel = null;
        }
    }

    @WrapOperation(
            method = "getRenderType",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)Lnet/minecraft/resources/Identifier;")
    )
    private Identifier classicBabyMobs$useAdultTexture(LivingEntityRenderer<?, ?, ?> renderer, LivingEntityRenderState state, Operation<Identifier> original) {
        ClassicBabies.Handler handler = ClassicBabies.active(state);
        if (handler == null) {
            return original.call(renderer, state);
        }
        Identifier override = ClassicBabies.textureOverride(handler, state);
        if (override != null) {
            return override;
        }
        state.isBaby = false;
        try {
            return original.call(renderer, state);
        } finally {
            state.isBaby = true;
        }
    }

    @WrapOperation(
            method = SUBMIT,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/EntityRenderState;FF)V")
    )
    private void classicBabyMobs$classicLayer(RenderLayer<?, ?> layer, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords,
                                              EntityRenderState state, float yRot, float xRot, Operation<Void> original) {
        if (!ClassicLayers.drawsAsAdult(layer) || !(state instanceof LivingEntityRenderState living) || ClassicBabies.active(living) == null) {
            original.call(layer, poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
            return;
        }
        living.isBaby = false;
        ClassicBabies.enterLayer(living.entityType);
        try {
            original.call(layer, poseStack, submitNodeCollector, lightCoords, state, yRot, xRot);
        } finally {
            living.isBaby = true;
            ClassicBabies.exitLayer();
        }
    }
}
