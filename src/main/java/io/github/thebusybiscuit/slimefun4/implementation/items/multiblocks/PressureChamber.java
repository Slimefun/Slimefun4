package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
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

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCrafter;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

public class PressureChamber extends MultiBlockMachine implements RecipeCrafter {

    @ParametersAreNonnullByDefault
    public PressureChamber(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { new ItemStack(Material.SMOOTH_STONE_SLAB), new CustomItemStack(Material.DISPENSER, "Dispenser (Facing down)"), new ItemStack(Material.SMOOTH_STONE_SLAB), new ItemStack(Material.PISTON), new ItemStack(Material.GLASS), new ItemStack(Material.PISTON), new ItemStack(Material.PISTON), new ItemStack(Material.CAULDRON), new ItemStack(Material.PISTON) }, BlockFace.UP);
    }

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.PRESSURE_CHAMBER);
    }

    @Override
    public @Nonnull List<ItemStack> getDisplayRecipes() {
        return craftedRecipes.stream().map(items -> items[0]).collect(Collectors.toList());
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block possibleDispenser = b.getRelative(BlockFace.UP).getRelative(BlockFace.UP);
        BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        if (state instanceof final Dispenser dispenser) {
            Inventory inv = dispenser.getInventory();

            if (inv.isEmpty()) {
                Slimefun.getLocalization().sendMessage(p, "machines.inventory-empty", true);
                return;
            }

            ItemStack[] givenItems = dispenser.getInventory().getContents();

            var searchResult = searchRecipes(givenItems, (recipe, match) -> {

                ItemStack recipeOutput = recipe.getOutput().generateOutput();
                MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, givenItems, recipeOutput);

                Bukkit.getPluginManager().callEvent(event);
                ItemStack output = event.getOutput();
                if (event.isCancelled() || !SlimefunUtils.canPlayerUseItem(p, output, true)) {
                    return false;
                }

                Inventory outputInv = findOutputInventory(output, possibleDispenser, inv);
                if (outputInv == null) {
                    Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
                    return false;
                }

                craft(p, b, output, outputInv, possibleDispenser);
                
                return true;
            });

            if (!searchResult.isMatch()) {
                Slimefun.getLocalization().sendMessage(p, "machines.unknown-material", true);
            }
        }
    }

    @ParametersAreNonnullByDefault
    private void craft(Player p, Block b, ItemStack output, Inventory dispInv, Block dispenser) {
        for (int i = 0; i < 4; i++) {
            int j = i;

            Slimefun.runSync(() -> {
                SoundEffect.PRESSURE_CHAMBER_WORKING_SOUND.playAt(b);
                p.getWorld().playEffect(b.getRelative(BlockFace.UP).getLocation(), Effect.SMOKE, 4);
                p.getWorld().playEffect(b.getRelative(BlockFace.UP).getLocation(), Effect.SMOKE, 4);
                p.getWorld().playEffect(b.getRelative(BlockFace.UP).getLocation(), Effect.SMOKE, 4);

                if (j < 3) {
                    SoundEffect.PRESSURE_CHAMBER_WORKING_SOUND.playAt(b);
                } else {
                    SoundEffect.PRESSURE_CHAMBER_FINISH_SOUND.playAt(b);
                    handleCraftedItem(output, dispenser, dispInv);
                }
            }, i * 20L);
        }
    }
}
