package me.mrCookieSlime.Slimefun.api;

import java.util.Optional;
import java.util.logging.Logger;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemState;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.VanillaItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * Provides a few static convenience methods.
 *
 * @author TheBusyBiscuit
 * @author Walshy
 * @author Poslovitch
 */
public final class Slimefun {

    private Slimefun() {}

    @Nonnull
    public static Logger getLogger() {
        return SlimefunPlugin.instance().getLogger();
    }

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
     * @return <code>true</code> if the item is a SlimefunItem, enabled, researched and if the player has the permission
     *         to use it,
     *         <code>false</code> otherwise.
     */
    public static boolean hasUnlocked(Player p, ItemStack item, boolean message) {
        SlimefunItem sfItem = SlimefunItem.getByItem(item);

        if (sfItem != null) {
            return hasUnlocked(p, sfItem, message);
        } else {
            return true;
        }
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
     * @return <code>true</code> if the item is enabled, researched and the player has the permission to use it,
     *         <code>false</code> otherwise.
     */
    public static boolean hasUnlocked(Player p, SlimefunItem sfItem, boolean message) {
        if (sfItem.getState() == ItemState.VANILLA_FALLBACK) {
            return true;
        }

        if (isEnabled(p, sfItem, message) && hasPermission(p, sfItem, message)) {
            if (sfItem.getResearch() == null) {
                return true;
            } else {
                Optional<PlayerProfile> profile = PlayerProfile.find(p);

                if (!profile.isPresent()) {
                    // We will return false since we cannot know the answer yet
                    // But we will schedule the Profile for loading.
                    PlayerProfile.request(p);
                    return false;
                } else if (profile.get().hasUnlocked(sfItem.getResearch())) {
                    return true;
                } else {
                    if (message && !(sfItem instanceof VanillaItem)) {
                        SlimefunPlugin.getLocalization().sendMessage(p, "messages.not-researched", true);
                    }

                    return false;
                }
            }
        }

        return false;
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
     * @return <code>true</code> if the item is a SlimefunItem and is enabled in the world the player is in,
     *         <code>false</code> otherwise.
     */
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