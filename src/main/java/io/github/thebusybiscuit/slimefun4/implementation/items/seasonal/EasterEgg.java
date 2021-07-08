package io.github.thebusybiscuit.slimefun4.implementation.items.seasonal;

import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSpawnReason;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.FireworkUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This {@link SlimefunItem} represents an {@link EasterEgg}.
 * The {@link EasterEgg} can be activated upon Right-Click and will
 * spawn a random {@link ItemStack} from the {@link ItemStack} Array specified in the constructor.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ChristmasPresent
 *
 */
public class EasterEgg extends SimpleSlimefunItem<ItemUseHandler> {

    private final ItemStack[] gifts;

    @ParametersAreNonnullByDefault
    public EasterEgg(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, ItemStack... gifts) {
        super(category, item, recipeType, recipe, recipeOutput);

        this.gifts = gifts;
    }

    @Override
    public @Nonnull ItemUseHandler getItemHandler() {
        return e -> {
            e.cancel();

            Player p = e.getPlayer();

            if (p.getGameMode() != GameMode.CREATIVE) {
                ItemUtils.consumeItem(e.getItem(), false);
            }

            FireworkUtils.launchRandom(p, 2);
            SlimefunUtils.spawnItem(p.getLocation(), gifts[ThreadLocalRandom.current().nextInt(gifts.length)].clone(), ItemSpawnReason.EASTER_EGG_OPENED, true);
        };
    }

}
