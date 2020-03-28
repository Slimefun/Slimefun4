package io.github.thebusybiscuit.slimefun4.core.guide;

import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.guide.BookSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.guide.ChestSlimefunGuide;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

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

    void displayItem(PlayerProfile profile, ItemStack item, int index, boolean addToHistory);

    void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory);

}
