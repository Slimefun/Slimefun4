package io.github.thebusybiscuit.slimefun4.api;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public abstract class JsonDataHolder {

	protected JsonObject data;
	private boolean dirty;
	
	public JsonDataHolder(JsonObject data) {
		this.data = data;
		this.dirty = false;
	}
	
	public void markDirty() {
		dirty = true;
	}
	
	public void markClean() {
		dirty = false;
	}
	
	public boolean isDirty() {
		return dirty;
	}

	// Setters
	public void setString(String key, String value) {
		data.addProperty(key, value);
		markDirty();
	}
	
	public void setInt(String key, int value) {
		data.addProperty(key, value);
		markDirty();
	}
	
	public void setBoolean(String key, boolean value) {
		data.addProperty(key, value);
		markDirty();
	}
	
	public void setFloat(String key, float value) {
		data.addProperty(key, value);
		markDirty();
	}
	
	public void setStringArray(String key, String[] array) {
		JsonArray json = new JsonArray();
		for (String value: array) json.add(value);
		data.add(key, json);
		markDirty();
	}
	
	public void setIntArray(String key, int[] array) {
		JsonArray json = new JsonArray();
		for (int value: array) json.add(value);
		data.add(key, json);
		markDirty();
	}
	
	public void setBooleanArray(String key, boolean[] array) {
		JsonArray json = new JsonArray();
		for (boolean value: array) json.add(value);
		data.add(key, json);
		markDirty();
	}
	
	public void setFloatArray(String key, float[] array) {
		JsonArray json = new JsonArray();
		for (float value: array) json.add(value);
		data.add(key, json);
		markDirty();
	}

	// Getters
	public Optional<String> getString(String key) {
		return getPrimitive(key, JsonElement::getAsString);
	}
	public String getString(String key, String defaultValue) {
		return getString(key).orElse(defaultValue);
	}
	
	public Optional<Integer> getInt(String key) {
		return getPrimitive(key, JsonElement::getAsInt);
	}
	public int getInt(String key, int defaultValue) {
		return getInt(key).orElse(defaultValue);
	}
	
	public Optional<Boolean> getBoolean(String key) {
		return getPrimitive(key, JsonElement::getAsBoolean);
	}
	public boolean getBoolean(String key, boolean defaultValue) {
		return getBoolean(key).orElse(defaultValue);
	}
	
	public Optional<Float> getFloat(String key) {
		return getPrimitive(key, JsonElement::getAsFloat);
	}
	public float getFloat(String key, float defaultValue) {
		return getFloat(key).orElse(defaultValue);
	}
	
	public Optional<String[]> getStringArray(String key) {
		return getArray(key, String[]::new, JsonElement::getAsString);
	}
	
	public Optional<Integer[]> getIntArray(String key) {
		return getArray(key, Integer[]::new, JsonElement::getAsInt);
	}
	
	public Optional<Boolean[]> getBooleanArray(String key) {
		return getArray(key, Boolean[]::new, JsonElement::getAsBoolean);
	}
	
	public Optional<Float[]> getFloatArray(String key) {
		return getArray(key, Float[]::new, JsonElement::getAsFloat);
	}
	
	protected <T> Optional<T[]> getArray(String key, IntFunction<T[]> constructor, Function<JsonElement, T> getter) {
		JsonElement element = data.get(key);
		
		if (element instanceof JsonArray) {
			JsonArray json = (JsonArray) element;
			T[] array = constructor.apply(json.size());
			
			for (int i = 0; i < array.length; i++) {
				array[i] = getter.apply(json.get(i));
			}
			
			return Optional.of(array);
		}
		else {
			return Optional.empty();
		}
	}
	
	protected <T> Optional<T> getPrimitive(String key, Function<JsonElement, T> getter) {
		JsonElement element = data.get(key);
		
		if (element instanceof JsonPrimitive) {
			return Optional.of(getter.apply(element));
		}
		else {
			return Optional.empty();
		}
	}

}