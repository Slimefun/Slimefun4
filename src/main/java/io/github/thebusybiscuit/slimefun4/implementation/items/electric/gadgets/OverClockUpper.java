package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class OverClockUpper extends SimpleSlimefunItem<ItemUseHandler> {
    public OverClockUpper(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();

            if (block.isPresent()) {
                Block b = block.get();

                if (BlockStorage.hasBlockInfo(b)) {
                    e.cancel();

                    if (e.getPlayer().isOp()) {
                        String json = BlockStorage.getBlockInfoAsJson(b);
                        e.getPlayer().sendMessage(json);
                    } else {
                        e.getPlayer().sendMessage("这个物品还没有实装");
                    }
                }
            }
        };
    }
}
