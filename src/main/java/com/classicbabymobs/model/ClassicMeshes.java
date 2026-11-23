package com.classicbabymobs.model;

import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.animal.armadillo.AdultArmadilloModel;
import net.minecraft.client.model.animal.bee.AdultBeeModel;
import net.minecraft.client.model.animal.camel.AdultCamelModel;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.ColdChickenModel;
import net.minecraft.client.model.animal.cow.ColdCowModel;
import net.minecraft.client.model.animal.cow.CowModel;
import net.minecraft.client.model.animal.cow.WarmCowModel;
import net.minecraft.client.model.animal.dolphin.DolphinModel;
import net.minecraft.client.model.animal.equine.AbstractEquineModel;
import net.minecraft.client.model.animal.feline.AdultFelineModel;
import net.minecraft.client.model.animal.fox.AdultFoxModel;
import net.minecraft.client.model.animal.goat.GoatModel;
import net.minecraft.client.model.animal.llama.LlamaModel;
import net.minecraft.client.model.animal.panda.PandaModel;
import net.minecraft.client.model.animal.pig.ColdPigModel;
import net.minecraft.client.model.animal.pig.PigModel;
import net.minecraft.client.model.animal.sheep.SheepFurModel;
import net.minecraft.client.model.animal.sheep.SheepModel;
import net.minecraft.client.model.animal.sniffer.SnifferModel;
import net.minecraft.client.model.animal.squid.SquidModel;
import net.minecraft.client.model.animal.turtle.AdultTurtleModel;
import net.minecraft.client.model.animal.wolf.AdultWolfModel;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.MeshTransformer;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.monster.hoglin.HoglinModel;
import net.minecraft.client.model.monster.piglin.AbstractPiglinModel;
import net.minecraft.client.model.monster.piglin.AdultPiglinModel;
import net.minecraft.client.model.monster.strider.AdultStriderModel;
import net.minecraft.client.model.monster.zombie.DrownedModel;
import net.minecraft.client.model.monster.zombie.ZombieVillagerModel;
import net.minecraft.client.model.npc.VillagerModel;
import net.minecraft.client.renderer.entity.ArmorModelSet;

/**
 * The baby meshes exactly as Minecraft 1.21.11 (the last version before Tiny Takeover) built them:
 * the adult mesh run through that version's "baby transformer". Where 26.x still has an identical
 * adult mesh we reuse it; where the adult mesh changed, the 1.21.11 mesh is copied here.
 */
public final class ClassicMeshes {
    private ClassicMeshes() {
    }

    // ---- 1.21.11 baby transformers (values copied from that version) ----
    public static final MeshTransformer HUMANOID = new BabyModelTransform(true, 16.0f, 0.0f, 2.0f, 2.0f, 24.0f, Set.of("head"));
    public static final MeshTransformer COW = new BabyModelTransform(false, 8.0f, 6.0f, Set.of("head"));
    public static final MeshTransformer PIG = new BabyModelTransform(false, 4.0f, 4.0f, Set.of("head"));
    public static final MeshTransformer SHEEP = new BabyModelTransform(false, 8.0f, 4.0f, 2.0f, 2.0f, 24.0f, Set.of("head"));
    public static final MeshTransformer CHICKEN = new BabyModelTransform(false, 5.0f, 2.0f, 2.0f, 1.99f, 24.0f, Set.of("head", "beak", "red_thing"));
    public static final MeshTransformer FELINE = new BabyModelTransform(true, 10.0f, 4.0f, Set.of("head"));
    public static final MeshTransformer CAT_SCALE = MeshTransformer.scaling(0.8f);
    public static final MeshTransformer FOX = new BabyModelTransform(true, 8.0f, 3.35f, Set.of("head"));
    public static final MeshTransformer WOLF = new BabyModelTransform(Set.of("head"));
    public static final MeshTransformer GOAT = new BabyModelTransform(true, 19.0f, 1.0f, 2.5f, 2.0f, 24.0f, Set.of("head"));
    public static final MeshTransformer PANDA = new BabyModelTransform(true, 23.0f, 4.8f, 2.7f, 3.0f, 49.0f, Set.of("head"));
    public static final MeshTransformer POLAR_BEAR = new BabyModelTransform(true, 16.0f, 4.0f, 2.25f, 2.0f, 24.0f, Set.of("head"));
    public static final MeshTransformer TURTLE = new BabyModelTransform(true, 120.0f, 0.0f, 9.0f, 6.0f, 120.0f, Set.of("head"));
    public static final MeshTransformer RABBIT = new BabyModelTransform(true, 22.0f, 2.0f, 2.65f, 2.5f, 36.0f, Set.of("head", "left_ear", "right_ear", "nose"));
    public static final MeshTransformer HOGLIN = new BabyModelTransform(true, 8.0f, 6.0f, 1.9f, 2.0f, 24.0f, Set.of("head"));
    public static final MeshTransformer EQUINE = new BabyModelTransform(true, 16.2f, 1.36f, 2.7272f, 2.0f, 20.0f, Set.of("head_parts"));
    public static final MeshTransformer LLAMA = ClassicMeshes::llamaToBaby;
    public static final MeshTransformer HALF = MeshTransformer.scaling(0.5f);
    public static final MeshTransformer ARMADILLO = MeshTransformer.scaling(0.6f);
    public static final MeshTransformer CAMEL = MeshTransformer.scaling(0.45f);
    public static final MeshTransformer VILLAGER_LIKE = MeshTransformer.scaling(0.9375f);
    public static final MeshTransformer HUSK_SCALE = MeshTransformer.scaling(1.0625f);
    public static final MeshTransformer LIVING_HORSE_SCALE = MeshTransformer.scaling(1.1f);

