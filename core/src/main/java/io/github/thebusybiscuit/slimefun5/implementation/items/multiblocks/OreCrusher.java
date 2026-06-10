package io.github.thebusybiscuit.slimefun5.implementation.items.multiblocks;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.events.MultiBlockCraftEvent;
import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun5.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun5.utils.SlimefunUtils;
import io.papermc.lib.PaperLib;

/**
 * The {@link OreCrusher} is a {@link MultiBlockMachine} which allows you to double ores
 * and crush some other {@link Material Materials} into various resources.
 *
 * @author TheBusyBiscuit
 *
 */
public class OreCrusher extends MultiBlockMachine {

    private final DoubleOreSetting doubleOres = new DoubleOreSetting(this);

    @ParametersAreNonnullByDefault
    public OreCrusher(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, null, null, null, new ItemStack(XMaterial.NETHER_BRICK_FENCE.parseMaterial()), null, new ItemStack(XMaterial.IRON_BARS.parseMaterial()), CustomItemStack.create(Material.DISPENSER, "Dispenser (Facing up)"), new ItemStack(XMaterial.IRON_BARS.parseMaterial()) }, BlockFace.SELF);

        addItemSetting(doubleOres);
    }

    @Override
    protected void registerDefaultRecipes(List<ItemStack> recipes) {
        recipes.add(new ItemStack(XMaterial.BLACKSTONE.parseMaterial(), 8));
        recipes.add(new ItemStack(XMaterial.RED_SAND.parseMaterial(), 1));

        recipes.add(new ItemStack(Material.COBBLESTONE, 8));
        recipes.add(new ItemStack(Material.SAND, 1));

        recipes.add(SlimefunItems.GOLD_4K.item());
        recipes.add(SlimefunItems.GOLD_DUST.item());

        recipes.add(SlimefunItems.GOLD_6K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 2).item());

        recipes.add(SlimefunItems.GOLD_8K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 2).item());

        recipes.add(SlimefunItems.GOLD_10K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 3).item());

        recipes.add(SlimefunItems.GOLD_12K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 3).item());

        recipes.add(SlimefunItems.GOLD_14K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 4).item());

        recipes.add(SlimefunItems.GOLD_16K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 4).item());

        recipes.add(SlimefunItems.GOLD_18K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 5).item());

        recipes.add(SlimefunItems.GOLD_20K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 5).item());

        recipes.add(SlimefunItems.GOLD_22K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 6).item());

        recipes.add(SlimefunItems.GOLD_24K.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, 6).item());

        recipes.add(new ItemStack(Material.GRAVEL));
        recipes.add(new ItemStack(Material.SAND));

        recipes.add(new ItemStack(XMaterial.MAGMA_BLOCK.parseMaterial()));
        recipes.add(SlimefunItems.SULFATE.item());

        recipes.add(SlimefunItems.CARBON.item());
        recipes.add(new ItemStack(Material.COAL, 8));

        recipes.add(SlimefunItems.COMPRESSED_CARBON.item());
        recipes.add(new SlimefunItemStack(SlimefunItems.CARBON, 4).item());

        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            recipes.add(new ItemStack(XMaterial.COBBLED_DEEPSLATE.parseMaterial(), 8));
            recipes.add(new ItemStack(Material.SAND, 1));
        }
    }

    public boolean isOreDoublingEnabled() {
        return doubleOres.getValue();
    }

    @Override
    public void postRegister() {
        super.postRegister();

        // @formatter:off
        displayRecipes.addAll(Arrays.asList(
            new ItemStack(Material.COAL_ORE), doubleOres.getCoal(),
            new ItemStack(Material.LAPIS_ORE), doubleOres.getLapisLazuli(),
            new ItemStack(Material.REDSTONE_ORE), doubleOres.getRedstone(),
            new ItemStack(Material.DIAMOND_ORE), doubleOres.getDiamond(),
            new ItemStack(Material.EMERALD_ORE), doubleOres.getEmerald(),
            new ItemStack(XMaterial.NETHER_QUARTZ_ORE.parseMaterial()), doubleOres.getNetherQuartz()
        ));
        // @formatter:on

        // Gold ore variants (1.16+)
        displayRecipes.add(new ItemStack(XMaterial.NETHER_GOLD_ORE.parseMaterial()));
        displayRecipes.add(doubleOres.getGoldNuggets());

        displayRecipes.add(new ItemStack(XMaterial.GILDED_BLACKSTONE.parseMaterial()));
        displayRecipes.add(doubleOres.getGoldNuggets());

        // Raw metal ores (1.17+)
        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            displayRecipes.add(new ItemStack(XMaterial.RAW_IRON.parseMaterial()));
            displayRecipes.add(SlimefunItems.IRON_DUST.item());

            displayRecipes.add(new ItemStack(XMaterial.RAW_COPPER.parseMaterial()));
            displayRecipes.add(SlimefunItems.COPPER_DUST.item());

            displayRecipes.add(new ItemStack(XMaterial.RAW_GOLD.parseMaterial()));
            displayRecipes.add(SlimefunItems.GOLD_DUST.item());
        }

        // Deepslate Ores (1.17+)
        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_17)) {
            // @formatter:off
            displayRecipes.addAll(Arrays.asList(
                new ItemStack(XMaterial.DEEPSLATE_COAL_ORE.parseMaterial()), doubleOres.getCoal(),
                new ItemStack(XMaterial.DEEPSLATE_LAPIS_ORE.parseMaterial()), doubleOres.getLapisLazuli(),
                new ItemStack(XMaterial.DEEPSLATE_REDSTONE_ORE.parseMaterial()), doubleOres.getRedstone(),
                new ItemStack(XMaterial.DEEPSLATE_DIAMOND_ORE.parseMaterial()), doubleOres.getDiamond(),
                new ItemStack(XMaterial.DEEPSLATE_EMERALD_ORE.parseMaterial()), doubleOres.getEmerald()
            ));
            // @formatter:on

            // More deepslate ores and copper ore
            displayRecipes.add(new ItemStack(XMaterial.DEEPSLATE_IRON_ORE.parseMaterial()));
            displayRecipes.add(new SlimefunItemStack(SlimefunItems.IRON_DUST, isOreDoublingEnabled() ? 2 : 1).item());

            displayRecipes.add(new ItemStack(XMaterial.DEEPSLATE_GOLD_ORE.parseMaterial()));
            displayRecipes.add(new SlimefunItemStack(SlimefunItems.GOLD_DUST, isOreDoublingEnabled() ? 2 : 1).item());

            displayRecipes.add(new ItemStack(XMaterial.DEEPSLATE_COPPER_ORE.parseMaterial()));
            displayRecipes.add(new SlimefunItemStack(SlimefunItems.COPPER_DUST, isOreDoublingEnabled() ? 2 : 1).item());

            displayRecipes.add(new ItemStack(XMaterial.COPPER_ORE.parseMaterial()));
            displayRecipes.add(new SlimefunItemStack(SlimefunItems.COPPER_DUST, isOreDoublingEnabled() ? 2 : 1).item());
        }
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return recipes.stream().map(items -> items[0]).collect(Collectors.toList());
    }

    @Override
    public void onInteract(Player p, Block b) {
        Block possibleDispenser = b.getRelative(BlockFace.DOWN);
        BlockState state = PaperLib.getBlockState(possibleDispenser, false).getState();

        if (state instanceof Dispenser) {
            Dispenser dispenser = (Dispenser) state;            Inventory inv = dispenser.getInventory();

            for (ItemStack current : inv.getContents()) {
                for (ItemStack convert : RecipeType.getRecipeInputs(this)) {
                    if (convert != null && SlimefunUtils.isItemSimilar(current, convert, true)) {
                        ItemStack adding = RecipeType.getRecipeOutput(this, convert);
                        Inventory outputInv = findOutputInventory(adding, possibleDispenser, inv);
                        MultiBlockCraftEvent event = new MultiBlockCraftEvent(p, this, current, adding);

                        Bukkit.getPluginManager().callEvent(event);
                        if (!event.isCancelled() && SlimefunUtils.canPlayerUseItem(p, adding, true)) {
                            if (outputInv != null) {
                                ItemStack removing = current.clone();
                                removing.setAmount(convert.getAmount());
                                inv.removeItem(removing);
                                outputInv.addItem(event.getOutput());
                                p.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 1);
                            } else {
                                Slimefun.getLocalization().sendMessage(p, "machines.full-inventory", true);
                            }
                        }

                        return;
                    }
                }
            }

            Slimefun.getLocalization().sendMessage(p, "machines.unknown-material", true);
        }
    }

    private class DoubleOreSetting extends ItemSetting<Boolean> {

        private final ItemStack coal = new ItemStack(Material.COAL, 1);
        private final ItemStack lapis = new ItemStack(XMaterial.LAPIS_LAZULI.parseMaterial(), 7);
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
                ironDust.setRecipeOutput(new SlimefunItemStack(SlimefunItems.IRON_DUST, value ? 2 : 1).item());
            }

            SlimefunItem goldDust = SlimefunItem.getById("GOLD_DUST");
            if (goldDust != null) {
                goldDust.setRecipeOutput(new SlimefunItemStack(SlimefunItems.GOLD_DUST, value ? 2 : 1).item());
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

