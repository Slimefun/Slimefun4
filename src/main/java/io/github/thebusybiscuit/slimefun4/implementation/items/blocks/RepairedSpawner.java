package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * A {@link RepairedSpawner} is the repaired variant of a {@link BrokenSpawner}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see BrokenSpawner
 *
 */
public class RepairedSpawner extends SimpleSlimefunItem<BlockPlaceHandler> {

    public RepairedSpawner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public BlockPlaceHandler getItemHandler() {
        return new BlockPlaceHandler(true) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                onPlace(e.getItemInHand(), e);
            }

            @Override
            public void onBlockPlacerPlace(BlockPlacerPlaceEvent e) {
                onPlace(e.getItemStack(), e);
            }

            private void onPlace(ItemStack item, BlockEvent e) {
                Optional<EntityType> entity = getEntityType(item);

                if (entity.isPresent() && e.getBlock().getType() == Material.SPAWNER) {
                    CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
                    spawner.setSpawnedType(entity.get());
                    spawner.update(true, false);
                }
            }
        };
    }

    /**
     * This method tries to obtain an {@link EntityType} from a given {@link ItemStack}.
     * The provided {@link ItemStack} must be a {@link RepairedSpawner} item.
     * 
     * @param item
     *            The {@link ItemStack} to extract the {@link EntityType} from
     * 
     * @return An {@link Optional} describing the result
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

    @Override
    public Collection<ItemStack> getDrops() {
        // There should be no drops by default since drops are handled by the
        // Pickaxe of Containment exclusively.
        return new ArrayList<>();
    }

}
