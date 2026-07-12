package io.github.thebusybiscuit.slimefun5.core.guide.wiki;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun5.core.guide.options.ItemDescriptionsOption;
import io.github.thebusybiscuit.slimefun5.core.services.localization.ItemTranslationService;
import io.github.thebusybiscuit.slimefun5.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun5.core.services.localization.TranslationConfig;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;

/**
 * Per-item wiki screen. Mirrors the familiar Slimefun recipe view: the crafting grid fills the
 * centre, the producing machine sits beside it, and the result item carries the authored
 * explanation (and energy stats) in its lore. Below it, the items that consume this one are shown
 * as clickable icons. Only the top and bottom rows are panes - the item area stays clean.
 *
 * Back returns to wherever the page was opened from (a topic guide, the browse list, or another
 * item via "used in"), not to a fixed screen.
 */
public final class WikiPage {

    private static final int[] BORDER = { 0, 1, 2, 6, 7, 8, 45, 46, 47, 48, 49, 50, 51, 52, 53 };
    private static final int[] RECIPE_SLOTS = { 3, 4, 5, 12, 13, 14, 21, 22, 23 };

    private static final int BACK_SLOT = 0;
    private static final int RECIPE_TYPE_SLOT = 10;
    private static final int OUTPUT_SLOT = 16;
    private static final int USED_IN_LABEL_SLOT = 28;
    private static final int USED_IN_START = 29;
    private static final int USED_IN_END = 34;
    private static final int USED_IN_CAPACITY = USED_IN_END - USED_IN_START + 1;

    /** Shared reverse-recipe index for the JVM lifetime; the first lookup builds it lazily. */
    private static final ReverseRecipeIndex REVERSE_INDEX = new ReverseRecipeIndex();

    private WikiPage() {}

    /** Pre-builds the shared reverse-recipe index so the first wiki click is instant. */
    public static void warmUpIndex() {
        REVERSE_INDEX.warmUp();
    }

