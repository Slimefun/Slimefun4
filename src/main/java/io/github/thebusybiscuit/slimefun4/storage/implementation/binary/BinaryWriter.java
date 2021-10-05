package io.github.thebusybiscuit.slimefun4.storage.implementation.binary;

import com.github.luben.zstd.ZstdOutputStream;
import io.github.thebusybiscuit.slimefun4.storage.DataObject;
import io.github.thebusybiscuit.slimefun4.storage.type.BooleanType;
import io.github.thebusybiscuit.slimefun4.storage.type.ByteArrayType;
import io.github.thebusybiscuit.slimefun4.storage.type.ByteType;
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
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 *
 */
// TODO: We probably want an object end
public class BinaryWriter {

    private final File file;

    public BinaryWriter(@Nonnull File file) {
        this.file = file;
    }

    public void write(@Nonnull DataObject object) {
        try (final DataOutputStream writer = new DataOutputStream(new ZstdOutputStream(new FileOutputStream(file)))) {
            // Write DataObject start
            writer.writeByte(TypeEnum.OBJECT.getId());
            // The root object does not have a name

            for (Map.Entry<NamespacedKey, Type> entry : object.getEntries()) {
                writeType(writer, entry.getKey(), entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ParametersAreNonnullByDefault
    private void writeType(DataOutputStream writer, NamespacedKey namespace, Type type) throws IOException {
        // We write the "id" of the type and the name.
        // This means we can read the type and the naming correctly
        writer.writeByte(type.getTypeEnum().getId());
        writer.writeUTF(namespace.toString());

        switch (type.getTypeEnum()) {
            case BYTE:
                writer.writeByte(((ByteType) type).getValue());
                break;
            case SHORT:
                writer.writeShort(((ShortType) type).getValue());
                break;
            case INT:
                writer.writeInt(((IntType) type).getValue());
                break;
            case DOUBLE:
                writer.writeDouble(((DoubleType) type).getValue());
                break;
            case FLOAT:
                writer.writeFloat(((FloatType) type).getValue());
                break;
            case LONG:
                writer.writeLong(((LongType) type).getValue());
                break;
            case BOOLEAN:
                writer.writeBoolean(((BooleanType) type).getValue());
                break;
            case STRING:
                writer.writeUTF(((StringType) type).getValue());
                break;
            case BYTE_ARRAY:
                final byte[] byteArray = ((ByteArrayType) type).getValue();
                final int byteArrayLength = byteArray.length;

                writer.writeInt(byteArrayLength);
                for (byte value : byteArray) {
                    writer.writeByte(value);
                }
                break;
            case SHORT_ARRAY:
                final short[] shortArray = ((ShortArrayType) type).getValue();
                final int shortArrayLength = shortArray.length;

                writer.writeInt(shortArrayLength);
                for (short value : shortArray) {
                    writer.writeShort(value);
                }
                break;
            case INT_ARRAY:
                final int[] intArray = ((IntArrayType) type).getValue();
                final int intArrayLength = intArray.length;

                writer.writeInt(intArrayLength);
                for (int value : intArray) {
                    writer.writeShort(value);
                }
                break;
            case DOUBLE_ARRAY:
                final double[] doubleArray = ((DoubleArrayType) type).getValue();
                final int doubleArrayLength = doubleArray.length;

                writer.writeInt(doubleArrayLength);
                for (double value : doubleArray) {
                    writer.writeDouble(value);
                }
                break;
            case FLOAT_ARRAY:
                final float[] floatArray = ((FloatArrayType) type).getValue();
                final int floatArrayLength = floatArray.length;

                writer.writeInt(floatArrayLength);
                for (float value : floatArray) {
                    writer.writeFloat(value);
                }
                break;
            case LONG_ARRAY:
                final long[] longArray = ((LongArrayType) type).getValue();
                final int longArrayLength = longArray.length;

                writer.writeInt(longArrayLength);
                for (long value : longArray) {
                    writer.writeLong(value);
                }
                break;
            case STRING_ARRAY:
                final String[] stringArray = ((StringArrayType) type).getValue();
                final int stringArrayLength = stringArray.length;

                writer.writeInt(stringArrayLength);
                for (String value : stringArray) {
                    writer.writeUTF(value);
                }
                break;
            case OBJECT:
                break;
        }
    }
}
