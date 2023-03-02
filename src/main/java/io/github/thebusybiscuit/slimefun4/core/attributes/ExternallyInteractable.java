package io.github.thebusybiscuit.slimefun4.core.attributes;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashSet;
import java.util.Set;

/**
 * Implement this interface for any {@link SlimefunItem} to provide methods for
 * external code to 'interact' with the item when placed as a block in the world.
 *
 * @author TheBusyBiscuit
 *
 */
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

    /**
     * This class represents the result of an interaction on an {@link ExternallyInteractable} item.
     */
    class InteractionResult {

        private final boolean interactionSuccessful;
        @Nonnull
        private final Set<ItemStack> resultItems = new HashSet<>();

        /**
         * Creates a new InteractionResult
         *
         * @param successful
         *                   Whether the interaction was successful or not
         */
        @ParametersAreNonnullByDefault
        public InteractionResult(boolean successful) {
            this.interactionSuccessful = successful;
        }

        /**
         * Returns whether the interaction was successful or not.
         *
         * @return boolean denoting success.
         */
        public boolean isInteractionSuccessful() {
            return interactionSuccessful;
        }

        /**
         * Returns the {@link ItemStack}(s) produced as a result of this interaction, if any.
         *
         * @return A list of {@link ItemStack}(s) to process.
         */
        @Nonnull
        public Set<ItemStack> getResultItems() {
            return resultItems;
        }
    }
}
