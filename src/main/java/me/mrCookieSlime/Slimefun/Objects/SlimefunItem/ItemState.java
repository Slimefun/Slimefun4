package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

/**
 * Defines whether a SlimefunItem is enabled, disabled or fall-back to its vanilla behavior.
 *
 * @since 4.1.10
 */
public enum ItemState {
	/**
	 * This SlimefunItem is enabled.
	 */
	ENABLED,

	/**
	 * This SlimefunItem is disabled and is not a {@link VanillaItem}.
	 */
	DISABLED,

	/**
	 * This SlimefunItem is fall-back to its vanilla behavior, because it is disabled and is a {@link VanillaItem}.
	 */
	VANILLA
}