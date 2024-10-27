package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.utils.UnbreakingAlgorithm;
import io.github.thebusybiscuit.slimefun4.utils.compatibility.VersionedEnchantment;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dropper;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.handlers.VanillaInventoryDropHandler;
import io.github.thebusybiscuit.slimefun4.implementation.items.multiblocks.Smeltery;
import io.papermc.lib.PaperLib;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * The {@link IgnitionChamber} is used to re-ignite a {@link Smeltery}.
 * 
 * @author AtomicScience
 * @author TheBusyBiscuit
 * 
 * @see Smeltery
 *
 */
public class IgnitionChamber extends SlimefunItem {

    // @formatter:off
    private static final BlockFace[] ADJACENT_FACES = {
        BlockFace.NORTH,
        BlockFace.EAST,
        BlockFace.SOUTH,
        BlockFace.WEST
    };
    // @formatter:on

    @ParametersAreNonnullByDefault
    public IgnitionChamber(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemHandler(new VanillaInventoryDropHandler<>(Dropper.class));
    }

    /**
     * This triggers an {@link IgnitionChamber} to be used from the given {@link Smeltery}
     * block and {@link Player}.
     * 
     * @param p
     *            The {@link Player} who triggered this action
     * @param smelteryBlock
     *            The {@link Dispenser} block of our {@link Smeltery}
     * 
     * @return Whether the operation completed successfully.
     *         This will return <code>false</code> when there is no
     *         chamber or no flint and steel present
     */
    @ParametersAreNonnullByDefault
    public static boolean useFlintAndSteel(Player p, Block smelteryBlock) {
        Validate.notNull(p, "The Player must not be null!");
        Validate.notNull(smelteryBlock, "The smeltery block cannot be null!");

        Inventory inv = findIgnitionChamber(smelteryBlock);

        // Check if there even is a chamber nearby
        if (inv == null) {
            return false;
        }

        // Check if the chamber contains a Flint and Steel
        if (!inv.contains(Material.FLINT_AND_STEEL)) {
            // Notify the Player there is a chamber but without any Flint and Steel
            Slimefun.getLocalization().sendMessage(p, "machines.ignition-chamber-no-flint", true);
            return false;
        }

        ItemStack item = inv.getItem(inv.first(Material.FLINT_AND_STEEL));

        // Only damage the Flint and Steel if it isn't unbreakable.
        damageFlintAndSteel(item, smelteryBlock);

        SoundEffect.IGNITION_CHAMBER_USE_FLINT_AND_STEEL_SOUND.playAt(smelteryBlock);
        return true;
    }

    private static void damageFlintAndSteel(ItemStack flintAndSteel, Block smelteryBlock) {
        ItemMeta meta = flintAndSteel.getItemMeta();
        Damageable damageable = (Damageable) meta;

        if (meta.isUnbreakable()) {
            return;
        }

        Enchantment unbreaking = VersionedEnchantment.UNBREAKING;
        int lvl = flintAndSteel.getEnchantmentLevel(unbreaking);

        if (!UnbreakingAlgorithm.TOOLS.evaluate(lvl)) {
            damageable.setDamage(damageable.getDamage() + 1);
        }

        if (damageable.getDamage() >= flintAndSteel.getType().getMaxDurability()) {
            // The Flint and Steel broke
            flintAndSteel.setAmount(0);
            SoundEffect.LIMITED_USE_ITEM_BREAK_SOUND.playAt(smelteryBlock);
        } else {
            SoundEffect.IGNITION_CHAMBER_USE_FLINT_AND_STEEL_SOUND.playAt(smelteryBlock);
            flintAndSteel.setItemMeta(meta);
        }
    }

    private static @Nullable Inventory findIgnitionChamber(@Nonnull Block b) {
        for (BlockFace face : ADJACENT_FACES) {
            Block block = b.getRelative(face);

            if (block.getType() == Material.DROPPER && BlockStorage.check(block) instanceof IgnitionChamber) {
                BlockState state = PaperLib.getBlockState(b.getRelative(face), false).getState();

                if (state instanceof Dropper dropper) {
                    return dropper.getInventory();
                }
            }
        }

        return null;
    }

}
