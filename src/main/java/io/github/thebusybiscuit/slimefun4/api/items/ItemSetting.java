package io.github.thebusybiscuit.slimefun4.api.items;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * This class represents a Setting for a {@link SlimefunItem} that can be modified via
 * the {@code Items.yml} {@link Config} file.
 * 
 * @author TheBusyBiscuit
 *
 * @param <T>
 *            The type of data stored under this {@link ItemSetting}
 */
public class ItemSetting<T> {

    private final String key;
    private final T defaultValue;

    private T value;

    /**
     * This creates a new {@link ItemSetting} with the given key and default value
     * 
     * @param key
     *            The key under which this setting will be stored (relative to the {@link SlimefunItem})
     * @param defaultValue
     *            The default value for this {@link ItemSetting}
     */
    @ParametersAreNonnullByDefault
    public ItemSetting(String key, T defaultValue) {
        Validate.notNull(key, "The key of an ItemSetting is not allowed to be null!");
        Validate.notNull(defaultValue, "The default value of an ItemSetting is not allowed to be null!");

        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     * This method checks if a given input would be valid as a value for this
     * {@link ItemSetting}. You can override this method to implement your own checks.
     * 
     * @param input
     *            The input value to validate
     * 
     * @return Whether the given input was valid
     */
    public boolean validateInput(T input) {
        return input != null;
    }

    /**
     * This method updates this {@link ItemSetting} with the given value.
     * Override this method to catch changes of a value.
     * A value may never be null.
     * 
     * @param newValue
     *            The new value for this {@link ItemSetting}
     */
    public void update(@Nonnull T newValue) {
        if (validateInput(newValue)) {
            this.value = newValue;
        } else {
            throw new IllegalArgumentException("The passed value was not valid. (Maybe null?)");
        }

        // Feel free to override this as necessary.
    }

    /**
     * This returns the key of this {@link ItemSetting}.
     * 
     * @return The key under which this setting is stored (relative to the {@link SlimefunItem})
     */
    @Nonnull
    public String getKey() {
        return key;
    }

    /**
     * This returns the <strong>current</strong> value of this {@link ItemSetting}.
     * 
     * @return The current value
     */
    @Nonnull
    public T getValue() {
        Validate.notNull(value, "An ItemSetting was invoked but was not initialized yet.");

        return value;
    }

    /**
     * This returns the <strong>default</strong> value of this {@link ItemSetting}.
     * 
     * @return The default value
     */
    @Nonnull
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * This method checks if this {@link ItemSetting} stores the given data type.
     * 
     * @param c
     *            The class of data type you want to compare
     * @return Whether this {@link ItemSetting} stores the given type
     */
    public boolean isType(@Nonnull Class<?> c) {
        return c.isInstance(defaultValue);
    }

    /**
     * This is an error message which should provide further context on what values
     * are allowed.
     * 
     * @return An error message which is displayed when this {@link ItemSetting} is misconfigured.
     */
    @Nonnull
    protected String getErrorMessage() {
        return "Only '" + defaultValue.getClass().getSimpleName() + "' values are allowed!";
    }

    /**
     * This method is called by a {@link SlimefunItem} which wants to load its {@link ItemSetting}
     * from the {@link Config} file.
     * 
     * @param item
     *            The {@link SlimefunItem} who called this method
     */
    @SuppressWarnings("unchecked")
    public void load(@Nonnull SlimefunItem item) {
        SlimefunPlugin.getItemCfg().setDefaultValue(item.getId() + '.' + getKey(), getDefaultValue());
        Object configuredValue = SlimefunPlugin.getItemCfg().getValue(item.getId() + '.' + getKey());

        if (defaultValue.getClass().isInstance(configuredValue)) {
            if (validateInput((T) configuredValue)) {
                this.value = (T) configuredValue;
            } else {
                Slimefun.getLogger().log(Level.WARNING, "Slimefun has found an invalid config setting in your Items.yml!");
                Slimefun.getLogger().log(Level.WARNING, "  at \"{0}.{1}\"", new Object[] { item.getId(), getKey() });
                Slimefun.getLogger().log(Level.WARNING, "{0} is not a valid input!", configuredValue);
                Slimefun.getLogger().log(Level.WARNING, getErrorMessage());
            }
        } else {
            this.value = defaultValue;
            String found = configuredValue == null ? "null" : configuredValue.getClass().getSimpleName();

            Slimefun.getLogger().log(Level.WARNING, "Slimefun has found an invalid config setting in your Items.yml!");
            Slimefun.getLogger().log(Level.WARNING, "Please only use settings that are valid.");
            Slimefun.getLogger().log(Level.WARNING, "  at \"{0}.{1}\"", new Object[] { item.getId(), getKey() });
            Slimefun.getLogger().log(Level.WARNING, "Expected \"{0}\" but found: \"{1}\"", new Object[] { defaultValue.getClass().getSimpleName(), found });
        }
    }

    @Override
    public String toString() {
        T currentValue = this.value != null ? this.value : defaultValue;
        return getClass().getSimpleName() + " {" + getKey() + " = " + currentValue + " (default: " + getDefaultValue() + ")";
    }

}
