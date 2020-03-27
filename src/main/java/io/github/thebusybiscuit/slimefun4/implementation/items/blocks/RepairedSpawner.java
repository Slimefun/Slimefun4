package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public class RepairedSpawner extends SimpleSlimefunItem<BlockPlaceHandler> {

    public RepairedSpawner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockPlaceHandler getItemHandler() {
        return (e, item) -> {
            if (SlimefunUtils.isItemSimilar(item, SlimefunItems.REPAIRED_SPAWNER, false)) {
                EntityType type = null;

                for (String line : item.getItemMeta().getLore()) {
                    if (ChatColor.stripColor(line).startsWith("类型: ") && !line.contains("<类型>")) {
                        type = EntityType.valueOf(ChatColor.stripColor(line).replace("类型: ", "").replace(' ', '_').toUpperCase(Locale.ROOT));
                    }
                }

                if (type != null) {
                    CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
                    spawner.setSpawnedType(type);
                    spawner.update(true, false);
                }

                return true;
            }
            else {
                return false;
            }
        };
    }

}
