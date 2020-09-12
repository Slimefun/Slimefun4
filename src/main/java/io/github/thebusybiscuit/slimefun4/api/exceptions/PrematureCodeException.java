package io.github.thebusybiscuit.slimefun4.api.exceptions;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

/**
 * A {@link PrematureCodeException} is thrown when a {@link SlimefunAddon} tried
 * to access Slimefun code before Slimefun was enabled.
 * Always let your code run inside onEnable() or later, never on class initialization.
 * 
 * @author TheBusyBiscuit
 *
 */
public class PrematureCodeException extends RuntimeException {

    private static final long serialVersionUID = -7409054512888866955L;

    /**
     * This constructs a new {@link PrematureCodeException} with the given error context.
     * 
     * @param message
     *            An error message to display
     */
    @ParametersAreNonnullByDefault
    public PrematureCodeException(String message) {
        super("Slimefun code was invoked before Slimefun finished loading: " + message);
    }

}
