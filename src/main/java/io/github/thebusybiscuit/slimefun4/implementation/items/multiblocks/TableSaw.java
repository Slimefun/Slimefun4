package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialConverter;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * The {@link TableSaw} is an implementation of a {@link MultiBlockMachine} that allows
 * you to turn Logs into Wooden Planks.
 * 
 * It also replaced the old "Saw Mill" from earlier versions.
 * 
 * @author dniym
 * 
 * @see MultiBlockMachine
 *
 */
public class TableSaw extends MultiBlockMachine {

    private final List<ItemStack> displayedRecipes = new ArrayList<>();

    public TableSaw(Category category, SlimefunItemStack item) {
        super(category, item, new ItemStack[] { null, null, null, new ItemStack(Material.SMOOTH_STONE_SLAB), new ItemStack(Material.STONECUTTER), new ItemStack(Material.SMOOTH_STONE_SLAB), null, new ItemStack(Material.IRON_BLOCK), null }, BlockFace.SELF);

        for (Material log : Tag.LOGS.getValues()) {
            Optional<Material> planks = MaterialConverter.getPlanksFromLog(log);

            if (planks.isPresent()) {
                displayedRecipes.add(new ItemStack(log));
                displayedRecipes.add(new ItemStack(planks.get(), 8));
            }
        }

        for (Material plank : Tag.PLANKS.getValues()) {
            displayedRecipes.add(new ItemStack(plank));
            displayedRecipes.add(new ItemStack(Material.STICK, 4));
        }
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return displayedRecipes;
    }

    @Override
    public void onInteract(Player p, Block b) {
        ItemStack item = p.getInventory().getItemInMainHand();

        boolean itemIsAPlank = false;
        Optional<Material> planks = MaterialConverter.getPlanksFromLog(item.getType());

        for (Material plank: Tag.PLANKS.getValues()) {
            if (item.getType() == plank) {
                itemIsAPlank = true;
                break;
            }
        }

        if (planks.isPresent() || itemIsAPlank) {
            ItemStack output = null;

            if (p.getGameMode() != GameMode.CREATIVE) {
                ItemUtils.consumeItem(item, true);
            }

            if (planks.isPresent()) {
                output = new ItemStack(planks.get(), 8);
            }
            else if (itemIsAPlank) {
                output = new ItemStack(Material.STICK, 4);
            }

            Inventory outputChest = findOutputChest(b, output);

            if (outputChest != null) {
                outputChest.addItem(output);
            }
            else {
                b.getWorld().dropItemNaturally(b.getLocation(), output);
            }

            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, item.getType());
        }
    }

}
