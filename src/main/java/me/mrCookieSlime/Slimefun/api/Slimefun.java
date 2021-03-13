package me.mrCookieSlime.Slimefun.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * Provides a few static convenience methods.
 * This class is slowly getting stripped away in favour of a more object-oriented approach.
 *
 * @author TheBusyBiscuit
 * @author Walshy
 * @author Poslovitch
 */
public final class Slimefun {

    private Slimefun() {}

    /**
     * Checks if this player can use this item.
     *
     * @param p
     *            the player to check, not null
     * @param item
     *            the item to check, not null
     * @param message
     *            whether a message should be sent to the player or not
     * 
     * @deprecated Moved to
     *             {@link SlimefunUtils#canPlayerUseItem(Player, ItemStack, boolean)}
     *
     * @return <code>true</code> if the item is a SlimefunItem, enabled, researched and if the player has the permission
     *         to use it,
     *         <code>false</code> otherwise.
     */
    @Deprecated
    public static boolean hasUnlocked(Player p, ItemStack item, boolean message) {
        return SlimefunUtils.canPlayerUseItem(p, item, message);
    }

    /**
     * Checks if this player can use this item.
     *
     * @param p
     *            the player to check, not null
     * @param sfItem
     *            the item to check, not null
     * @param message
     *            whether a message should be sent to the player or not
     * 
     * @deprecated Please use {@link SlimefunItem#canUse(Player, boolean)} instead.
     *
     * @return <code>true</code> if the item is enabled, researched and the player has the permission to use it,
     *         <code>false</code> otherwise.
     */
    @Deprecated
    public static boolean hasUnlocked(Player p, SlimefunItem sfItem, boolean message) {
        return sfItem.canUse(p, message);
    }

    /**
     * Checks if this player has the permission to use this item.
     *
     * @param p
     *            the player to check, not null
     * @param item
     *            the item to check, null returns <code>true</code>
     * @param message
     *            whether a message should be sent to the player or not
     *
     * @return <code>true</code> if the item is not null and if the player has the permission to use it,
     *         <code>false</code> otherwise.
     */
    public static boolean hasPermission(Player p, SlimefunItem item, boolean message) {
        if (item == null) {
            return true;
        } else if (SlimefunPlugin.getPermissionsService().hasPermission(p, item)) {
            return true;
        } else {
            if (message) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.no-permission", true);
            }

            return false;
        }
    }

    /**
     * Checks if this item is enabled in the world this player is in.
     *
     * @param p
     *            the player to get the world he is in, not null
     * @param item
     *            the item to check, not null
     * @param message
     *            whether a message should be sent to the player or not
     * 
     * @deprecated This method will be removed.
     *
     * @return <code>true</code> if the item is a SlimefunItem and is enabled in the world the player is in,
     *         <code>false</code> otherwise.
     */
    @Deprecated
    public static boolean isEnabled(Player p, ItemStack item, boolean message) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        return sfItem == null || isEnabled(p, sfItem, message);
    }

    /**
     * Checks if this item is enabled in the world this player is in.
     *
     * @param p
     *            the player to get the world he is in, not null
     * @param sfItem
     *            the item to check, not null
     * @param message
     *            whether a message should be sent to the player or not
     *
     * @return <code>true</code> if the item is enabled in the world the player is in,
     *         <code>false</code> otherwise.
     */
    public static boolean isEnabled(Player p, SlimefunItem sfItem, boolean message) {
        if (sfItem.isDisabled()) {
            if (message) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.disabled-item", true);
            }

            return false;
        } else if (!SlimefunPlugin.getWorldSettingsService().isEnabled(p.getWorld(), sfItem)) {
            if (message) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.disabled-in-world", true);
            }

            return false;
        }
        return true;
    }
}