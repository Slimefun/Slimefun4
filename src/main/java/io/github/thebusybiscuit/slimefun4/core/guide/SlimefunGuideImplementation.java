package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
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
    @Nonnull
    SlimefunGuideLayout getLayout();

    /**
     * Returns the {@link ItemStack} representation for this {@link SlimefunGuideImplementation}.
     * In other words: The {@link ItemStack} you hold in your hand and that you use to
     * open your {@link SlimefunGuide}
     * 
     * @return The {@link ItemStack} representation for this {@link SlimefunGuideImplementation}
     */
    @Nonnull
    ItemStack getItem();

    /**
     * This method returns whether this {@link SlimefunGuideImplementation} is meant
     * for Survival Mode.
     * 
     * @return Whether this is a survival mode implementation
     */
    boolean isSurvivalMode();

    void openMainMenu(PlayerProfile profile, int page);

    void openCategory(PlayerProfile profile, Category category, int page);

    void openSearch(PlayerProfile profile, String input, boolean addToHistory);

    void displayItem(PlayerProfile profile, ItemStack item, int index, boolean addToHistory);

    void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory);

    default void unlockItem(Player p, SlimefunItem sfitem, Consumer<Player> callback) {
        Research research = sfitem.getResearch();

        if (p.getGameMode() == GameMode.CREATIVE && SlimefunPlugin.getRegistry().isFreeCreativeResearchingEnabled()) {
            research.unlock(p, true, callback);
        } else {
            p.setLevel(p.getLevel() - research.getCost());
            research.unlock(p, false, callback);
        }
    }

}
