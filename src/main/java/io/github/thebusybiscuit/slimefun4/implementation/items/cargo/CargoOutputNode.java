package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class CargoOutputNode extends AbstractCargoNode {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };

    @ParametersAreNonnullByDefault
    public CargoOutputNode(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

    @Override
    public boolean hasItemFilter() {
        return false;
    }

    @Override
    protected void onPlace(BlockPlaceEvent e) {
        // We only require the default values
    }

    @Override
    protected void createBorder(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Override
    protected void updateBlockMenu(BlockMenu menu, Block b) {
        addChannelSelector(b, menu, 12, 13, 14);
    }

    @Override
    protected void markDirty(Location loc) {
        // No need to mark anything as dirty, there is no item filter.
    }

}
