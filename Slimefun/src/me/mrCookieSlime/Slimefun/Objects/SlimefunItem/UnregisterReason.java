package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

/**
 * Defines how a block handled by Slimefun is being unregistered.
 * <p>
 * It is notably used by {@link me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler#onBreak(org.bukkit.entity.Player, org.bukkit.block.Block, SlimefunItem, UnregisterReason)}.
 *
 * @since 4.0
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
