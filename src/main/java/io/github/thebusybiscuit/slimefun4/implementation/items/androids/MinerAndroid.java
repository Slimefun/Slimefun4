package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.materials.MaterialCollections;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public abstract class MinerAndroid extends ProgrammableAndroid {

    // Determines the drops a miner android will get
    private final ItemStack effectivePickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

    public MinerAndroid(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);
    }

    @Override
    public AndroidType getAndroidType() {
        return AndroidType.MINER;
    }

    @Override
    protected void mine(Block b, BlockMenu menu, Block block) {
        Collection<ItemStack> drops = block.getDrops(effectivePickaxe);

        if (!MaterialCollections.getAllUnbreakableBlocks().contains(block.getType()) && !drops.isEmpty() && SlimefunPlugin.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"))), block.getLocation(), ProtectableAction.BREAK_BLOCK)) {
            String item = BlockStorage.checkID(block);

            AndroidMineEvent event = new AndroidMineEvent(block, new AndroidInstance(this, b));
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            // We only want to break non-Slimefun blocks
            if (item == null) {
                for (ItemStack drop : drops) {
                    if (menu.fits(drop, getOutputSlots())) {
                        menu.pushItem(drop, getOutputSlots());
                        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

    @Override
    protected void movedig(Block b, BlockMenu menu, BlockFace face, Block block) {
        Collection<ItemStack> drops = block.getDrops(effectivePickaxe);

        if (!MaterialCollections.getAllUnbreakableBlocks().contains(block.getType()) && !drops.isEmpty() && SlimefunPlugin.getProtectionManager().hasPermission(Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner"))), block.getLocation(), ProtectableAction.BREAK_BLOCK)) {
            SlimefunItem item = BlockStorage.check(block);

            AndroidMineEvent event = new AndroidMineEvent(block, new AndroidInstance(this, b));
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }

            // We only want to break non-Slimefun blocks
            if (item == null) {
                for (ItemStack drop : drops) {
                    if (menu.fits(drop, getOutputSlots())) {
                        menu.pushItem(drop, getOutputSlots());
                        block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());

                        block.setType(Material.AIR);
                        move(b, face, block);

                        b.setType(Material.AIR);
                        BlockStorage.moveBlockInfo(b.getLocation(), block.getLocation());
                    }
                }
            }
        }
        else {
            move(b, face, block);
        }
    }

}
