package io.github.thebusybiscuit.slimefun4.implementation.items.geo;

import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.AdvancedMenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

public abstract class GEOMiner extends AContainer implements RecipeDisplayItem {

    private static final int[] BORDER = { 0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 26, 27, 35, 36, 44, 45, 53 };
    private static final int[] OUTPUT_BORDER = { 19, 20, 21, 22, 23, 24, 25, 28, 34, 37, 43, 46, 47, 48, 49, 50, 51, 52 };
    private static final int[] OUTPUT_SLOTS = { 29, 30, 31, 32, 33, 38, 39, 40, 41, 42 };
    private static final int PROCESSING_TIME = 14;

    public GEOMiner(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemHandler(onPlace());
        registerBlockHandler(getId(), (p, b, stack, reason) -> {
            SimpleHologram.remove(b);

            BlockMenu inv = BlockStorage.getInventory(b);

            if (inv != null) {
                inv.dropItems(b.getLocation(), getOutputSlots());
            }

            progress.remove(b);
            processing.remove(b);
            return true;
        });
    }

    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                SimpleHologram.update(e.getBlock(), "&7Idling...");
            }
        };
    }

    @Override
    public String getMachineIdentifier() {
        return "GEO_MINER";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

    @Override
    public int[] getInputSlots() {
        return new int[0];
    }

    @Override
    public int[] getOutputSlots() {
        return OUTPUT_SLOTS;
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
    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : BORDER) {
            preset.addItem(i, new CustomItem(Material.GRAY_STAINED_GLASS_PANE, " "), (p, slot, item, action) -> false);
        }

        for (int i : OUTPUT_BORDER) {
            preset.addItem(i, new CustomItem(Material.ORANGE_STAINED_GLASS_PANE, " "), (p, slot, item, action) -> false);
        }

        preset.addItem(4, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "), (p, slot, item, action) -> false);

        for (int i : getOutputSlots()) {
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
    protected void tick(Block b) {
        BlockMenu inv = BlockStorage.getInventory(b);

        if (isProcessing(b)) {
            int timeleft = progress.get(b);

            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(inv, 4, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (getCharge(b.getLocation()) < getEnergyConsumption()) {
                    return;
                }

                removeCharge(b.getLocation(), getEnergyConsumption());
                progress.put(b, timeleft - 1);
            } else {
                inv.replaceExistingItem(4, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "));
                inv.pushItem(processing.get(b).getOutput()[0], getOutputSlots());

                progress.remove(b);
                processing.remove(b);
            }
        } else if (!BlockStorage.hasChunkInfo(b.getWorld(), b.getX() >> 4, b.getZ() >> 4)) {
            SimpleHologram.update(b, "&4GEO-Scan required!");
        } else {
            start(b, inv);
        }
    }

    private void start(Block b, BlockMenu inv) {
        for (GEOResource resource : SlimefunPlugin.getRegistry().getGEOResources().values()) {
            if (resource.isObtainableFromGEOMiner()) {
                OptionalInt optional = SlimefunPlugin.getGPSNetwork().getResourceManager().getSupplies(resource, b.getWorld(), b.getX() >> 4, b.getZ() >> 4);

                if (!optional.isPresent()) {
                    SimpleHologram.update(b, "&4GEO-Scan required!");
                    return;
                }

                int supplies = optional.getAsInt();
                if (supplies > 0) {
                    MachineRecipe r = new MachineRecipe(PROCESSING_TIME / getSpeed(), new ItemStack[0], new ItemStack[] { resource.getItem().clone() });

                    if (!inv.fits(r.getOutput()[0], getOutputSlots())) {
                        return;
                    }

                    processing.put(b, r);
                    progress.put(b, r.getTicks());
                    SlimefunPlugin.getGPSNetwork().getResourceManager().setSupplies(resource, b.getWorld(), b.getX() >> 4, b.getZ() >> 4, supplies - 1);
                    SimpleHologram.update(b, "&7Mining: &r" + resource.getName());
                    return;
                }
            }
        }

        SimpleHologram.update(b, "&7Finished");
    }

}
