package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.ColoredMaterial;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

/**
 * This abstract class is the super class of all cargo nodes.
 * 
 * @author TheBusyBiscuit
 *
 */
abstract class AbstractCargoNode extends SlimefunItem {

    protected static final String FREQUENCY = "frequency";

    public AbstractCargoNode(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        addItemHandler(new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlock();

                // The owner and frequency are required by every node
                BlockStorage.addBlockInfo(b, "owner", e.getPlayer().getUniqueId().toString());
                BlockStorage.addBlockInfo(b, FREQUENCY, "0");

                onPlace(e);
            }

        });

        new BlockMenuPreset(getId(), ChatUtils.removeColorCodes(item.getItemMeta().getDisplayName())) {

            @Override
            public void init() {
                createBorder(this);
            }

            @Override
            public void newInstance(BlockMenu menu, Block b) {
                updateBlockMenu(menu, b);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.cargo.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }
        };
    }

    protected void addChannelSelector(Block b, BlockMenu menu, int slotPrev, int slotCurrent, int slotNext) {
        boolean isChestTerminalInstalled = SlimefunPlugin.getThirdPartySupportService().isChestTerminalInstalled();
        int channel = getSelectedChannel(b);

        menu.replaceExistingItem(slotPrev, new CustomItem(HeadTexture.CARGO_ARROW_LEFT.getAsItemStack(), "&bPrevious Channel", "", "&e> Click to decrease the Channel ID by 1"));
        menu.addMenuClickHandler(slotPrev, (p, slot, item, action) -> {
            int newChannel = channel - 1;

            if (newChannel < 0) {
                if (isChestTerminalInstalled) {
                    newChannel = 16;
                } else {
                    newChannel = 15;
                }
            }

            BlockStorage.addBlockInfo(b, FREQUENCY, String.valueOf(newChannel));
            updateBlockMenu(menu, b);
            return false;
        });

        if (channel == 16) {
            menu.replaceExistingItem(slotCurrent, new CustomItem(HeadTexture.CHEST_TERMINAL.getAsItemStack(), "&bChannel ID: &3" + (channel + 1)));
            menu.addMenuClickHandler(slotCurrent, ChestMenuUtils.getEmptyClickHandler());
        } else {
            menu.replaceExistingItem(slotCurrent, new CustomItem(ColoredMaterial.WOOL.get(channel), "&bChannel ID: &3" + (channel + 1)));
            menu.addMenuClickHandler(slotCurrent, ChestMenuUtils.getEmptyClickHandler());
        }

        menu.replaceExistingItem(slotNext, new CustomItem(HeadTexture.CARGO_ARROW_RIGHT.getAsItemStack(), "&bNext Channel", "", "&e> Click to increase the Channel ID by 1"));
        menu.addMenuClickHandler(slotNext, (p, slot, item, action) -> {
            int newChannel = channel + 1;

            if (isChestTerminalInstalled) {
                if (newChannel > 16) {
                    newChannel = 0;
                }
            } else if (newChannel > 15) {
                newChannel = 0;
            }

            BlockStorage.addBlockInfo(b, FREQUENCY, String.valueOf(newChannel));
            updateBlockMenu(menu, b);
            return false;
        });
    }

    private int getSelectedChannel(Block b) {
        if (!BlockStorage.hasBlockInfo(b)) {
            return 0;
        } else {
            String frequency = BlockStorage.getLocationInfo(b.getLocation(), FREQUENCY);

            if (frequency == null) {
                return 0;
            } else {
                int channel = Integer.parseInt(frequency);
                return NumberUtils.clamp(0, channel, 16);
            }
        }
    }

    protected abstract void onPlace(BlockPlaceEvent e);

    protected abstract void createBorder(BlockMenuPreset preset);

    protected abstract void updateBlockMenu(BlockMenu menu, Block b);

}
