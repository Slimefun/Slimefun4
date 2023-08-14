package io.github.thebusybiscuit.slimefun4.api.guide;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.bakedlibs.dough.items.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import javax.annotation.Nonnull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * The {@link SlimefunGuideUnlockProvider} used for process unlock research
 * in Slimefun Guide.
 * <p>
 * You could trail the method to unlock your research,
 * not only use experience.
 */
public interface SlimefunGuideUnlockProvider {

    /**
     * This method used for check {@link Player}
     * could unlock specific research or not
     *
     * @param research {@link Research}
     * @param p {@link Player}
     *
     * @return whether player can unlock research or not
     */
    boolean canUnlock(@Nonnull Research research, @Nonnull Player p);

    /**
     * This method used for processing unlock research
     * For example, taken player's experience level or money.
     *
     * @param research {@link Research}
     * @param p {@link Player}
     */
    void processUnlock(@Nonnull Research research, @Nonnull Player p);

    /**
     * This returns the unit name of research unlock token
     *
     * @return unit name
     */
    @Nonnull String getUnitName();

    /**
     * This returns guide item when research locked
     * By default, it shows up in the guide as a barrier with a name & the experience cost in it.
     *
     * @return locked item {@link ItemStack}
     */
    @Nonnull
    default ItemStack getLockedItem(@Nonnull Research research, @Nonnull SlimefunItem sfItem, @Nonnull Player p) {
        return new CustomItemStack(ChestMenuUtils.getNotResearchedItem(), ChatColor.WHITE + ItemUtils.getItemName(sfItem.getItem()), "&4&l" + Slimefun.getLocalization().getMessage(p, "guide.locked"), "", "&a> Click to unlock", "", "&7Cost: &b" + research.getCost() + " " + getUnitName());
    }
}
