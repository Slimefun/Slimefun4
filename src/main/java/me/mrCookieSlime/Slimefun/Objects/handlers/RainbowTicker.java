package me.mrCookieSlime.Slimefun.Objects.handlers;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

import io.github.thebusybiscuit.cscorelib2.collections.LoopIterator;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollection;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.RainbowBlock;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This is a {@link BlockTicker} that is exclusively used for Rainbow blocks.
 * On every tick it cycles through the {@link LoopIterator} and chooses the next {@link Material}
 * and sets itself to that.
 * 
 * @author TheBusyBiscuit
 * 
 * @see RainbowBlock
 *
 */
public class RainbowTicker extends BlockTicker {

    private final LoopIterator<Material> iterator;
    private final boolean waterlogged;
    private Material material;

    public RainbowTicker(Material... materials) {
        if (materials.length == 0) {
            throw new IllegalArgumentException("A RainbowTicker must have at least one Material associated with it!");
        }

        waterlogged = containsWaterlogged(materials);
        iterator = new LoopIterator<>(Arrays.asList(materials));
        material = iterator.next();
    }

    /**
     * This method checks whether a given {@link Material} array contains any {@link Material}
     * that would result in a {@link Waterlogged} {@link BlockData}.
     * This is done to save performance, so we don't have to validate {@link BlockData} at
     * runtime.
     * 
     * @param materials
     *            The {@link Material} Array to check
     * 
     * @return Whether the array contained any {@link Waterlogged} materials
     */
    private boolean containsWaterlogged(Material[] materials) {
        if (SlimefunPlugin.getMinecraftVersion() == MinecraftVersion.UNIT_TEST) {
            // BlockData is not available to us during Unit Tests :/
            return false;
        }

        for (Material type : materials) {
            // This BlockData is purely virtual and only created on startup, it should have
            // no impact on performance, in fact it should save performance as it preloads
            // the data but also saves heavy calls for non-waterlogged Materials
            if (type.createBlockData() instanceof Waterlogged) {
                return true;
            }
        }

        return false;
    }

    public RainbowTicker(MaterialCollection collection) {
        this(collection.getAsArray());
    }

    @Override
    public void tick(Block b, SlimefunItem item, Config data) {
        if (b.getType() == Material.AIR) {
            // The block was broken, setting the Material now would result in a
            // duplication glitch
            return;
        }

        if (waterlogged) {
            BlockData blockData = b.getBlockData();

            b.setType(material, true);

            if (blockData instanceof Waterlogged && ((Waterlogged) blockData).isWaterlogged()) {
                Waterlogged block = (Waterlogged) b.getBlockData();
                block.setWaterlogged(true);
                b.setBlockData(block);
            }
        }
        else {
            b.setType(material, false);
        }
    }

    @Override
    public void uniqueTick() {
        material = iterator.next();
    }

    @Override
    public boolean isSynchronized() {
        return true;
    }

}
