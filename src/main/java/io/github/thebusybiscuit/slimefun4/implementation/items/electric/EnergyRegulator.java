package io.github.thebusybiscuit.slimefun4.implementation.items.electric;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.core.attributes.MachineProcessHolder;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineOperation;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The {@link EnergyRegulator} is a special type of {@link SlimefunItem} which serves as the heart of every
 * {@link EnergyNet}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see EnergyNet
 * @see EnergyNetComponent
 *
 */
public class EnergyRegulator extends SlimefunItem implements HologramOwner {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    private static final int[] END_BORDER = {45, 46, 48, 49, 50, 52, 53};
    private static final int PREV = 47;
    private static final int NEXT = 51;

    private static int AMOUNT = 36;

    @ParametersAreNonnullByDefault
    public EnergyRegulator(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(onBreak());
    }

    @Nonnull
    private BlockBreakHandler onBreak() {
        return new SimpleBlockBreakHandler() {

            @Override
            public void onBlockBreak(@Nonnull Block b) {
                removeHologram(b);
            }
        };
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(@Nonnull BlockPlaceEvent e) {
                updateHologram(e.getBlock(), "&7Connecting...");
            }

        };
    }

    @Override
    public void preRegister() {
        addItemHandler(onPlace());

        BlockUseHandler handler = blockUserHandler -> {
            Location location;
            Player player = blockUserHandler.getPlayer();

            try {
                location = blockUserHandler.getClickedBlock().orElseThrow().getLocation();
            } catch (NoSuchElementException e) {
                blockUserHandler.cancel();
                return;
            }

            EnergyNet network = getEnergyNetwork(location);

            if (network == null)
                return;


            getGUI(player, 0, network).open(player);
        };

        addItemHandler(handler);

        addItemHandler(new BlockTicker() {

            @Override
            public boolean isSynchronized() {
                return false;
            }

            @Override
            public void tick(Block b, SlimefunItem item, Config data) {
                EnergyRegulator.this.tick(b);
            }
        });
    }

    private void tick(@Nonnull Block b) {
        EnergyNet network = EnergyNet.getNetworkFromLocationOrCreate(b.getLocation());
        network.tick(b);
    }

    private EnergyNet getEnergyNetwork(Location location) {
        EnergyNet network;

        try {
            network = Slimefun.getNetworkManager().getNetworkFromLocation(location, EnergyNet.class).orElseThrow();
        } catch (NoSuchElementException e) {
            return null;
        }

        return network;
    }


    private List<ItemStack> getDisplayMachines(EnergyNet network) {

        Map<Location, EnergyNetComponent> consumers = network.getConsumers();

        List<ItemStack> print = new ArrayList<>();

        for (var entry : consumers.entrySet()) {
            EnergyNetComponent cmp = entry.getValue();
            Location location = entry.getKey();

            if (!(cmp instanceof SlimefunItem sf)) {
                continue;
            }

            ItemStack element = sf.getItem().clone();
            ItemMeta meta = element.getItemMeta();

            if (meta == null) {
                continue;
            }

            List<String> lore = element.getItemMeta().getLore();

            if (lore == null) {
                continue;
            }

            if (sf instanceof MachineProcessHolder<?> holder) {
                //This isn't very reliable as end-game machines which are faster will show as offline if clicked when a recipe just completed
                MachineProcessor<MachineOperation> processor = (MachineProcessor<MachineOperation>) holder.getMachineProcessor();
                MachineOperation op = processor.getOperation(location);

                String msg = op == null ? "§4§lOffline" : "§a§lOnline";
                lore.add("");
                lore.add("§bStored Energy:§3 " + cmp.getCharge(location) +" J");
                lore.add(msg);
                meta.setLore(lore);
                element.setItemMeta(meta);
            }

            print.add(element);
        }

        print.sort(Comparator.comparing(ItemStack::getType));

        return print;
    }

    private ChestMenu getGUI(Player p, int page, EnergyNet network) {
        ChestMenu chestMenu = new ChestMenu(getItemName());

        ChestMenu.MenuClickHandler blank = ChestMenuUtils.getEmptyClickHandler();
        ItemStack background = ChestMenuUtils.getBackground();

        for (int i : BORDER) {
            chestMenu.addItem(i, background, blank);
        }

        List<ItemStack> displayMachines = getDisplayMachines(network);
        int length = displayMachines.size();
        int pages = length / AMOUNT;

        int start = page * AMOUNT;
        int end = start + AMOUNT;

        for (int i = start; i < end; i++) {
            int slot = i % AMOUNT + 9;

            ItemStack item;
            try {
                item = displayMachines.get(i);
            } catch (IndexOutOfBoundsException e) {
                break;
            }

            if (item == null) {
                break;
            }

            chestMenu.addItem(slot, displayMachines.get(i), blank);
        }

        chestMenu.addItem(PREV, ChestMenuUtils.getPreviousButton(p, page, pages), (p1, slot, item, action) -> {
            int prev = page - 1;
            if (prev == -1)
                return false;

            getGUI(p1, prev, network).open(p1);
            return true;
        });

        chestMenu.addItem(NEXT, ChestMenuUtils.getNextButton(p, page, pages), (p1, slot, item, action) -> {
            int next = page + 1;
            if (next > pages)
                return false;

            getGUI(p1, next, network).open(p1);
            return true;
        });

        for (int i : END_BORDER) {
            chestMenu.addItem(i, background, blank);
        }

        return chestMenu;
    }

}
