package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

/**
 * This class is used to speed up parsing of a {@link JsonObject} that is stored at
 * a given {@link Location}.
 * 
 * This simply utilises a {@link HashMap} to cache the data and then provides the same getters
 * as a normal {@link Config}.
 * 
 * @author creator3
 * 
 * @see BlockStorage
 *
 */
public class BlockInfoConfig extends Config {

    private final Map<String, String> data;

    public BlockInfoConfig() {
        this(new HashMap<>());
    }

    public BlockInfoConfig(Map<String, String> data) {
        super(null, null);
        this.data = data;
    }

    @Nonnull
    public Map<String, String> getMap() {
        return data;
    }

    @Override
    public void setValue(String path, Object value) {
        if (value != null && !(value instanceof String)) {
            throw new UnsupportedOperationException("Can't set \"" + path + "\" to \"" + value + "\" (type: " + value.getClass().getSimpleName() + ") because BlockInfoConfig only supports Strings");
        }

        if (value == null) {
            data.remove(path);
        } else {
            data.put(path, (String) value);
        }
    }

    @Override
    public boolean contains(String path) {
        return data.containsKey(path);
    }

    @Override
    public Object getValue(String path) {
        return getString(path);
    }

    @Override
    public String getString(String path) {
        return data.get(path);
    }

    @Override
    public Set<String> getKeys() {
        return data.keySet();
    }

    @Override
    public Set<String> getKeys(String path) {
        throw new UnsupportedOperationException("Cannot get keys for BlockInfoConfig");
    }

    @Override
    public File getFile() {
        throw new UnsupportedOperationException("BlockInfoConfigs do not have a File");
    }

    @Override
    public FileConfiguration getConfiguration() {
        throw new UnsupportedOperationException("BlockInfoConfigs do not have a FileConfiguration");
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("BlockInfoConfigs cannot be saved to a File");
    }

    @Override
    public void save(File file) {
        throw new UnsupportedOperationException("BlockInfoConfigs cannot be saved to a File");
    }

    @Override
    public void createFile() {
        throw new UnsupportedOperationException("BlockInfoConfigs cannot be created from a File");
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("BlockInfoConfigs cannot be reloaded");
    }

    @Nonnull
    public String toJSON() {
        return new GsonBuilder().create().toJson(data);
    }

}