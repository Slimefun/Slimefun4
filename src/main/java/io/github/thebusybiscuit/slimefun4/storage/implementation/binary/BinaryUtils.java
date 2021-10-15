package io.github.thebusybiscuit.slimefun4.storage.implementation.binary;

import io.github.thebusybiscuit.slimefun4.storage.DataObject;
import io.github.thebusybiscuit.slimefun4.storage.NamedKey;
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
import io.github.thebusybiscuit.slimefun4.storage.type.StringArrayType;
import io.github.thebusybiscuit.slimefun4.storage.type.StringType;
import io.github.thebusybiscuit.slimefun4.storage.type.Type;
import io.github.thebusybiscuit.slimefun4.storage.type.TypeEnum;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class BinaryUtils {

    private BinaryUtils() {}

    /**
     * Return the String representation of the given {@link DataObject}.
     *
     * @param object The given {@link DataObject}
     * @return The String representation of this {@link DataObject}
     */
    @Nonnull
    public static String toString(@Nonnull DataObject object) {
        return toString(object, false);
    }

    /**
     * Return the String representation of the given {@link DataObject}.
     *
     * @param object The given {@link DataObject}
     * @param verbose Whether the full printing should be forced (false = cutoff allowed)
     * @return The String representation of this {@link DataObject}
     */
    @Nonnull
    public static String toString(@Nonnull DataObject object, boolean verbose) {
        final StringBuilder sb = new StringBuilder(TypeEnum.OBJECT.name())
            .append('(').append("None").append("):\n");

        writeObject(sb, object, 1, verbose);
        return sb.toString();
    }

    private static void writeObject(@Nonnull StringBuilder sb, @Nonnull DataObject object, int depth, boolean verbose) {
        for (Map.Entry<NamedKey, Type> entry : object.getEntries()) {
            final String spacing = repeat(' ', depth * 2);

            final Type type = entry.getValue();
            final TypeEnum typeEnum = type.getTypeEnum();

            sb.append(spacing).append(typeEnum.name()).append("('")
                .append(entry.getKey().toString()).append("'): ");

            if (typeEnum != TypeEnum.OBJECT) {
                writeValue(sb, type, verbose);
                sb.append('\n');
            } else {
                sb.append('\n');
                writeObject(sb, ((DataObjectType) type).getValue(), depth + 1, verbose);
            }
        }
    }

    private static void writeValue(@Nonnull StringBuilder sb, @Nonnull Type type, boolean verbose) {
        switch (type.getTypeEnum()) {
            case BYTE:
                sb.append(((ByteType) type).getValue());
                break;
            case SHORT:
                sb.append(((ShortType) type).getValue());
                break;
            case INT:
                sb.append(((IntType) type).getValue());
                break;
            case DOUBLE:
                sb.append(((DoubleType) type).getValue());
                break;
            case FLOAT:
                sb.append(((FloatType) type).getValue());
                break;
            case LONG:
                sb.append(((LongType) type).getValue());
                break;
            case BOOLEAN:
                sb.append(((BooleanType) type).getValue() ? "true" : "false");
                break;
            case STRING:
                sb.append('\'').append(((StringType) type).getValue()).append('\'');
                break;
            case BYTE_ARRAY:
                sb.append(printArray(((ByteArrayType) type).getValue(), verbose));
                break;
            case SHORT_ARRAY:
                sb.append(printArray(((ShortArrayType) type).getValue(), verbose));
                break;
            case INT_ARRAY:
                sb.append(printArray(((IntArrayType) type).getValue(), verbose));
                break;
            case DOUBLE_ARRAY:
                sb.append(printArray(((DoubleArrayType) type).getValue(), verbose));
                break;
            case FLOAT_ARRAY:
                sb.append(printArray(((FloatArrayType) type).getValue(), verbose));
                break;
            case LONG_ARRAY:
                sb.append(printArray(((LongArrayType) type).getValue(), verbose));
                break;
            case STRING_ARRAY:
                sb.append(printArray(((StringArrayType) type).getValue(), verbose));
                break;
            default:
                break;
        }
    }

    private static String repeat(char c, int amount) {
        final char[] chars = new char[amount];

        Arrays.fill(chars, c);

        return new String(chars);
    }

    //////////////////////////////////////////
    // Writes
    //////////////////////////////////////////
    /**
     * Writes the given byte array to a {@link DataOutputStream}
     *
     * @param writer The {@link DataOutputStream}
     * @param array The given byte array
     */
    public static void writeArray(@Nonnull DataOutputStream writer, @Nonnull byte[] array) throws IOException {
        writer.writeInt(array.length);
        for (byte value : array) {
            writer.writeByte(value);
        }
    }

    public static void writeArray(@Nonnull DataOutputStream writer, @Nonnull short[] array) throws IOException {
        writer.writeInt(array.length);
        for (short value : array) {
            writer.writeShort(value);
        }
    }

    public static void writeArray(@Nonnull DataOutputStream writer, @Nonnull int[] array) throws IOException {
        writer.writeInt(array.length);
        for (int value : array) {
            writer.writeInt(value);
        }
    }

    public static void writeArray(@Nonnull DataOutputStream writer, @Nonnull double[] array) throws IOException {
        writer.writeInt(array.length);
        for (double value : array) {
            writer.writeDouble(value);
        }
    }

    public static void writeArray(@Nonnull DataOutputStream writer, @Nonnull float[] array) throws IOException {
        writer.writeInt(array.length);
        for (float value : array) {
            writer.writeFloat(value);
        }
    }

    public static void writeArray(@Nonnull DataOutputStream writer, @Nonnull long[] array) throws IOException {
        writer.writeInt(array.length);
        for (long value : array) {
            writer.writeLong(value);
        }
    }

    public static void writeArray(@Nonnull DataOutputStream writer, @Nonnull String[] array) throws IOException {
        writer.writeInt(array.length);
        for (String value : array) {
            writer.writeUTF(value);
        }
    }

    //////////////////////////////////////////
    // Print
    //////////////////////////////////////////
    @Nonnull
    private static String printArray(byte[] arr, boolean verbose) {
        // Max of 20 values or all if verbose
        String arrayAsString = Arrays.toString(verbose ? arr : Arrays.copyOfRange(arr, 0, Math.min(20, arr.length)));
        // If it was cut off then remove the ending square bracket to add 3 dots and re-add square bracket
        if (arr.length > 20 && !verbose)
            arrayAsString = arrayAsString.substring(0, arrayAsString.length() - 1) + "...]";

        return arrayAsString + " (" + arr.length + " values)";
    }

    @Nonnull
    private static String printArray(short[] arr, boolean verbose) {
        // Max of 20 values or all if verbose
        String arrayAsString = Arrays.toString(verbose ? arr : Arrays.copyOfRange(arr, 0, Math.min(20, arr.length)));
        // If it was cut off then remove the ending square bracket to add 3 dots and re-add square bracket
        if (arr.length > 20 && !verbose)
            arrayAsString = arrayAsString.substring(0, arrayAsString.length() - 1) + "...]";

        return arrayAsString + " (" + arr.length + " values)";
    }

    @Nonnull
    private static String printArray(int[] arr, boolean verbose) {
        // Max of 20 values or all if verbose
        String arrayAsString = Arrays.toString(verbose ? arr : Arrays.copyOfRange(arr, 0, Math.min(20, arr.length)));
        // If it was cut off then remove the ending square bracket to add 3 dots and re-add square bracket
        if (arr.length > 20 && !verbose)
            arrayAsString = arrayAsString.substring(0, arrayAsString.length() - 1) + "...]";

        return arrayAsString + " (" + arr.length + " values)";
    }

    @Nonnull
    private static String printArray(double[] arr, boolean verbose) {
        // Max of 20 values or all if verbose
        String arrayAsString = Arrays.toString(verbose ? arr : Arrays.copyOfRange(arr, 0, Math.min(20, arr.length)));
        // If it was cut off then remove the ending square bracket to add 3 dots and re-add square bracket
        if (arr.length > 20 && !verbose)
            arrayAsString = arrayAsString.substring(0, arrayAsString.length() - 1) + "...]";

        return arrayAsString + " (" + arr.length + " values)";
    }

    @Nonnull
    private static String printArray(float[] arr, boolean verbose) {
        // Max of 20 values or all if verbose
        String arrayAsString = Arrays.toString(verbose ? arr : Arrays.copyOfRange(arr, 0, Math.min(20, arr.length)));
        // If it was cut off then remove the ending square bracket to add 3 dots and re-add square bracket
        if (arr.length > 20 && !verbose)
            arrayAsString = arrayAsString.substring(0, arrayAsString.length() - 1) + "...]";

        return arrayAsString + " (" + arr.length + " values)";
    }

    @Nonnull
    private static String printArray(long[] arr, boolean verbose) {
        // Max of 20 values or all if verbose
        String arrayAsString = Arrays.toString(verbose ? arr : Arrays.copyOfRange(arr, 0, Math.min(20, arr.length)));
        // If it was cut off then remove the ending square bracket to add 3 dots and re-add square bracket
        if (arr.length > 20 && !verbose)
            arrayAsString = arrayAsString.substring(0, arrayAsString.length() - 1) + "...]";

        return arrayAsString + " (" + arr.length + " values)";
    }

    @Nonnull
    private static String printArray(String[] arr, boolean verbose) {
        // Max of 20 values or all if verbose
        String arrayAsString = Arrays.toString(verbose ? arr : Arrays.copyOfRange(arr, 0, Math.min(20, arr.length)));
        // If it was cut off then remove the ending square bracket to add 3 dots and re-add square bracket
        if (arr.length > 20 && !verbose)
            arrayAsString = arrayAsString.substring(0, arrayAsString.length() - 1) + "...]";

        return arrayAsString + " (" + arr.length + " values)";
    }
}
