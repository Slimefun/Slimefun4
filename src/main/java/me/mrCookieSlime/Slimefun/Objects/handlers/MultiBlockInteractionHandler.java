package me.mrCookieSlime.Slimefun.Objects.handlers;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.multiblocks.MultiBlockMachine;

/**
 * This {@link ItemHandler} is called whenever a {@link Player} interacts with
 * this {@link MultiBlock}.
 * Note that this {@link MultiBlockInteractionHandler} should be assigned to
 * a class that inherits from {@link MultiBlockMachine}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see ItemHandler
 * @see MultiBlock
 * @see MultiBlockMachine
 *
 */
@FunctionalInterface
public interface MultiBlockInteractionHandler extends ItemHandler {

    boolean onInteract(Player p, MultiBlock mb, Block b);

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return MultiBlockInteractionHandler.class;
    }
}
