package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
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
public class RepairedSpawner extends AbstractMonsterSpawner {

    @ParametersAreNonnullByDefault
    public RepairedSpawner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(new BlockPlaceHandler(true) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                onPlace(e.getItemInHand(), e);
            }

            @Override
            public void onBlockPlacerPlace(BlockPlacerPlaceEvent e) {
                onPlace(e.getItemStack(), e);
            }

            @ParametersAreNonnullByDefault
            private void onPlace(ItemStack item, BlockEvent e) {
                if (e.getBlock().getType() == Material.SPAWNER) {
                    getEntityType(item).ifPresent(entity -> {
                        CreatureSpawner spawner = (CreatureSpawner) e.getBlock().getState();
                        spawner.setSpawnedType(entity);
                        spawner.update(true, false);
                    });
                }
            }
        });
    }

    @Override
    public Collection<ItemStack> getDrops() {
        /**
         * There should be no drops by default since drops are handled
         * by the Pickaxe of Containment exclusively.
         */
        return new ArrayList<>();
    }

}
