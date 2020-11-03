package io.github.thebusybiscuit.slimefun4.utils;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public final class ChestMenuUtils {

    private ChestMenuUtils() {}

    private static final ItemStack UI_BACKGROUND = new SlimefunItemStack("_UI_BACKGROUND", Material.GRAY_STAINED_GLASS_PANE, " ");
    private static final ItemStack INPUT_SLOT = new SlimefunItemStack("_UI_INPUT_SLOT", Material.CYAN_STAINED_GLASS_PANE, " ");
    private static final ItemStack OUTPUT_SLOT = new SlimefunItemStack("_UI_OUTPUT_SLOT", Material.ORANGE_STAINED_GLASS_PANE, " ");

    private static final ItemStack NO_PERMISSION = new SlimefunItemStack("_UI_NO_PERMISSION", Material.BARRIER, "No Permission");
    private static final ItemStack NOT_RESEARCHED = new SlimefunItemStack("_UI_NOT_RESEARCHED", Material.BARRIER, "Not researched");

    private static final ItemStack BACK_BUTTON = new SlimefunItemStack("_UI_BACK", Material.ENCHANTED_BOOK, "&7\u21E6 Back", meta -> meta.addItemFlags(ItemFlag.HIDE_ENCHANTS));
    private static final ItemStack MENU_BUTTON = new SlimefunItemStack("_UI_MENU", Material.COMPARATOR, "&eSettings / Info", "", "&7\u21E8 Click to see more");
    private static final ItemStack SEARCH_BUTTON = new SlimefunItemStack("_UI_SEARCH", Material.NAME_TAG, "&bSearch");
    private static final ItemStack WIKI_BUTTON = new SlimefunItemStack("_UI_WIKI", Material.KNOWLEDGE_BOOK, "&3Slimefun Wiki");

    private static final ItemStack PREV_BUTTON_ACTIVE = new SlimefunItemStack("_UI_PREVIOUS_ACTIVE", Material.LIME_STAINED_GLASS_PANE, "&r\u21E6 Previous Page");
    private static final ItemStack NEXT_BUTTON_ACTIVE = new SlimefunItemStack("_UI_NEXT_ACTIVE", Material.LIME_STAINED_GLASS_PANE, "&rNext Page \u21E8");
    private static final ItemStack PREV_BUTTON_INACTIVE = new SlimefunItemStack("_UI_PREVIOUS_INACTIVE", Material.BLACK_STAINED_GLASS_PANE, "&8\u21E6 Previous Page");
    private static final ItemStack NEXT_BUTTON_INACTIVE = new SlimefunItemStack("_UI_NEXT_INACTIVE", Material.BLACK_STAINED_GLASS_PANE, "&8Next Page \u21E8");

    private static final MenuClickHandler CLICK_HANDLER = (p, s, i, a) -> false;

    @Nonnull
    public static ItemStack getBackground() {
        return UI_BACKGROUND;
    }

    @Nonnull
    public static ItemStack getNoPermissionItem() {
        return NO_PERMISSION;
    }

    @Nonnull
    public static ItemStack getNotResearchedItem() {
        return NOT_RESEARCHED;
    }

    @Nonnull
    public static ItemStack getInputSlotTexture() {
        return INPUT_SLOT;
    }

    @Nonnull
    public static ItemStack getOutputSlotTexture() {
        return OUTPUT_SLOT;
    }

    @Nonnull
    public static MenuClickHandler getEmptyClickHandler() {
        return CLICK_HANDLER;
    }

    @Nonnull
    public static ItemStack getBackButton(@Nonnull Player p, String... lore) {
        return new CustomItem(BACK_BUTTON, "&7\u21E6 " + SlimefunPlugin.getLocalization().getMessage(p, "guide.back.title"), lore);
    }

    @Nonnull
    public static ItemStack getMenuButton(@Nonnull Player p) {
        return new CustomItem(MENU_BUTTON, ChatColor.YELLOW + SlimefunPlugin.getLocalization().getMessage(p, "guide.title.settings"), "", "&7\u21E8 " + SlimefunPlugin.getLocalization().getMessage(p, "guide.tooltips.open-category"));
    }

    @Nonnull
    public static ItemStack getSearchButton(@Nonnull Player p) {
        return new CustomItem(SEARCH_BUTTON, meta -> {
            meta.setDisplayName(ChatColors.color(SlimefunPlugin.getLocalization().getMessage(p, "guide.search.name")));

            List<String> lore = Arrays.asList("", ChatColor.GRAY + "\u21E8 " + SlimefunPlugin.getLocalization().getMessage(p, "guide.search.tooltip"));
            lore.replaceAll(ChatColors::color);
            meta.setLore(lore);
        });
    }

    @Nonnull
    public static ItemStack getWikiButton() {
        return WIKI_BUTTON;
    }

    @Nonnull
    public static ItemStack getPreviousButton(@Nonnull Player p, int page, int pages) {
        if (pages == 1 || page == 1) {
            return new CustomItem(PREV_BUTTON_INACTIVE, meta -> {
                meta.setDisplayName(ChatColor.DARK_GRAY + "\u21E6 " + SlimefunPlugin.getLocalization().getMessage(p, "guide.pages.previous"));
                meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")"));
            });
        } else {
            return new CustomItem(PREV_BUTTON_ACTIVE, meta -> {
                meta.setDisplayName(ChatColor.WHITE + "\u21E6 " + SlimefunPlugin.getLocalization().getMessage(p, "guide.pages.previous"));
                meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")"));
            });
        }
    }

    @Nonnull
    public static ItemStack getNextButton(@Nonnull Player p, int page, int pages) {
        if (pages == 1 || page == pages) {
            return new CustomItem(NEXT_BUTTON_INACTIVE, meta -> {
                meta.setDisplayName(ChatColor.DARK_GRAY + SlimefunPlugin.getLocalization().getMessage(p, "guide.pages.next") + " \u21E8");
                meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")"));
            });
        } else {
            return new CustomItem(NEXT_BUTTON_ACTIVE, meta -> {
                meta.setDisplayName(ChatColor.WHITE + SlimefunPlugin.getLocalization().getMessage(p, "guide.pages.next") + " \u21E8");
                meta.setLore(Arrays.asList("", ChatColor.GRAY + "(" + page + " / " + pages + ")"));
            });
        }
    }

    public static void drawBackground(@Nonnull ChestMenu menu, int... slots) {
        for (int slot : slots) {
            menu.addItem(slot, getBackground(), getEmptyClickHandler());
        }
    }

    public static void updateProgressbar(@Nonnull ChestMenu menu, int slot, int timeLeft, int time, @Nonnull ItemStack indicator) {
        Inventory inv = menu.toInventory();

        // We don't need to update the progress bar if noone is watching :o
        if (inv == null || inv.getViewers().isEmpty()) {
            return;
        }

        ItemStack item = indicator.clone();
        ItemMeta im = item.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        if (im instanceof Damageable) {
            ((Damageable) im).setDamage(getDurability(item, timeLeft, time));
        }

        im.setDisplayName(" ");
        im.setLore(Arrays.asList(getProgressBar(timeLeft, time), "", ChatColor.GRAY + NumberUtils.getTimeLeft(timeLeft / 2) + " left"));
        item.setItemMeta(im);

        menu.replaceExistingItem(slot, item);
    }

    @Nonnull
    public static String getProgressBar(int time, int total) {
        StringBuilder builder = new StringBuilder();
        float percentage = Math.round(((((total - time) * 100.0F) / total) * 100.0F) / 100.0F);

        builder.append(NumberUtils.getColorFromPercentage(percentage));

        int rest = 20;
        for (int i = (int) percentage; i >= 5; i = i - 5) {
            builder.append(':');
            rest--;
        }

        builder.append("&7");

        for (int i = 0; i < rest; i++) {
            builder.append(':');
        }

        builder.append(" - ").append(percentage).append('%');
        return ChatColors.color(builder.toString());
    }

    private static short getDurability(@Nonnull ItemStack item, int timeLeft, int max) {
        return (short) ((item.getType().getMaxDurability() / max) * timeLeft);
    }

}
