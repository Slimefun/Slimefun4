package me.mrCookieSlime.Slimefun.Objects;

import java.util.Calendar;

import org.bukkit.inventory.ItemStack;

/**
 * Represents a {@link Category} that is only displayed in the Guide during
 * a specified month.
 * <p>
 * See {@link Category} for the complete documentation.
 * 
 * @author TheBusyBiscuit
 * @since 4.0
 * 
 * @see Category
 * @see LockedCategory
 */
public class SeasonalCategory extends Category {

	private int month = -1;

	/**
	 * The constructor for a SeasonCategory.
	 * <p>
	 * See {@link Category#Category(ItemStack, int)} for more information about creating
	 * a category.
	 * 
	 * @param month The month when the category should be displayed (from 1 = January ; to 12 = December)
	 * @param tier The tier for this category
	 * @param item The display item for this category
	 * 
	 * @since 4.0
	 */
	public SeasonalCategory(int month, int tier, ItemStack item) {
		super(item, tier);
		this.month = month - 1;
	}

	/**
	 * Gets the month during which the category should be displayed.
	 * 
	 * @return the month id (from 1 = January ; to 12 = December)
	 * 
	 * @since 4.0
	 * @see #isUnlocked()
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * Checks if the category should be displayed in the Guide.
	 * 
	 * @return true if it should, otherwise false
	 * 
	 * @since 4.0
	 * @see #getMonth()
	 */
	public boolean isUnlocked() {
		if (month == -1) return true;
		Calendar calendar = Calendar.getInstance();
		return month == calendar.get(Calendar.MONTH);
	}
}
