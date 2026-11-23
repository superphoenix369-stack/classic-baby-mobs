package com.terraformersmc.modmenu.api;

/**
 * Compile-time stand-in for Mod Menu's API (only the part this mod uses).
 * It is NOT packaged into the mod jar; at runtime the real Mod Menu provides it.
 */
public interface ModMenuApi {
    default ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> null;
    }
}
