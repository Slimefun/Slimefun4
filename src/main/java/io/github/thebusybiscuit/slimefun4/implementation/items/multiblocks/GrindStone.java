package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.ArrayList;
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
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCrafter;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeStructure;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

public class GrindStone extends MultiBlockMachine implements RecipeCrafter {

    @ParametersAreNonnullByDefault
    public GrindStone(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.OAK_FENCE), null, null, new CustomItemStack(Material.DISPENSER, "Dispenser (Facing up)"), null }, BlockFace.SELF);
    }

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.GRIND_STONE);
    }

    @Override
    protected void registerDefaultRecipes(@Nonnull List<ItemStack> recipes) {
        final List<ItemStack> defaultRecipes = new ArrayList<>();

        defaultRecipes.add(new ItemStack(Material.BLAZE_ROD));
        defaultRecipes.add(new ItemStack(Material.BLAZE_POWDER, 4));

        defaultRecipes.add(new ItemStack(Material.BONE));
        defaultRecipes.add(new ItemStack(Material.BONE_MEAL, 4));

        defaultRecipes.add(new ItemStack(Material.BONE_BLOCK));
        defaultRecipes.add(new ItemStack(Material.BONE_MEAL, 9));

        defaultRecipes.add(new ItemStack(Material.COBBLESTONE));
        defaultRecipes.add(new ItemStack(Material.GRAVEL));

        defaultRecipes.add(new ItemStack(Material.ANDESITE));
        defaultRecipes.add(new ItemStack(Material.GRAVEL));

        defaultRecipes.add(new ItemStack(Material.BLACKSTONE));
        defaultRecipes.add(new ItemStack(Material.GRAVEL));

        defaultRecipes.add(new ItemStack(Material.DIORITE));
        defaultRecipes.add(new ItemStack(Material.GRAVEL));

        defaultRecipes.add(new ItemStack(Material.GRANITE));
        defaultRecipes.add(new ItemStack(Material.GRAVEL));

        defaultRecipes.add(new ItemStack(Material.DIRT));
        defaultRecipes.add(SlimefunItems.STONE_CHUNK);

        defaultRecipes.add(new ItemStack(Material.SANDSTONE));
        defaultRecipes.add(new ItemStack(Material.SAND, 4));

        defaultRecipes.add(new ItemStack(Material.RED_SANDSTONE));
        defaultRecipes.add(new ItemStack(Material.RED_SAND, 4));

        defaultRecipes.add(new ItemStack(Material.PRISMARINE_BRICKS));
        defaultRecipes.add(new ItemStack(Material.PRISMARINE, 2));

        defaultRecipes.add(new ItemStack(Material.PRISMARINE));
        defaultRecipes.add(new ItemStack(Material.PRISMARINE_SHARD, 4));

        defaultRecipes.add(new ItemStack(Material.NETHER_WART_BLOCK));
        defaultRecipes.add(new ItemStack(Material.NETHER_WART, 9));

        defaultRecipes.add(new ItemStack(Material.QUARTZ_BLOCK));
        defaultRecipes.add(new ItemStack(Material.QUARTZ, 4));

        defaultRecipes.add(new ItemStack(Material.BASALT, 2));
        defaultRecipes.add(new ItemStack(Material.BLACKSTONE));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            defaultRecipes.add(new ItemStack(Material.AMETHYST_BLOCK));
            defaultRecipes.add(new ItemStack(Material.AMETHYST_SHARD, 4));

            defaultRecipes.add(new ItemStack(Material.COBBLED_DEEPSLATE));
            defaultRecipes.add(new ItemStack(Material.GRAVEL));
        }

        defaultRecipes.add(SlimefunItems.MAGIC_LUMP_2);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_1, 4));

        defaultRecipes.add(SlimefunItems.MAGIC_LUMP_3);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.MAGIC_LUMP_2, 4));

        defaultRecipes.add(SlimefunItems.ENDER_LUMP_2);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.ENDER_LUMP_1, 4));

        defaultRecipes.add(SlimefunItems.ENDER_LUMP_3);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.ENDER_LUMP_2, 4));

        defaultRecipes.add(new ItemStack(Material.DIAMOND));
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.CARBON, 4));

        for (int i = 0; i+1 < defaultRecipes.size(); i+=2) {
            RecipeCategory.GRIND_STONE.registerRecipe(Recipe.of(RecipeStructure.SUBSET, defaultRecipes.get(i), defaultRecipes.get(i+1)));
        }

        recipes.addAll(defaultRecipes);
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

                final ItemStack output = recipe.getOutput().generateOutput();
                final MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, givenItems, output);
                Bukkit.getPluginManager().callEvent(event);

                if (event.isCancelled() || !SlimefunUtils.canPlayerUseItem(p, output, true)) {
                    return false;
                }

                final Inventory outputInv = findOutputInventory(output, possibleDispenser, inv);
                if (outputInv == null) {
                    Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
                    return false;
                }

                SoundEffect.GRIND_STONE_INTERACT_SOUND.playAt(b);
                outputInv.addItem(event.getOutput());
                
                return true;
            });

            if (!searchResult.isMatch()) {
                Slimefun.getLocalization().sendMessage(p, "machines.unknown-material", true);
            }
        }
    }
}
