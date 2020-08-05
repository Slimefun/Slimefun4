package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class CargoInputNode extends AbstractFilterNode {

    private static final int[] BORDER = { 0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 17, 18, 22, 23, 26, 27, 31, 32, 33, 34, 35, 36, 40, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    public CargoInputNode(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);
    }

    @Override
    protected int[] getBorder() {
        return BORDER;
    }

    @Override
    protected void onPlace(BlockPlaceEvent e) {
        super.onPlace(e);
        BlockStorage.addBlockInfo(e.getBlock(), "round-robin", String.valueOf(false));
    }

    @Override
    protected void updateBlockMenu(BlockMenu menu, Block b) {
        super.updateBlockMenu(menu, b);

        String roundRobinMode = BlockStorage.getLocationInfo(b.getLocation(), "round-robin");
        if (!BlockStorage.hasBlockInfo(b) || roundRobinMode == null || roundRobinMode.equals(String.valueOf(false))) {
            menu.replaceExistingItem(24, new CustomItem(SlimefunUtils.getCustomHead(HeadTexture.ENERGY_REGULATOR.getTexture()), "&7Round-Robin Mode: &4\u2718", "", "&e> Click to enable Round Robin Mode", "&e(Items will be equally distributed on the Channel)"));
            menu.addMenuClickHandler(24, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, "round-robin", String.valueOf(true));
                updateBlockMenu(menu, b);
                return false;
            });
        }
        else {
            menu.replaceExistingItem(24, new CustomItem(SlimefunUtils.getCustomHead(HeadTexture.ENERGY_REGULATOR.getTexture()), "&7Round-Robin Mode: &2\u2714", "", "&e> Click to disable Round Robin Mode", "&e(Items will be equally distributed on the Channel)"));
            menu.addMenuClickHandler(24, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, "round-robin", String.valueOf(false));
                updateBlockMenu(menu, b);
                return false;
            });
        }
    }

}
