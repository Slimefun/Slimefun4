package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import com.google.gson.GsonBuilder;

import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;

public class BlockInfoConfig extends Config {
	
	private Map<String, String> data;
	
	public BlockInfoConfig() {
		this(new HashMap<>());
	}
	
	public BlockInfoConfig(Map<String, String> data) {
		super(null, null);
		this.data = data;
	}
	
	public Map<String, String> getMap(){
		return data;
	}
	
	@Override
	protected void store(String path, Object value) {
		if (value != null && !(value instanceof String)) {
			throw new UnsupportedOperationException("Can't set \"" + path + "\" to \"" + value + "\" (type: " + value.getClass().getSimpleName() + ") because BlockInfoConfig only supports Strings");
		}
		
		checkPath(path);
		
		if (value == null) {
			data.remove(path);
		} 
		else {
			data.put(path, (String) value);
		}
	}

	
	private void checkPath(String path) {
		if (path.indexOf('.') != -1) {
			throw new UnsupportedOperationException("BlockInfoConfig only supports Map<String,String> (path: " + path + ")");
		}
	}

	@Override
	public boolean contains(String path) {
		checkPath(path);
		return data.containsKey(path);
	}

	@Override
	public Object getValue(String path) {
		return getString(path);
	}

	@Override
	public String getString(String path) {
		checkPath(path);
		return data.get(path);
	}
	
	@Override
	public Set<String> getKeys() {
		return data.keySet();
	}
	
	private UnsupportedOperationException invalidType(String path) {
		return new UnsupportedOperationException("Can't get \"" + path + "\" because BlockInfoConfig only supports String values");
	}
	
	@Override
	public int getInt(String path) {
		throw invalidType(path);
	}

	@Override
	public boolean getBoolean(String path) {
		throw invalidType(path);
	}

	@Override
	public List<String> getStringList(String path) {
		throw invalidType(path);
	}

	@Override
	public List<Integer> getIntList(String path) {
		throw invalidType(path);
	}

	@Override
	public Double getDouble(String path) {
		throw invalidType(path);
	}

	@Override
	public Set<String> getKeys(String path) {
		throw invalidType(path);
	}

	@Override
	public File getFile() {
		throw new UnsupportedOperationException();
	}

	@Override
	public FileConfiguration getConfiguration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save(File file) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void createFile() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reload() {
		throw new UnsupportedOperationException();
	}

	public String toJSON() {
		return new GsonBuilder().create().toJson(data);
	}

}