    private static final CubeDeformation INNER_ARMOR = new CubeDeformation(0.5f);
    private static final CubeDeformation OUTER_ARMOR = new CubeDeformation(1.0f);

    // ---- Farm animals ----
    public static LayerDefinition cow() {
        return CowModel.createBodyLayer().apply(COW);
    }

    public static LayerDefinition warmCow() {
        return WarmCowModel.createBodyLayer().apply(COW);
    }

    public static LayerDefinition coldCow() {
        return ColdCowModel.createBodyLayer().apply(COW);
    }

    public static LayerDefinition pig() {
        return PigModel.createBodyLayer(CubeDeformation.NONE).apply(PIG);
    }

    public static LayerDefinition coldPig() {
        return ColdPigModel.createBodyLayer(CubeDeformation.NONE).apply(PIG);
    }

    public static LayerDefinition sheep() {
        return SheepModel.createBodyLayer().apply(SHEEP);
    }

    public static LayerDefinition sheepWool() {
        return SheepFurModel.createFurLayer().apply(SHEEP);
    }

    /** In 1.21.11 the baby undercoat was the sheep body mesh, scaled like the body. */
    public static LayerDefinition sheepUndercoat() {
        return SheepModel.createBodyLayer().apply(SHEEP);
    }

    public static LayerDefinition chicken() {
        return AdultChickenModel.createBodyLayer().apply(CHICKEN);
    }

    public static LayerDefinition coldChicken() {
        return ColdChickenModel.createBodyLayer().apply(CHICKEN);
    }

    public static LayerDefinition goat() {
        return GoatModel.createBodyLayer().apply(GOAT);
    }

