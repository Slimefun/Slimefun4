package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.Collection;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
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

public class EnhancedCraftingTable extends AbstractCraftingTable implements RecipeCrafter {

    @ParametersAreNonnullByDefault
    public EnhancedCraftingTable(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.CRAFTING_TABLE), null, null, new ItemStack(Material.DISPENSER), null }, BlockFace.SELF);
    }

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.ENHANCED_CRAFTING_TABLE);
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block possibleDispenser = b.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        if (state instanceof final Dispenser dispenser) {
            Inventory inv = dispenser.getInventory();

            if (inv.isEmpty()) {
                Slimefun.getLocalization().sendMessage(p, "machines.inventory-empty", true);
                return;
            }

            ItemStack[] givenInputs = dispenser.getInventory().getContents();

            var searchResult = searchRecipes(givenInputs, (recipe, match) -> {

                ItemStack recipeOutput = recipe.getOutput().generateOutput();
                MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, givenInputs, recipeOutput);

                Bukkit.getPluginManager().callEvent(event);
                ItemStack output = event.getOutput();
                if (event.isCancelled() || !SlimefunUtils.canPlayerUseItem(p, output, true)) {
                    return false;
                }

                Inventory fakeInv = createVirtualInventory(inv);
                Inventory outputInv = findOutputInventory(output, possibleDispenser, inv, fakeInv);
                if (outputInv == null) {
                    Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
                    return false;
                }
            
                SlimefunItem sfItem = SlimefunItem.getByItem(output);

                if (sfItem instanceof final SlimefunBackpack backpack) {
                    upgradeBackpack(p, inv, backpack, output);
                }

                SoundEffect.ENHANCED_CRAFTING_TABLE_CRAFT_SOUND.playAt(b);
                outputInv.addItem(output);

                return true;
            });

            if (!searchResult.isMatch()) {
                Slimefun.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
            }
        }
    }
}
