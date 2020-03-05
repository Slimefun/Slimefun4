package me.mrCookieSlime.Slimefun.Objects;

import java.util.Arrays;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * Represents a {@link Category} that cannot be opened until the parent category/categories
 * are fully unlocked.
 * <p>
 * See {@link Category} for the complete documentation.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Category
 * @see SeasonalCategory
 * 
 */
public class LockedCategory extends Category {

    private final List<Category> parents;

    /**
     * The basic constructor for a LockedCategory.
     * <p>
     * See {@link Category#Category(ItemStack, int)} for more information about creating
     * a category.
     * <p>
     * Like {@link Category#Category(ItemStack)}, the tier is automatically set to 3.
     * 
     * @param key
     *            A unique identifier for this category
     * @param item
     *            The display item for this category
     * @param parents
     *            The parent categories for this category
     * 
     */
    public LockedCategory(NamespacedKey key, ItemStack item, Category... parents) {
        this(key, item, 3, parents);
    }

    /**
     * The constructor for a LockedCategory.
     * <p>
     * See {@link Category#Category(ItemStack, int)} for more information about creating
     * a category.
     * 
     * @param key
     *            A unique identifier for this category
     * @param item
     *            The display item for this category
     * @param tier
     *            The tier of this category
     * @param parents
     *            The parent categories for this category
     * 
     */
    public LockedCategory(NamespacedKey key, ItemStack item, int tier, Category... parents) {
        super(key, item, tier);
        this.parents = Arrays.asList(parents);
    }

    /**
     * Gets the list of parent categories for this {@link LockedCategory}.
     * 
     * @return the list of parent categories
     * 
     * @see #addParent(Category)
     * @see #removeParent(Category)
     */
    public List<Category> getParents() {
        return parents;
    }

    /**
     * Adds a parent {@link Category} to this {@link LockedCategory}.
     * 
     * @param category
     *            The {@link Category} to add as a parent
     *
     * @see #getParents()
     * @see #removeParent(Category)
     */
    public void addParent(Category category) {
        if (category == this || category == null) {
            throw new IllegalArgumentException("Category '" + item.getItemMeta().getDisplayName() + "' cannot be a parent of itself or have a 'null' parent.");
        }

        this.parents.add(category);
    }

    /**
     * Removes a {@link Category} from the parents of this {@link LockedCategory}.
     * 
     * @param category
     *            The {@link Category} to remove from the parents of this {@link LockedCategory}
     * 
     * @see #getParents()
     * @see #addParent(Category)
     */
    public void removeParent(Category category) {
        this.parents.remove(category);
    }

    /**
     * Checks if the {@link Player} has fully unlocked all parent categories.
     * 
     * @param p
     *            The {@link Player} to check
     * @return Whether the {@link Player} has fully completed all parent categories, otherwise false
     */
    public boolean hasUnlocked(Player p) {
        return hasUnlocked(p, PlayerProfile.get(p));
    }

    public boolean hasUnlocked(Player p, PlayerProfile profile) {
        for (Category category : parents) {
            for (SlimefunItem item : category.getItems()) {
                // Should we replace this all with Slimefun.hasUnlocked() ?
                if (Slimefun.isEnabled(p, item, false) && Slimefun.hasPermission(p, item, false) && item.getResearch() != null && !profile.hasUnlocked(item.getResearch())) return false;
            }
        }

        return true;
    }
}
