package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.Collection;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCrafter;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.SlimefunBackpack;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

public class MagicWorkbench extends AbstractCraftingTable implements RecipeCrafter {

    @ParametersAreNonnullByDefault
    public MagicWorkbench(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, null, null, new ItemStack(Material.BOOKSHELF), new ItemStack(Material.CRAFTING_TABLE), new ItemStack(Material.DISPENSER) }, BlockFace.UP);
    }

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.MAGIC_WORKBENCH);
    }

    @Override
    public void onInteract(Player p, Block b) {
        final Block possibleDispenser = locateDispenser(b);

        if (possibleDispenser == null) {
            // How even...
            return;
        }

        final BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        if (state instanceof final Dispenser dispenser) {
            final Inventory inv = dispenser.getInventory();
            
            if (inv.isEmpty()) {
                Slimefun.getLocalization().sendMessage(p, "machines.inventory-empty", true);
                return;
            }

            final ItemStack[] givenInputs = dispenser.getInventory().getContents();

            final var searchResult = searchRecipes(givenInputs, (recipe, match) -> {

                final ItemStack recipeOutput = recipe.getOutput().generateOutput();
                final MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, givenInputs, recipeOutput);

                Bukkit.getPluginManager().callEvent(event);
                final ItemStack output = event.getOutput();
                if (event.isCancelled() || !SlimefunUtils.canPlayerUseItem(p, output, true)) {
                    return false;
                }

                final Inventory fakeInv = createVirtualInventory(inv);
                final Inventory outputInv = findOutputInventory(output, possibleDispenser, inv, fakeInv);
                if (outputInv == null) {
                    Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
                    return false;
                }
                
                final SlimefunItem sfItem = SlimefunItem.getByItem(output);

                if (sfItem instanceof final SlimefunBackpack backpack) {
                    upgradeBackpack(p, inv, backpack, output);
                }

                startAnimation(p, b, inv, possibleDispenser, recipeOutput);

                return true;
            });

            if (!searchResult.isMatch()) {
                Slimefun.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
            }
        }
    }

    private void startAnimation(Player p, Block b, Inventory dispInv, Block dispenser, ItemStack output) {
        for (int j = 0; j < 4; j++) {
            int current = j;
            Slimefun.runSync(() -> {
                p.getWorld().playEffect(b.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
                p.getWorld().playEffect(b.getLocation(), Effect.ENDER_SIGNAL, 1);

                if (current < 3) {
                    SoundEffect.MAGIC_WORKBENCH_START_ANIMATION_SOUND.playAt(b);
                } else {
                    SoundEffect.MAGIC_WORKBENCH_FINISH_SOUND.playAt(b);
                    handleCraftedItem(output, dispenser, dispInv);
                }
            }, j * 20L);
        }
    }

    private Block locateDispenser(Block b) {
        Block block = null;

        if (b.getRelative(1, 0, 0).getType() == Material.DISPENSER) {
            block = b.getRelative(1, 0, 0);
        } else if (b.getRelative(0, 0, 1).getType() == Material.DISPENSER) {
            block = b.getRelative(0, 0, 1);
        } else if (b.getRelative(-1, 0, 0).getType() == Material.DISPENSER) {
            block = b.getRelative(-1, 0, 0);
        } else if (b.getRelative(0, 0, -1).getType() == Material.DISPENSER) {
            block = b.getRelative(0, 0, -1);
        }

        return block;
    }
}
