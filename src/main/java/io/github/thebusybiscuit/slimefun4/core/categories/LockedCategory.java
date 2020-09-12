package io.github.thebusybiscuit.slimefun4.core.categories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
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

    private final NamespacedKey[] keys;
    private final Set<Category> parents = new HashSet<>();

    /**
     * The basic constructor for a LockedCategory.
     * Like {@link Category}, the default tier is automatically set to 3.
     * 
     * @param key
     *            A unique identifier for this category
     * @param item
     *            The display item for this category
     * @param parents
     *            The parent categories for this category
     * 
     */
    @ParametersAreNonnullByDefault
    public LockedCategory(NamespacedKey key, ItemStack item, NamespacedKey... parents) {
        this(key, item, 3, parents);
    }

    /**
     * The constructor for a LockedCategory.
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
    @ParametersAreNonnullByDefault
    public LockedCategory(NamespacedKey key, ItemStack item, int tier, NamespacedKey... parents) {
        super(key, item, tier);
        Validate.noNullElements(parents, "A LockedCategory must not have any 'null' parents!");

        this.keys = parents;
    }

    @Override
    public void register() {
        super.register();

        List<NamespacedKey> namespacedKeys = new ArrayList<>();

        for (NamespacedKey key : keys) {
            if (key != null) {
                namespacedKeys.add(key);
            }
        }

        for (Category category : SlimefunPlugin.getRegistry().getCategories()) {
            if (namespacedKeys.remove(category.getKey())) {
                addParent(category);
            }
        }

        for (NamespacedKey key : namespacedKeys) {
            Slimefun.getLogger().log(Level.INFO, "Parent \"{0}\" for Category \"{1}\" was not found, probably just disabled.", new Object[] { key, getKey() });
        }
    }

    /**
     * Gets the list of parent categories for this {@link LockedCategory}.
     * 
     * @return the list of parent categories
     * 
     * @see #addParent(Category)
     * @see #removeParent(Category)
     */
    @Nonnull
    public Set<Category> getParents() {
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

        parents.add(category);
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
    public void removeParent(@Nonnull Category category) {
        parents.remove(category);
    }

    /**
     * Checks if the {@link Player} has fully unlocked all parent categories.
     * 
     * @param p
     *            The {@link Player} to check
     * @param profile
     *            The {@link PlayerProfile} that belongs to the given {@link Player}
     * @return Whether the {@link Player} has fully completed all parent categories, otherwise false
     */
    public boolean hasUnlocked(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        for (Category category : parents) {
            for (SlimefunItem item : category.getItems()) {
                // Should probably be replaced with Slimefun.hasUnlocked(...)
                // However this will result in better performance because we don't
                // request the PlayerProfile everytime
                if (Slimefun.isEnabled(p, item, false) && Slimefun.hasPermission(p, item, false) && !profile.hasUnlocked(item.getResearch())) {
                    return false;
                }
            }
        }

        return true;
    }
}
