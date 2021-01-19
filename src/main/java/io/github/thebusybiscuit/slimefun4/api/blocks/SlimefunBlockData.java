package io.github.thebusybiscuit.slimefun4.api.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This class is used to speed up parsing of a {@link JsonObject} that is stored at
 * a given {@link Location}.
 * 
 * This simply utilises a {@link HashMap} to cache the data and then provides the same getters
 * as a normal {@link Config}.
 * 
 * @author TheBusyBiscuit
 * @author creator3
 * 
 * @see BlockDatabase
 *
 */
public class SlimefunBlockData {

    /**
     * The {@link ReadWriteLock} protects us from concurrent modifications.
     * This way, we can be flexible with our data access and be {@link Thread}-safe.
     */
    private final ReadWriteLock lock;

    /**
     * The internal data structure: A simple {@link HashMap}
     * to store the keys and values
     */
    private final Map<String, String> data;

    /**
     * This creates a new {@link SlimefunBlockData} object.
     * It is initialized with an empty {@link HashMap}.
     */
    public SlimefunBlockData() {
        this(new HashMap<>());
    }

    /**
     * This creates a new {@link SlimefunBlockData} object
     * and also initializes it using the given {@link Map}
     * 
     * @param data
     *            The data {@link Map}
     */
    public SlimefunBlockData(@Nonnull Map<String, String> data) {
        Validate.notNull(data, "Map cannot be null");

        this.lock = new ReentrantReadWriteLock();
        this.data = data;
    }

    @Nonnull
    public String getId() {
        return getValue("id");
    }

    @Nonnull
    public SlimefunItem getSlimefunItem() {
        return SlimefunItem.getByID(getId());
    }

    public void setValue(@Nonnull String key, @Nullable Object value) {
        Validate.notNull(key, "Cannot set block data with a key that is null");

        if (Objects.equals(key, "id") && value == null) {
            throw new IllegalArgumentException("SlimefunItem Ids are not nullable.");
        }

        if (value == null) {
            lock.writeLock().lock();

            try {
                data.remove(key);
            } finally {
                lock.readLock().unlock();
            }
        } else if (value instanceof String) {
            lock.writeLock().lock();

            try {
                data.put(key, (String) value);
            } finally {
                lock.readLock().unlock();
            }
        } else {
            throw new UnsupportedOperationException("Can't set \"" + key + "\" to \"" + value + "\" (type: " + value.getClass().getSimpleName() + ") because SlimefunBlockData only supports Strings");
        }
    }

    @Nullable
    public String getValue(@Nonnull String key) {
        Validate.notNull(key, "Keys cannot be null.");

        lock.readLock().lock();

        try {
            return data.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean hasValue(@Nonnull String key) {
        Validate.notNull(key, "Keys cannot be null.");

        lock.readLock().lock();

        try {
            return data.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Nonnull
    public String toJSON() {
        lock.readLock().lock();

        try {
            return new GsonBuilder().create().toJson(data);
        } finally {
            lock.readLock().unlock();
        }
    }

}