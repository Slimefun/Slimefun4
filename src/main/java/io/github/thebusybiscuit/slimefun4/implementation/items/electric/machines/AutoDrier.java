package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * The {@link AutoDrier} is an implementation of {@link AContainer} that features recipes
 * related to "drying out" items.
 * It also allows you to convert Rotten Flesh into Leather.
 * 
 * @author Linox
 *
 */
public class AutoDrier extends AContainer implements RecipeDisplayItem {

    private final List<ItemStack> recipeList = new ArrayList<>();

    public AutoDrier(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        recipeList.add(new ItemStack(Material.ROTTEN_FLESH));
        recipeList.add(new ItemStack(Material.LEATHER));

        recipeList.add(new ItemStack(Material.WET_SPONGE));
        recipeList.add(new ItemStack(Material.SPONGE));

        recipeList.add(new ItemStack(Material.KELP));
        recipeList.add(new ItemStack(Material.DRIED_KELP));

        recipeList.add(new ItemStack(Material.POTION));
        recipeList.add(new ItemStack(Material.GLASS_BOTTLE));

        recipeList.add(new ItemStack(Material.OAK_SAPLING));
        recipeList.add(new ItemStack(Material.STICK, 2));

        recipeList.add(new ItemStack(Material.OAK_LEAVES));
        recipeList.add(new ItemStack(Material.STICK));

        recipeList.add(new ItemStack(Material.WATER_BUCKET));
        recipeList.add(new ItemStack(Material.BUCKET));

        recipeList.add(new ItemStack(Material.COOKED_BEEF));
        recipeList.add(SlimefunItems.BEEF_JERKY);

        recipeList.add(new ItemStack(Material.COOKED_PORKCHOP));
        recipeList.add(SlimefunItems.PORK_JERKY);

        recipeList.add(new ItemStack(Material.COOKED_CHICKEN));
        recipeList.add(SlimefunItems.CHICKEN_JERKY);

        recipeList.add(new ItemStack(Material.COOKED_MUTTON));
        recipeList.add(SlimefunItems.MUTTON_JERKY);

        recipeList.add(new ItemStack(Material.COOKED_RABBIT));
        recipeList.add(SlimefunItems.RABBIT_JERKY);

        recipeList.add(new ItemStack(Material.COOKED_COD));
        recipeList.add(SlimefunItems.FISH_JERKY);

        recipeList.add(new ItemStack(Material.COOKED_SALMON));
        recipeList.add(SlimefunItems.FISH_JERKY);
    }

    @Override
    public String getInventoryTitle() {
        return "&eAuto Drier";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return recipeList;
    }

    @Override
    protected void tick(Block b) {
        BlockMenu menu = BlockStorage.getInventory(b);

        if (isProcessing(b)) {
            int timeleft = progress.get(b);
            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                    return;
                }

                ChargableBlock.addCharge(b, -getEnergyConsumption());
                progress.put(b, timeleft - 1);
            }
            else {
                menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
                menu.pushItem(processing.get(b).getOutput()[0], getOutputSlots());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            MachineRecipe r = null;
            int inputSlot = -1;

            for (int slot : getInputSlots()) {
                ItemStack item = menu.getItemInSlot(slot);
                if (item != null) {
                    ItemStack output = getOutput(item);

                    if (output != null) {
                        r = new MachineRecipe(6, new ItemStack[] { item }, new ItemStack[] { output.clone() });
                        inputSlot = slot;
                        break;
                    }
                }
            }

            if (r != null) {
                if (inputSlot == -1) return;
                if (!menu.fits(r.getOutput()[0], getOutputSlots())) return;

                menu.consumeItem(inputSlot);
                processing.put(b, r);
                progress.put(b, r.getTicks());
            }
        }
    }

    private ItemStack getOutput(ItemStack item) {
        for (int i = 0; i < recipeList.size(); i += 2) {
            if (SlimefunUtils.isItemSimilar(item, recipeList.get(i), true)) {
                return recipeList.get(i + 1);
            }
        }

        if (Tag.SAPLINGS.isTagged(item.getType())) {
            return new ItemStack(Material.STICK, 2);
        }
        else if (Tag.LEAVES.isTagged(item.getType())) {
            return new ItemStack(Material.STICK, 1);
        }
        else if (item.getType() == Material.SPLASH_POTION || item.getType() == Material.LINGERING_POTION) {
            return new ItemStack(Material.GLASS_BOTTLE);
        }

        return null;
    }

    @Override
    public int getEnergyConsumption() {
        return 5;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public int getCapacity() {
        return 128;
    }

    @Override
    public String getMachineIdentifier() {
        return "AUTO_DRIER";
    }
}
