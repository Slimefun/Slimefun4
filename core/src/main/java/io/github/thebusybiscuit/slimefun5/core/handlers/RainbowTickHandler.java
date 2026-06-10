package io.github.thebusybiscuit.slimefun5.core.handlers;

import io.github.thebusybiscuit.slimefun5.utils.compatibility.BlockDataCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;

import io.github.bakedlibs.dough.collections.LoopIterator;
import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.blocks.RainbowBlock;
import io.github.thebusybiscuit.slimefun5.utils.ColoredMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;

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
public class RainbowTickHandler extends BlockTicker {

    private final LoopIterator<Material> iterator;
    private final boolean glassPanes;
    private Material material;

    public RainbowTickHandler(@Nonnull List<Material> materials) {
        Validate.notNull(materials, "The materials List cannot be null!");

        // Java-8 universal port: on legacy servers some coloured materials don't exist (XMaterial
        // resolves them to null). Strip those out so the item still registers; if none remain, fall
        // back to a single static material so the block simply doesn't cycle colours on that version.
        List<Material> filtered = new java.util.ArrayList<>();

        for (Material mat : materials) {
            if (mat != null) {
                filtered.add(mat);
            }
        }

        if (filtered.isEmpty()) {
            filtered.add(Material.STONE);
        }

        glassPanes = containsGlassPanes(filtered);
        iterator = new LoopIterator<>(filtered);
        material = iterator.next();
    }

    public RainbowTickHandler(@Nonnull Material... materials) {
        this(Arrays.asList(materials));
    }

    public RainbowTickHandler(@Nonnull ColoredMaterial material) {
        this(material.asList());
    }

    /**
     * This method checks whether a given {@link Material} array contains any {@link Material}
     * that would result in a {@link GlassPane} {@link BlockData}.
     * This is done to save performance, so we don't have to validate {@link BlockData} at
     * runtime.
     * 
     * @param materials
     *            The {@link Material} Array to check
     * 
     * @return Whether the array contained any {@link GlassPane} materials
     */
    private boolean containsGlassPanes(@Nonnull List<Material> materials) {
        if (Slimefun.getMinecraftVersion() == MinecraftVersion.UNIT_TEST) {
            // BlockData is not available to us during Unit Tests :/
            return false;
        }

        for (Material type : materials) {
            /*
              This BlockData is purely virtual and only created on startup, it should have
              no impact on performance, in fact it should save performance as it preloads
              the data but also saves heavy calls for other Materials
             */
            if (BlockDataCompat.isInstance(BlockDataCompat.createBlockData(type), "org.bukkit.block.data.type.GlassPane")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void tick(Block b, SlimefunItem item, Config data) {
        if (MaterialCompat.isAir(b.getType())) {
            /*
              The block was broken, setting the Material now would result in a
              duplication glitch
             */
            return;
        }

        if (glassPanes) {
            Object previousData = BlockDataCompat.getBlockData(b);

            if (BlockDataCompat.isInstance(previousData, "org.bukkit.block.data.type.GlassPane")) {
                Object block = BlockDataCompat.createBlockData(material, bd -> {
                    BlockDataCompat.set(bd, "setWaterlogged", BlockDataCompat.get(previousData, "isWaterlogged"));

                    Object allowedFaces = BlockDataCompat.get(previousData, "getAllowedFaces");

                    if (allowedFaces instanceof Iterable) {
                        for (Object face : (Iterable<?>) allowedFaces) {
                            ReflectionCompat.invoke(bd, "setFace", face, ReflectionCompat.invoke(previousData, "hasFace", face));
                        }
                    }
                });

                BlockDataCompat.setBlockData(b, block, false);
                return;
            }
        }

        b.setType(material, false);
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

