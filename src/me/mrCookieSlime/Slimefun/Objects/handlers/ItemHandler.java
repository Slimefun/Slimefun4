package me.mrCookieSlime.Slimefun.Objects.handlers;

@FunctionalInterface
public interface ItemHandler {
	
	String toCodename();

	default boolean isPrivate() {
		return false;
	}
}
