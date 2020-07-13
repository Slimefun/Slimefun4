package io.github.thebusybiscuit.slimefun4.implementation.items.geo;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public abstract class OilPump extends AContainer implements RecipeDisplayItem {

    private final GEOResource oil;

    public OilPump(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        oil = SlimefunPlugin.getRegistry().getGEOResources().get(new NamespacedKey(SlimefunPlugin.instance(), "oil")).orElse(null);

        new BlockMenuPreset(getID(), getInventoryTitle()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                if (!(p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.ACCESS_INVENTORIES))) {
                    return false;
                }

                if (!SlimefunPlugin.getGPSNetwork().getResourceManager().getSupplies(oil, b.getWorld(), b.getX() >> 4, b.getZ() >> 4).isPresent()) {
                    SlimefunPlugin.getLocalization().sendMessage(p, "gps.geo.scan-required", true);
                    return false;
                }

                return true;
            }

            @Override
            public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
                if (flow == ItemTransportFlow.INSERT) {
                    return getInputSlots();
                }
                else {
                    return getOutputSlots();
                }
            }
        };
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return Arrays.asList(new ItemStack(Material.BUCKET), SlimefunItems.OIL_BUCKET);
    }

    @Override
    public String getMachineIdentifier() {
        return "OIL_PUMP";
    }

    @Override
    public String getInventoryTitle() {
        return "&4Oil Pump";
    }

    @Override
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_SHOVEL);
    }

    @Override
    protected void tick(Block b) {
        BlockMenu inv = BlockStorage.getInventory(b);

        if (isProcessing(b)) {
            int timeleft = progress.get(b);

            if (timeleft > 0) {
                ChestMenuUtils.updateProgressbar(inv, 22, timeleft, processing.get(b).getTicks(), getProgressBar());

                if (ChargableBlock.getCharge(b) < getEnergyConsumption()) {
                    return;
                }

                ChargableBlock.addCharge(b, -getEnergyConsumption());
                progress.put(b, timeleft - 1);
            }
            else {
                inv.replaceExistingItem(22, new CustomItem(Material.BLACK_STAINED_GLASS_PANE, " "));
                inv.pushItem(SlimefunItems.OIL_BUCKET, getOutputSlots());

                progress.remove(b);
                processing.remove(b);
            }
        }
        else if (inv.fits(SlimefunItems.OIL_BUCKET, getOutputSlots())) {
            for (int slot : getInputSlots()) {
                if (SlimefunUtils.isItemSimilar(inv.getItemInSlot(slot), new ItemStack(Material.BUCKET), true, false)) {
                    OptionalInt supplies = SlimefunPlugin.getGPSNetwork().getResourceManager().getSupplies(oil, b.getWorld(), b.getX() >> 4, b.getZ() >> 4);

                    if (supplies.isPresent() && supplies.getAsInt() > 0) {
                        MachineRecipe r = new MachineRecipe(26, new ItemStack[0], new ItemStack[] { SlimefunItems.OIL_BUCKET });

                        inv.consumeItem(slot);
                        processing.put(b, r);
                        progress.put(b, r.getTicks());
                        SlimefunPlugin.getGPSNetwork().getResourceManager().setSupplies(oil, b.getWorld(), b.getX() >> 4, b.getZ() >> 4, supplies.getAsInt() - 1);
                    }
                    else {
                        ItemStack item = inv.getItemInSlot(slot).clone();
                        inv.replaceExistingItem(slot, null);
                        inv.pushItem(item, getOutputSlots());
                    }
                    break;
                }
            }
        }
    }
}
