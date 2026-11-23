package com.classicbabymobs.mixin;

import com.classicbabymobs.ClassicBabies;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 26.1 added a special held-item position for the new tiny babies; classic babies hold items like 1.21.11. */
@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin {
    @Inject(
            method = "useBabyOffset(Lnet/minecraft/client/renderer/entity/state/ArmedEntityRenderState;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void classicBabyMobs$oldHandPosition(ArmedEntityRenderState state, CallbackInfoReturnable<Boolean> cir) {
        if (ClassicBabies.isClassic(state)) {
            cir.setReturnValue(false);
        }
    }
}
