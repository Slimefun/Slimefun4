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
import io.github.thebusybiscuit.slimefun4.implementation.items.food.Juice;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

/**
 * The {@link Juicer} is a {@link MultiBlockMachine} which can be used to
 * craft {@link Juice}.
 * 
 * @author TheBusyBiscuit
 * @author Liruxo
 * 
 * @see Juice
 *
 */
public class Juicer extends MultiBlockMachine implements RecipeCrafter {

    @ParametersAreNonnullByDefault
    public Juicer(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, new ItemStack(Material.GLASS), null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, null, new CustomItemStack(Material.DISPENSER, "Dispenser (Facing up)"), null }, BlockFace.SELF);
    }

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.JUICER);
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

                outputInv.addItem(event.getOutput());
                SoundEffect.JUICER_USE_SOUND.playAt(b);
                // Not changed since this is supposed to be a natural sound.
                p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, Material.HAY_BLOCK);
                
                return true;
            });

            if (!searchResult.isMatch()) {
                Slimefun.getLocalization().sendMessage(p, "machines.unknown-material", true);
            }
        }
    }
}
