package io.github.thebusybiscuit.slimefun4.api.accessibility;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents the base for anything which will handle {@link AccessData}
 *
 * @author md5sha256
 */
public interface AccessManager {

    /**
     * Get the {@link AccessLevel} for a player, defaulting to {@link ConcreteAccessLevel#NONE} if {@link #hasDataFor(Object)} is false.
     *
     * @param player The instance of the player.
     * @return Returns the access level this player has.
     */
    @Nonnull
    AccessLevel getAccessLevel(@Nonnull Player player);

    /**
     * Get the {@link AccessLevel} for a player, defaulting to {@link ConcreteAccessLevel#NONE} if {@link #hasDataFor(Object)} is false.
     *
     * @param player The unique id of the player
     * @return Returns the access level this player has.
     */
    @Nonnull
    AccessLevel getAccessLevel(@Nonnull UUID player);

    /**
     * Get the {@link AccessData} object backing a certain class. Mutations to the returned
     * object will be reflected by this manager.
     *
     * @param clazz The instance of the class
     * @param <T>   Generic type of the class
     * @return Returns a {@link Optional} of the AccessData object backing the provided class.
     * @see #getOrRegisterAccessData(Class, AccessData)
     */
    @Nonnull
    <T> Optional<AccessData<T>> getAccessData(@Nonnull Class<T> clazz);

    /**
     * Get the {@link AccessData} object backing a certain class, or register a default if
     * {@link #getAccessData(Class)} is not present. Mutations to the returned
     * object will be reflected by this manager.
     *
     * @param clazz The instance of the class
     * @param <T>   Generic type of the class
     * @return Returns the AccessData object backing the provided class
     */
    @Nonnull
    <T> AccessData<T> getOrRegisterAccessData(@Nonnull Class<T> clazz,
                                              @Nonnull final AccessData<T> def);

    /**
     * Check whether a {@link Player} has a given {@link AccessLevel} or higher.
     *
     * @param player      The instance of the player
     * @param accessLevel The instance of the AccessLevel
     * @return Returns true if the player has the provided access level or higher.
     */
    default boolean hasLevel(@Nonnull Player player, @Nonnull AccessLevel accessLevel) {
        return getAccessLevel(player).compare(accessLevel) >= 0;
    }

    /**
     * Check whether a player has a given {@link AccessLevel} or higher.
     *
     * @param player      The unique id of the player
     * @param accessLevel The instance of the AccessLevel
     * @return Returns true if the player has the provided access level or higher.
     */
    default boolean hasLevel(@Nonnull UUID player, @Nonnull AccessLevel accessLevel) {
        return getAccessLevel(player).compare(accessLevel) >= 0;
    }

    /**
     * Check whether the manager has data for a given object. This method is useful in
     * understanding whether a player is on the access list when {@link #getAccessLevel(Player)}
     * provides the default result.
     *
     * @param object The instance of the object.
     * @return Returns whether the manager has data for a given object.
     */
    boolean hasDataFor(@Nonnull Object object);

    /**
     * @return Returns whether this access manager has no data.
     */
    boolean isEmpty();

    /**
     * Reset this manager to an empty state.
     */
    void reset();

    /**
     * Purge all currently held data and read from the {@link ItemStack}
     *
     * @param itemStack The instance of the {@link ItemStack}
     */
    void load(@Nonnull NamespacedKey namespace, @Nonnull ItemStack itemStack);

    /**
     * Purge all currently held data and read from the {@link Block}
     *
     * @param block The instance of the {@link Block}
     */
    void load(@Nonnull NamespacedKey namespace, @Nonnull Block block);

    /**
     * Save / serialize all currently held data onto a given {@link SlimefunItemStack}
     *
     * @param itemStack The instance of the {@link ItemStack}
     */
    void saveTo(@Nonnull NamespacedKey namespace, @Nonnull ItemStack itemStack);

    /**
     * Save / serialize all currently held data onto a given {@link Block}
     *
     * @param block The instance of the {@link Block}
     */
    void saveTo(@Nonnull NamespacedKey namespace, @Nonnull Block block);

    /**
     * Purge all currently held data and read from the String data.
     *
     * @param data The serialized data in string form.
     * @throws IllegalArgumentException Thrown if the data is invalid.
     */
    default void loadFromString(@Nonnull String data) throws IllegalArgumentException {
        loadFromJsonElement(new JsonParser().parse(data));
    }

    /**
     * Purge all currently held data and read from the String data.
     *
     * @param data The serialized data in string form.
     * @throws IllegalArgumentException Thrown if the data is invalid.
     */
    void loadFromJsonElement(@Nonnull JsonElement data) throws IllegalArgumentException;

    /**
     * Save all currently held data into a string.
     *
     * @return Returns a string representing the state of this manager.
     */
    @Nonnull
    default String saveToString() {
        return saveToJsonElement().toString();
    }

    @Nonnull
    JsonElement saveToJsonElement();

}
