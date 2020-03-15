package io.github.thebusybiscuit.slimefun4.implementation.guide;

import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;

public class CheatSheetSlimefunGuide extends ChestSlimefunGuide {

    public CheatSheetSlimefunGuide(boolean showVanillaRecipes) {
        super(showVanillaRecipes);
    }

    @Override
    public SlimefunGuideLayout getLayout() {
        return SlimefunGuideLayout.CHEAT_SHEET;
    }
}
