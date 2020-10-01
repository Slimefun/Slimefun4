package io.github.thebusybiscuit.slimefun4.implementation.guide;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.categories.FlexCategory;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.itemstack.SlimefunGuideItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This is an admin-variant of the {@link ChestSlimefunGuide} which allows a {@link Player}
 * to spawn in a {@link SlimefunItem} via click rather than showing their {@link Recipe}.
 * 
 * @author TheBusyBiscuit
 *
 */
public class CheatSheetSlimefunGuide extends ChestSlimefunGuide {

    private final ItemStack item;

    public CheatSheetSlimefunGuide() {
        super(false);

        item = new SlimefunGuideItem(this, "&cSlimefun Guide &4(Cheat Sheet)");
    }

    @Override
    public boolean isSurvivalMode() {
        return false;
    }

    @Override
    protected List<Category> getVisibleCategories(Player p, PlayerProfile profile) {
        List<Category> categories = new LinkedList<>();

        for (Category category : SlimefunPlugin.getRegistry().getCategories()) {
            if (!(category instanceof FlexCategory)) {
                categories.add(category);
            }
        }

        return categories;
    }

    @Override
    public SlimefunGuideLayout getLayout() {
        return SlimefunGuideLayout.CHEAT_SHEET;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    protected void createHeader(Player p, PlayerProfile profile, ChestMenu menu) {
        super.createHeader(p, profile, menu);

        // Remove Settings Panel
        menu.addItem(1, ChestMenuUtils.getBackground());
        menu.addMenuClickHandler(1, ChestMenuUtils.getEmptyClickHandler());
    }
}
