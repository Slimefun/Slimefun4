package io.github.thebusybiscuit.slimefun4.core.attributes;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Implement this interface for any {@link SlimefunItem} to provide methods for
 * external code to 'interact' with the item when placed as a block in the world.
 *
 * @author Sefiraat
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
    @Nonnull InteractionResult onInteract(@Nonnull Location location);

    /**
     * This class represents the result of an interaction on an {@link ExternallyInteractable} item.
     */
    class InteractionResult {

        private final boolean interactionSuccessful;
        private @Nullable String resultMessage;
        private final @Nonnull Set<ItemStack> resultItems = new HashSet<>();

        /**
         * Creates a new InteractionResult.
         *
         * @param successful
         *                   Whether the interaction was successful or not.
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
         * Adds an or several {@link ItemStack}'s into the result.
         */
        public void addResultItems(ItemStack... itemStacks) {
            Collections.addAll(resultItems, itemStacks);
        }

        /**
         * This returned whether items are included as part of the result.
         *
         * @return True if items are included in the result.
         */
        public boolean resultedInItems() {
            return !this.resultItems.isEmpty();
        }

        /**
         * Returns the {@link ItemStack}(s) produced as a result of this interaction, if any.
         *
         * @return An unmodifiable {@link Set} of {@link ItemStack}(s) created due to the interaction.
         */
        public @Nonnull Set<ItemStack> getResultItems() {
            return Collections.unmodifiableSet(resultItems);
        }

        /**
         * Sets a custom result message for this interaction.
         *
         * @param
         *       resultMessage The message to be sent with the Result
         */
        public void setResultMessage(@Nullable String resultMessage) {
            this.resultMessage = resultMessage;
        }

        /**
         * Returns whether this result has a result message or not.
         *
         * @return True if a result message is present
         */
        public boolean hasResultMessage() {
            return this.resultMessage != null;
        }

        /**
         * Returns the custom result message for this result.
         *
         * @return A String of the provided custom result message.
         */
        public @Nullable String getResultMessage() {
            return resultMessage;
        }
    }
}