    /** The adult rabbit was remodelled in 26.1, so this is the 1.21.11 rabbit mesh. */
    public static LayerDefinition rabbit() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition leftHaunch = root.addOrReplaceChild("left_haunch", CubeListBuilder.create().texOffs(30, 15).addBox(-1.0f, 0.0f, 0.0f, 2.0f, 4.0f, 5.0f), PartPose.offsetAndRotation(3.0f, 17.5f, 3.7f, -0.36651915f, 0.0f, 0.0f));
        PartDefinition rightHaunch = root.addOrReplaceChild("right_haunch", CubeListBuilder.create().texOffs(16, 15).addBox(-1.0f, 0.0f, 0.0f, 2.0f, 4.0f, 5.0f), PartPose.offsetAndRotation(-3.0f, 17.5f, 3.7f, -0.36651915f, 0.0f, 0.0f));
        leftHaunch.addOrReplaceChild("left_hind_foot", CubeListBuilder.create().texOffs(26, 24).addBox(-1.0f, 5.5f, -3.7f, 2.0f, 1.0f, 7.0f), PartPose.rotation(0.36651915f, 0.0f, 0.0f));
        rightHaunch.addOrReplaceChild("right_hind_foot", CubeListBuilder.create().texOffs(8, 24).addBox(-1.0f, 5.5f, -3.7f, 2.0f, 1.0f, 7.0f), PartPose.rotation(0.36651915f, 0.0f, 0.0f));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0f, -2.0f, -10.0f, 6.0f, 5.0f, 10.0f), PartPose.offsetAndRotation(0.0f, 19.0f, 8.0f, -0.34906584f, 0.0f, 0.0f));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(8, 15).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 7.0f, 2.0f), PartPose.offsetAndRotation(3.0f, 17.0f, -1.0f, -0.19198622f, 0.0f, 0.0f));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 15).addBox(-1.0f, 0.0f, -1.0f, 2.0f, 7.0f, 2.0f), PartPose.offsetAndRotation(-3.0f, 17.0f, -1.0f, -0.19198622f, 0.0f, 0.0f));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0).addBox(-2.5f, -4.0f, -5.0f, 5.0f, 4.0f, 5.0f), PartPose.offset(0.0f, 16.0f, -1.0f));
        head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(52, 0).addBox(-2.5f, -9.0f, -1.0f, 2.0f, 5.0f, 1.0f), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, -0.2617994f, 0.0f));
        head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(58, 0).addBox(0.5f, -9.0f, -1.0f, 2.0f, 5.0f, 1.0f), PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.2617994f, 0.0f));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(52, 6).addBox(-1.5f, -1.5f, 0.0f, 3.0f, 3.0f, 2.0f), PartPose.offsetAndRotation(0.0f, 20.0f, 7.0f, -0.3490659f, 0.0f, 0.0f));
        head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(32, 9).addBox(-0.5f, -2.5f, -5.5f, 1.0f, 1.0f, 1.0f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32).apply(RABBIT);
    }

    // ---- Mounts ----
    /** 1.21.11 AbstractEquineModel#createFullScaleBabyMesh: adult body with longer legs. */
    private static MeshDefinition equineFullScaleBabyMesh(CubeDeformation g) {
        MeshDefinition mesh = AbstractEquineModel.createBodyMesh(g);
        PartDefinition root = mesh.getRoot();
        CubeDeformation babyLegFudge = g.extend(0.0f, 5.5f, 0.0f);
        root.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, babyLegFudge), PartPose.offset(4.0f, 14.0f, 7.0f));
        root.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0f, -1.01f, -1.0f, 4.0f, 11.0f, 4.0f, babyLegFudge), PartPose.offset(-4.0f, 14.0f, 7.0f));
        root.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(48, 21).mirror().addBox(-3.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, babyLegFudge), PartPose.offset(4.0f, 14.0f, -10.0f));
        root.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(48, 21).addBox(-1.0f, -1.01f, -1.9f, 4.0f, 11.0f, 4.0f, babyLegFudge), PartPose.offset(-4.0f, 14.0f, -10.0f));
        return mesh;
    }

    /** Baby horse (the living horse was drawn 10% bigger than undead horses). */
    public static LayerDefinition horse() {
        return LayerDefinition.create(equineFullScaleBabyMesh(CubeDeformation.NONE), 64, 64).apply(EQUINE).apply(LIVING_HORSE_SCALE);
    }

    /** Baby skeleton horse and zombie horse. */
    public static LayerDefinition undeadHorse() {
        return LayerDefinition.create(equineFullScaleBabyMesh(CubeDeformation.NONE), 64, 64).apply(EQUINE);
    }

    /** 1.21.11 DonkeyModel#createBabyLayer; donkeys use 0.87, mules 0.92. */
    public static LayerDefinition donkey(float scale) {
        return LayerDefinition.create(equineFullScaleBabyMesh(CubeDeformation.NONE), 64, 64)
                .apply(ClassicMeshes::addDonkeyParts)
                .apply(EQUINE)
                .apply(MeshTransformer.scaling(scale));
    }

    private static MeshDefinition addDonkeyParts(MeshDefinition mesh) {
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.getChild("body");
        CubeListBuilder chest = CubeListBuilder.create().texOffs(26, 21).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 8.0f, 3.0f);
        body.addOrReplaceChild("left_chest", chest, PartPose.offsetAndRotation(6.0f, -8.0f, 0.0f, 0.0f, -1.5707964f, 0.0f));
        body.addOrReplaceChild("right_chest", chest, PartPose.offsetAndRotation(-6.0f, -8.0f, 0.0f, 0.0f, 1.5707964f, 0.0f));
        PartDefinition head = root.getChild("head_parts").getChild("head");
        CubeListBuilder ear = CubeListBuilder.create().texOffs(0, 12).addBox(-1.0f, -7.0f, 0.0f, 2.0f, 7.0f, 1.0f);
        head.addOrReplaceChild("left_ear", ear, PartPose.offsetAndRotation(1.25f, -10.0f, 4.0f, 0.2617994f, 0.0f, 0.2617994f));
        head.addOrReplaceChild("right_ear", ear, PartPose.offsetAndRotation(-1.25f, -10.0f, 4.0f, 0.2617994f, 0.0f, -0.2617994f));
        return mesh;
    }

    public static LayerDefinition camel() {
        return AdultCamelModel.createBodyLayer().apply(CAMEL);
    }

    public static LayerDefinition llama() {
        return LlamaModel.createBodyLayer(CubeDeformation.NONE).apply(LLAMA);
    }

    /** Carpet / trader-llama blanket layer for babies, as in 1.21.11. */
    public static LayerDefinition llamaDecor() {
        return LlamaModel.createBodyLayer(new CubeDeformation(0.5f)).apply(LLAMA);
    }

    /** 1.21.11 LlamaModel#transformToBaby. */
    private static MeshDefinition llamaToBaby(MeshDefinition mesh) {
        UnaryOperator<PartPose> headTransform = p -> p.translated(0.0f, 21.0f, 3.52f).scaled(0.71428573f, 0.64935064f, 0.7936508f);
        UnaryOperator<PartPose> bodyTransform = p -> p.translated(0.0f, 33.0f, 0.0f).scaled(0.625f, 0.45454544f, 0.45454544f);
        UnaryOperator<PartPose> defaultTransform = p -> p.translated(0.0f, 33.0f, 0.0f).scaled(0.45454544f, 0.41322312f, 0.45454544f);
        MeshDefinition babyMesh = new MeshDefinition();
        for (Map.Entry<String, PartDefinition> entry : mesh.getRoot().getChildren()) {
            String name = entry.getKey();
            PartDefinition part = entry.getValue();
            UnaryOperator<PartPose> transform = switch (name) {
                case "head" -> headTransform;
                case "body" -> bodyTransform;
                default -> defaultTransform;
            };
            babyMesh.getRoot().addOrReplaceChild(name, part.transformed(transform));
        }
        return babyMesh;
    }

    public static LayerDefinition strider() {
        return AdultStriderModel.createBodyLayer().apply(HALF);
    }

    // ---- Pets ----
    public static LayerDefinition cat() {
        return LayerDefinition.create(AdultFelineModel.createBodyMesh(CubeDeformation.NONE), 64, 32).apply(FELINE).apply(CAT_SCALE);
    }

    public static LayerDefinition catCollar() {
        return LayerDefinition.create(AdultFelineModel.createBodyMesh(new CubeDeformation(0.01f)), 64, 32).apply(FELINE).apply(CAT_SCALE);
    }

    public static LayerDefinition ocelot() {
        return LayerDefinition.create(AdultFelineModel.createBodyMesh(CubeDeformation.NONE), 64, 32).apply(FELINE);
    }

    public static LayerDefinition wolf() {
        return LayerDefinition.create(AdultWolfModel.createBodyLayer(CubeDeformation.NONE), 64, 32).apply(WOLF);
    }

    public static LayerDefinition fox() {
        return AdultFoxModel.createBodyLayer().apply(FOX);
    }

    // ---- Wild animals ----
    public static LayerDefinition armadillo() {
        return AdultArmadilloModel.createBodyLayer().apply(ARMADILLO);
    }

    public static LayerDefinition bee() {
        return AdultBeeModel.createBodyLayer().apply(HALF);
    }

    public static LayerDefinition panda() {
        return PandaModel.createBodyLayer().apply(PANDA);
    }

    /** 1.21.11 PolarBearModel#createBodyLayer(true): baby transform first, then the 1.2x bear scale. */
    public static LayerDefinition polarBear() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5f, -3.0f, -3.0f, 7.0f, 7.0f, 7.0f).texOffs(0, 44).addBox("mouth", -2.5f, 1.0f, -6.0f, 5.0f, 3.0f, 3.0f).texOffs(26, 0).addBox("right_ear", -4.5f, -4.0f, -1.0f, 2.0f, 2.0f, 1.0f).texOffs(26, 0).mirror().addBox("left_ear", 2.5f, -4.0f, -1.0f, 2.0f, 2.0f, 1.0f), PartPose.offset(0.0f, 10.0f, -16.0f));
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-5.0f, -13.0f, -7.0f, 14.0f, 14.0f, 11.0f).texOffs(39, 0).addBox(-4.0f, -25.0f, -7.0f, 12.0f, 12.0f, 10.0f), PartPose.offsetAndRotation(-2.0f, 9.0f, 12.0f, 1.5707964f, 0.0f, 0.0f));
        CubeListBuilder hindLeg = CubeListBuilder.create().texOffs(50, 22).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 8.0f);
        root.addOrReplaceChild("right_hind_leg", hindLeg, PartPose.offset(-4.5f, 14.0f, 6.0f));
        root.addOrReplaceChild("left_hind_leg", hindLeg, PartPose.offset(4.5f, 14.0f, 6.0f));
        CubeListBuilder frontLeg = CubeListBuilder.create().texOffs(50, 40).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 6.0f);
        root.addOrReplaceChild("right_front_leg", frontLeg, PartPose.offset(-3.5f, 14.0f, -8.0f));
        root.addOrReplaceChild("left_front_leg", frontLeg, PartPose.offset(3.5f, 14.0f, -8.0f));
        return LayerDefinition.create(mesh, 128, 64).apply(POLAR_BEAR).apply(MeshTransformer.scaling(1.2f));
    }

    public static LayerDefinition sniffer() {
        return SnifferModel.createBodyLayer().apply(HALF);
    }

    // ---- Water mobs ----
    /** 1.21.11 axolotl mesh (the 26.x adult body sits half a pixel lower). */
    public static LayerDefinition axolotl() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 11).addBox(-4.0f, -2.0f, -9.0f, 8.0f, 4.0f, 10.0f).texOffs(2, 17).addBox(0.0f, -3.0f, -8.0f, 0.0f, 5.0f, 9.0f), PartPose.offset(0.0f, 20.0f, 5.0f));
        CubeDeformation fudge = new CubeDeformation(0.001f);
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 1).addBox(-4.0f, -3.0f, -5.0f, 8.0f, 5.0f, 5.0f, fudge), PartPose.offset(0.0f, 0.0f, -9.0f));
        CubeListBuilder topGills = CubeListBuilder.create().texOffs(3, 37).addBox(-4.0f, -3.0f, 0.0f, 8.0f, 3.0f, 0.0f, fudge);
        CubeListBuilder leftGills = CubeListBuilder.create().texOffs(0, 40).addBox(-3.0f, -5.0f, 0.0f, 3.0f, 7.0f, 0.0f, fudge);
        CubeListBuilder rightGills = CubeListBuilder.create().texOffs(11, 40).addBox(0.0f, -5.0f, 0.0f, 3.0f, 7.0f, 0.0f, fudge);
        head.addOrReplaceChild("top_gills", topGills, PartPose.offset(0.0f, -3.0f, -1.0f));
        head.addOrReplaceChild("left_gills", leftGills, PartPose.offset(-4.0f, 0.0f, -1.0f));
        head.addOrReplaceChild("right_gills", rightGills, PartPose.offset(4.0f, 0.0f, -1.0f));
        CubeListBuilder leftLeg = CubeListBuilder.create().texOffs(2, 13).addBox(-1.0f, 0.0f, 0.0f, 3.0f, 5.0f, 0.0f, fudge);
        CubeListBuilder rightLeg = CubeListBuilder.create().texOffs(2, 13).addBox(-2.0f, 0.0f, 0.0f, 3.0f, 5.0f, 0.0f, fudge);
        body.addOrReplaceChild("right_hind_leg", rightLeg, PartPose.offset(-3.5f, 1.0f, -1.0f));
        body.addOrReplaceChild("left_hind_leg", leftLeg, PartPose.offset(3.5f, 1.0f, -1.0f));
        body.addOrReplaceChild("right_front_leg", rightLeg, PartPose.offset(-3.5f, 1.0f, -8.0f));
        body.addOrReplaceChild("left_front_leg", leftLeg, PartPose.offset(3.5f, 1.0f, -8.0f));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(2, 19).addBox(0.0f, -3.0f, 0.0f, 0.0f, 5.0f, 12.0f), PartPose.offset(0.0f, 0.0f, 1.0f));
        return LayerDefinition.create(mesh, 64, 64).apply(HALF);
    }

    public static LayerDefinition dolphin() {
        return DolphinModel.createBodyLayer().apply(HALF);
    }

    public static LayerDefinition squid() {
        return SquidModel.createBodyLayer().apply(HALF);
    }

    public static LayerDefinition turtle() {
        return AdultTurtleModel.createBodyLayer().apply(TURTLE);
    }

    // ---- Nether mobs ----
    /** 1.21.11 HoglinModel#createBabyLayer: the mane sat further back on babies. */
    public static LayerDefinition hoglin() {
        return HoglinModel.createBodyLayer().apply(mesh -> {
            PartDefinition body = mesh.getRoot().getChild("body");
            body.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(90, 33).addBox(0.0f, 0.0f, -9.0f, 0.0f, 10.0f, 19.0f, new CubeDeformation(0.001f)), PartPose.offset(0.0f, -14.0f, -3.0f));
            return mesh;
        }).apply(HOGLIN);
    }

    public static LayerDefinition piglin() {
        return AdultPiglinModel.createBodyLayer().apply(HUMANOID);
    }

    public static ArmorModelSet<LayerDefinition> piglinArmor() {
        return AbstractPiglinModel.createArmorMeshSet(INNER_ARMOR, new CubeDeformation(1.02f))
                .map(mesh -> LayerDefinition.create(mesh, 64, 32))
                .map(layer -> layer.apply(HUMANOID));
    }

    // ---- Villagers and zombies ----
    public static LayerDefinition villager() {
        return LayerDefinition.create(VillagerModel.createBodyModel(), 64, 64).apply(VILLAGER_LIKE).apply(HALF);
    }

    public static LayerDefinition villagerNoHat() {
        return LayerDefinition.create(VillagerModel.createNoHatModel(), 64, 64).apply(VILLAGER_LIKE).apply(HALF);
    }

    public static LayerDefinition zombie() {
        return LayerDefinition.create(HumanoidModel.createMesh(CubeDeformation.NONE, 0.0f), 64, 64).apply(HUMANOID);
    }

    public static LayerDefinition husk() {
        return zombie().apply(HUSK_SCALE);
    }

    public static ArmorModelSet<LayerDefinition> humanoidArmor() {
        return HumanoidModel.createArmorMeshSet(INNER_ARMOR, OUTER_ARMOR)
                .map(mesh -> LayerDefinition.create(mesh, 64, 32))
                .map(layer -> layer.apply(HUMANOID));
    }

    public static ArmorModelSet<LayerDefinition> huskArmor() {
        return humanoidArmor().map(layer -> layer.apply(HUSK_SCALE));
    }

    public static LayerDefinition drowned() {
        return DrownedModel.createBodyLayer(CubeDeformation.NONE).apply(HUMANOID);
    }

    public static LayerDefinition drownedOuter() {
        return DrownedModel.createBodyLayer(new CubeDeformation(0.25f)).apply(HUMANOID);
    }

    public static LayerDefinition zombieVillager() {
        return ZombieVillagerModel.createBodyLayer().apply(HUMANOID);
    }

    public static LayerDefinition zombieVillagerNoHat() {
        return ZombieVillagerModel.createNoHatLayer().apply(HUMANOID);
    }

    public static ArmorModelSet<LayerDefinition> zombieVillagerArmor() {
        return ZombieVillagerModel.createArmorLayerSet(INNER_ARMOR, OUTER_ARMOR).map(layer -> layer.apply(HUMANOID));
    }
}
