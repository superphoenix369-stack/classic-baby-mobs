package com.classicbabymobs.screen;

import com.classicbabymobs.BabyMob;
import com.classicbabymobs.ClassicConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

/**
 * One toggle per baby mob: "Classic" (pre-Tiny Takeover) or "New" (26.x).
 * Changes apply immediately, so you can open this from the pause menu and watch babies change.
 */
public class ClassicConfigScreen extends OptionsSubScreen {
    private static final Component TITLE = Component.translatable("classic_baby_mobs.title");
    private static final Component CLASSIC = Component.translatable("classic_baby_mobs.value.classic");
    private static final Component NEW = Component.translatable("classic_baby_mobs.value.new");

    public ClassicConfigScreen(@Nullable Screen lastScreen) {
        super(lastScreen, Minecraft.getInstance().options, TITLE);
    }

    @Override
    protected void addOptions() {
        if (this.list == null) {
            return;
        }
        this.list.addBig(OptionInstance.createBoolean(
                "classic_baby_mobs.option.enabled",
                value -> Tooltip.create(Component.translatable("classic_baby_mobs.option.enabled.tooltip")),
                ClassicConfig.isEnabled(),
                value -> ClassicConfig.setEnabled(value)
        ));

        for (BabyMob.Group group : BabyMob.Group.values()) {
            this.list.addHeader(Component.translatable(group.translationKey()));
            List<OptionInstance<?>> options = new ArrayList<>();
            for (BabyMob mob : BabyMob.values()) {
                if (mob.group() == group) {
                    options.add(mobOption(mob));
                }
            }
            this.list.addSmall(options.toArray(new OptionInstance<?>[0]));
        }
    }

    private static OptionInstance<Boolean> mobOption(BabyMob mob) {
        return OptionInstance.createBoolean(
                mob.translationKey(),
                value -> Tooltip.create(Component.translatable(value
                        ? "classic_baby_mobs.value.classic.tooltip"
                        : "classic_baby_mobs.value.new.tooltip")),
                (caption, value) -> Options.genericValueLabel(caption, value ? CLASSIC : NEW),
                ClassicConfig.getMob(mob),
                value -> ClassicConfig.setMob(mob, value)
        );
    }

    @Override
    protected void addFooter() {
        LinearLayout footer = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        footer.addChild(Button.builder(Component.translatable("classic_baby_mobs.button.all_classic"), button -> {
            ClassicConfig.setAll(true);
            this.rebuildWidgets();
        }).width(100).build());
        footer.addChild(Button.builder(Component.translatable("classic_baby_mobs.button.all_new"), button -> {
            ClassicConfig.setAll(false);
            this.rebuildWidgets();
        }).width(100).build());
        footer.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(100).build());
    }

    @Override
    public void removed() {
        ClassicConfig.save();
        super.removed();
    }
}
