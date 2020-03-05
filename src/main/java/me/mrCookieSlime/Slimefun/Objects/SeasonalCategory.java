package me.mrCookieSlime.Slimefun.Objects;

import java.util.Calendar;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a {@link Category} that is only displayed in the Guide during
 * a specified month.
 * <p>
 * See {@link Category} for the complete documentation.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Category
 * @see LockedCategory
 */
public class SeasonalCategory extends Category {

    private final int month;

    /**
     * The constructor for a SeasonCategory.
     * <p>
     * See {@link Category#Category(ItemStack, int)} for more information about creating
     * a category.
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
    public SeasonalCategory(NamespacedKey key, int month, int tier, ItemStack item) {
        super(key, item, tier);

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("There is no month no. " + month);
        }

        this.month = month - 1;
    }

    /**
     * Gets the month during which the category should be displayed.
     * 
     * @return the id of the month this {@link SeasonalCategory} is assigned to (from 1 = January ; to 12 = December)
     */
    public int getMonth() {
        return this.month;
    }

    /**
     * Checks if the category should currently be displayed in the Guide.
     * This is based on {@link SeasonalCategory#getMonth()}.
     * 
     * @return true if it should, otherwise false
     */
    public boolean isUnlocked() {
        Calendar calendar = Calendar.getInstance();
        return month == calendar.get(Calendar.MONTH);
    }
}
