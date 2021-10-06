package io.github.thebusybiscuit.slimefun4.storage;

import io.github.thebusybiscuit.slimefun4.storage.type.BooleanType;
import io.github.thebusybiscuit.slimefun4.storage.type.ByteArrayType;
import io.github.thebusybiscuit.slimefun4.storage.type.ByteType;
import io.github.thebusybiscuit.slimefun4.storage.type.DataObjectType;
import io.github.thebusybiscuit.slimefun4.storage.type.DoubleArrayType;
import io.github.thebusybiscuit.slimefun4.storage.type.DoubleType;
import io.github.thebusybiscuit.slimefun4.storage.type.FloatArrayType;
import io.github.thebusybiscuit.slimefun4.storage.type.FloatType;
import io.github.thebusybiscuit.slimefun4.storage.type.IntArrayType;
import io.github.thebusybiscuit.slimefun4.storage.type.IntType;
import io.github.thebusybiscuit.slimefun4.storage.type.LongArrayType;
import io.github.thebusybiscuit.slimefun4.storage.type.LongType;
import io.github.thebusybiscuit.slimefun4.storage.type.ShortArrayType;
import io.github.thebusybiscuit.slimefun4.storage.type.ShortType;
import io.github.thebusybiscuit.slimefun4.storage.type.StringType;
import io.github.thebusybiscuit.slimefun4.storage.type.Type;
import org.bukkit.NamespacedKey;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

// Types
// * Byte
// * Short
// * Int
// * Double
// * Float
// * Long
// * boolean
// * Byte Array
// * Short Array
// * Int Array
// * Double Array
// * Float Array
// * Long Array
// * String
// * DataObject

// * UUID??

public class DataObject {

    private final Map<NamespacedKey, Type> data = new LinkedHashMap<>();

    // Fuck tons of API things

