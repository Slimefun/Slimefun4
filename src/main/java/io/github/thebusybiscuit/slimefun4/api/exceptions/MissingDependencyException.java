package io.github.thebusybiscuit.slimefun4.api.exceptions;

import javax.annotation.ParametersAreNonnullByDefault;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

/**
 * A {@link MissingDependencyException} is thrown when a {@link SlimefunAddon} tried
 * to register Items without marking Slimefun as a dependency.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunAddon
 *
 */
public class MissingDependencyException extends RuntimeException {

    private static final long serialVersionUID = -2255888430181930571L;

    /**
     * This constructs a new {@link MissingDependencyException} for the given
     * {@link SlimefunAddon} and the given dependency ("Slimefun")
     * 
     * @param addon
     *            The {@link SlimefunAddon} that caused this exception
     * @param dependency
     *            The dependency that is required ("Slimefun")
     */
    @ParametersAreNonnullByDefault
    public MissingDependencyException(SlimefunAddon addon, String dependency) {
        super("Slimefun Addon \"" + addon.getName() + "\" forgot to define \"" + dependency + "\" as a depend or softdepend inside the plugin.yml file");
    }

}
