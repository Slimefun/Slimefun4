package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.ArrayList;
import java.util.Arrays;
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
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.Recipe;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCategory;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeCrafter;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeStructure;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

/**
 * The {@link OreCrusher} is a {@link MultiBlockMachine} which allows you to double ores
 * and crush some other {@link Material Materials} into various resources.
 * 
 * @author TheBusyBiscuit
 *
 */
public class OreCrusher extends MultiBlockMachine implements RecipeCrafter {

    private final DoubleOreSetting doubleOres = new DoubleOreSetting(this);

    @ParametersAreNonnullByDefault
    public OreCrusher(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.IRON_BARS), new CustomItemStack(Material.DISPENSER, "Dispenser (Facing up)"), new ItemStack(Material.IRON_BARS) }, BlockFace.SELF);

        addItemSetting(doubleOres);
    }

    @Override
    public Collection<RecipeCategory> getCraftedCategories() {
        return List.of(RecipeCategory.ORE_CRUSHER);
    }

    @Override
    protected void registerDefaultRecipes(List<ItemStack> recipes) {
        List<ItemStack> defaultRecipes = new ArrayList<>();

        defaultRecipes.add(new ItemStack(Material.BLACKSTONE, 8));
        defaultRecipes.add(new ItemStack(Material.RED_SAND, 1));

        defaultRecipes.add(new ItemStack(Material.COBBLESTONE, 8));
        defaultRecipes.add(new ItemStack(Material.SAND, 1));

        defaultRecipes.add(SlimefunItems.GOLD_4K);
        defaultRecipes.add(SlimefunItems.GOLD_DUST);

        defaultRecipes.add(SlimefunItems.GOLD_6K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 2));

        defaultRecipes.add(SlimefunItems.GOLD_8K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 2));

        defaultRecipes.add(SlimefunItems.GOLD_10K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 3));

        defaultRecipes.add(SlimefunItems.GOLD_12K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 3));

        defaultRecipes.add(SlimefunItems.GOLD_14K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 4));

        defaultRecipes.add(SlimefunItems.GOLD_16K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 4));

        defaultRecipes.add(SlimefunItems.GOLD_18K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 5));

        defaultRecipes.add(SlimefunItems.GOLD_20K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 5));

        defaultRecipes.add(SlimefunItems.GOLD_22K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 6));

        defaultRecipes.add(SlimefunItems.GOLD_24K);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 6));

        defaultRecipes.add(new ItemStack(Material.GRAVEL));
        defaultRecipes.add(new ItemStack(Material.SAND));

        defaultRecipes.add(new ItemStack(Material.MAGMA_BLOCK));
        defaultRecipes.add(SlimefunItems.SULFATE);

        defaultRecipes.add(SlimefunItems.CARBON);
        defaultRecipes.add(new ItemStack(Material.COAL, 8));

        defaultRecipes.add(SlimefunItems.COMPRESSED_CARBON);
        defaultRecipes.add(new SlimefunItemStack(SlimefunItems.CARBON, 4));

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            defaultRecipes.add(new ItemStack(Material.COBBLED_DEEPSLATE, 8));
            defaultRecipes.add(new ItemStack(Material.SAND, 1));
        }

        for (int i = 0; i+1 < defaultRecipes.size(); i+=2) {
            RecipeCategory.ORE_CRUSHER.registerRecipe(Recipe.of(RecipeStructure.SUBSET, defaultRecipes.get(i), defaultRecipes.get(i+1)));
        }

        recipes.addAll(defaultRecipes);
    }

    public boolean isOreDoublingEnabled() {
        return doubleOres.getValue();
    }

    @Override
    public void postRegister() {
        super.postRegister();

        List<ItemStack> oreRecipes = new ArrayList<>();

        // @formatter:off
        oreRecipes.addAll(Arrays.asList(
            new ItemStack(Material.COAL_ORE), doubleOres.getCoal(),
            new ItemStack(Material.LAPIS_ORE), doubleOres.getLapisLazuli(),
            new ItemStack(Material.REDSTONE_ORE), doubleOres.getRedstone(),
            new ItemStack(Material.DIAMOND_ORE), doubleOres.getDiamond(),
            new ItemStack(Material.EMERALD_ORE), doubleOres.getEmerald(),
            new ItemStack(Material.NETHER_QUARTZ_ORE), doubleOres.getNetherQuartz()
        ));
        // @formatter:on

        // Gold ore variants (1.16+)
        oreRecipes.add(new ItemStack(Material.NETHER_GOLD_ORE));
        oreRecipes.add(doubleOres.getGoldNuggets());

        oreRecipes.add(new ItemStack(Material.GILDED_BLACKSTONE));
        oreRecipes.add(doubleOres.getGoldNuggets());

        // Raw metal ores (1.17+)
        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            oreRecipes.add(new ItemStack(Material.RAW_IRON));
            oreRecipes.add(SlimefunItems.IRON_DUST);

            oreRecipes.add(new ItemStack(Material.RAW_COPPER));
            oreRecipes.add(SlimefunItems.COPPER_DUST);

            oreRecipes.add(new ItemStack(Material.RAW_GOLD));
            oreRecipes.add(SlimefunItems.GOLD_DUST);
        }

        // Deepslate Ores (1.17+)
        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            // @formatter:off
            oreRecipes.addAll(Arrays.asList(
                new ItemStack(Material.DEEPSLATE_COAL_ORE), doubleOres.getCoal(),
                new ItemStack(Material.DEEPSLATE_LAPIS_ORE), doubleOres.getLapisLazuli(),
                new ItemStack(Material.DEEPSLATE_REDSTONE_ORE), doubleOres.getRedstone(),
                new ItemStack(Material.DEEPSLATE_DIAMOND_ORE), doubleOres.getDiamond(),
                new ItemStack(Material.DEEPSLATE_EMERALD_ORE), doubleOres.getEmerald()
            ));
            // @formatter:on

            // More deepslate ores and copper ore
            oreRecipes.add(new ItemStack(Material.DEEPSLATE_IRON_ORE));
            oreRecipes.add(new SlimefunItemStack(SlimefunItems.IRON_DUST, isOreDoublingEnabled() ? 2 : 1));

            oreRecipes.add(new ItemStack(Material.DEEPSLATE_GOLD_ORE));
            oreRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, isOreDoublingEnabled() ? 2 : 1));

            oreRecipes.add(new ItemStack(Material.DEEPSLATE_COPPER_ORE));
            oreRecipes.add(new SlimefunItemStack(SlimefunItems.COPPER_DUST, isOreDoublingEnabled() ? 2 : 1));

            oreRecipes.add(new ItemStack(Material.COPPER_ORE));
            oreRecipes.add(new SlimefunItemStack(SlimefunItems.COPPER_DUST, isOreDoublingEnabled() ? 2 : 1));
        }

        for (int i = 0; i+1 < oreRecipes.size(); i+=2) {
            RecipeCategory.ORE_CRUSHER.registerRecipe(Recipe.of(RecipeStructure.SUBSET, oreRecipes.get(i), oreRecipes.get(i+1)));
        }

        displayRecipes.addAll(oreRecipes);
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return craftedRecipes.stream().map(items -> items[0]).collect(Collectors.toList());
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

                outputInv.addItem(event.getOutput());
                p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 1);

                return true;
            });

            if (!searchResult.isMatch()) {
                Slimefun.getLocalization().sendMessage(p, "machines.unknown-material", true);
            }
        }
    }

    private class DoubleOreSetting extends ItemSetting<Boolean> {

        private final ItemStack coal = new ItemStack(Material.COAL, 1);
        private final ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI, 7);
        private final ItemStack redstone = new ItemStack(Material.REDSTONE, 4);
        private final ItemStack diamond = new ItemStack(Material.DIAMOND, 1);
        private final ItemStack emerald = new ItemStack(Material.EMERALD, 1);
        private final ItemStack quartz = new ItemStack(Material.QUARTZ, 1);
        private final ItemStack goldNuggets = new ItemStack(Material.GOLD_NUGGET, 4);

        DoubleOreSetting(@Nonnull OreCrusher oreCrusher) {
            super(oreCrusher, "double-ores", true);
        }

        private void apply(boolean value) {
            coal.setAmount(value ? 2 : 1);
            lapis.setAmount(value ? 14 : 7);
            redstone.setAmount(value ? 8 : 4);
            diamond.setAmount(value ? 2 : 1);
            emerald.setAmount(value ? 2 : 1);
            quartz.setAmount(value ? 2 : 1);
            goldNuggets.setAmount(value ? 8 : 4);

            SlimefunItem ironDust = SlimefunItem.getById("IRON_DUST");
            if (ironDust != null) {
                ironDust.setRecipeOutput(new SlimefunItemStack(SlimefunItems.IRON_DUST, value ? 2 : 1));
            }

            SlimefunItem goldDust = SlimefunItem.getById("GOLD_DUST");
            if (goldDust != null) {
                goldDust.setRecipeOutput(new SlimefunItemStack(SlimefunItems.GOLD_DUST, value ? 2 : 1));
            }
        }

        @Override
        public void update(Boolean newValue) {
            super.update(newValue);
            apply(newValue);
        }

        @Override
        public void reload() {
            super.reload();
            apply(getValue());
        }

        public @Nonnull ItemStack getCoal() {
            return coal;
        }

        public @Nonnull ItemStack getLapisLazuli() {
            return lapis;
        }

        public @Nonnull ItemStack getRedstone() {
            return redstone;
        }

        public @Nonnull ItemStack getDiamond() {
            return diamond;
        }

        public @Nonnull ItemStack getEmerald() {
            return emerald;
        }

        public @Nonnull ItemStack getNetherQuartz() {
            return quartz;
        }

        public @Nonnull ItemStack getGoldNuggets() {
            return goldNuggets;
        }

    }

}
