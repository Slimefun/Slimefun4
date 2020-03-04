package io.github.thebusybiscuit.slimefun4.implementation.items.geo;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class PortableGEOScanner extends SimpleSlimefunItem<ItemUseHandler> {

    public PortableGEOScanner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();
            e.cancel();

            if (block.isPresent()) {
                SlimefunPlugin.getGPSNetwork().getResourceManager().scan(e.getPlayer(), block.get());
            }
        };
    }

}
