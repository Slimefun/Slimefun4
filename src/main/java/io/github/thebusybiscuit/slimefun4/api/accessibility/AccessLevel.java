package io.github.thebusybiscuit.slimefun4.api.accessibility;

import com.google.gson.JsonElement;
import io.github.thebusybiscuit.slimefun4.core.attributes.TieredAccessible;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a specific level of access. As this is an interface, complex
 * access level tress can be created.
 *
 * @see ConcreteAccessLevel
 * @see TieredAccessible
 *
 * @author md5sha256
 */
public interface AccessLevel {

    /**
     * Obtain the display name for this access level.
     *
     *
     * @return Returns a never null {@link String}, the display name to use in chat.
     */
    @Nonnull
    String getRawDisplayName();

    @Nonnull String getDisplayName(@Nonnull Player player);

    /**
     * Obtain the next {@link AccessLevel}. Implementations should adhere to the contract
     * that the next access level has a more privileges. That is to say, unless the next
     * access level is null, {@link #compare(AccessLevel)} should return 1.
     *
     * @return Returns the next access level, or null.
     */
    @Nullable
    AccessLevel getNextLevel();

    /**
     * Obtain the previous {@link AccessLevel}. Implementations should adhere to the contract
     * that the previous access level should have less privileges. That is to say, unless the next
     * access level is null, {@link #compare(AccessLevel)} should return 1.
     *
     * @return Returns the next access level, or null.
     */
    @Nullable
    AccessLevel getPreviousLevel();

    /**
     * Get the nearest equivalent {@link ConcreteAccessLevel}.
     *
     * @return Returns the nearest ConcreteAccessLevel that this level's
     * privileges represent.
     * NOTE: Implementations are not guaranteed to "round down"! However,
     * the result of the {@link #compare(AccessLevel)}} between this object instance
     * and the returned concrete access level should be 0.
     */
    @Nonnull
    ConcreteAccessLevel toConcreteAccessLevel();

    /**
     * Convenience method to check if this AccessLevel is
     * an instance of a {@link ConcreteAccessLevel}
     * @return Returns whether this access level is a concrete access level.
     */
    default boolean isConcrete() {
        return this instanceof ConcreteAccessLevel;
    }

    /**
     * Compare this access level to another.
     * This method is semantically identical to that of the
     * {@link Comparable} interface. This method exists for
     * interoperability with enum values.
     */
    int compare(AccessLevel accessLevel);

    /**
     * Serialize this access level to a JSON string.
     * @return Returns the JSON representation of this access level.
     */
    @Nonnull
    String saveToString();

    /**
     * Serialize this access level to an {@link JsonElement}
     * @return Returns a JsonElement representing this access level.
     */
    @Nonnull
    JsonElement saveToJsonElement();


}
