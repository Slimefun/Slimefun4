package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.ChatColor;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Optional;

public class RepairedSpawner extends SimpleSlimefunItem<BlockPlaceHandler> {

    public RepairedSpawner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockPlaceHandler getItemHandler() {
        return (p, e, item) -> {
            if (SlimefunUtils.isItemSimilar(item, SlimefunItems.REPAIRED_SPAWNER, false, false)) {
                Optional<EntityType> entity = getEntityType(item);

                if (entity.isPresent()) {
                    CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
                    spawner.setSpawnedType(entity.get());
                    spawner.update(true, false);
                }

                return true;
            } else {
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
            EntityType type;
            if (ChatColor.stripColor(line).startsWith("类型: ") && !line.contains("<类型>")) {
                type = EntityType.valueOf(ChatColor.stripColor(line).replace("类型: ", "").replace(' ', '_').toUpperCase(Locale.ROOT));
                return Optional.of(type);
            }
            if (ChatColor.stripColor(line).startsWith("Type: ") && !line.contains("<Type>")) {
                type = EntityType.valueOf(ChatColor.stripColor(line).replace("Type: ", "").replace(' ', '_').toUpperCase(Locale.ROOT));
                return Optional.of(type);
            }
        }

        return Optional.empty();
    }

}
