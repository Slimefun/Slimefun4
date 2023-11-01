package io.github.thebusybiscuit.slimefun4.implementation.items.geo;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.OptionalInt;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.config.Config;
import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.RecipeDisplayItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.MachineRecipe;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public class OilPump extends AContainer implements RecipeDisplayItem {

    private final GEOResource oil;

    private final ItemStack emptyBucket = new ItemStack(Material.BUCKET);

    @ParametersAreNonnullByDefault
    public OilPump(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        oil = Slimefun.getRegistry().getGEOResources().get(new NamespacedKey(Slimefun.instance(), "oil")).orElse(null);

        new BlockMenuPreset(getId(), getInventoryTitle()) {

            @Override
            public void init() {
                constructMenu(this);
            }

            @Override
            public boolean canOpen(Block b, Player p) {
                if (!(p.hasPermission("slimefun.inventory.bypass") || Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.INTERACT_BLOCK))) {
                    return false;
                }

                if (!Slimefun.getGPSNetwork().getResourceManager().getSupplies(oil, b.getWorld(), b.getX() >> 4, b.getZ() >> 4).isPresent()) {
                    Slimefun.getLocalization().sendMessage(p, "gps.geo.scan-required", true);
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

    private boolean isOnResourceCooldown(Block b) {
        String resourceCooldownUntil = BlockStorage.getLocationInfo(b.getLocation(), "resource-cooldown-until");
        if (resourceCooldownUntil == null) return false;
        long cooldownUntilMillis = Long.parseLong(resourceCooldownUntil);
        return cooldownUntilMillis > Instant.now().toEpochMilli();
    }

    private void setOnResourceCooldown(Block b) {
        BlockStorage.addBlockInfo(b, "resource-cooldown-until", String.valueOf(Instant.now().plusSeconds(5).toEpochMilli()));
    }

    private void removeResourceCooldown(Block b) {
        BlockStorage.addBlockInfo(b, "resource-cooldown-until", null);
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        return Arrays.asList(emptyBucket, SlimefunItems.OIL_BUCKET);
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

            if (this.isOnResourceCooldown(b)) {
                /*
                 * Oil Pumps can cause lag getting supplies each tick if there are none.
                 * We add a cooldown to limit the amount of processing required.
                 */ 
                return null;
            } else {
                /*
                 * Not on resource cooldown, so remove the data if it exists.
                 */
                this.removeResourceCooldown(b);
            }
            

            for (int slot : getInputSlots()) {
                if (SlimefunUtils.isItemSimilar(inv.getItemInSlot(slot), emptyBucket, true, false)) {
                    OptionalInt supplies = Slimefun.getGPSNetwork().getResourceManager().getSupplies(oil, b.getWorld(), b.getX() >> 4, b.getZ() >> 4);

                    if (supplies.isPresent() && supplies.getAsInt() > 0) {
                        MachineRecipe recipe = new MachineRecipe(26, new ItemStack[] { emptyBucket }, new ItemStack[] { SlimefunItems.OIL_BUCKET });

                        inv.consumeItem(slot);
                        Slimefun.getGPSNetwork().getResourceManager().setSupplies(oil, b.getWorld(), b.getX() >> 4, b.getZ() >> 4, supplies.getAsInt() - 1);
                        return recipe;
                    } else {
                        /*
                         * Set the oil pump on cooldown to prevent it
                         * from immediately starting all over again (to prevent lag)
                         * We don't move the bucket into the output slots as it can cause the entire
                         * system to be filled with buckets.
                         */
                        this.setOnResourceCooldown(b);
                        return null;
                    }
                }
            }
        }

        return null;
    }
}
