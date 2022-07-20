package io.github.thebusybiscuit.slimefun4.api.items.groups;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;

/**
 * The {@link SubItemGroup} is a child {@link ItemGroup} of the
 * {@link NestedItemGroup}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see NestedItemGroup
 *
 */
public class SubItemGroup extends ItemGroup {

    private final NestedItemGroup parentItemGroup;

    @ParametersAreNonnullByDefault
    public SubItemGroup(NamespacedKey key, NestedItemGroup parent, ItemStack item) {
        this(key, parent, item, 3);
    }

    @ParametersAreNonnullByDefault
    public SubItemGroup(NamespacedKey key, NestedItemGroup parent, ItemStack item, int tier) {
        super(key, item, tier);

        Validate.notNull(parent, "The parent group cannot be null");

        parentItemGroup = parent;
        parent.addSubGroup(this);
    }

    @Override
    public final boolean isVisible(@Nonnull Player p) {
        /*
         * Sub Categories are always hidden,
         * they won't show up in the normal guide view.
         */
        return false;
    }

    @Override
    public final boolean isAccessible(@Nonnull Player p) {
        /*
         * Sub Categories are accessible, they are invisible
         * but their items are available to the guide search.
         */
        return true;
    }

    /**
     * This method returns the parent {@link NestedItemGroup} which this
     * {@link SubItemGroup} belongs to.
     * 
     * @return The parent {@link NestedItemGroup}
     */
    public final @Nonnull NestedItemGroup getParent() {
        return parentItemGroup;
    }

    @Override
    public final void register(@Nonnull SlimefunAddon addon) {
        super.register(addon);

        if (!parentItemGroup.isRegistered()) {
            parentItemGroup.register(addon);
        }
    }

}
