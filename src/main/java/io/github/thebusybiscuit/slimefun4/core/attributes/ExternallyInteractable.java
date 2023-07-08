package io.github.thebusybiscuit.slimefun4.core.attributes;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.attributes.interactions.InteractionResult;
import org.bukkit.Location;

import javax.annotation.Nonnull;

/**
 * Implement this interface for any {@link SlimefunItem} to provide methods for
 * external code to 'interact' with the item when placed as a block in the world.
 *
 * @author Sefiraat
 */
@FunctionalInterface
public interface ExternallyInteractable {

    /**
     * This method should be used by the implementing class to fulfill the actions needed
     * when being interacted with returning the result of the interaction.
     *
     * @param location
     *                 The {@link Location} of the Block being targeted for the interaction.
     *
     * @return The {@link InteractionResult} denoting the result of the interaction.
     */
    @Nonnull
    InteractionResult onInteract(@Nonnull Location location);

}
