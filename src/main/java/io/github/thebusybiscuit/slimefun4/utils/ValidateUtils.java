package io.github.thebusybiscuit.slimefun4.utils;

import com.google.common.base.Preconditions;

import java.util.Collection;

/**
 * This utility class holds method to check the arguments.
 *
 * @author ybw0014
 */
public final class ValidateUtils {

    private ValidateUtils() {}

    /**
     * Validate that the specified argument {@link Collection} is neither null nor a size of zero (no elements);
     * otherwise throwing an exception.
     *
     * @param collection The {@link Collection} to be validated
     * @param message The error message
     */
    public static void notEmpty(Collection<?> collection, String message) {
        Preconditions.checkArgument(collection != null && collection.size() != 0, message);
    }


    /**
     * Validate that the specified argument array is neither null nor a size of zero (no elements);
     * otherwise throwing an exception.
     *
     * @param array The array to be validated
     * @param message The error message
     */
    public static void notEmpty(Object[] array, String message) {
        Preconditions.checkArgument(array != null && array.length > 0, message);
    }

    /**
     * Validate that the specified argument {@link Collection} is neither null nor contains any elements that are null;
     * otherwise throwing an exception with the specified message.
     *
     * @param collection The {@link Collection} to be validated
     * @param message The error message
     */
    public static void noNullElements(Collection<?> collection, String message) {
        Preconditions.checkArgument(collection != null);
        for (Object object : collection) {
            Preconditions.checkArgument(object != null, message);
        }
    }

    /**
     * Validate that the specified argument array is neither null nor contains any elements that are null;
     * otherwise throwing an exception with the specified message.
     *
     * @param array The array to be validated
     * @param message The error message
     */
    public static void noNullElements(Object[] array, String message) {
        Preconditions.checkArgument(array != null);
        for (Object object : array) {
            Preconditions.checkArgument(object != null, message);
        }
    }
}
