package com.classicbabymobs;

import java.util.Locale;

/**
 * Every baby mob whose look was changed by the Tiny Takeover drop (Java Edition 26.1).
 * Each entry is one toggle in the options screen.
 */
public enum BabyMob {
    // Farm animals
    CHICKEN(Group.FARM, "chicken"),
    COW(Group.FARM, "cow"),
    MOOSHROOM(Group.FARM, "mooshroom"),
    PIG(Group.FARM, "pig"),
    SHEEP(Group.FARM, "sheep"),
    GOAT(Group.FARM, "goat"),
    RABBIT(Group.FARM, "rabbit"),

    // Mounts
    HORSE(Group.MOUNTS, "horse"),
    DONKEY(Group.MOUNTS, "donkey"),
    MULE(Group.MOUNTS, "mule"),
    SKELETON_HORSE(Group.MOUNTS, "skeleton_horse"),
    ZOMBIE_HORSE(Group.MOUNTS, "zombie_horse"),
    CAMEL(Group.MOUNTS, "camel"),
    LLAMA(Group.MOUNTS, "llama"),
    TRADER_LLAMA(Group.MOUNTS, "trader_llama"),
    STRIDER(Group.MOUNTS, "strider"),

    // Pets
    CAT(Group.PETS, "cat"),
    OCELOT(Group.PETS, "ocelot"),
    WOLF(Group.PETS, "wolf"),
    FOX(Group.PETS, "fox"),

    // Wild animals
    ARMADILLO(Group.WILD, "armadillo"),
    BEE(Group.WILD, "bee"),
    PANDA(Group.WILD, "panda"),
    POLAR_BEAR(Group.WILD, "polar_bear"),
    SNIFFER(Group.WILD, "sniffer"),

    // Water mobs
    AXOLOTL(Group.WATER, "axolotl"),
    DOLPHIN(Group.WATER, "dolphin"),
    SQUID(Group.WATER, "squid"),
    GLOW_SQUID(Group.WATER, "glow_squid"),
    TURTLE(Group.WATER, "turtle"),

    // Nether mobs
    HOGLIN(Group.NETHER, "hoglin"),
    ZOGLIN(Group.NETHER, "zoglin"),
    PIGLIN(Group.NETHER, "piglin"),
    ZOMBIFIED_PIGLIN(Group.NETHER, "zombified_piglin"),

    // Villagers and zombies
    VILLAGER(Group.VILLAGERS, "villager"),
    ZOMBIE(Group.VILLAGERS, "zombie"),
    HUSK(Group.VILLAGERS, "husk"),
    DROWNED(Group.VILLAGERS, "drowned"),
    ZOMBIE_VILLAGER(Group.VILLAGERS, "zombie_villager");

    public enum Group {
        FARM, MOUNTS, PETS, WILD, WATER, NETHER, VILLAGERS;

        public String translationKey() {
            return "classic_baby_mobs.group." + this.name().toLowerCase(Locale.ROOT);
        }
    }

    private final Group group;
    private final String id;

    BabyMob(Group group, String id) {
        this.group = group;
        this.id = id;
    }

    public Group group() {
        return this.group;
    }

    /** Key used in the config file, e.g. "polar_bear". */
    public String id() {
        return this.id;
    }

    /** Vanilla translation key for the mob's name, so the menu is translated for free. */
    public String translationKey() {
        return "entity.minecraft." + this.id;
    }

    public static BabyMob byId(String id) {
        for (BabyMob mob : values()) {
            if (mob.id.equals(id)) {
                return mob;
            }
        }
        return null;
    }
}
