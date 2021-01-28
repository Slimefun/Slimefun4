package io.github.thebusybiscuit.slimefun4.implementation.items.geo;

import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.HologramOwner;
import io.github.thebusybiscuit.slimefun4.core.attributes.Processor;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.machines.MachineProcessor;
import io.github.thebusybiscuit.slimefun4.core.machines.MiningOperation;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNetComponentType;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public class GEOMiner extends SlimefunItem implements RecipeDisplayItem, EnergyNetComponent, InventoryBlock, HologramOwner, Processor<MiningOperation> {

    private static final int[] BORDER = { 0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 53 };
    private static final int[] OUTPUT_BORDER = { 19, 20, 21, 22, 23, 24, 25, 28, 34, 37, 43, 46, 47, 48, 49, 50, 51, 52 };
    private static final int[] OUTPUT_SLOTS = { 29, 30, 31, 32, 33, 38, 39, 40, 41, 42 };

    private static final int PROCESSING_TIME = 14;
    private static final int ENERGY_CONSUMPTION = 24;

    private final MachineProcessor<MiningOperation> processor = new MachineProcessor<>();

    @ParametersAreNonnullByDefault
    public GEOMiner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        processor.setProgressBar(new ItemStack(Material.DIAMOND_PICKAXE));
        createPreset(this, getItemName(), this::constructMenu);
        addItemHandler(onPlace());

        registerBlockHandler(getId(), (p, b, stack, reason) -> {
            removeHologram(b);

            BlockMenu inv = BlockStorage.getInventory(b);

            if (inv != null) {
                inv.dropItems(b.getLocation(), OUTPUT_SLOTS);
            }

            getMachineProcessor().removeOperation(b);
            return true;
        });
    }

    @Override
    public MachineProcessor<MiningOperation> getMachineProcessor() {
        return processor;
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                updateHologram(e.getBlock(), "&7Idling...");
            }
        };
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> displayRecipes = new LinkedList<>();

        for (GEOResource resource : SlimefunPlugin.getRegistry().getGEOResources().values()) {
            if (resource.isObtainableFromGEOMiner()) {
                displayRecipes.add(new CustomItem(resource.getItem(), ChatColor.RESET + resource.getName()));
            }
        }

        return displayRecipes;
    }

    @Override
    public String getLabelLocalPath() {
        return "guide.tooltips.recipes.miner";
    }

    @Override
    public EnergyNetComponentType getEnergyComponentType() {
        return EnergyNetComponentType.CONSUMER;
    }

    @Override
    public int getCapacity() {
        return 512;
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return OUTPUT_SLOTS;
    }

    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " "), (p, slot, item, action) -> false);
        }

        for (int i : OUTPUT_BORDER) {
            preset.addItem(i, new CustomItem(Material.ORANGE_STAINED_GLASS_PANE, " "), (p, slot, item, action) -> false);
        }

        preset.addItem(4, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "), (p, slot, item, action) -> false);

        for (int i : OUTPUT_SLOTS) {
            preset.addMenuClickHandler(i, new AdvancedMenuClickHandler() {

                @Override
                public boolean onClick(Player p, int slot, ItemStack cursor, ClickAction action) {
                    return false;
                }

                @Override
                public boolean onClick(InventoryClickEvent e, Player p, int slot, ItemStack cursor, ClickAction action) {
                    return cursor == null || cursor.getType() == null || cursor.getType() == Material.AIR;
                }
            });
        }
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                GEOMiner.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        });
    }

    protected void tick(Block b) {
        BlockMenu inv = BlockStorage.getInventory(b);
        MiningOperation operation = processor.getOperation(b);

        if (operation != null) {
            if (!operation.isFinished()) {
                processor.updateProgressBar(inv, 4, operation);

                if (getCharge(b.getLocation()) < ENERGY_CONSUMPTION) {
                    return;
                }

                removeCharge(b.getLocation(), ENERGY_CONSUMPTION);
                operation.addProgress(1);
            } else {
                inv.replaceExistingItem(4, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "));
                inv.pushItem(operation.getResult(), OUTPUT_SLOTS);

                processor.removeOperation(b);
            }
        } else if (!BlockStorage.hasChunkInfo(b.getWorld(), b.getX() >> 4, b.getZ() >> 4)) {
            updateHologram(b, "&4GEO-Scan required!");
        } else {
            start(b, inv);
        }
    }

    private void start(Block b, BlockMenu inv) {
        for (GEOResource resource : SlimefunPlugin.getRegistry().getGEOResources().values()) {
            if (resource.isObtainableFromGEOMiner()) {
                OptionalInt optional = SlimefunPlugin.getGPSNetwork().getResourceManager().getSupplies(resource, b.getWorld(), b.getX() >> 4, b.getZ() >> 4);

                if (!optional.isPresent()) {
                    updateHologram(b, "&4GEO-Scan required!");
                    return;
                }

                int supplies = optional.getAsInt();
                if (supplies > 0) {
                    if (!inv.fits(resource.getItem(), OUTPUT_SLOTS)) {
                        return;
                    }

                    processor.addOperation(b, new MiningOperation(resource.getItem().clone(), PROCESSING_TIME));
                    SlimefunPlugin.getGPSNetwork().getResourceManager().setSupplies(resource, b.getWorld(), b.getX() >> 4, b.getZ() >> 4, supplies - 1);
                    updateHologram(b, "&7Mining: &r" + resource.getName());
                    return;
                }
            }
        }

        updateHologram(b, "&7Finished");
    }

}
