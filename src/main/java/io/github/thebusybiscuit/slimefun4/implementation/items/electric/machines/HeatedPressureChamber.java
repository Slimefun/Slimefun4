package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.inventory.DirtyChestMenu;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public abstract class HeatedPressureChamber extends AContainer {

    public HeatedPressureChamber(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        new BlockMenuPreset(getID(), getItemName()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                return p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES);
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                return new int[0];
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(DirtyChestMenu menu, ItemTransportFlow flow, ItemStack item) {
                if (flow == ItemTransportFlow.WITHDRAW) return getOutputSlots();

                List<Integer> slots = new ArrayList<>();

                for (int slot : getInputSlots()) {
                    if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), item, true)) {
                        slots.add(slot);
                    }
                }

                if (slots.isEmpty()) {
                    return getInputSlots();
                }
                else {
                    Collections.sort(slots, compareSlots(menu));
                    int[] array = new int[slots.size()];

                    for (int i = 0; i < slots.size(); i++) {
                        array[i] = slots.get(i);
                    }

                    return array;
                }
            }
        };

        this.registerDefaultRecipes();
    }

    private Comparator<Integer> compareSlots(DirtyChestMenu menu) {
        return (slot1, slot2) -> menu.getItemInSlot(slot1).getAmount() - menu.getItemInSlot(slot2).getAmount();
    }

    @Override
    protected void registerDefaultRecipes() {
        registerRecipe(45, new ItemStack[] { SlimefunItems.OIL_BUCKET }, new ItemStack[] { new CustomItem(SlimefunItems.PLASTIC_SHEET, 8) });
        registerRecipe(30, new ItemStack[] { SlimefunItems.GOLD_24K, SlimefunItems.URANIUM }, new ItemStack[] { SlimefunItems.BLISTERING_INGOT });
        registerRecipe(30, new ItemStack[] { SlimefunItems.BLISTERING_INGOT, SlimefunItems.CARBONADO }, new ItemStack[] { SlimefunItems.BLISTERING_INGOT_2 });
        registerRecipe(60, new ItemStack[] { SlimefunItems.BLISTERING_INGOT_2, new ItemStack(Material.NETHER_STAR) }, new ItemStack[] { SlimefunItems.BLISTERING_INGOT_3 });
        registerRecipe(90, new ItemStack[] { SlimefunItems.PLUTONIUM, SlimefunItems.URANIUM }, new ItemStack[] { SlimefunItems.BOOSTED_URANIUM });
        registerRecipe(60, new ItemStack[] { SlimefunItems.NETHER_ICE, SlimefunItems.PLUTONIUM }, new ItemStack[] { new CustomItem(SlimefunItems.ENRICHED_NETHER_ICE, 4) });
        registerRecipe(45, new ItemStack[] { SlimefunItems.ENRICHED_NETHER_ICE }, new ItemStack[] { new CustomItem(SlimefunItems.NETHER_ICE_COOLANT_CELL, 8) });
        registerRecipe(8, new ItemStack[] { SlimefunItems.MAGNESIUM_DUST, SlimefunItems.SALT }, new ItemStack[] { SlimefunItems.MAGNESIUM_SALT });
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.FLINT_AND_STEEL);
    }

    @Override
    public int[] getInputSlots() {
        return new int[] { 19, 20 };
    }

    @Override
    public int[] getOutputSlots() {
        return new int[] { 24, 25 };
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                HeatedPressureChamber.this.tick(b);
            }

            @Override
            public boolean isSynchronized() {
                return false;
            }
        });
    }

    @Override
    protected void tick(Block b) {
        BlockMenu menu = BlockStorage.getInventory(b);

        if (isProcessing(b)) {
            int timeleft = progress.get(b);

            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(menu, 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                    return;
                }

                ChargableBlock.addCharge(b, -getEnergyConsumption());
                progress.put(b, timeleft - 1);
            }
            else {
                menu.replaceExistingItem(22, new CustomItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), " "));
                menu.pushItem(processing.get(b).getOutput()[0], getOutputSlots());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else {
            Map<Integer, Integer> found = new HashMap<>();
            MachineRecipe recipe = findRecipe(menu, found);

            if (recipe != null) {
                if (!menu.fits(recipe.getOutput()[0], getOutputSlots())) {
                    return;
                }

                for (Map.Entry<Integer, Integer> entry : found.entrySet()) {
                    menu.consumeItem(entry.getKey(), entry.getValue());
                }

                processing.put(b, recipe);
                progress.put(b, recipe.getTicks());
            }
        }
    }

    private MachineRecipe findRecipe(BlockMenu menu, Map<Integer, Integer> found) {
        for (MachineRecipe recipe : recipes) {
            for (ItemStack input : recipe.getInput()) {
                for (int slot : getInputSlots()) {
                    if (SlimefunUtils.isItemSimilar(menu.getItemInSlot(slot), input, true)) {
                        found.put(slot, input.getAmount());
                        break;
                    }
                }
            }

            if (found.size() == recipe.getInput().length) {
                return recipe;
            }
            else {
                found.clear();
            }
        }

        return null;
    }

    @Override
    public String getMachineIdentifier() {
        return "HEATED_PRESSURE_CHAMBER";
    }

}
