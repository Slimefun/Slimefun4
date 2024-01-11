package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
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
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCrafter;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeStructure;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

public class Compressor extends MultiBlockMachine implements RecipeCrafter {

    @ParametersAreNonnullByDefault
    public Compressor(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.PISTON), new CustomItemStack(Material.DISPENSER, "Dispenser (Facing up)"), new ItemStack(Material.PISTON) }, BlockFace.SELF);
    }

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.COMPRESSOR);
    }

    @Override
    protected void registerDefaultRecipes(List<ItemStack> recipes) {
        recipes.add(new SlimefunItemStack(SlimefunItems.STONE_CHUNK, 4));
        recipes.add(new ItemStack(Material.COBBLESTONE));

        recipes.add(new ItemStack(Material.FLINT, 8));
        recipes.add(new ItemStack(Material.COBBLESTONE));

        recipes.add(new ItemStack(Material.COAL_BLOCK, 8));
        recipes.add(new SlimefunItemStack(SlimefunItems.CARBON, 9));

        recipes.add(new ItemStack(Material.CHARCOAL, 4));
        recipes.add(new ItemStack(Material.COAL));

        RecipeCategory.COMPRESSOR.registerRecipes(List.of(
            Recipe.of(RecipeStructure.SUBSET, new SlimefunItemStack(SlimefunItems.STONE_CHUNK, 4), new ItemStack(Material.COBBLESTONE)),
            Recipe.of(RecipeStructure.SUBSET, new ItemStack(Material.FLINT, 4), new ItemStack(Material.COBBLESTONE)),
            Recipe.of(RecipeStructure.SUBSET, new ItemStack(Material.COAL_BLOCK, 4), new SlimefunItemStack(SlimefunItems.CARBON, 9)),
            Recipe.of(RecipeStructure.SUBSET, new ItemStack(Material.CHARCOAL, 4), new ItemStack(Material.COAL))
        ));
    }

    @Override
    public @Nonnull List<ItemStack> getDisplayRecipes() {
        return craftedRecipes.stream().map(items -> items[0]).collect(Collectors.toList());
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

                craft(p, output, b, outputInv);
                
                return true;
            });

            if (!searchResult.isMatch()) {
                Slimefun.getLocalization().sendMessage(p, "machines.unknown-material", true);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void craft(Player p, ItemStack output, Block dispenser, Inventory dispInv) {
        for (int i = 0; i < 4; i++) {
            int j = i;

            Slimefun.runSync(() -> {
                if (j < 3) {
                    if (j == 1) {
                        SoundEffect.COMPRESSOR_CRAFT_CONTRACT_SOUND.playFor(p);
                    } else {
                        SoundEffect.COMPRESSOR_CRAFT_EXTEND_SOUND.playFor(p);
                    }
                 } else {
                    handleCraftedItem(output, dispenser, dispInv);
                }
            }, i * 20L);
        }
    }
}
