package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

/**
 * This abstract super class represents all filtered Cargo nodes.
 * 
 * @author TheBusyBiscuit
 * 
 * @see CargoInputNode
 * @see AdvancedCargoOutputNode
 *
 */
abstract class AbstractFilterNode extends AbstractCargoNode {

    protected static final int[] SLOTS = { 19, 20, 21, 28, 29, 30, 37, 38, 39 };

    public AbstractFilterNode(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        registerBlockHandler(getID(), (p, b, stack, reason) -> {
            BlockMenu inv = BlockStorage.getInventory(b);

            if (inv != null) {
                inv.dropItems(b.getLocation(), SLOTS);
            }

            return true;
        });
    }

    protected abstract int[] getBorder();

    @Override
    protected void onPlace(BlockPlaceEvent e) {
        Block b = e.getBlock();
        BlockStorage.addBlockInfo(b, "index", "0");
        BlockStorage.addBlockInfo(b, "filter-type", "whitelist");
        BlockStorage.addBlockInfo(b, "filter-lore", String.valueOf(true));
        BlockStorage.addBlockInfo(b, "filter-durability", String.valueOf(false));
    }

    @Override
    protected void createBorder(BlockMenuPreset preset) {
        for (int i : getBorder()) {
            preset.addItem(i, new CustomItem(Material.CYAN_STAINED_GLASS_PANE, " "), ChestMenuUtils.getEmptyClickHandler());
        }

        preset.addItem(2, new CustomItem(Material.PAPER, "&3Items", "", "&bPut in all Items you want to", "&bblacklist/whitelist"), ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    protected void updateBlockMenu(BlockMenu menu, Block b) {
        String filterType = BlockStorage.getLocationInfo(b.getLocation(), "filter-type");

        if (!BlockStorage.hasBlockInfo(b) || filterType == null || filterType.equals("whitelist")) {
            menu.replaceExistingItem(15, new CustomItem(Material.WHITE_WOOL, "&7Type: &rWhitelist", "", "&e> Click to change it to Blacklist"));
            menu.addMenuClickHandler(15, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, "filter-type", "blacklist");
                updateBlockMenu(menu, b);
                return false;
            });
        }
        else {
            menu.replaceExistingItem(15, new CustomItem(Material.BLACK_WOOL, "&7Type: &8Blacklist", "", "&e> Click to change it to Whitelist"));
            menu.addMenuClickHandler(15, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, "filter-type", "whitelist");
                updateBlockMenu(menu, b);
                return false;
            });
        }

        String durability = BlockStorage.getLocationInfo(b.getLocation(), "filter-durability");

        if (!BlockStorage.hasBlockInfo(b) || durability == null || durability.equals(String.valueOf(false))) {
            ItemStack is = new ItemStack(Material.STONE_SWORD);
            ItemMeta meta = is.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            is.setItemMeta(meta);

            menu.replaceExistingItem(16, new CustomItem(is, "&7Include Sub-IDs/Durability: &4\u2718", "", "&e> Click to toggle whether the Durability has to match"));
            menu.addMenuClickHandler(16, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, "filter-durability", String.valueOf(true));
                updateBlockMenu(menu, b);
                return false;
            });
        }
        else {
            ItemStack is = new ItemStack(Material.GOLDEN_SWORD);
            ItemMeta meta = is.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ((Damageable) meta).setDamage(20);
            is.setItemMeta(meta);

            menu.replaceExistingItem(16, new CustomItem(is, "&7Include Sub-IDs/Durability: &2\u2714", "", "&e> Click to toggle whether the Durability has to match"));
            menu.addMenuClickHandler(16, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, "filter-durability", String.valueOf(false));
                updateBlockMenu(menu, b);
                return false;
            });
        }

        String lore = BlockStorage.getLocationInfo(b.getLocation(), "filter-lore");

        if (!BlockStorage.hasBlockInfo(b) || lore == null || lore.equals(String.valueOf(true))) {
            menu.replaceExistingItem(25, new CustomItem(Material.MAP, "&7Include Lore: &2\u2714", "", "&e> Click to toggle whether the Lore has to match"));
            menu.addMenuClickHandler(25, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, "filter-lore", String.valueOf(false));
                updateBlockMenu(menu, b);
                return false;
            });
        }
        else {
            menu.replaceExistingItem(25, new CustomItem(Material.MAP, "&7Include Lore: &4\u2718", "", "&e> Click to toggle whether the Lore has to match"));
            menu.addMenuClickHandler(25, (p, slot, item, action) -> {
                BlockStorage.addBlockInfo(b, "filter-lore", String.valueOf(true));
                updateBlockMenu(menu, b);
                return false;
            });
        }

        addChannelSelector(b, menu, 41, 42, 43);
    }

}
