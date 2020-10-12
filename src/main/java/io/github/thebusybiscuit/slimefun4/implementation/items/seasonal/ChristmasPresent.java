package io.github.thebusybiscuit.slimefun4.implementation.items.seasonal;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.FireworkUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link ChristmasPresent} is a seasonal {@link SlimefunItem} that drops a random
 * gift when being placed down.
 * 
 * @author TheBusyBiscuit
 * 
 * @see EasterEgg
 *
 */
public class ChristmasPresent extends SimpleSlimefunItem<BlockPlaceHandler> implements NotPlaceable {

    private final ItemStack[] gifts;

    public ChristmasPresent(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack... gifts) {
        super(category, item, recipeType, recipe);

        this.gifts = gifts;
    }

    @Override
    public BlockPlaceHandler getItemHandler() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                e.setCancelled(true);

                if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(e.getItemInHand(), false);
                }

                FireworkUtils.launchRandom(e.getPlayer(), 3);

                Block b = e.getBlock();
                ItemStack gift = gifts[ThreadLocalRandom.current().nextInt(gifts.length)].clone();
                b.getWorld().dropItemNaturally(b.getLocation(), gift);
            }

        };
    }

}
