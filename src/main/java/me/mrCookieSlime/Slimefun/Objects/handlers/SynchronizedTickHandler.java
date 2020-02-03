package me.mrCookieSlime.Slimefun.Objects.handlers;

@FunctionalInterface
public interface SynchronizedTickHandler extends TickHandler {
	
	@Override
	default Class<? extends ItemHandler> getIdentifier() {
		return SynchronizedTickHandler.class;
	}

}
