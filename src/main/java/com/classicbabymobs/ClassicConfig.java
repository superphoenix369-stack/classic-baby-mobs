package com.classicbabymobs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores which baby mobs use their classic (pre-Tiny Takeover) look.
 * Saved as config/classic_baby_mobs.json. Every mob defaults to classic.
 */
public final class ClassicConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("Classic Baby Mobs");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("classic_baby_mobs.json");

    private static boolean enabled = true;
    private static final Map<BabyMob, Boolean> CLASSIC = new EnumMap<>(BabyMob.class);

    static {
        for (BabyMob mob : BabyMob.values()) {
            CLASSIC.put(mob, Boolean.TRUE);
        }
    }

    private ClassicConfig() {
    }

    /** True when the mod is switched on and this mob is set to its classic look. */
    public static boolean isClassic(BabyMob mob) {
        return enabled && CLASSIC.getOrDefault(mob, Boolean.TRUE);
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    /** The per-mob choice, ignoring the master switch. */
    public static boolean getMob(BabyMob mob) {
        return CLASSIC.getOrDefault(mob, Boolean.TRUE);
    }

    public static void setMob(BabyMob mob, boolean classic) {
        CLASSIC.put(mob, classic);
    }

    public static void setAll(boolean classic) {
        for (BabyMob mob : BabyMob.values()) {
            CLASSIC.put(mob, classic);
        }
    }

    public static void load() {
        if (!Files.exists(FILE)) {
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
            JsonElement root = JsonParser.parseReader(reader);
            if (!root.isJsonObject()) {
                return;
            }
            JsonObject object = root.getAsJsonObject();
            if (object.has("enabled")) {
                enabled = object.get("enabled").getAsBoolean();
            }
            if (object.has("classic_mobs") && object.get("classic_mobs").isJsonObject()) {
                for (Map.Entry<String, JsonElement> entry : object.getAsJsonObject("classic_mobs").entrySet()) {
                    BabyMob mob = BabyMob.byId(entry.getKey());
                    if (mob != null && entry.getValue().isJsonPrimitive()) {
                        CLASSIC.put(mob, entry.getValue().getAsBoolean());
                    }
                }
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.warn("Could not read {}, using defaults", FILE, e);
        }
        // Write back so newly added mobs show up in the file.
        save();
    }

    public static void save() {
        JsonObject root = new JsonObject();
        root.addProperty("enabled", enabled);
        JsonObject mobs = new JsonObject();
        for (BabyMob mob : BabyMob.values()) {
            mobs.addProperty(mob.id(), CLASSIC.getOrDefault(mob, Boolean.TRUE));
        }
        root.add("classic_mobs", mobs);
        try {
            Files.createDirectories(FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
                GSON.toJson(root, writer);
            }
        } catch (IOException e) {
            LOGGER.warn("Could not save {}", FILE, e);
        }
    }
}
