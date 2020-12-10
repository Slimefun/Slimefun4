package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import java.util.Collection;
import java.util.UUID;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import io.github.thebusybiscuit.slimefun4.api.events.AndroidMineEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.InfiniteBlockGenerator;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;

public class MinerAndroid extends ProgrammableAndroid {

    // Determines the drops a miner android will get
    private final ItemStack effectivePickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
    private final ItemSetting<Boolean> firesEvent = new ItemSetting<>("trigger-event-for-generators", false);

    @ParametersAreNonnullByDefault
    public MinerAndroid(Category category, int tier, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, tier, item, recipeType, recipe);
        addItemSetting(firesEvent);
    }

    @Override
    public AndroidType getAndroidType() {
        return AndroidType.MINER;
    }

    @Override
    protected void dig(Block b, BlockMenu menu, Block block) {
        Collection<ItemStack> drops = block.getDrops(effectivePickaxe);

        if (!SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(block.getType()) && !drops.isEmpty()) {
            OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")));

            if (SlimefunPlugin.getProtectionManager().hasPermission(owner, block.getLocation(), ProtectableAction.BREAK_BLOCK)) {
                AndroidMineEvent event = new AndroidMineEvent(block, new AndroidInstance(this, b));
                Bukkit.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                // We only want to break non-Slimefun blocks
                if (!BlockStorage.hasBlockInfo(block)) {
                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                    breakBlock(menu, drops, block);
                }
            }
        }
    }

    @Override
    protected void moveAndDig(Block b, BlockMenu menu, BlockFace face, Block block) {
        Collection<ItemStack> drops = block.getDrops(effectivePickaxe);

        if (!SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(block.getType()) && !drops.isEmpty()) {
            OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(BlockStorage.getLocationInfo(b.getLocation(), "owner")));

            if (SlimefunPlugin.getProtectionManager().hasPermission(owner, block.getLocation(), ProtectableAction.BREAK_BLOCK)) {
                AndroidMineEvent event = new AndroidMineEvent(block, new AndroidInstance(this, b));
                Bukkit.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                // We only want to break non-Slimefun blocks
                if (!BlockStorage.hasBlockInfo(block)) {
                    block.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType());
                    breakBlock(menu, drops, block);
                    move(b, face, block);
                }
            } else {
                move(b, face, block);
            }
        } else {
            move(b, face, block);
        }
    }

    @ParametersAreNonnullByDefault
    private void breakBlock(BlockMenu menu, Collection<ItemStack> drops, Block block) {
        // Push our drops to the inventory
        for (ItemStack drop : drops) {
            menu.pushItem(drop, getOutputSlots());
        }

        InfiniteBlockGenerator generator = InfiniteBlockGenerator.findAt(block);

        if (generator != null) {
            if (firesEvent.getValue()) {
                generator.callEvent(block);
            }

            // "poof" a "new" block was generated
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.075F, 0.8F);
            block.getWorld().spawnParticle(Particle.SMOKE_NORMAL, block.getX() + 0.5, block.getY() + 1.25, block.getZ() + 0.5, 8, 0.5, 0.5, 0.5, 0.015);
        } else {
            block.setType(Material.AIR);
        }
    }

}
