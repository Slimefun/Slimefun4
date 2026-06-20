package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

/**
 * Per-item wiki screen: shows the item's icon, an authored/auto-generated explanation,
 * optional energy stats, a "view recipe" shortcut into the survival guide, and the items
 * that consume this item in their recipe ("used in").
 *
 * Slot layout (9x6 = 54):
 * <pre>
 *   0   Back button (sits on the top-left border corner)
 *   4   Header: the item's own icon
 *   19  Description (writable book)
 *   22  Stats (only when the item exposes energy data)
 *   25  View recipe (crafting table)
 *   28-34  "Used in" consumer icons (up to 7; a "+X more" note caps the row)
 * </pre>
 */
public final class WikiPage {

    private static final int[] BORDER = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private static final int HEADER_SLOT = 4;
    private static final int DESCRIPTION_SLOT = 19;
    private static final int STATS_SLOT = 22;
    private static final int RECIPE_SLOT = 25;
    private static final int BACK_SLOT = 0;

    private static final int USED_IN_START = 28;
    private static final int USED_IN_END = 34;
    private static final int USED_IN_CAPACITY = USED_IN_END - USED_IN_START + 1;

    /** Shared reverse-recipe index for the JVM lifetime; the first lookup builds it lazily. */
    private static final ReverseRecipeIndex REVERSE_INDEX = new ReverseRecipeIndex();

    private WikiPage() {}

    /** Pre-builds the shared reverse-recipe index so the first wiki click is instant. */
    public static void warmUpIndex() {
        REVERSE_INDEX.warmUp();
    }

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull SlimefunItem item) {
        ChestMenu menu = new ChestMenu("Wiki: " + item.getItemName());
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        addBackButton(menu, p, guide);
        addHeader(menu, item);
        addDescription(menu, item);
        addStats(menu, item);
        addRecipeButton(menu, p, item);
        addUsedIn(menu, p, guide, item);

        menu.open(p);
    }

    private static void addBackButton(@Nonnull ChestMenu menu, @Nonnull Player p, @Nonnull ItemStack guide) {
        menu.addItem(BACK_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back"));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            WikiIndex.open(pl, guide);
            return false;
        });
    }

    private static void addHeader(@Nonnull ChestMenu menu, @Nonnull SlimefunItem item) {
        ItemStack display = item.getItem();

        if (display == null || display.getType() == org.bukkit.Material.AIR) {
            display = MaterialCompat.stack(XMaterial.BARRIER);
        }

        menu.addItem(HEADER_SLOT, display);
        menu.addMenuClickHandler(HEADER_SLOT, ChestMenuUtils.getEmptyClickHandler());
    }

    private static void addDescription(@Nonnull ChestMenu menu, @Nonnull SlimefunItem item) {
        List<String> lore = Slimefun.getWikiText().get(item);
        menu.addItem(DESCRIPTION_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.WRITABLE_BOOK), "&eDescription", lore.toArray(new String[0])));
        menu.addMenuClickHandler(DESCRIPTION_SLOT, ChestMenuUtils.getEmptyClickHandler());
    }

    /** Adds an energy stats item only when the item exposes energy data; otherwise leaves the slot empty. */
    private static void addStats(@Nonnull ChestMenu menu, @Nonnull SlimefunItem item) {
        if (!(item instanceof EnergyNetComponent)) {
            return;
        }

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("&7Capacity: &e" + ((EnergyNetComponent) item).getCapacity() + " J");

        if (item instanceof AContainer) {
            AContainer container = (AContainer) item;
            lore.add("&7Energy: &e" + container.getEnergyConsumption() + " J/tick");
            lore.add("&7Speed: &e" + container.getSpeed() + "x");
        }

        menu.addItem(STATS_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.REDSTONE), "&6Stats", lore.toArray(new String[0])));
        menu.addMenuClickHandler(STATS_SLOT, ChestMenuUtils.getEmptyClickHandler());
    }

    private static void addRecipeButton(@Nonnull ChestMenu menu, @Nonnull Player p, @Nonnull SlimefunItem item) {
        menu.addItem(RECIPE_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.CRAFTING_TABLE), "&aView recipe"));
        menu.addMenuClickHandler(RECIPE_SLOT, (pl, slot, clicked, action) -> {
            PlayerProfile.get(pl, profile -> Slimefun.runSync(() -> SlimefunGuide.displayItem(profile, item, true)));
            return false;
        });
    }

    /** Fills the "used in" row with consumer icons, capping the row with a "+X more" note when needed. */
    private static void addUsedIn(@Nonnull ChestMenu menu, @Nonnull Player p, @Nonnull ItemStack guide, @Nonnull SlimefunItem item) {
        List<SlimefunItem> consumers = REVERSE_INDEX.getConsumers(item);

        if (consumers.isEmpty()) {
            menu.addItem(USED_IN_START, CustomItemStack.create(MaterialCompat.stack(XMaterial.BARRIER), "&7Not used in any recipe"));
            menu.addMenuClickHandler(USED_IN_START, ChestMenuUtils.getEmptyClickHandler());
            return;
        }

        // Reserve the last slot for an overflow note when there are more consumers than fit.
        boolean overflow = consumers.size() > USED_IN_CAPACITY;
        int shown = overflow ? USED_IN_CAPACITY - 1 : consumers.size();

        for (int i = 0; i < shown; i++) {
            SlimefunItem consumer = consumers.get(i);
            int slot = USED_IN_START + i;

            menu.addItem(slot, consumer.getItem());
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                open(pl, guide, consumer);
                return false;
            });
        }

        if (overflow) {
            int remaining = consumers.size() - shown;
            menu.addItem(USED_IN_END, CustomItemStack.create(MaterialCompat.stack(XMaterial.PAPER), "&7+" + remaining + " more"));
            menu.addMenuClickHandler(USED_IN_END, ChestMenuUtils.getEmptyClickHandler());
        }
    }
}
