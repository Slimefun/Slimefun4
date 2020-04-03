package io.github.thebusybiscuit.slimefun4.core.categories;

import java.time.LocalDate;
import java.time.Month;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.LockedCategory;

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

    /**
     * Checks if the category should currently be displayed in the Guide.
     * This is based on {@link SeasonalCategory#getMonth()}.
     * 
     * @return true if it should, otherwise false
     */
    public boolean isVisible() {
        return month == LocalDate.now().getMonth();
    }
}
