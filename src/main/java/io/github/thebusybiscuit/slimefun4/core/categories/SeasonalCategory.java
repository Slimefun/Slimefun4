package io.github.thebusybiscuit.slimefun4.core.categories;

import java.time.LocalDate;
import java.time.Month;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.Category;

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
     * The constructor for a {@link SeasonalCategory}.
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
    @ParametersAreNonnullByDefault
    public SeasonalCategory(NamespacedKey key, Month month, int tier, ItemStack item) {
        super(key, item, tier);
        Validate.notNull(month, "The Month cannot be null");

        this.month = month;
    }

    /**
     * This method returns the {@link Month} in which this {@link SeasonalCategory} will appear.
     * 
     * @return the {@link Month} in which this {@link SeasonalCategory} appears
     */
    @Nonnull
    public Month getMonth() {
        return month;
    }

    @Override
    public boolean isHidden(@Nonnull Player p) {
        // Hide this Category if the month differs
        if (month != LocalDate.now().getMonth()) {
            return true;
        }

        return super.isHidden(p);
    }
}
