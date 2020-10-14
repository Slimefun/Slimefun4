package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.Collection;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

/**
 * Represents a {@link MinerAndroid Programmable Android (Miner)}.
 * This {@link ProgrammableAndroid} can dig blocks and move.
 *
 * @author Linox
 *
 * @see AndroidMineEvent
 *
 */
public class MinerAndroid extends ProgrammableAndroid {

    // Determines the drops a miner android will get
    private final ItemStack effectivePickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

    @ParametersAreNonnullByDefault
    public MinerAndroid(Category category, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, tier, item, recipeType, recipe);
    }

    @Override
    @Nonnull
    public AndroidType getAndroidType() {
        return AndroidType.MINER;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void dig(Block b, BlockMenu menu, Block block) {
        Collection<ItemStack> drops = block.getDrops(effectivePickaxe);

        if (!SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(block.getType()) && !drops.isEmpty()) {
            OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")));

            if (SlimefunPlugin.getProtectionManager().hasPermission(owner, block.getLocation(), ProtectableAction.BREAK_BLOCK)) {
                AndroidMineEvent event = new AndroidMineEvent(block, new AndroidInstance(this, b));

                // We only want to break non-Slimefun blocks by default, although an addon may want it differently.
                if (BlockStorage.hasBlockInfo(block)) {
                    event.setCancelled(true);
                }

                Bukkit.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    for (ItemStack drop : drops) {
                        if (menu.fits(drop, getOutputSlots())) {
                            menu.pushItem(drop, getOutputSlots());
                        }
                    }

                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                    block.setType(Material.AIR);
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void moveAndDig(Block b, BlockMenu menu, BlockFace face, Block block) {
        dig(b, menu, block);
        move(b, face, block);
    }

}
