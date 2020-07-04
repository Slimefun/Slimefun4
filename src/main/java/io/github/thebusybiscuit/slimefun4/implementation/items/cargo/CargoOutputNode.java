package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CargoOutputNode extends AbstractCargoNode {

    private static final int[] BORDER = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

    public CargoOutputNode(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        registerBlockHandler(getID(), new SlimefunBlockHandler() {

            @Override
            public void onPlace(Player p, Block b, SlimefunItem item) {
                BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
                BlockStorage.addBlockInfo(b, FREQUENCY, "0");
            }

            @Override
            public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
                return true;
            }
        });
    }

    @Override
    protected void createBorder(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());
        }
    }

    @Override
    protected void updateBlockMenu(BlockMenu menu, Block b) {
        menu.replaceExistingItem(12, new CustomItem(SlimefunUtils.getCustomHead("f2599bd986659b8ce2c4988525c94e19ddd39fad08a38284a197f1b70675acc"), "&b信道", "", "&e> 单击将信道ID减一"));
        menu.addMenuClickHandler(12, (p, slot, item, action) -> {
            int channel = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "frequency")) - 1;

            if (channel < 0) {
                if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) channel = 16;
                else channel = 15;
            }

            BlockStorage.addBlockInfo(b, "frequency", String.valueOf(channel));
            updateBlockMenu(menu, b);
            return false;
        });

        int channel = ((!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "frequency") == null) ? 0 : (Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "frequency"))));

        if (channel == 16) {
            menu.replaceExistingItem(13, new CustomItem(SlimefunUtils.getCustomHead("7a44ff3a5f49c69cab676bad8d98a063fa78cfa61916fdef3e267557fec18283"), "&b信道 ID: &3" + (channel + 1)));
            menu.addMenuClickHandler(13, ChestMenuUtils.getEmptyClickHandler());
        } else {
            menu.replaceExistingItem(13, new CustomItem(MaterialCollections.getAllWoolColors().get(channel), "&b频道ID: &3" + (channel + 1)));
            menu.addMenuClickHandler(13, ChestMenuUtils.getEmptyClickHandler());
        }

        menu.replaceExistingItem(14, new CustomItem(SlimefunUtils.getCustomHead("c2f910c47da042e4aa28af6cc81cf48ac6caf37dab35f88db993accb9dfe516"), "&b信道", "", "&e> 单击将信道ID加一"));
        menu.addMenuClickHandler(14, (p, slot, item, action) -> {
            int channeln = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "frequency")) + 1;

            if (SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled()) {
                if (channeln > 16) channeln = 0;
            } else {
                if (channeln > 15) channeln = 0;
            }

            BlockStorage.addBlockInfo(b, "frequency", String.valueOf(channeln));
            updateBlockMenu(menu, b);
            return false;
        });
    }
}
