package com.classicbabymobs;

import com.classicbabymobs.model.ClassicCatModel;
import com.classicbabymobs.model.ClassicDonkeyModel;
import com.classicbabymobs.model.ClassicEquineModel;
import com.classicbabymobs.model.ClassicFoxModel;
import com.classicbabymobs.model.ClassicHoglinModel;
import com.classicbabymobs.model.ClassicMeshes;
import com.classicbabymobs.model.ClassicOcelotModel;
import com.classicbabymobs.model.ClassicPolarBearModel;
import com.classicbabymobs.model.ClassicRabbitModel;
import com.classicbabymobs.model.ClassicSnifferModel;
import com.classicbabymobs.model.ClassicWolfModel;
import com.classicbabymobs.model.ClassicZombieModels;
import com.google.common.base.Suppliers;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.animal.armadillo.AdultArmadilloModel;
import net.minecraft.client.model.animal.axolotl.AdultAxolotlModel;
import net.minecraft.client.model.animal.bee.AdultBeeModel;
import net.minecraft.client.model.animal.camel.AdultCamelModel;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.ColdChickenModel;
import net.minecraft.client.model.animal.cow.ColdCowModel;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.animal.cow.WarmCowModel;
import net.minecraft.client.model.animal.dolphin.DolphinModel;
import net.minecraft.client.model.animal.goat.GoatModel;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.model.animal.panda.PandaModel;
import net.minecraft.client.model.animal.pig.ColdPigModel;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.animal.squid.SquidModel;
import net.minecraft.client.model.animal.turtle.AdultTurtleModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.monster.piglin.AdultPiglinModel;
import net.minecraft.client.model.monster.strider.AdultStriderModel;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.entity.state.CowRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.PigRenderState;
import net.minecraft.client.renderer.entity.state.RabbitRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.cow.CowVariant;
import net.minecraft.world.entity.animal.pig.PigVariant;
import org.jspecify.annotations.Nullable;

/**
 * Knows, for every baby mob changed by Tiny Takeover, which model it used in 1.21.11 and decides at
 * render time whether a given baby should be drawn the classic way.
 * <p>
 * Models are built straight from their layer definitions the first time they are needed, so turning a
 * mob on or off in the menu takes effect on the next frame without reloading resources.
 */
public final class ClassicBabies {
    public static final String MOD_ID = "classic_baby_mobs";

    /** What to draw for one entity type when its classic look is on. */
    public static final class Handler {
        final BabyMob mob;
        final Function<LivingEntityRenderState, @Nullable EntityModel<?>> body;
        final @Nullable Function<LivingEntityRenderState, Identifier> texture;

        Handler(BabyMob mob, Function<LivingEntityRenderState, @Nullable EntityModel<?>> body,
                @Nullable Function<LivingEntityRenderState, Identifier> texture) {
            this.mob = mob;
            this.body = body;
            this.texture = texture;
        }

        public BabyMob mob() {
            return this.mob;
        }
    }

    private static final Map<EntityType<?>, Handler> HANDLERS = new IdentityHashMap<>();
    private static volatile boolean registered;

    private ClassicBabies() {
    }

    // ------------------------------------------------------------------ runtime queries

    /** The handler to use for this render state, or null if it should be drawn the vanilla 26.x way. */
    public static @Nullable Handler active(EntityRenderState state) {
        if (!(state instanceof LivingEntityRenderState living) || !living.isBaby || living.entityType == null) {
            return null;
        }
        ensureRegistered();
        Handler handler = HANDLERS.get(living.entityType);
        if (handler == null || !ClassicConfig.isClassic(handler.mob)) {
            return null;
        }
        return handler;
    }

    public static boolean isClassic(EntityRenderState state) {
        return active(state) != null;
    }

    public static @Nullable EntityModel<?> bodyModel(Handler handler, LivingEntityRenderState state) {
        return handler.body.apply(state);
    }

    /** A texture that replaces the vanilla one entirely, or null to use the vanilla adult texture. */
    public static @Nullable Identifier textureOverride(Handler handler, LivingEntityRenderState state) {
        return handler.texture == null ? null : handler.texture.apply(state);
    }

    // ------------------------------------------------------------------ layer context
    // While a render layer is drawn for a classic baby, the layer is told the entity is an adult (so it
    // picks adult textures and the 1.21.11 behaviour) and these fields say which classic layer model to use.

    private static @Nullable EntityType<?> layerEntity;

    public static void enterLayer(EntityType<?> type) {
        layerEntity = type;
    }

    public static void exitLayer() {
        layerEntity = null;
    }

    public static boolean inClassicLayer() {
        return layerEntity != null;
    }

    public static @Nullable EntityType<?> layerEntity() {
        return layerEntity;
    }

    // ------------------------------------------------------------------ classic layer models

