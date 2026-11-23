package com.classicbabymobs;

import com.classicbabymobs.screen.ClassicConfigScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

/** Client entry point: loads the config and registers the "open menu" key (unbound by default). */
public class ClassicBabyMobs implements ClientModInitializer {
    public static final KeyMapping.Category KEY_CATEGORY =
            KeyMapping.Category.register(Identifier.fromNamespaceAndPath(ClassicBabies.MOD_ID, "main"));

    private static KeyMapping openMenuKey;

    @Override
    public void onInitializeClient() {
        ClassicConfig.load();

        openMenuKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.classic_baby_mobs.open_menu",
                InputConstants.UNKNOWN.getValue(),
                KEY_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openMenuKey.consumeClick()) {
                if (client.gui.screen() == null) {
                    client.gui.setScreen(new ClassicConfigScreen(null));
                }
            }
        });
    }
}
