package io.github.thebusybiscuit.slimefun4.api.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public interface ItemRestriction {

    /**
     * This method represents a check.
     * The returned boolean will decide whether to allow the action.
     * 
     * @param profile
     *            The Player's profile
     * @param p
     *            The Player itself
     * @param item
     *            The SlimefunItem that the {@link ItemStack} represents
     * @param itemstack
     *            The ItemStack that is being tested.
     * 
     * @return Whether the action was allowed
     */
    boolean isAllowed(PlayerProfile profile, Player p, SlimefunItem item, ItemStack itemstack);

    /**
     * This method is executed if an ItemRestriction took affect.
     * Override it to send a message to the Player telling them they cannot
     * use that item, or do something else in there.
     * 
     * @param profile
     *            The Player's profile
     * @param p
     *            The Player to warn
     * @param item
     *            The SlimefunItem that the {@link ItemStack} represents
     * @param itemstack
     *            The ItemStack that was prevented from being used
     */
    void warnPlayer(PlayerProfile profile, Player p, SlimefunItem item, ItemStack itemstack);

}
