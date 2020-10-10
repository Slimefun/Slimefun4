package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.settings.DoubleRangeSetting;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class InfusedHopper extends SimpleSlimefunItem<BlockTicker> {

    private final ItemSetting<Boolean> silent = new ItemSetting<>("silent", false);
    private final ItemSetting<Double> radius = new DoubleRangeSetting("radius", 0.1, 3.5, Double.MAX_VALUE);

    public InfusedHopper(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        addItemSetting(silent, radius);
    }

    @Override
    public BlockTicker getItemHandler() {
        return new BlockTicker() {

            @Override
            public void tick(Block b, SlimefunItem sfItem, Config data) {
                if (b.getType() != Material.HOPPER) {
                    // we're no longer a hopper, we were probably destroyed. skipping this tick.
                    BlockStorage.clearBlockInfo(b);
                    return;
                }

                Location l = b.getLocation().add(0.5, 1.2, 0.5);
                boolean sound = false;
                double range = radius.getValue();

                for (Entity item : b.getWorld().getNearbyEntities(l, range, range, range, n -> isValidItem(l, n))) {
                    item.setVelocity(new Vector(0, 0.1, 0));
                    item.teleport(l);
                    sound = true;
                }

                if (sound && !silent.getValue().booleanValue()) {
                    b.getWorld().playSound(b.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1F, 2F);
                }
            }

            @Override
            public boolean isSynchronized() {
                return true;
            }
        };
    }

    private boolean isValidItem(Location l, Entity entity) {
        if (entity instanceof Item && entity.isValid()) {
            Item item = (Item) entity;
            return !SlimefunUtils.hasNoPickupFlag(item) && item.getLocation().distanceSquared(l) > 0.25;
        }

        return false;
    }
}
