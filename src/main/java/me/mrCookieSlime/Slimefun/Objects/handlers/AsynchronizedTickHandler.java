package me.mrCookieSlime.Slimefun.Objects.handlers;

@FunctionalInterface
public interface AsynchronizedTickHandler extends TickHandler {

    @Override
    default Class<? extends ItemHandler> getIdentifier() {
        return AsynchronizedTickHandler.class;
    }

}