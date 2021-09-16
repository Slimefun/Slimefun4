package io.github.thebusybiscuit.slimefun4.implementation.items.electric.machines.entities;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.IronGolem;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;

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

    @ParametersAreNonnullByDefault
    public IronGolemAssembler(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
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
        preset.addItem(1, new CustomItemStack(getHead(), "&7Emplacement de citrouilles", "", "&fCet emplacement accepte les citrouilles"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(7, new CustomItemStack(getBody(), "&7Emplacement de bloc de fer", "", "&fCet emplacement accepte les blocs de fer"), ChestMenuUtils.getEmptyClickHandler());
        preset.addItem(13, new CustomItemStack(Material.CLOCK, "&7Temps de recharge: &b30 secondes", "", "&fLe fonctionnement de cette machine prend jusqu'à une demi-minute", "&fpour s'enclencher. Laissez lui un peu de temps!"), ChestMenuUtils.getEmptyClickHandler());
    }

    @Override
    public IronGolem spawnEntity(Location l) {
        if (Slimefun.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_15)) {
            // This sound doesn't exist in 1.14 and earlier :/
            l.getWorld().playSound(l, Sound.ENTITY_IRON_GOLEM_REPAIR, 0.5F, 1);
        }

        return l.getWorld().spawn(l, IronGolem.class);
    }

}