    /** Opens the item's wiki page; Back returns to the wiki home. */
    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull SlimefunItem item) {
        open(p, guide, item, () -> WikiIndex.open(p, guide));
    }

    /** Opens the item's wiki page; Back runs the given action (returns to wherever you came from). */
    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull SlimefunItem item, @Nonnull Runnable onBack) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.wiki.item-title").replace("%item%", item.getItemName()));
        menu.addMenuOpeningHandler(SoundEffect.GUIDE_BUTTON_CLICK_SOUND::playFor);
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(BACK_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), Slimefun.getLocalization().getMessage(p, "guide.wiki.back")));
        menu.addMenuClickHandler(BACK_SLOT, (pl, slot, clicked, action) -> {
            onBack.run();
            return false;
        });

        addRecipeType(menu, p, item);
        addRecipe(menu, p, guide, item, onBack);
        addOutput(menu, p, item);
        addUsedIn(menu, p, guide, item);

        menu.open(p);
    }

    private static void addRecipeType(@Nonnull ChestMenu menu, @Nonnull Player p, @Nonnull SlimefunItem item) {
        if (item.getRecipeType() == null) {
            return;
        }

        ItemStack icon = item.getRecipeType().getItem(p);

        if (icon != null) {
            menu.addItem(RECIPE_TYPE_SLOT, icon);
            menu.addMenuClickHandler(RECIPE_TYPE_SLOT, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    /** Renders the crafting grid; Slimefun ingredients are clickable and open their own wiki page. */
    private static void addRecipe(@Nonnull ChestMenu menu, @Nonnull Player p, @Nonnull ItemStack guide, @Nonnull SlimefunItem item, @Nonnull Runnable onBack) {
        ItemStack[] recipe = item.getRecipe();

        for (int i = 0; i < RECIPE_SLOTS.length && i < recipe.length; i++) {
            ItemStack ingredient = recipe[i];

            if (ingredient == null) {
                continue;
            }

            int slot = RECIPE_SLOTS[i];
            menu.addItem(slot, ingredient);

            SlimefunItem ingredientItem = SlimefunItem.getByItem(ingredient);

            if (ingredientItem != null) {
                menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                    open(pl, guide, ingredientItem, onBack);
                    return false;
                });
            } else {
                menu.addMenuClickHandler(slot, ChestMenuUtils.getEmptyClickHandler());
            }
        }
    }

    /**
     * The result item carries the authored explanation (and energy stats) appended to its lore. This
     * display copy pre-renders the item's translated name/lore for {@code p}'s language itself and then
     * strips its Slimefun id (see {@link #stripSlimefunIdentity}) - it is never sent back through the
     * normal per-viewer rendering path, so nothing may re-render (and clobber) the wiki body appended
     * here. See the Task C report for why: the packet-translation layer rewrites any outbound
     * ClientboundContainerSetSlot/SetContent packet item that still carries the Slimefun id.
     */
    private static void addOutput(@Nonnull ChestMenu menu, @Nonnull Player p, @Nonnull SlimefunItem item) {
        ItemStack output = item.getItem();

        if (output == null || output.getType() == Material.AIR) {
            output = MaterialCompat.stack(XMaterial.BARRIER);
        }

        ItemStack display = output.clone();
        ItemMeta meta = display.getItemMeta();

        if (meta != null) {
            String languageId = languageOf(p);
            applyTranslatedDisplay(p, item, meta, languageId);

            List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
            lore.add("");
            lore.add(ChatColor.translateAlternateColorCodes('&', "&8&m                    "));

            for (String line : Slimefun.getWikiText().get(item, languageId)) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line));
            }

            appendStats(p, lore, item);
            meta.setLore(lore);
            display.setItemMeta(meta);
            stripSlimefunIdentity(display);
        }

        menu.addItem(OUTPUT_SLOT, display);
        menu.addMenuClickHandler(OUTPUT_SLOT, ChestMenuUtils.getEmptyClickHandler());
    }

    /**
     * Pre-renders the item's translated display name and base (type/description/stats/usage) lore for
     * {@code p}'s language onto {@code meta}, using the same rendering the packet-translation layer would
     * otherwise apply. Needed because this display copy has its Slimefun id stripped right after (so the
     * wiki body appended afterwards can't be clobbered), which also means the packet layer will never get
     * a chance to translate it - so it must already be correct before that happens.
     */
    private static void applyTranslatedDisplay(@Nonnull Player p, @Nonnull SlimefunItem item, @Nonnull ItemMeta meta, @Nonnull String languageId) {
        boolean includeDescription = ItemDescriptionsOption.isEnabledFor(p);
        ItemTranslationService.RenderedDisplay rendered = Slimefun.getItemTranslationService()
            .renderForPacket(item.getId(), languageId, TranslationConfig.fallback(), includeDescription);

        if (rendered == null) {
            return;
        }

        meta.setDisplayName(rendered.name);

        if (!rendered.lore.isEmpty()) {
            meta.setLore(new ArrayList<>(rendered.lore));
        }
    }

    /**
     * Strips the Slimefun item-id marker from this display copy so the packet-translation layer skips it
     * entirely (it only rewrites items it can resolve back to a registered id) - preventing it from
     * overwriting the wiki body/stats lore just appended above with its own per-viewer render.
     */
    private static void stripSlimefunIdentity(@Nonnull ItemStack display) {
        ItemMeta meta = display.getItemMeta();

        if (meta == null) {
            return;
        }

        PdcCompat.remove(meta, Slimefun.getItemDataService().getKey());
        display.setItemMeta(meta);
    }

    @Nonnull
    private static String languageOf(@Nonnull Player p) {
        Language language = Slimefun.getLocalization().getLanguage(p);
        return language != null ? language.getId() : "en";
    }

    private static void appendStats(@Nonnull Player p, @Nonnull List<String> lore, @Nonnull SlimefunItem item) {
        if (!(item instanceof EnergyNetComponent)) {
            return;
        }

        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', Slimefun.getLocalization().getMessage(p, "guide.wiki.stats.title")));
        lore.add(ChatColor.translateAlternateColorCodes('&', Slimefun.getLocalization().getMessage(p, "guide.wiki.stats.capacity").replace("%capacity%", String.valueOf(((EnergyNetComponent) item).getCapacity()))));

        if (item instanceof AContainer) {
            AContainer container = (AContainer) item;
            lore.add(ChatColor.translateAlternateColorCodes('&', Slimefun.getLocalization().getMessage(p, "guide.wiki.stats.energy").replace("%energy%", String.valueOf(container.getEnergyConsumption()))));
            lore.add(ChatColor.translateAlternateColorCodes('&', Slimefun.getLocalization().getMessage(p, "guide.wiki.stats.speed").replace("%speed%", String.valueOf(container.getSpeed()))));
        }
    }

    /** Shows the items that consume this one, as clickable icons that open their wiki pages. */
    private static void addUsedIn(@Nonnull ChestMenu menu, @Nonnull Player p, @Nonnull ItemStack guide, @Nonnull SlimefunItem item) {
        List<SlimefunItem> consumers = REVERSE_INDEX.getConsumers(item);

        if (consumers.isEmpty()) {
            return;
        }

        List<String> usedInLore = new ArrayList<>();
        usedInLore.add(Slimefun.getLocalization().getMessage(p, "guide.wiki.used-in.name"));
        usedInLore.add("");
        usedInLore.addAll(Slimefun.getLocalization().getMessages(p, "guide.wiki.used-in.lore"));
        menu.addItem(USED_IN_LABEL_SLOT, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOKSHELF), usedInLore),
            ChestMenuUtils.getEmptyClickHandler());

        boolean overflow = consumers.size() > USED_IN_CAPACITY;
        int shown = overflow ? USED_IN_CAPACITY - 1 : consumers.size();

        for (int i = 0; i < shown; i++) {
            SlimefunItem consumer = consumers.get(i);
            int slot = USED_IN_START + i;

            menu.addItem(slot, consumer.getItem());
            menu.addMenuClickHandler(slot, (pl, sl, clicked, action) -> {
                open(pl, guide, consumer, () -> open(pl, guide, item));
                return false;
            });
        }

        if (overflow) {
            int remaining = consumers.size() - shown;
            menu.addItem(USED_IN_END, CustomItemStack.create(MaterialCompat.stack(XMaterial.PAPER), Slimefun.getLocalization().getMessage(p, "guide.wiki.more").replace("%count%", String.valueOf(remaining))));
            menu.addMenuClickHandler(USED_IN_END, ChestMenuUtils.getEmptyClickHandler());
        }
    }
}
