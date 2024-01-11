package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCrafter;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

/**
 * An abstract super class for the {@link Smeltery} and
 * {@link MakeshiftSmeltery}.
 * 
 * @author TheBusyBiscuit
 *
 */
abstract class AbstractSmeltery extends MultiBlockMachine implements RecipeCrafter {

    @ParametersAreNonnullByDefault
    protected AbstractSmeltery(ItemGroup itemGroup, SlimefunItemStack item, ItemStack[] recipe, BlockFace trigger) {
        super(itemGroup, item, recipe, trigger);
    }

    @Override
    public void onInteract(Player p, Block b) {
        final Block possibleDispenser = b.getRelative(BlockFace.DOWN);
        final BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        if (state instanceof final Dispenser dispenser) {
            final Inventory inv = dispenser.getInventory();

            if (inv.isEmpty()) {
                Slimefun.getLocalization().sendMessage(p, "machines.inventory-empty", true);
                return;
            }

            final ItemStack[] givenItems = dispenser.getInventory().getContents();

            final var searchResult = searchRecipes(givenItems, (recipe, match) -> {

                final ItemStack recipeOutput = recipe.getOutput().generateOutput();
                final MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, givenItems, recipeOutput);

                Bukkit.getPluginManager().callEvent(event);
                final ItemStack output = event.getOutput();
                if (event.isCancelled() || !SlimefunUtils.canPlayerUseItem(p, output, true)) {
                    return false;
                }

                final Inventory outputInv = findOutputInventory(output, possibleDispenser, inv);
                if (outputInv == null) {
                    Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
                    return false;
                }

                craft(p, b, inv, givenItems, output, outputInv);
                
                return true;
            });

            if (!searchResult.isMatch()) {
                Slimefun.getLocalization().sendMessage(p, "machines.unknown-material", true);
            }
        }
    }

    protected void craft(Player p, Block b, Inventory inv, ItemStack[] givenItems, ItemStack output, Inventory outputInv) {
        outputInv.addItem(output);
        SoundEffect.SMELTERY_CRAFT_SOUND.playAt(b);
        p.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
    }
}
