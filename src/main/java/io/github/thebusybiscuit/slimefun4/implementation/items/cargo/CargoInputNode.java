package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;

import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class CargoInputNode extends AbstractFilterNode {

    private static final int[] BORDER = { 0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 17, 18, 22, 23, 26, 27, 31, 32, 33, 34, 35, 36, 40, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private static final String ROUND_ROBIN_MODE = "round-robin";
    private static final String SMART_FILL_MODE = "smart-fill";

    @ParametersAreNonnullByDefault
    public CargoInputNode(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(itemGroup, item, recipeType, recipe, recipeOutput);
    }

    @Override
    protected int[] getBorder() {
        return BORDER;
    }

    @Override
    protected void onPlace(BlockPlaceEvent e) {
        super.onPlace(e);

        BlockStorage.addBlockInfo(e.getBlock(), ROUND_ROBIN_MODE, String.valueOf(false));
        BlockStorage.addBlockInfo(e.getBlock(), SMART_FILL_MODE, String.valueOf(false));
    }

    @Override
    protected void updateBlockMenu(BlockMenu menu, Block b) {
        super.updateBlockMenu(menu, b);

        String roundRobinMode = BlockStorage.getLocationInfo(b.getLocation(), ROUND_ROBIN_MODE);
        if (!BlockStorage.hasBlockInfo(b) || roundRobinMode == null || roundRobinMode.equals(String.valueOf(false))) {
            menu.replaceExistingItem(24, new CustomItemStack(HeadTexture.ENERGY_REGULATOR.getAsItemStack(), "&7Mode Round-Robin: &4\u2718", "", "&e> Clic pour activer le mode Round-Robin", "&e(Les objets seront répartis équitablement sur le canal)"));
            menu.addMenuClickHandler(24, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, ROUND_ROBIN_MODE, String.valueOf(true));
                updateBlockMenu(menu, b);
                return false;
            });
        } else {
            menu.replaceExistingItem(24, new CustomItemStack(HeadTexture.ENERGY_REGULATOR.getAsItemStack(), "&7Mode Round-Robin: &2\u2714", "", "&e> Clic pour désactiver le mode Round-Robin", "&e(Les objets seront répartis équitablement sur le canal)"));
            menu.addMenuClickHandler(24, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, ROUND_ROBIN_MODE, String.valueOf(false));
                updateBlockMenu(menu, b);
                return false;
            });
        }

        String smartFillNode = BlockStorage.getLocationInfo(b.getLocation(), SMART_FILL_MODE);
        if (!BlockStorage.hasBlockInfo(b) || smartFillNode == null || smartFillNode.equals(String.valueOf(false))) {
            menu.replaceExistingItem(16, new CustomItemStack(Material.WRITABLE_BOOK, "&7Mode: \"Remplissage intelligent\" &4\u2718", "", "&e> Clic pour activer le mode \"Smart-Filling\"", "", "&fDans ce mode, le nœud Cargo va tenter", "&fde conserver une quantité constante d'objets", "&fdans l'inventaire. Ce n'est pas parfait", "&fet cela remplit toujours les emplacements vides", "&fqui se trouvent avant une pile d'un élément configuré."));
            menu.addMenuClickHandler(16, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, SMART_FILL_MODE, String.valueOf(true));
                updateBlockMenu(menu, b);
                return false;
            });
        } else {
            menu.replaceExistingItem(16, new CustomItemStack(Material.WRITTEN_BOOK, "&7Mode: \"SRemplissage intelligent\" &2\u2714", "", "&e> Clic pour désactiver le mode \"Smart-Filling\"", "", "&fDans ce mode, le nœud Cargo va tenter", "&fde conserver une quantité constante d'objets", "&fdans l'inventaire. Ce n'est pas parfait", "&fet cela remplit toujours les emplacements vides", "&fqui se trouvent avant une pile d'un élément configuré."));
            menu.addMenuClickHandler(16, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, SMART_FILL_MODE, String.valueOf(false));
                updateBlockMenu(menu, b);
                return false;
            });
        }
    }

}