    //region Getters
    ////////////////////////////////////////
    // Getters
    ////////////////////////////////////////
    /**
     * Get a byte value in this {@link DataObject}, if the key doesn't exist it returns -1.
     *
     * @param key The key of the data to retrieve
     * @return The byte associated with this key or -1 if it doesn't exist
     */
    public byte getByte(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof ByteType) {
            return ((ByteType) type).getValue();
        } else {
            return -1;
        }
    }

    /**
     * Get a byte value in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The byte associated with this key or the specified default value if it doesn't exist
     */
    public byte getByte(NamespacedKey key, byte defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof ByteType) {
            return ((ByteType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a short value in this {@link DataObject}, if the key doesn't exist it returns -1.
     *
     * @param key The key of the data to retrieve
     * @return The short associated with this key or -1 if it doesn't exist
     */
    public short getShort(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof ShortType) {
            return ((ShortType) type).getValue();
        } else {
            return -1;
        }
    }

    /**
     * Get a short value in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The short associated with this key or the specified default value if it doesn't exist
     */
    public short getShort(NamespacedKey key, short defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof ShortType) {
            return ((ShortType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a int value in this {@link DataObject}, if the key doesn't exist it returns -1.
     *
     * @param key The key of the data to retrieve
     * @return The int associated with this key or -1 if it doesn't exist
     */
    public int getInt(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof IntType) {
            return ((IntType) type).getValue();
        } else {
            return -1;
        }
    }

    /**
     * Get a int value in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The int associated with this key or the specified default value if it doesn't exist
     */
    public int getInt(NamespacedKey key, int defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof IntType) {
            return ((IntType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a double value in this {@link DataObject}, if the key doesn't exist it returns -1.
     *
     * @param key The key of the data to retrieve
     * @return The double associated with this key or -1 if it doesn't exist
     */
    public double getDouble(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof DoubleType) {
            return ((DoubleType) type).getValue();
        } else {
            return -1;
        }
    }

    /**
     * Get a double value in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The double associated with this key or the specified default value if it doesn't exist
     */
    public double getDouble(NamespacedKey key, double defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof DoubleType) {
            return ((DoubleType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a float value in this {@link DataObject}, if the key doesn't exist it returns -1.
     *
     * @param key The key of the data to retrieve
     * @return The float associated with this key or -1 if it doesn't exist
     */
    public float getFloat(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof FloatType) {
            return ((FloatType) type).getValue();
        } else {
            return -1;
        }
    }

    /**
     * Get a float value in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The float associated with this key or the specified default value if it doesn't exist
     */
    public float getFloat(NamespacedKey key, float defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof FloatType) {
            return ((FloatType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a long value in this {@link DataObject}, if the key doesn't exist it returns -1.
     *
     * @param key The key of the data to retrieve
     * @return The long associated with this key or -1 if it doesn't exist
     */
    public long getLong(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof LongType) {
            return ((LongType) type).getValue();
        } else {
            return -1;
        }
    }

    /**
     * Get a long value in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The long associated with this key or the specified default value if it doesn't exist
     */
    public long getLong(NamespacedKey key, long defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof LongType) {
            return ((LongType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a boolean value in this {@link DataObject}, if the key doesn't exist it returns false.
     *
     * @param key The key of the data to retrieve
     * @return The boolean associated with this key or false if it doesn't exist
     */
    public boolean getBoolean(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof BooleanType) {
            return ((BooleanType) type).getValue();
        } else {
            return false;
        }
    }

    /**
     * Get a boolean value in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The boolean associated with this key or the specified default value if it doesn't exist
     */
    public boolean getBoolean(NamespacedKey key, boolean defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof BooleanType) {
            return ((BooleanType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a byte array in this {@link DataObject}, if the key doesn't exist it returns null.
     *
     * @param key The key of the data to retrieve
     * @return The byte array associated with this key or null if it doesn't exist
     */
    @Nullable
    public byte[] getByteArray(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof ByteArrayType) {
            return ((ByteArrayType) type).getValue();
        } else {
            return null;
        }
    }

    /**
     * Get a byte array in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The byte array associated with this key or the specified default value if it doesn't exist
     */
    @Nullable
    public byte[] getByteArray(NamespacedKey key, byte[] defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof ByteArrayType) {
            return ((ByteArrayType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a short array in this {@link DataObject}, if the key doesn't exist it returns null.
     *
     * @param key The key of the data to retrieve
     * @return The short array associated with this key or null if it doesn't exist
     */
    @Nullable
    public short[] getShortArray(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof ShortArrayType) {
            return ((ShortArrayType) type).getValue();
        } else {
            return null;
        }
    }

    /**
     * Get a short array in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The short array associated with this key or the specified default value if it doesn't exist
     */
    @Nullable
    public short[] getShortArray(NamespacedKey key, @Nullable short[] defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof ShortArrayType) {
            return ((ShortArrayType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a int array in this {@link DataObject}, if the key doesn't exist it returns null.
     *
     * @param key The key of the data to retrieve
     * @return The int array associated with this key or null if it doesn't exist
     */
    @Nullable
    public int[] getIntArray(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof IntArrayType) {
            return ((IntArrayType) type).getValue();
        } else {
            return null;
        }
    }

    /**
     * Get a int array in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The int array associated with this key or the specified default value if it doesn't exist
     */
    @Nullable
    public int[] getIntArray(NamespacedKey key, @Nullable int[] defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof IntArrayType) {
            return ((IntArrayType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a double array in this {@link DataObject}, if the key doesn't exist it returns null.
     *
     * @param key The key of the data to retrieve
     * @return The double array associated with this key or null if it doesn't exist
     */
    @Nullable
    public double[] getDoubleArray(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof DoubleArrayType) {
            return ((DoubleArrayType) type).getValue();
        } else {
            return null;
        }
    }

    /**
     * Get a double array in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The double array associated with this key or the specified default value if it doesn't exist
     */
    @Nullable
    public double[] getDoubleArray(NamespacedKey key, @Nullable double[] defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof DoubleArrayType) {
            return ((DoubleArrayType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a float array in this {@link DataObject}, if the key doesn't exist it returns null.
     *
     * @param key The key of the data to retrieve
     * @return The float array associated with this key or null if it doesn't exist
     */
    @Nullable
    public float[] getFloatArray(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof FloatArrayType) {
            return ((FloatArrayType) type).getValue();
        } else {
            return null;
        }
    }

    /**
     * Get a float array in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The float array associated with this key or the specified default value if it doesn't exist
     */
    @Nullable
    public float[] getFloatArray(NamespacedKey key, @Nullable float[] defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof FloatArrayType) {
            return ((FloatArrayType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a long array in this {@link DataObject}, if the key doesn't exist it returns null.
     *
     * @param key The key of the data to retrieve
     * @return The long array associated with this key or null if it doesn't exist
     */
    @Nullable
    public long[] getLongArray(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof LongArrayType) {
            return ((LongArrayType) type).getValue();
        } else {
            return null;
        }
    }

    /**
     * Get a long array in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The long array associated with this key or the specified default value if it doesn't exist
     */
    @Nullable
    public long[] getLongArray(NamespacedKey key, @Nullable long[] defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof LongArrayType) {
            return ((LongArrayType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a String in this {@link DataObject}, if the key doesn't exist it returns null.
     *
     * @param key The key of the data to retrieve
     * @return The String associated with this key or null if it doesn't exist
     */
    @Nullable
    public String getString(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof StringType) {
            return ((StringType) type).getValue();
        } else {
            return null;
        }
    }

    /**
     * Get a String in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The String associated with this key or the specified default value if it doesn't exist
     */
    @Nullable
    public String getString(NamespacedKey key, String defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof StringType) {
            return ((StringType) type).getValue();
        } else {
            return defaultValue;
        }
    }

    /**
     * Get a {@link DataObject} in this {@link DataObject}, if the key doesn't exist it returns null.
     *
     * @param key The key of the data to retrieve
     * @return The {@link DataObject} associated with this key or null if it doesn't exist
     */
    @Nullable
    public DataObject getDataObject(NamespacedKey key) {
        final Type type = this.data.get(key);
        if (type instanceof DataObjectType) {
            return ((DataObjectType) type).getValue();
        } else {
            return null;
        }
    }

    /**
     * Get a {@link DataObject} in this {@link DataObject}, if the key doesn't exist it returns the specified default value.
     *
     * @param key The key of the data to retrieve
     * @param defaultValue The specified default value
     * @return The {@link DataObject} associated with this key or the specified default value if it doesn't exist
     */
    @Nullable
    public DataObject getDataObject(NamespacedKey key, @Nullable DataObject defaultValue) {
        final Type type = this.data.get(key);
        if (type instanceof DataObjectType) {
            return ((DataObjectType) type).getValue();
        } else {
            return defaultValue;
        }
    }
    //endregion

    //region Setters
    ///////////////////////////////
    // Setters
    ///////////////////////////////
    @Nonnull
    @CheckReturnValue
    public DataObject setByte(NamespacedKey key, byte value) {
        ByteType type = new ByteType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setShort(NamespacedKey key, short value) {
        ShortType type = new ShortType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setInt(NamespacedKey key, int value) {
        IntType type = new IntType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setDouble(NamespacedKey key, double value) {
        DoubleType type = new DoubleType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setFloat(NamespacedKey key, float value) {
        FloatType type = new FloatType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setLong(NamespacedKey key, long value) {
        LongType type = new LongType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setBoolean(NamespacedKey key, boolean value) {
        BooleanType type = new BooleanType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setByteArray(NamespacedKey key, byte[] value) {
        ByteArrayType type = new ByteArrayType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setShortArray(NamespacedKey key, short[] value) {
        ShortArrayType type = new ShortArrayType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setIntArray(NamespacedKey key, int[] value) {
        IntArrayType type = new IntArrayType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setDoubleArray(NamespacedKey key, double[] value) {
        DoubleArrayType type = new DoubleArrayType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setFloatArray(NamespacedKey key, float[] value) {
        FloatArrayType type = new FloatArrayType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setLongArray(NamespacedKey key, long[] value) {
        LongArrayType type = new LongArrayType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setString(NamespacedKey key, String value) {
        StringType type = new StringType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public DataObject setDataObject(NamespacedKey key, DataObject value) {
        DataObjectType type = new DataObjectType(value);
        this.data.put(key, type);
        return this;
    }

    @Nonnull
    @CheckReturnValue
    public <T> DataObject set(NamespacedKey key, T value, Transformer<T> transformer) {
        transformer.transformInto(this, key, value);
        return this;
    }
    //endregion

    @Nonnull
    public Set<Map.Entry<NamespacedKey, Type>> getEntries() {
        return this.data.entrySet();
    }

    @Nonnull
    @Override
    public String toString() {
        return "DataObject{"
            + "data=" + data.toString()
            + "}";
    }
}
