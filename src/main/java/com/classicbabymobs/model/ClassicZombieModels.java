package com.classicbabymobs.model;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.piglin.AdultZombifiedPiglinModel;
import net.minecraft.client.model.monster.zombie.DrownedModel;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.model.monster.zombie.ZombieVillagerModel;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.client.renderer.entity.state.ZombieVillagerRenderState;
import net.minecraft.client.renderer.entity.state.ZombifiedPiglinRenderState;

/**
 * Classic baby zombie-type models. Since 26.1 a baby zombie that holds an item lowers its arms;
 * in 1.21.11 babies always held their arms out like adults. These models run the arm animation
 * as an adult would, so the old pose comes back.
 */
public final class ClassicZombieModels {
    private ClassicZombieModels() {
    }

    public static class Zombie extends ZombieModel<ZombieRenderState> {
        public Zombie(ModelPart root) {
            super(root);
        }

        @Override
        public void setupAnim(ZombieRenderState state) {
            boolean baby = state.isBaby;
            state.isBaby = false;
            try {
                super.setupAnim(state);
            } finally {
                state.isBaby = baby;
            }
        }
    }

    public static class Drowned extends DrownedModel {
        public Drowned(ModelPart root) {
            super(root);
        }

        @Override
        public void setupAnim(ZombieRenderState state) {
            boolean baby = state.isBaby;
            state.isBaby = false;
            try {
                super.setupAnim(state);
            } finally {
                state.isBaby = baby;
            }
        }
    }

    public static class ZombieVillager extends ZombieVillagerModel<ZombieVillagerRenderState> {
        public ZombieVillager(ModelPart root) {
            super(root);
        }

        @Override
        public void setupAnim(ZombieVillagerRenderState state) {
            boolean baby = state.isBaby;
            state.isBaby = false;
            try {
                super.setupAnim(state);
            } finally {
                state.isBaby = baby;
            }
        }
    }

    public static class ZombifiedPiglin extends AdultZombifiedPiglinModel {
        public ZombifiedPiglin(ModelPart root) {
            super(root);
        }

        @Override
        public void setupAnim(ZombifiedPiglinRenderState state) {
            boolean baby = state.isBaby;
            state.isBaby = false;
            try {
                super.setupAnim(state);
            } finally {
                state.isBaby = baby;
            }
        }
    }
}
