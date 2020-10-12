package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.GlassPane;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.collections.LoopIterator;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.core.attributes.TickingBlock;
import io.github.thebusybiscuit.slimefun4.core.attributes.TickingMethod;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * A {@link RainbowBlock} is a {@link TickingBlock} which changes its {@link Material}
 * on every tick.
 * 
 * @author TheBusyBiscuit
 *
 */
public class RainbowBlock extends SlimefunItem implements TickingBlock {

    private final LoopIterator<Material> iterator;
    private final boolean glassPanes;
    private Material material;

    @ParametersAreNonnullByDefault
    public RainbowBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, @Nonnull List<Material> materials) {
        super(category, item, recipeType, recipe, recipeOutput);
        Validate.noNullElements(materials, "A Rainbow Block cannot have a Material that is null!");

        if (materials.isEmpty()) {
            throw new IllegalArgumentException("A Rainbow Block must have at least one Material associated with it!");
        }

        glassPanes = containsGlassPanes(materials);
        iterator = new LoopIterator<>(materials);
        material = iterator.next();
    }

    @ParametersAreNonnullByDefault
    public RainbowBlock(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput, Material... materials) {
        this(category, item, recipeType, recipe, recipeOutput, Arrays.asList(materials));
    }

    @Override
    public void tick(@Nonnull Block b) {
        if (b.getType() == Material.AIR) {
            // The block was broken, setting the Material now would result in a
            // duplication glitch
            return;
        }

        if (glassPanes) {
            BlockData blockData = b.getBlockData();

            if (blockData instanceof GlassPane) {
                BlockData block = material.createBlockData(bd -> {
                    if (bd instanceof GlassPane) {
                        GlassPane previousData = (GlassPane) blockData;
                        GlassPane nextData = (GlassPane) bd;

                        nextData.setWaterlogged(previousData.isWaterlogged());

                        for (BlockFace face : previousData.getAllowedFaces()) {
                            nextData.setFace(face, previousData.hasFace(face));
                        }
                    }
                });

                b.setBlockData(block, false);
                return;
            }
        }

        b.setType(material, false);
    }

    @Override
    public void onTickCycleStart() {
        material = iterator.next();
    }

    @Override
    public TickingMethod getTickingMethod() {
        return TickingMethod.MAIN_THREAD;
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
        if (SlimefunPlugin.getMinecraftVersion() == MinecraftVersion.UNIT_TEST) {
            // BlockData is not available to us during Unit Tests :/
            return false;
        }

        for (Material type : materials) {
            // This BlockData is purely virtual and only created on startup, it should have
            // no impact on performance, in fact it should save performance as it preloads
            // the data but also saves heavy calls for other Materials
            if (type.createBlockData() instanceof GlassPane) {
                return true;
            }
        }

        return false;
    }

}
