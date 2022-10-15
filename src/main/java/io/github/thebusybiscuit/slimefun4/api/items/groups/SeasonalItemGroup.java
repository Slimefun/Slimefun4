package io.github.thebusybiscuit.slimefun4.api.items.groups;

import java.time.LocalDate;
import java.time.Month;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;

/**
 * Represents a {@link ItemGroup} that is only displayed in the Guide during
 * a specified {@link Month}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ItemGroup
 * @see LockedItemGroup
 */
public class SeasonalItemGroup extends ItemGroup {

    private final Month month;

    /**
     * The constructor for a {@link SeasonalItemGroup}.
     * 
     * @param key
     *            The {@link NamespacedKey} that is used to identify this {@link ItemGroup}
     * @param month
     *            The month when the {@link ItemGroup} should be displayed (from 1 = January ; to 12 = December)
     * @param tier
     *            The tier of this {@link ItemGroup}
     * @param item
     *            The display item for this {@link ItemGroup}
     */
    @ParametersAreNonnullByDefault
    public SeasonalItemGroup(NamespacedKey key, Month month, int tier, ItemStack item) {
        super(key, item, tier);
        Validate.notNull(month, "The Month cannot be null");

        this.month = month;
    }

    /**
     * This method returns the {@link Month} in which this {@link SeasonalItemGroup} will appear.
     * 
     * @return the {@link Month} in which this {@link SeasonalItemGroup} appears
     */
    public @Nonnull Month getMonth() {
        return month;
    }

    @Override
    public boolean isAccessible(@Nonnull Player p) {
        // Block this ItemGroup if the month differs
        if (month != LocalDate.now().getMonth()) {
            return false;
        }

        return super.isAccessible(p);
    }
}
