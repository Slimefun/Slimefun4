package io.github.thebusybiscuit.slimefun4.core.categories;

import me.mrCookieSlime.Slimefun.Objects.Category;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDate;
import java.time.Month;

/**
 * Represents a {@link Category} that is only displayed in the Guide during
 * a specified {@link Month}.
 *
 * @author TheBusyBiscuit
 *
 * @see Category
 * @see LockedCategory
 */
public class SeasonalCategory extends Category {

    private final Month month;

    /**
     * The constructor for a SeasonCategory.
     *
     * @param key
     *            The {@link NamespacedKey} that is used to identify this {@link Category}
     * @param month
     *            The month when the category should be displayed (from 1 = January ; to 12 = December)
     * @param tier
     *            The tier of this category
     * @param item
     *            The display item for this category
     */
    public SeasonalCategory(NamespacedKey key, Month month, int tier, ItemStack item) {
        super(key, item, tier);

        this.month = month;
    }

    /**
     * This method returns the {@link Month} in which this {@link SeasonalCategory} will appear.
     *
     * @return the {@link Month} in which this {@link SeasonalCategory} appears
     */
    public Month getMonth() {
        return month;
    }

    @Override
    public boolean isHidden(Player p) {
        if (month != LocalDate.now().getMonth()) {
            return true;
        }

        return super.isHidden(p);
    }
}