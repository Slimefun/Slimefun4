package io.github.thebusybiscuit.slimefun4.storage;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.regex.Pattern;

public class NamedKey {

    private static final Pattern NAMING_FORMAT = Pattern.compile("^[a-z0-9_-]+$");

    private final String plugin;
    private final String name;

    /**
     * Create a defaulted NamedKey (no plugin association).
     * This is used when you're a fully controlled object (not shared!).
     *
     * @param name The name of this key
     */
    private NamedKey(@Nonnull String name) {
        Validate.isTrue(name != null && NAMING_FORMAT.matcher(name).matches(),
            "Name contains invalid characters! Valid: a-zA-Z0-9_-");

        this.plugin = null;
        this.name = name;
    }

    /**
     * Create a plugin associated NamedKey.
     * This is used when you're a shared object (if you're not sure, use this one).
     *
     * @param plugin The plugin who owns this key
     * @param name   The name of this key
     */
    public NamedKey(@Nonnull JavaPlugin plugin, @Nonnull String name) {
        Validate.notNull(plugin, "Plugin cannot be null!");
        Validate.notNull(plugin, "Name cannot be null!");

        this.plugin = plugin.getName().toLowerCase(Locale.ROOT);
        this.name = name.toLowerCase(Locale.ROOT);

        Validate.isTrue(NAMING_FORMAT.matcher(this.plugin).matches(),
            "Plugin name contains invalid characters! Valid: a-zA-Z0-9_-");
        Validate.isTrue(NAMING_FORMAT.matcher(this.name).matches(),
            "Name contains invalid characters! Valid: a-zA-Z0-9_-");
    }

    /**
     * Get the name of this NamedKey.
     *
     * @return The name of this NamedKey
     */
    @Nonnull
    public String getName() {
        return this.name;
    }

    /**
     * Convert this object into it's named key. Used for writing and comparing.
     * <p>
     * If a plugin is associated it will be formatted like so: {@code plugin:name}.<br/>
     * If it's a defaulted key it will be formatted like so: {@code name}.
     *
     * @return The NamedKey as a {@link String}. Used for writing and comparing.
     */
    @Override
    public String toString() {
        if (this.plugin != null) {
            return this.plugin + ':' + this.name;
        } else {
            return this.name;
        }
    }

    @Override
    public int hashCode() {
        int total = 1;
        total = total * 31 + (this.plugin != null ? this.plugin.hashCode() : 0);
        total = total * 31 + this.name.hashCode();
        return total;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof NamedKey) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    /**
     * Get a defaulted NamedKey. This is a named key without a {@link JavaPlugin} association.
     * This is used when you're a fully controlled object (not shared!).
     *
     * @param name The name of this key
     * @return The newly created NamedKey
     */
    @Nonnull
    public static NamedKey ofDefault(@Nonnull String name) {
        return new NamedKey(name);
    }
}
