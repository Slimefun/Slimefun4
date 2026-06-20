package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Screen 3: a paginated list of the repo's branches. Branches are fetched async, then the menu is
 * rendered on the main thread. Selecting a branch starts the build via {@link AddonInstaller}.
 */
public final class BranchSelectMenu {

    private BranchSelectMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull AddonCatalog.Entry entry, int page) {
        // Fetch branches off the main thread, then render.
        Slimefun.instance().getServer().getScheduler().runTaskAsynchronously(Slimefun.instance(), () -> {
            BranchService service = new BranchService();
            List<String> branches = service.fetchBranches(entry);
            Slimefun.instance().getServer().getScheduler().runTask(Slimefun.instance(), () -> render(p, guide, entry, branches, page));
        });
    }

    private static void render(Player p, ItemStack guide, AddonCatalog.Entry entry, List<String> branches, int page) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, 0, 2, 3, 4, 5, 6, 7, 8, 45, 47, 48, 49, 50, 51, 53);

        menu.addItem(1, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            AddonDetailMenu.open(pl, guide, entry);
            return false;
        });

        if (branches.isEmpty()) {
            menu.addItem(22, CustomItemStack.create(MaterialCompat.stack(XMaterial.BARRIER), "&cNo branches found", "", "&7Couldn't reach GitHub or the repo has no branches."));
            menu.addMenuClickHandler(22, ChestMenuUtils.getEmptyClickHandler());
            menu.open(p);
            return;
        }

        for (int i = page * 36; i < branches.size() && i < (page + 1) * 36; i++) {
            String branch = branches.get(i);
            menu.addItem(i - page * 36 + 9, CustomItemStack.create(MaterialCompat.stack(XMaterial.WHEAT_SEEDS), "&a" + branch, "", "&7⇨ &eClick to build this branch"));
            menu.addMenuClickHandler(i - page * 36 + 9, (pl, slot, item, action) -> {
                inst().buildFromBranch(pl, entry, branch, System.currentTimeMillis());
                pl.closeInventory();
                return false;
            });
        }

        int pages = (branches.size() - 1) / 36 + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page + 1, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            if (page > 0) {
                render(pl, guide, entry, branches, page - 1);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page + 1, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            if (page + 1 < pages) {
                render(pl, guide, entry, branches, page + 1);
            }

            return false;
        });

        menu.open(p);
    }

    private static AddonInstaller inst() {
        return AddonInstallerMenu.installer();
    }
}
