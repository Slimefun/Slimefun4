package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class TrashCan extends SlimefunItem implements InventoryBlock {

    private final int[] border = { 0, 1, 2, 3, 5, 4, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26 };
    private final ItemStack background = new CustomItem(Material.RED_STAINED_GLASS_PANE, " ");

    public TrashCan(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        createPreset(this, this::constructMenu);
    }

    private void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, background, ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Override
    public int[] getInputSlots() {
        return new int[] { 10, 11, 12, 13, 14, 15, 16 };
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                BlockMenu menu = BlockStorage.getInventory(b);

                for (int slot : getInputSlots()) {
                    menu.replaceExistingItem(slot, null);
                }
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        });
    }

}
