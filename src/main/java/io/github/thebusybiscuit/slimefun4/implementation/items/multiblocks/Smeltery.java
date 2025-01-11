package io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.settings.IntRangeSetting;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.blocks.IgnitionChamber;
import io.github.thebusybiscuit.slimefun4.implementation.items.misc.AlloyIngot;

/**
 * The {@link Smeltery} is an upgraded version of the {@link MakeshiftSmeltery}
 * with the additional capabilities to create {@link AlloyIngot}s and utilise an
 * {@link IgnitionChamber}.
 *
 * @author TheBusyBiscuit
 * @author AtomicScience
 * @author Sfiguz7
 * @author Liruxo
 * @author emanueljg
 * @author SoSeDiK
 * @author Redemption198
 *
 * @see AbstractSmeltery
 * @see MakeshiftSmeltery
 * @see IgnitionChamber
 *
 */
public class Smeltery extends AbstractSmeltery {

    private final ItemSetting<Integer> fireBreakingChance = new IntRangeSetting(this, "fire-breaking-chance", 0, 34, 100);

    @ParametersAreNonnullByDefault
    public Smeltery(ItemGroup itemGroup, SlimefunItemStack item) {
        super(itemGroup, item, new ItemStack[] { null, new ItemStack(Material.NETHER_BRICK_FENCE), null, new ItemStack(Material.NETHER_BRICKS), CustomItemStack.create(Material.DISPENSER, "Dispenser (Facing up)"), new ItemStack(Material.NETHER_BRICKS), null, new ItemStack(Material.FLINT_AND_STEEL), null }, BlockFace.DOWN);

        addItemSetting(fireBreakingChance);
    }

    @Override
    protected void registerDefaultRecipes(@Nonnull List<ItemStack> recipes) {
        recipes.add(SlimefunItems.IRON_DUST);
        recipes.add(new ItemStack(Material.IRON_INGOT));
    }

    @Override
    public List<ItemStack> getDisplayRecipes() {
        List<ItemStack> items = new ArrayList<>();

        for (int i = 0; i < recipes.size() - 1; i += 2) {
            if (recipes.get(i) == null || Arrays.stream(recipes.get(i)).skip(1).anyMatch(Objects::nonNull)) {
                continue;
            }

            items.add(recipes.get(i)[0]);
            items.add(recipes.get(i + 1)[0]);
        }

        return items;
    }

    @Override
    protected void craft(Player p, Block b, Inventory inv, ItemStack[] recipe, ItemStack output, Inventory outputInv) {
        super.craft(p, b, inv, recipe, output, outputInv);

        if (ThreadLocalRandom.current().nextInt(100) < fireBreakingChance.getValue()) {
            consumeFire(p, b.getRelative(BlockFace.DOWN), b);
        }
    }

    @ParametersAreNonnullByDefault
    private void consumeFire(Player p, Block dispenser, Block b) {
        boolean isFireRenewed = IgnitionChamber.useFlintAndSteel(p, dispenser);

        if (!isFireRenewed) {
            Block fire = b.getRelative(BlockFace.DOWN).getRelative(BlockFace.DOWN);
            fire.getWorld().playEffect(fire.getLocation(), Effect.STEP_SOUND, fire.getType());
            fire.setType(Material.AIR);
        }
    }

}
