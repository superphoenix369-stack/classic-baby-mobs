# Classic Baby Mobs (Fabric, Minecraft Java 26.3)

Client-side Fabric mod that restores the pre-Tiny Takeover (1.21.11) look of baby mobs, chosen per mob in an options screen. See `MODRINTH.md` for the user-facing description.

## Building the jar

The easiest way needs no setup on your computer:

1. Create a new repository on GitHub and upload the contents of this folder, including the hidden `.github` folder.
2. Open the repository's **Actions** tab. The `build` workflow runs automatically. If it doesn't, choose it and click **Run workflow**.
3. When it finishes, open the run and download **classic-baby-mobs** under *Artifacts*. Inside is `classic-baby-mobs-1.0.0+26.3.jar`, the file you upload to Modrinth.

To build locally instead, install JDK 25 and run `./gradlew build` (on Windows, `gradlew.bat build`). The jar appears in `build/libs/`.

## How it works

- `ClassicBabies` maps each entity type to its classic baby model, built from the 1.21.11 meshes in `model/ClassicMeshes`.
- `LivingEntityRendererMixin` swaps the model in while a classic baby is drawn, looks up the texture as if the mob were an adult, and tells certain render layers to use classic layer models.
- Small mixins handle layers with their own baby models (wool, undercoat, collars, llama blankets, drowned outer layer, armor, villager outfits), held-item placement, and the cat and wolf textures.
- `ClassicConfigScreen` is the options screen. It is built from vanilla option widgets and opens from Mod Menu or a key binding.
- `src/main/java/com/terraformersmc/modmenu/api` is a compile-only copy of the two Mod Menu interfaces used. It is excluded from the jar.

## Credits and license

Code: MIT (see `LICENSE`). Model geometry values come from Minecraft 1.21.11. The bundled rabbit textures in `assets/classic_baby_mobs/textures/entity/rabbit` are Mojang's original 1.21.11 textures. They are needed because Tiny Takeover also replaced the adult rabbit. Minecraft assets belong to Mojang Studios / Microsoft. Not an official Minecraft product.
