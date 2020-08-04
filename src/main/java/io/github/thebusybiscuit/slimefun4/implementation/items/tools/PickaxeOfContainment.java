package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.BrokenSpawner;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.RepairedSpawner;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link PickaxeOfContainment} is a Pickaxe that allows you to break Spawners.
 * Upon breaking a Spawner, a {@link BrokenSpawner} will be dropped.
 * But it also allows you to break a {@link RepairedSpawner}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see BrokenSpawner
 * @see RepairedSpawner
 *
 */
public class PickaxeOfContainment extends SimpleSlimefunItem<ToolUseHandler> {

    public PickaxeOfContainment(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            Block b = e.getBlock();

            if (b.getType() == Material.SPAWNER) {
                ItemStack spawner = breakSpawner(b);
                b.getLocation().getWorld().dropItemNaturally(b.getLocation(), spawner);

                e.setExpToDrop(0);
                e.setDropItems(false);
            }
        };
    }

    private ItemStack breakSpawner(Block b) {
        // If the spawner's BlockStorage has BlockInfo, then it's not a vanilla spawner and
        // should not give a broken spawner.
        ItemStack spawner = SlimefunItems.BROKEN_SPAWNER.clone();

        if (BlockStorage.hasBlockInfo(b)) {
            spawner = SlimefunItems.REPAIRED_SPAWNER.clone();
        }

        ItemMeta im = spawner.getItemMeta();
        List<String> lore = im.getLore();

        BlockState state = PaperLib.getBlockState(b, false).getState();

        if (state instanceof CreatureSpawner) {
            EntityType entityType = ((CreatureSpawner) state).getSpawnedType();

            for (int i = 0; i < lore.size(); i++) {
                if (lore.get(i).contains("<Type>")) {
                    lore.set(i, lore.get(i).replace("<Type>", ChatUtils.humanize(entityType.name())));
                    break;
                }
            }

            im.setLore(lore);
            spawner.setItemMeta(im);
            return spawner;
        }

        return new ItemStack(Material.SPAWNER);
    }

}
