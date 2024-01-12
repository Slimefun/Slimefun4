package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCrafter;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeSearchResult;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeStructure;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.OutputChest;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

/**
 * The {@link TableSaw} is an implementation of a {@link MultiBlockMachine} that allows
 * you to turn Logs into Wooden Planks.
 * 
 * It also replaced the old "Saw Mill" from earlier versions.
 * 
 * @author dniym
 * @author svr333
 * @author TheBusyBiscuit
 * 
 * @see MultiBlockMachine
 *
 */
public class TableSaw extends MultiBlockMachine implements RecipeCrafter {

    private final List<ItemStack> displayedRecipes = new ArrayList<>();

    @ParametersAreNonnullByDefault
    public TableSaw(ItemGroup group, SlimefunItemStack item) {
        // @formatter:off
        super(group, item, new ItemStack[] {
            null, null, null,
            new ItemStack(Material.SMOOTH_STONE_SLAB), new ItemStack(Material.STONECUTTER), new ItemStack(Material.SMOOTH_STONE_SLAB),
            null, new ItemStack(Material.IRON_BLOCK), null
        }, BlockFace.SELF);
        // @formatter:on

        for (Material log : Tag.LOGS.getValues()) {
            Optional<Material> planks = getPlanks(log);

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
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.TABLE_SAW);
    }

    @Override
    protected void registerDefaultRecipes(List<ItemStack> recipes) {
        for (final Material log : Tag.LOGS.getValues()) {
            Optional<Material> planks = getPlanks(log);
            if (planks.isPresent()) {
                RecipeCategory.TABLE_SAW.registerRecipe(Recipe.of(RecipeStructure.SUBSET, new ItemStack(log), new ItemStack(planks.get(), 8)));
            }
        }
        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_20)) {
            RecipeCategory.TABLE_SAW.registerRecipe(Recipe.of(RecipeStructure.SUBSET, new ItemStack(Material.BAMBOO_BLOCK), new ItemStack(Material.BAMBOO_PLANKS, 4)));
            RecipeCategory.TABLE_SAW.registerRecipe(Recipe.of(RecipeStructure.SUBSET, new ItemStack(Material.STRIPPED_BAMBOO_BLOCK), new ItemStack(Material.BAMBOO_PLANKS, 4)));
        }
        for (final Material plank : Tag.PLANKS.getValues()) {
            RecipeCategory.TABLE_SAW.registerRecipe(Recipe.of(RecipeStructure.SUBSET, new ItemStack(plank), new ItemStack(Material.STICK, 4)));
        }
    }

    /**
     * This method returns the corresponding plank {@link Material} for a given wood {@link Material}.
     * The result is wrapped by an {@link Optional}.
     * <p>
     * {@literal Material.OAK_LOG} for example will return {@literal Material.OAK_PLANKS}.
     * 
     * @param log
     *            The log type.
     * 
     * @return An {@link Optional} containing the corresponding plank type (or an empty {@link Optional})
     */
    private @Nonnull Optional<Material> getPlanks(@Nonnull Material log) {
        String materialName = log.name().replace("STRIPPED_", "");
        int endIndex = materialName.lastIndexOf('_');

        if (endIndex > 0) {
            materialName = materialName.substring(0, endIndex) + "_PLANKS";
            return Optional.ofNullable(Material.getMaterial(materialName));
        } else {
            // Fixed #3651 - Do not panic because of one weird wood type.
            warn("Could not find a corresponding plank for wood type: '" + log.name() + "'");
            return Optional.empty();
        }
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return displayedRecipes;
    }

    @Override
    public void onInteract(@Nonnull Player p, @Nonnull Block b) {
        ItemStack item = p.getInventory().getItemInMainHand();
        if (item == null) {
            return;
        }

        ItemStack[] givenItem = new ItemStack[] { item };
        RecipeSearchResult searchResult = searchRecipes(givenItem, (recipe, matchResult) -> {

            ItemStack recipeOutput = recipe.getOutput().generateOutput();
            MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, item, recipeOutput);

            Bukkit.getPluginManager().callEvent(event);
            ItemStack output = event.getOutput();
            if (event.isCancelled() || !SlimefunUtils.canPlayerUseItem(p, output, true)) {
                return false;
            }
            
            outputItems(b, output);
            b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, item.getType());
            
            return p.getGameMode() != GameMode.CREATIVE;
        });

        if (!searchResult.isMatch()) {
            Slimefun.getLocalization().sendMessage(p, "machines.wrong-item", true);
        }
    }

    private @Nullable ItemStack getOutputFromMaterial(@Nonnull Material item) {
        if (Tag.LOGS.isTagged(item)) {
            Optional<Material> planks = getPlanks(item);

            if (planks.isPresent()) {
                return new ItemStack(planks.get(), 8);
            } else {
                return null;
            }
        } else if (Tag.PLANKS.isTagged(item)) {
            return new ItemStack(Material.STICK, 4);
        } else {
            return null;
        }
    }

    private void outputItems(@Nonnull Block b, @Nonnull ItemStack output) {
        Optional<Inventory> outputChest = OutputChest.findOutputChestFor(b, output);

        if (outputChest.isPresent()) {
            outputChest.get().addItem(output);
        } else {
            b.getWorld().dropItemNaturally(b.getLocation(), output);
        }
    }
}
