package io.github.thebusybiscuit.slimefun4.implementation.items.geo;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public class OilPump extends AContainer implements RecipeDisplayItem {

    private final GEOResource oil;

    public OilPump(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        oil = SlimefunPlugin.getRegistry().getGEOResources().get(new NamespacedKey(SlimefunPlugin.instance(), "oil")).orElse(null);

        new BlockMenuPreset(getId(), getInventoryTitle()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                if (!(p.hasPermission("slimefun.inventory.bypass") || SlimefunPlugin.getProtectionManager().hasPermission(p, b.getLocation(), ProtectableAction.INTERACT_BLOCK))) {
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
                } else {
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
    public ItemStack getProgressBar() {
        return new ItemStack(Material.DIAMOND_SHOVEL);
    }

    @Override
    protected MachineRecipe findNextRecipe(BlockMenu inv) {
        if (inv.fits(SlimefunItems.OIL_BUCKET, getOutputSlots())) {
            Block b = inv.getBlock();

            for (int slot : getInputSlots()) {
                if (SlimefunUtils.isItemSimilar(inv.getItemInSlot(slot), new ItemStack(Material.BUCKET), true, false)) {
                    OptionalInt supplies = SlimefunPlugin.getGPSNetwork().getResourceManager().getSupplies(oil, b.getWorld(), b.getX() >> 4, b.getZ() >> 4);

                    if (supplies.isPresent() && supplies.getAsInt() > 0) {
                        MachineRecipe recipe = new MachineRecipe(26, new ItemStack[0], new ItemStack[] { SlimefunItems.OIL_BUCKET });

                        inv.consumeItem(slot);
                        SlimefunPlugin.getGPSNetwork().getResourceManager().setSupplies(oil, b.getWorld(), b.getX() >> 4, b.getZ() >> 4, supplies.getAsInt() - 1);
                        return recipe;
                    } else {
                        // Move the empty bucket to the output slot to prevent this
                        // from immediately starting all over again (to prevent lag)
                        ItemStack item = inv.getItemInSlot(slot).clone();
                        inv.replaceExistingItem(slot, null);
                        inv.pushItem(item, getOutputSlots());
                        return null;
                    }
                }
            }
        }

        return null;
    }
}
