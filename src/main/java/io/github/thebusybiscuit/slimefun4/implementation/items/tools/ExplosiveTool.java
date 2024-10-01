package io.github.thebusybiscuit.slimefun4.implementation.items.tools;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.ExplosionResult;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.protection.Interaction;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.events.ExplosiveToolBreakBlocksEvent;
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.DamageableItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.ToolUseHandler;
import io.github.thebusybiscuit.slimefun4.core.services.sounds.SoundEffect;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;

import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * This {@link SlimefunItem} is a super class for items like the {@link ExplosivePickaxe} or {@link ExplosiveShovel}.
 *
 * @author TheBusyBiscuit
 *
 * @see ExplosivePickaxe
 * @see ExplosiveShovel
 *
 */
public class ExplosiveTool extends SimpleSlimefunItem<ToolUseHandler> implements NotPlaceable, DamageableItem {

    private final ItemSetting<Boolean> damageOnUse = new ItemSetting<>(this, "damage-on-use", true);
    private final ItemSetting<Boolean> callExplosionEvent = new ItemSetting<>(this, "call-explosion-event", false);

    private static Constructor<?> pre21ExplodeEventConstructor;
    static {
        if (Slimefun.getMinecraftVersion().isBefore(MinecraftVersion.MINECRAFT_1_21)) {
            try {
                pre21ExplodeEventConstructor = BlockExplodeEvent.class.getConstructor(Block.class, List.class, float.class);
            } catch (Exception e) {
                Slimefun.logger().log(Level.SEVERE, "Could not find constructor for BlockExplodeEvent", e);
            }
        }
    }

    @ParametersAreNonnullByDefault
    public ExplosiveTool(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);

        addItemSetting(damageOnUse, callExplosionEvent);
    }

    @Nonnull
    @Override
    public ToolUseHandler getItemHandler() {
        return (e, tool, fortune, drops) -> {
            Player p = e.getPlayer();

            if (!p.isSneaking()) {
                Block b = e.getBlock();

                b.getWorld().createExplosion(b.getLocation(), 0);
                SoundEffect.EXPLOSIVE_TOOL_EXPLODE_SOUND.playAt(b);

                List<Block> blocks = findBlocks(b);
                breakBlocks(e, p, tool, b, blocks, drops);
            }
        };
    }

    @ParametersAreNonnullByDefault
    private void breakBlocks(BlockBreakEvent e, Player p, ItemStack item, Block b, List<Block> blocks, List<ItemStack> drops) {
        List<Block> blocksToDestroy = new ArrayList<>();

        if (callExplosionEvent.getValue()) {
            BlockExplodeEvent blockExplodeEvent = createNewBlockExplodeEvent(b, blocks, 0);
            Bukkit.getServer().getPluginManager().callEvent(blockExplodeEvent);

            if (!blockExplodeEvent.isCancelled()) {
                for (Block block : blockExplodeEvent.blockList()) {
                    if (canBreak(p, block)) {
                        if (Slimefun.getIntegrations().isCustomBlock(block)) {
                            drops.addAll(CustomBlock.byAlreadyPlaced(block).getLoot());
                            CustomBlock.remove(block.getLocation());
                        }
                        blocksToDestroy.add(block);
                    }
                }
            }
        } else {
            for (Block block : blocks) {
                if (canBreak(p, block)) {
                    if (Slimefun.getIntegrations().isCustomBlock(block)) {
                        drops.addAll(CustomBlock.byAlreadyPlaced(block).getLoot());
                        CustomBlock.remove(block.getLocation());
                    }
                    blocksToDestroy.add(block);
                }
            }
        }

        ExplosiveToolBreakBlocksEvent event = new ExplosiveToolBreakBlocksEvent(p, b, blocksToDestroy, item, this);
        Bukkit.getServer().getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            for (Block block : blocksToDestroy) {
                breakBlock(e, p, item, block, drops);
            }
        }
    }

    @Nonnull
    private List<Block> findBlocks(@Nonnull Block b) {
        List<Block> blocks = new ArrayList<>(26);

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    // We can skip the center block since that will break as usual
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    blocks.add(b.getRelative(x, y, z));
                }
            }
        }

        return blocks;
    }

    @Override
    public boolean isDamageable() {
        return damageOnUse.getValue();
    }

    protected boolean canBreak(@Nonnull Player p, @Nonnull Block b) {
        if (b.isEmpty() || b.isLiquid()) {
            return false;
        } else if (SlimefunTag.UNBREAKABLE_MATERIALS.isTagged(b.getType())) {
            return false;
        } else if (!b.getWorld().getWorldBorder().isInside(b.getLocation())) {
            return false;
        } else {
            return Slimefun.getProtectionManager().hasPermission(p, b.getLocation(), Interaction.BREAK_BLOCK);
        }
    }

    @ParametersAreNonnullByDefault
    private void breakBlock(BlockBreakEvent e, Player p, ItemStack item, Block b, List<ItemStack> drops) {
        Slimefun.getProtectionManager().logAction(p, b, Interaction.BREAK_BLOCK);
        Material material = b.getType();

        b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, material);
        SlimefunItem sfItem = BlockStorage.check(b);

        if (sfItem != null && !sfItem.useVanillaBlockBreaking()) {
            /*
             * Fixes #2989
             * We create a dummy here to pass onto the BlockBreakHandler.
             * This will set the correct block context.
             */
            BlockBreakEvent dummyEvent = new BlockBreakEvent(b, e.getPlayer());

            /*
             * Fixes #3036 and handling in general.
             * Call the BlockBreakHandler if the block has one to allow for proper handling.
             */
            sfItem.callItemHandler(BlockBreakHandler.class, handler -> handler.onPlayerBreak(dummyEvent, item, drops));

            // Make sure the event wasn't cancelled by the BlockBreakHandler.
            if (!dummyEvent.isCancelled()) {
                drops.addAll(sfItem.getDrops(p));
                b.setType(Material.AIR);
                BlockStorage.clearBlockInfo(b);
            }
        } else {
            b.breakNaturally(item);
        }

        damageItem(p, item);
    }

    private BlockExplodeEvent createNewBlockExplodeEvent(
        Block block,
        List<Block> blocks,
        float yield
    ) {
        var version = Slimefun.getMinecraftVersion();
        if (version.isAtLeast(MinecraftVersion.MINECRAFT_1_21)) {
            return new BlockExplodeEvent(block, block.getState(), blocks, yield, ExplosionResult.DESTROY);
        } else if (pre21ExplodeEventConstructor != null) {
            try {
                return (BlockExplodeEvent) pre21ExplodeEventConstructor.newInstance(block, blocks, yield);
            } catch (Exception e) {
                Slimefun.logger().log(Level.SEVERE, "Could not find constructor for BlockExplodeEvent", e);
            }

            return null;
        } else {
            throw new IllegalStateException("BlockExplodeEvent constructor not found");
        }
    }
}
