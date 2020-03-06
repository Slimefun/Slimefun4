package me.mrCookieSlime.Slimefun.Objects.handlers;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

import io.github.thebusybiscuit.cscorelib2.collections.LoopIterator;
import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollection;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.RainbowBlock;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
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
    private Material material;

    public RainbowTicker(Material... materials) {
        iterator = new LoopIterator<>(Arrays.asList(materials));
        material = iterator.next();
    }

    public RainbowTicker(MaterialCollection collection) {
        this(collection.getAsArray());
    }

    @Override
    public void tick(Block b, SlimefunItem item, Config data) {
        BlockData blockData = b.getBlockData();
        boolean waterlogged = blockData instanceof Waterlogged && ((Waterlogged) blockData).isWaterlogged();

        b.setType(material, true);

        if (waterlogged) {
            Waterlogged block = (Waterlogged) b.getBlockData();
            block.setWaterlogged(true);
            b.setBlockData(block);
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
