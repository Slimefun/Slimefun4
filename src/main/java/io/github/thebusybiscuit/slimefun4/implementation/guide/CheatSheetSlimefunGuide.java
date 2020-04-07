package io.github.thebusybiscuit.slimefun4.implementation.guide;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.entity.Player;

public class CheatSheetSlimefunGuide extends ChestSlimefunGuide {

    public CheatSheetSlimefunGuide() {
        super(false);
    }

    @Override
    public SlimefunGuideLayout getLayout() {
        return SlimefunGuideLayout.CHEAT_SHEET;
    }

    @Override
    protected boolean isSurvivalMode() {
        return false;
    }

    @Override
    protected void createHeader(Player p, PlayerProfile profile, ChestMenu menu) {
        super.createHeader(p, profile, menu);

        // Remove Settings Panel
        menu.addItem(1, ChestMenuUtils.getBackground());
        menu.addMenuClickHandler(1, ChestMenuUtils.getEmptyClickHandler());
    }
}