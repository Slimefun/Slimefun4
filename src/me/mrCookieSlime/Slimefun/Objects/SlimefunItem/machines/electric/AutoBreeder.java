package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric;

import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.InvUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.Animals;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.interfaces.InventoryBlock;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.holograms.AutoBreederHologram;

public class AutoBreeder extends SlimefunItem implements InventoryBlock {

    private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};

    public AutoBreeder(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, name, recipeType, recipe);
        createPreset(this, "&6Auto Breeder", this::constructMenu);

        registerBlockHandler(name, (p, b, tool, reason) -> {
            AutoBreederHologram.remove(b);
            BlockMenu inv = BlockStorage.getInventory(b);
            if (inv != null) {
                for (int slot : getInputSlots()) {
                    if (inv.getItemInSlot(slot) != null) {
                        b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
                        inv.replaceExistingItem(slot, null);
                    }
                }
            }
            return true;
        });
    }

    protected void constructMenu(BlockMenuPreset preset) {
        for (int i : border) {
            preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "), (p, slot, item, action) -> false);
        }
    }

    public int getEnergyConsumption() {
        return 60;
    }

    @Override
    public int[] getInputSlots() {
        return new int[] {10, 11, 12, 13, 14, 15, 16};
    }

    @Override
    public int[] getOutputSlots() {
        return new int[0];
    }

    @Override
    public void preRegister() {
        addItemHandler(new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sf, Config data) {
                try {
                    AutoBreeder.this.tick(b);
                } catch (Exception x) {
                    Slimefun.getLogger().log(Level.SEVERE, "An Error occured while ticking an Auto Breeder for Slimefun " + Slimefun.getVersion(), x);
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        });
    }

    protected void tick(Block b) throws Exception {
        for (Entity n : AutoBreederHologram.getArmorStand(b, true).getNearbyEntities(4D, 2D, 4D)) {
            if (Animals.isFeedable(n)) {
                for (int slot : getInputSlots()) {
                    if (SlimefunManager.isItemSimiliar(BlockStorage.getInventory(b).getItemInSlot(slot), SlimefunItems.ORGANIC_FOOD, false)) {
                        if (ChargableBlock.getCharge(b) < getEnergyConsumption()) return;
                        ChargableBlock.addCharge(b, -getEnergyConsumption());
                        BlockStorage.getInventory(b).replaceExistingItem(slot, InvUtils.decreaseItem(BlockStorage.getInventory(b).getItemInSlot(slot), 1));
                        Animals.feed(n);
                        n.getWorld().spawnParticle(Particle.HEART,((LivingEntity) n).getEyeLocation(), 8, 0.2F, 0.2F, 0.2F);
                        return;
                    }
                }
            }
        }
    }

}