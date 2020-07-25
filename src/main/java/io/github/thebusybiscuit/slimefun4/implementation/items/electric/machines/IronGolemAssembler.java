package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.IronGolem;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;

/**
 * The {@link IronGolemAssembler} is an electrical machine that can automatically spawn
 * a {@link IronGolem} if the required ingredients have been provided.
 * 
 * @author TheBusyBiscuit
 * 
 * @see WitherAssembler
 *
 */
public class IronGolemAssembler extends AbstractEntityAssembler<IronGolem> {

    public IronGolemAssembler(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public int getCapacity() {
        return 4096;
    }

    @Override
    public int getEnergyConsumption() {
        return 2048;
    }

    @Override
    public ItemStack getHead() {
        return new ItemStack(Material.CARVED_PUMPKIN);
    }

    @Override
    public Material getHeadBorder() {
        return Material.ORANGE_STAINED_GLASS_PANE;
    }

    @Override
    public ItemStack getBody() {
        return new ItemStack(Material.IRON_BLOCK, 4);
    }

    @Override
    public Material getBodyBorder() {
        return Material.WHITE_STAINED_GLASS_PANE;
    }

    @Override
    protected void constructMenu(BlockMenuPreset preset) {
        preset.addItem(1, new CustomItem(getHead(), "&7Pumpkin Slot", "", "&fThis Slot accepts Pumpkins"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(7, new CustomItem(getBody(), "&7Iron Block Slot", "", "&fThis Slot accepts Iron Blocks"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(13, new CustomItem(Material.CLOCK, "&7Cooldown: &b30 Seconds", "", "&fThis Machine takes up to half a Minute to operate", "&fso give it some Time!"), ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    public IronGolem spawnEntity(Location l) {
        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_15)) {
            // This sound doesn't exist in 1.14 and earlier :/
            l.getWorld().playSound(l, Sound.ENTITY_IRON_GOLEM_REPAIR, 0.5F, 1);
        }

        return l.getWorld().spawn(l, IronGolem.class);
    }

}
