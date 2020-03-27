package me.mrCookieSlime.Slimefun.Objects;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.time.Month;
import java.util.Calendar;

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
     * Gets the month during which the category should be displayed.
     *
     * @return the id of the month this {@link SeasonalCategory} is assigned to (from 1 = January ; to 12 = December)
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
    public boolean isUnlocked() {
        Calendar calendar = Calendar.getInstance();
        return month.ordinal() == calendar.get(Calendar.MONTH);
    }
}