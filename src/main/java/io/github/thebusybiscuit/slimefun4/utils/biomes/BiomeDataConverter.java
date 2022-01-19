package io.github.thebusybiscuit.slimefun4.utils.biomes;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;

/**
 * A simple functional interface for converting a {@link JsonElement} into the desired data type
 * needed for your {@link BiomeMap}.
 * 
 * @author TheBusyBiscuit
 *
 * @param <T>
 *            Your target data type
 * 
 * @see BiomeMap
 */
@FunctionalInterface
public interface BiomeDataConverter<T> {

    /**
     * Override this method and provide a way to convert a {@link JsonElement} into your
     * desired data type.
     * <p>
     * For primitive data values, you can also just use the following method references:
     * 
     * <ul>
     * <li>JsonElement::getAsString</li>
     * <li>JsonElement::getAsInt</li>
     * <li>JsonElement::getAsDouble</li>
     * <li>JsonElement::getAsFloat</li>
     * <li>JsonElement::getAsLong</li>
     * <li>JsonElement::getAsBoolean</li>
     * </ul>
     * 
     * or similar.
     * 
     * @param jsonElement
     *            The {@link JsonElement} to convert
     * 
     * @return Your desired data type.
     */
    @Nonnull
    T convert(@Nonnull JsonElement jsonElement);

}
