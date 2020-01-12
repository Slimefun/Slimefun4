package me.mrCookieSlime.Slimefun.Objects.handlers;

@FunctionalInterface
public interface ItemHandler {

	default boolean isPrivate() {
		return false;
	}

	Class<? extends ItemHandler> getIdentifier();
}