    public static final Supplier<SheepFurModel> SHEEP_WOOL = lazy(() -> new SheepFurModel(ClassicMeshes.sheepWool().bakeRoot()));
    public static final Supplier<SheepFurModel> SHEEP_UNDERCOAT = lazy(() -> new SheepFurModel(ClassicMeshes.sheepUndercoat().bakeRoot()));
    public static final Supplier<ClassicCatModel> CAT_COLLAR = lazy(() -> new ClassicCatModel(ClassicMeshes.catCollar().bakeRoot()));
    public static final Supplier<LlamaModel> LLAMA_DECOR = lazy(() -> new LlamaModel(ClassicMeshes.llamaDecor().bakeRoot()));
    public static final Supplier<ClassicZombieModels.Drowned> DROWNED_OUTER = lazy(() -> new ClassicZombieModels.Drowned(ClassicMeshes.drownedOuter().bakeRoot()));
    public static final Supplier<VillagerModel> VILLAGER_NO_HAT = lazy(() -> new VillagerModel(ClassicMeshes.villagerNoHat().bakeRoot()));
    public static final Supplier<ClassicZombieModels.ZombieVillager> ZOMBIE_VILLAGER_NO_HAT = lazy(() -> new ClassicZombieModels.ZombieVillager(ClassicMeshes.zombieVillagerNoHat().bakeRoot()));

    private static final Supplier<ArmorModelSet<ClassicZombieModels.Zombie>> ZOMBIE_ARMOR = lazy(() -> bakeArmor(ClassicMeshes.humanoidArmor(), ClassicZombieModels.Zombie::new));
    private static final Supplier<ArmorModelSet<ClassicZombieModels.Zombie>> HUSK_ARMOR = lazy(() -> bakeArmor(ClassicMeshes.huskArmor(), ClassicZombieModels.Zombie::new));
    private static final Supplier<ArmorModelSet<ClassicZombieModels.Drowned>> DROWNED_ARMOR = lazy(() -> bakeArmor(ClassicMeshes.humanoidArmor(), ClassicZombieModels.Drowned::new));
    private static final Supplier<ArmorModelSet<ClassicZombieModels.ZombieVillager>> ZOMBIE_VILLAGER_ARMOR = lazy(() -> bakeArmor(ClassicMeshes.zombieVillagerArmor(), ClassicZombieModels.ZombieVillager::new));
    private static final Supplier<ArmorModelSet<AdultPiglinModel>> PIGLIN_ARMOR = lazy(() -> bakeArmor(ClassicMeshes.piglinArmor(), AdultPiglinModel::new));
    private static final Supplier<ArmorModelSet<ClassicZombieModels.ZombifiedPiglin>> ZOMBIFIED_PIGLIN_ARMOR = lazy(() -> bakeArmor(ClassicMeshes.piglinArmor(), ClassicZombieModels.ZombifiedPiglin::new));

    /** Classic baby armor for the entity whose layer is being drawn, or null to keep vanilla's. */
    public static @Nullable ArmorModelSet<?> armorFor(@Nullable EntityType<?> type) {
        if (type == EntityTypes.ZOMBIE) return ZOMBIE_ARMOR.get();
        if (type == EntityTypes.HUSK) return HUSK_ARMOR.get();
        if (type == EntityTypes.DROWNED) return DROWNED_ARMOR.get();
        if (type == EntityTypes.ZOMBIE_VILLAGER) return ZOMBIE_VILLAGER_ARMOR.get();
        if (type == EntityTypes.PIGLIN) return PIGLIN_ARMOR.get();
        if (type == EntityTypes.ZOMBIFIED_PIGLIN) return ZOMBIFIED_PIGLIN_ARMOR.get();
        return null;
    }

    private static <M> ArmorModelSet<M> bakeArmor(ArmorModelSet<LayerDefinition> layers, Function<net.minecraft.client.model.geom.ModelPart, M> factory) {
        return layers.map(layer -> factory.apply(layer.bakeRoot()));
    }

    // ------------------------------------------------------------------ registration

    private static void ensureRegistered() {
        if (!registered) {
            synchronized (HANDLERS) {
                if (!registered) {
                    registerAll();
                    registered = true;
                }
            }
        }
    }

    private static <T> Supplier<T> lazy(Supplier<T> supplier) {
        return Suppliers.memoize(supplier::get);
    }

    private static void simple(EntityType<?> type, BabyMob mob, Supplier<? extends EntityModel<?>> model) {
        Supplier<? extends EntityModel<?>> cached = lazy(model);
        HANDLERS.put(type, new Handler(mob, state -> cached.get(), null));
    }

