package io.github.thebusybiscuit.slimefun4.implementation.items.seasonal;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.utils.FireworkUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class ChristmasPresent extends SimpleSlimefunItem<BlockPlaceHandler> implements NotPlaceable {

    private final ItemStack[] gifts;

    public ChristmasPresent(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack... gifts) {
        super(category, item, recipeType, recipe);

        this.gifts = gifts;
    }

    @Override
    public BlockPlaceHandler getItemHandler() {
        return (p, e, item) -> {
            if (isItem(item)) {
                e.setCancelled(true);

                if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    ItemUtils.consumeItem(item, false);
                }

                FireworkUtils.launchRandom(p, 3);

                ItemStack gift = gifts[ThreadLocalRandom.current().nextInt(gifts.length)].clone();
                e.getBlockPlaced().getWorld().dropItemNaturally(e.getBlockPlaced().getLocation(), gift);
                return true;
            }

            return false;
        };
    }

}