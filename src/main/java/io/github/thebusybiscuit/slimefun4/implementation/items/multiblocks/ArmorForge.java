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

import io.github.bakedlibs.dough.items.CustomItemStack;
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

public class ArmorForge extends AbstractCraftingTable implements RecipeCrafter {

    @ParametersAreNonnullByDefault
    public ArmorForge(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.ANVIL), null, null, new CustomItemStack(Material.DISPENSER, "Dispenser (Facing up)"), null }, BlockFace.SELF);
    }

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.ARMOR_FORGE);
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

                for (int j = 0; j < 4; j++) {
                    int current = j;

                    Slimefun.runSync(() -> {
                        if (current < 3) {
                            SoundEffect.ARMOR_FORGE_WORKING_SOUND.playAt(possibleDispenser);
                        } else {
                            SoundEffect.ARMOR_FORGE_FINISH_SOUND.playAt(possibleDispenser);
                            handleCraftedItem(output, possibleDispenser, inv);
                        }
                    }, j * 20L);
                }

                return true;
            });

            if (!searchResult.isMatch()) {
                Slimefun.getLocalization().sendMessage(p, "machines.pattern-not-found", true);
            }
        }
    }
}