    private static void registerAll() {
        // Farm animals ------------------------------------------------------------
        Supplier<CowModel> cow = lazy(() -> new CowModel(ClassicMeshes.cow().bakeRoot()));
        Supplier<CowModel> warmCow = lazy(() -> new WarmCowModel(ClassicMeshes.warmCow().bakeRoot()));
        Supplier<CowModel> coldCow = lazy(() -> new ColdCowModel(ClassicMeshes.coldCow().bakeRoot()));
        HANDLERS.put(EntityTypes.COW, new Handler(BabyMob.COW, state -> {
            CowVariant variant = state instanceof CowRenderState cowState ? cowState.variant : null;
            if (variant == null) {
                return null;
            }
            return switch (variant.modelAndTexture().model()) {
                case WARM -> warmCow.get();
                case COLD -> coldCow.get();
                default -> cow.get();
            };
        }, null));
        simple(EntityTypes.MOOSHROOM, BabyMob.MOOSHROOM, () -> new CowModel(ClassicMeshes.cow().bakeRoot()));

        Supplier<PigModel> pig = lazy(() -> new PigModel(ClassicMeshes.pig().bakeRoot()));
        Supplier<PigModel> coldPig = lazy(() -> new ColdPigModel(ClassicMeshes.coldPig().bakeRoot()));
        HANDLERS.put(EntityTypes.PIG, new Handler(BabyMob.PIG, state -> {
            PigVariant variant = state instanceof PigRenderState pigState ? pigState.variant : null;
            if (variant == null) {
                return null;
            }
            return variant.modelAndTexture().model() == PigVariant.ModelType.COLD ? coldPig.get() : pig.get();
        }, null));

        Supplier<AdultChickenModel> chicken = lazy(() -> new AdultChickenModel(ClassicMeshes.chicken().bakeRoot()));
        Supplier<AdultChickenModel> coldChicken = lazy(() -> new ColdChickenModel(ClassicMeshes.coldChicken().bakeRoot()));
        HANDLERS.put(EntityTypes.CHICKEN, new Handler(BabyMob.CHICKEN, state -> {
            ChickenVariant variant = state instanceof ChickenRenderState chickenState ? chickenState.variant : null;
            if (variant == null) {
                return null;
            }
            return variant.modelAndTexture().model() == ChickenVariant.ModelType.COLD ? coldChicken.get() : chicken.get();
        }, null));

        simple(EntityTypes.SHEEP, BabyMob.SHEEP, () -> new SheepModel(ClassicMeshes.sheep().bakeRoot()));
        simple(EntityTypes.GOAT, BabyMob.GOAT, () -> new GoatModel(ClassicMeshes.goat().bakeRoot()));

        Supplier<ClassicRabbitModel> rabbit = lazy(() -> new ClassicRabbitModel(ClassicMeshes.rabbit().bakeRoot()));
        HANDLERS.put(EntityTypes.RABBIT, new Handler(BabyMob.RABBIT, state -> rabbit.get(), ClassicBabies::rabbitTexture));

        // Mounts ------------------------------------------------------------------
        simple(EntityTypes.HORSE, BabyMob.HORSE, () -> new ClassicEquineModel<>(ClassicMeshes.horse().bakeRoot()));
        simple(EntityTypes.SKELETON_HORSE, BabyMob.SKELETON_HORSE, () -> new ClassicEquineModel<>(ClassicMeshes.undeadHorse().bakeRoot()));
        simple(EntityTypes.ZOMBIE_HORSE, BabyMob.ZOMBIE_HORSE, () -> new ClassicEquineModel<>(ClassicMeshes.undeadHorse().bakeRoot()));
        simple(EntityTypes.DONKEY, BabyMob.DONKEY, () -> new ClassicDonkeyModel(ClassicMeshes.donkey(0.87f).bakeRoot()));
        simple(EntityTypes.MULE, BabyMob.MULE, () -> new ClassicDonkeyModel(ClassicMeshes.donkey(0.92f).bakeRoot()));
        simple(EntityTypes.CAMEL, BabyMob.CAMEL, () -> new AdultCamelModel(ClassicMeshes.camel().bakeRoot()));
        simple(EntityTypes.LLAMA, BabyMob.LLAMA, () -> new LlamaModel(ClassicMeshes.llama().bakeRoot()));
        simple(EntityTypes.TRADER_LLAMA, BabyMob.TRADER_LLAMA, () -> new LlamaModel(ClassicMeshes.llama().bakeRoot()));
        simple(EntityTypes.STRIDER, BabyMob.STRIDER, () -> new AdultStriderModel(ClassicMeshes.strider().bakeRoot()));

        // Pets --------------------------------------------------------------------
        simple(EntityTypes.CAT, BabyMob.CAT, () -> new ClassicCatModel(ClassicMeshes.cat().bakeRoot()));
        simple(EntityTypes.OCELOT, BabyMob.OCELOT, () -> new ClassicOcelotModel(ClassicMeshes.ocelot().bakeRoot()));
        simple(EntityTypes.WOLF, BabyMob.WOLF, () -> new ClassicWolfModel(ClassicMeshes.wolf().bakeRoot()));
        simple(EntityTypes.FOX, BabyMob.FOX, () -> new ClassicFoxModel(ClassicMeshes.fox().bakeRoot()));

        // Wild animals ------------------------------------------------------------
        simple(EntityTypes.ARMADILLO, BabyMob.ARMADILLO, () -> new AdultArmadilloModel(ClassicMeshes.armadillo().bakeRoot()));
        simple(EntityTypes.BEE, BabyMob.BEE, () -> new AdultBeeModel(ClassicMeshes.bee().bakeRoot()));
        simple(EntityTypes.PANDA, BabyMob.PANDA, () -> new PandaModel(ClassicMeshes.panda().bakeRoot()));
        simple(EntityTypes.POLAR_BEAR, BabyMob.POLAR_BEAR, () -> new ClassicPolarBearModel(ClassicMeshes.polarBear().bakeRoot()));
        simple(EntityTypes.SNIFFER, BabyMob.SNIFFER, () -> new ClassicSnifferModel(ClassicMeshes.sniffer().bakeRoot()));

        // Water mobs --------------------------------------------------------------
        simple(EntityTypes.AXOLOTL, BabyMob.AXOLOTL, () -> new AdultAxolotlModel(ClassicMeshes.axolotl().bakeRoot()));
        simple(EntityTypes.DOLPHIN, BabyMob.DOLPHIN, () -> new DolphinModel(ClassicMeshes.dolphin().bakeRoot()));
        simple(EntityTypes.SQUID, BabyMob.SQUID, () -> new SquidModel(ClassicMeshes.squid().bakeRoot()));
        simple(EntityTypes.GLOW_SQUID, BabyMob.GLOW_SQUID, () -> new SquidModel(ClassicMeshes.squid().bakeRoot()));
        simple(EntityTypes.TURTLE, BabyMob.TURTLE, () -> new AdultTurtleModel(ClassicMeshes.turtle().bakeRoot()));

        // Nether mobs -------------------------------------------------------------
        simple(EntityTypes.HOGLIN, BabyMob.HOGLIN, () -> new ClassicHoglinModel(ClassicMeshes.hoglin().bakeRoot()));
        simple(EntityTypes.ZOGLIN, BabyMob.ZOGLIN, () -> new ClassicHoglinModel(ClassicMeshes.hoglin().bakeRoot()));
        simple(EntityTypes.PIGLIN, BabyMob.PIGLIN, () -> new AdultPiglinModel(ClassicMeshes.piglin().bakeRoot()));
        simple(EntityTypes.ZOMBIFIED_PIGLIN, BabyMob.ZOMBIFIED_PIGLIN, () -> new ClassicZombieModels.ZombifiedPiglin(ClassicMeshes.piglin().bakeRoot()));

        // Villagers and zombies ---------------------------------------------------
        simple(EntityTypes.VILLAGER, BabyMob.VILLAGER, () -> new VillagerModel(ClassicMeshes.villager().bakeRoot()));
        simple(EntityTypes.ZOMBIE, BabyMob.ZOMBIE, () -> new ClassicZombieModels.Zombie(ClassicMeshes.zombie().bakeRoot()));
        simple(EntityTypes.HUSK, BabyMob.HUSK, () -> new ClassicZombieModels.Zombie(ClassicMeshes.husk().bakeRoot()));
        simple(EntityTypes.DROWNED, BabyMob.DROWNED, () -> new ClassicZombieModels.Drowned(ClassicMeshes.drowned().bakeRoot()));
        simple(EntityTypes.ZOMBIE_VILLAGER, BabyMob.ZOMBIE_VILLAGER, () -> new ClassicZombieModels.ZombieVillager(ClassicMeshes.zombieVillager().bakeRoot()));
    }

    // ------------------------------------------------------------------ rabbit textures
    // The adult rabbit was remodelled in 26.1, so its texture no longer fits the old model.
    // The original 1.21.11 rabbit textures are bundled with this mod.

    private static Identifier rabbitTexture(LivingEntityRenderState state) {
        String name = "brown";
        if (state instanceof RabbitRenderState rabbitState) {
            if (rabbitState.isToast) {
                name = "toast";
            } else {
                name = switch (rabbitState.variant) {
                    case WHITE -> "white";
                    case BLACK -> "black";
                    case WHITE_SPLOTCHED -> "white_splotched";
                    case GOLD -> "gold";
                    case SALT -> "salt";
                    case EVIL -> "caerbannog";
                    default -> "brown";
                };
            }
        }
        return Identifier.fromNamespaceAndPath(MOD_ID, "textures/entity/rabbit/" + name + ".png");
    }
}
