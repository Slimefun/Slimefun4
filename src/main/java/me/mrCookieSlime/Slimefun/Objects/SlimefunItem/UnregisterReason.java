package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;

/**
 * Defines how a block handled by Slimefun is being unregistered.
 * <p>
 * It is notably used by
 * {@link me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler#onBreak(org.bukkit.entity.Player, org.bukkit.block.Block, SlimefunItem, UnregisterReason)}.
 *
 * @author TheBusyBiscuit
 * 
 * @see SlimefunBlockHandler
 * 
 */
public enum UnregisterReason {

    /**
     * An explosion destroys the block.
     */
    EXPLODE,

    /**
     * A player breaks the block.
     */
    PLAYER_BREAK,

    /**
     * An android miner breaks the block.
     */
    ANDROID_DIG

}
