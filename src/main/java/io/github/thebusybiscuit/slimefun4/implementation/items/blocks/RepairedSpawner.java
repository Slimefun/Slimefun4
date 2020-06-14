package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.Locale;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockPlaceHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class RepairedSpawner extends SimpleSlimefunItem<BlockPlaceHandler> {

    public RepairedSpawner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockPlaceHandler getItemHandler() {
        return (e, item) -> {
            // We need to explicitly ignore the lore here
            if (SlimefunUtils.isItemSimilar(item, SlimefunItems.REPAIRED_SPAWNER, false, false)) {
                Optional<EntityType> entity = getEntityType(item);

                if (entity.isPresent()) {
                    CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
                    spawner.setSpawnedType(entity.get());
                    spawner.update(true, false);
                }

                return true;
            }
            else {
                return false;
            }
        };
    }

    /**
     * This method tries to obtain an {@link EntityType} from a given {@link ItemStack}.
     * The provided {@link ItemStack} must be a {@link RepairedSpawner} item.
     * 
     * @param item
     * @return
     */
    public Optional<EntityType> getEntityType(ItemStack item) {
        for (String line : item.getItemMeta().getLore()) {
            if (ChatColor.stripColor(line).startsWith("Type: ") && !line.contains("<Type>")) {
                EntityType type = EntityType.valueOf(ChatColor.stripColor(line).replace("Type: ", "").replace(' ', '_').toUpperCase(Locale.ROOT));
                return Optional.of(type);
            }
        }

        return Optional.empty();
    }

}
