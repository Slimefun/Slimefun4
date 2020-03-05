package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.implementation.guide.BookSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.guide.ChestSlimefunGuide;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.GuideHandler;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

/**
 * This interface is used for the different implementations that add behaviour
 * to the {@link SlimefunGuide}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuideLayout
 * @see ChestSlimefunGuide
 * @see BookSlimefunGuide
 *
 */
public interface SlimefunGuideImplementation {

    /**
     * Every {@link SlimefunGuideImplementation} can be associated with a
     * {@link SlimefunGuideLayout}.
     * 
     * @return The layout this {@link SlimefunGuideImplementation} represents
     */
    SlimefunGuideLayout getLayout();

    /**
     * Returns the {@link ItemStack} representation for this {@link SlimefunGuideImplementation}.
     * In other words: The {@link ItemStack} you hold in your hand and that you use to
     * open your {@link SlimefunGuide}
     * 
     * @return The {@link ItemStack} representation for this {@link SlimefunGuideImplementation}
     */
    ItemStack getItem();

    void openMainMenu(PlayerProfile profile, boolean survival, int page);

    void openCategory(PlayerProfile profile, Category category, boolean survival, int page);

    void openSearch(PlayerProfile profile, String input, boolean survival, boolean addToHistory);

    void displayItem(PlayerProfile profile, ItemStack item, boolean addToHistory);

    void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory);

    /**
     * Retrieves the last page in the {@link SlimefunGuide} that was visited by a {@link Player}.
     * Optionally also rewinds the history back to that entry.
     * 
     * @param profile
     *            The {@link PlayerProfile} of the {@link Player} you are querying
     * @param remove
     *            Whether to remove the current entry so it moves back to the entry returned.
     * @return The last Guide Entry that was saved to the given Players guide history.
     */
    default Object getLastEntry(PlayerProfile profile, boolean remove) {
        LinkedList<Object> history = profile.getGuideHistory();

        if (remove && !history.isEmpty()) {
            history.removeLast();
        }

        return history.isEmpty() ? null : history.getLast();
    }

    /**
     * Opens the given Guide Entry to the {@link Player} of the specified {@link PlayerProfile}.
     * 
     * @param profile
     *            The {@link PlayerProfile} of the {@link Player} we are opening this to
     * @param entry
     *            The Guide Entry to open
     * @param survival
     *            Whether this is the survival-version of the guide or cheat sheet version
     */
    default void openEntry(PlayerProfile profile, Object entry, boolean survival) {
        if (entry == null) openMainMenu(profile, survival, 1);
        else if (entry instanceof Category) openCategory(profile, (Category) entry, survival, 1);
        else if (entry instanceof SlimefunItem) displayItem(profile, (SlimefunItem) entry, false);
        else if (entry instanceof GuideHandler) ((GuideHandler) entry).run(profile.getPlayer(), survival, getLayout() == SlimefunGuideLayout.BOOK);
        else if (entry instanceof String) openSearch(profile, (String) entry, survival, false);
        else displayItem(profile, (ItemStack) entry, false);
    }
}
