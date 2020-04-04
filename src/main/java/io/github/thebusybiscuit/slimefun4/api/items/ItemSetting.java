package io.github.thebusybiscuit.slimefun4.api.items;

import org.apache.commons.lang.Validate;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

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
    public ItemSetting(String key, T defaultValue) {
        Validate.notNull(key, "The key of an ItemSetting is not allowed to be null!");
        Validate.notNull(defaultValue, "The default value of an ItemSetting is not allowed to be null!");

        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     * This method updates this {@link ItemSetting} with the given value.
     * Override this method to catch changes of a value.
     * A value may never be null.
     * 
     * @param newValue
     *            The new value for this {@link ItemSetting}
     */
    public void update(T newValue) {
        Validate.notNull(newValue, "An ItemSetting cannot have a null value!");
        this.value = newValue;

        // Feel free to override this as necessary.
    }

    /**
     * This returns the key of this {@link ItemSetting}.
     * 
     * @return The key under which this setting is stored (relative to the {@link SlimefunItem})
     */
    public String getKey() {
        return key;
    }

    /**
     * This returns the <strong>current</strong> value of this {@link ItemSetting}.
     * 
     * @return The current value
     */
    public T getValue() {
        Validate.notNull(value, "An ItemSetting was invoked but was not initialized yet.");

        return value;
    }

    /**
     * This returns the <strong>default</strong> value of this {@link ItemSetting}.
     * 
     * @return The default value
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * This method is called by a {@link SlimefunItem} which wants to load its {@link ItemSetting}
     * from the {@link Config} file.
     * 
     * @param item
     *            The {@link SlimefunItem} who called this method
     */
    @SuppressWarnings("unchecked")
    public void load(SlimefunItem item) {
        SlimefunPlugin.getItemCfg().setDefaultValue(item.getID() + '.' + getKey(), getDefaultValue());
        Object configuredValue = SlimefunPlugin.getItemCfg().getValue(item.getID() + '.' + getKey());

        if (defaultValue.getClass().isInstance(configuredValue)) {
            this.value = (T) configuredValue;
        }
    }

}
