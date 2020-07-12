package io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors;

import java.util.Locale;

/**
 * This enum holds the various modes a {@link Reactor} can operate as.
 * Each mode has a different focus of operation.
 * 
 * @author TheBusyBiscuit
 *
 */
public enum ReactorMode {

    /**
     * If a {@link Reactor} is operation in production mode, the focus will
     * be set on the production of nuclear byproducts, such as Neptunium or Plutonium.
     * The key difference here is that a {@link Reactor} in production mode will still
     * continue to operate, even if the energy buffer is full.
     */
    PRODUCTION,

    /**
     * If a {@link Reactor} is operating in generator mode, the focus will be the
     * generation of power. If the energy buffer is full, the {@link Reactor} will
     * no longer consume fuel.
     */
    GENERATOR;

    @Override
    public String toString() {
        return super.toString().toLowerCase(Locale.ROOT);
    }

}
