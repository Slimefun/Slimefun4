package io.github.thebusybiscuit.slimefun4.core.categories;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.json.ChatComponent;
import io.github.thebusybiscuit.cscorelib2.chat.json.ClickEvent;
import io.github.thebusybiscuit.cscorelib2.chat.json.HoverEvent;
import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.guide.BookSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.guide.ChestSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

public class MultiCategory extends FlexCategory {

    private static final int CATEGORY_SIZE = 36;
    private final List<SubCategory> subCategories = new ArrayList<>();

    @ParametersAreNonnullByDefault
    public MultiCategory(NamespacedKey key, ItemStack item) {
        this(key, item, 3);
    }

    @ParametersAreNonnullByDefault
    public MultiCategory(NamespacedKey key, ItemStack item, int tier) {
        super(key, item, tier);
    }

    /**
     * This will add the given {@link SubCategory} to this {@link MultiCategory}.
     * 
     * @param category
     *            The {@link SubCategory} to add.
     */
    public void addSubCategory(@Nonnull SubCategory category) {
        Validate.notNull(category, "The Category cannot be null!");

        subCategories.add(category);
    }

    /**
     * This will remove the given {@link SubCategory} from this {@link MultiCategory} (if present).
     * 
     * @param category
     *            The {@link SubCategory} to remove.
     */
    public void removeSubCategory(@Nonnull SubCategory category) {
        Validate.notNull(category, "The Category cannot be null!");

        subCategories.remove(category);
    }

    @Override
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideLayout layout) {
        return true;
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideLayout layout) {
        switch (layout) {
        case BOOK:
            showBookMenu(p, profile, 1);
            break;
        case CHEAT_SHEET:
            showChestMenu(p, profile, layout, 1, true);
            break;
        case CHEST:
            showChestMenu(p, profile, layout, 1, false);
            break;
        default:
            break;
        }
    }

    @ParametersAreNonnullByDefault
    private void showBookMenu(Player p, PlayerProfile profile, int page) {
        profile.getGuideHistory().add(this, page);

        List<ChatComponent> lines = new LinkedList<>();

        for (SubCategory category : subCategories) {
            ChatComponent chatComponent = new ChatComponent(ChatUtils.crop(ChatColor.DARK_GREEN, ItemUtils.getItemName(category.getItem(p))) + "\n");
            chatComponent.setHoverEvent(new HoverEvent(ItemUtils.getItemName(category.getItem(p)), "", ChatColor.GRAY + "\u21E8 " + ChatColor.GREEN + SlimefunPlugin.getLocalization().getMessage(p, "guide.tooltips.open-category")));
            chatComponent.setClickEvent(new ClickEvent(category.getKey(), pl -> SlimefunGuide.openCategory(profile, category, SlimefunGuideLayout.BOOK, 1)));
            lines.add(chatComponent);
        }

        BookSlimefunGuide guide = (BookSlimefunGuide) SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideLayout.BOOK);
        guide.showBook(p, profile, lines, true);
    }

    @ParametersAreNonnullByDefault
    private void showChestMenu(Player p, PlayerProfile profile, SlimefunGuideLayout layout, int page, boolean creativeMode) {
        if (!creativeMode) {
            profile.getGuideHistory().add(this, page);
        }

        ChestMenu menu = new ChestMenu(SlimefunPlugin.getLocalization().getMessage(p, "guide.title.main"));
        ChestSlimefunGuide guide = (ChestSlimefunGuide) SlimefunPlugin.getRegistry().getGuideLayout(layout);

        menu.setEmptySlotsClickable(false);
        menu.addMenuOpeningHandler(pl -> pl.playSound(pl.getLocation(), guide.getSound(), 1, 1));
        guide.createHeader(p, profile, menu);
        int index = 9;

        int target = (CATEGORY_SIZE * (page - 1)) - 1;

        while (target < (subCategories.size() - 1) && index < CATEGORY_SIZE + 9) {
            target++;

            SubCategory category = subCategories.get(target);
            menu.addItem(index, category.getItem(p));
            menu.addMenuClickHandler(index, (pl, slot, item, action) -> {
                SlimefunGuide.openCategory(profile, category, layout, 1);
                return false;
            });

            index++;
        }

        int pages = target == subCategories.size() - 1 ? page : (subCategories.size() - 1) / CATEGORY_SIZE + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;

            if (next != page && next > 0) {
                showChestMenu(p, profile, layout, next, creativeMode);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;

            if (next != page && next <= pages) {
                showChestMenu(p, profile, layout, next, creativeMode);
            }

            return false;
        });

        menu.open(p);
    }

}
