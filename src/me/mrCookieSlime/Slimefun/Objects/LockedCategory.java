package me.mrCookieSlime.Slimefun.Objects;

import java.util.Arrays;
import java.util.List;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a {@link Category} that cannot be opened until the parent category/categories
 * are fully unlocked.
 * <p>
 * See {@link Category} for the complete documentation.
 * 
 * @author TheBusyBiscuit
 * @since 4.0
 * 
 * @see Category
 * @see SeasonalCategory
 */
public class LockedCategory extends Category {

	private List<Category> parents;

	/**
	 * The basic constructor for a LockedCategory.
	 * <p>
	 * See {@link Category#Category(ItemStack, int)} for more information about creating
	 * a category.
	 * <p>
	 * Like {@link Category#Category(ItemStack)}, the tier is automatically set to 3.
	 * 
	 * @param item The display item for this category
	 * @param parents The parent categories for this category
	 * 
	 * @since 4.0
	 * @see #LockedCategory(ItemStack, int, Category...)
	 */
	public LockedCategory(ItemStack item, Category... parents) {
		super(item);
		this.parents = Arrays.asList(parents);
	}

	/**
	 * The constructor for a LockedCategory.
	 * <p>
	 * See {@link Category#Category(ItemStack, int)} for more information about creating
	 * a category.
	 * 
	 * @param item The display item for this category
	 * @param tier The tier of this category
	 * @param parents The parent categories for this category
	 * 
	 * @since 4.0
	 * @see #LockedCategory(ItemStack, Category...)
	 */
	public LockedCategory(ItemStack item, int tier, Category... parents) {
		super(item, tier);
		this.parents = Arrays.asList(parents);
	}

	/**
	 * Gets the list of parent categories for this category.
	 * 
	 * @return the list of the parent categories
	 * 
	 * @since 4.0
	 * @see #addParent(Category)
	 * @see #removeParent(Category)
	 */
	public List<Category> getParents() {
		return parents;
	}

	/**
	 * Adds a parent category to this category.
	 * 
	 * @param category Category to add as a parent
	 *
	 * @since 4.0
	 * @see #getParents()
	 * @see #removeParent(Category)
	 */
	public void addParent(Category category) {
		if (category == this) throw new IllegalArgumentException("Category '" + this.getItem().getItemMeta().getDisplayName() + "' cannot be a parent of itself.");
		this.parents.add(category);
	}

	/**
	 * Removes a category as parent to this category.
	 * 
	 * @param category Category to remove from parents
	 * 
	 * @since 4.0
	 * @see #getParents()
	 * @see #addParent(Category)
	 */
	public void removeParent(Category category) {
		this.parents.remove(category);
	}

	/**
	 * Checks if the player has fully unlocked parent categories.
	 * 
	 * @param p Player to check
	 * @return true if the player has fully completed the parent categories, otherwise false
	 * 
	 * @since 4.0
	 */
	public boolean hasUnlocked(Player p) {
		PlayerProfile profile = PlayerProfile.get(p);
		
		for (Category category: parents) {
			for (SlimefunItem item: category.getItems()) {
				if (Slimefun.isEnabled(p, item.getItem(), false) && Slimefun.hasPermission(p, item, false) && item.getResearch() != null && !profile.hasUnlocked(item.getResearch())) return false;
			}
		}
		return true;
	}
}
