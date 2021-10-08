package io.github.thebusybiscuit.slimefun4.storage.implementation.binary;

import io.github.thebusybiscuit.slimefun4.storage.DataObject;
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
import org.apache.commons.lang.Validate;
import org.bukkit.NamespacedKey;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class BinaryWriter {

    private final File file;

    public BinaryWriter(@Nonnull File file) {
        this.file = file;
    }

    public void write(@Nonnull DataObject object) {
//        try (final DataOutputStream writer = new DataOutputStream(new ZstdOutputStream(new FileOutputStream(file)))) {
        try (final DataOutputStream writer = new DataOutputStream(new FileOutputStream(file))) {
            // Write DataObject start - there is no name for root
            writer.writeByte(TypeEnum.OBJECT.getId());

            // Write the object out
            writeObject(writer, object);

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeObject(@Nonnull DataOutputStream writer, @Nonnull DataObject object) throws IOException {
        for (Map.Entry<NamespacedKey, Type> entry : object.getEntries()) {
            writeType(writer, entry.getKey(), entry.getValue());
        }
        // Indicate the end of the object
        writer.writeByte(TypeEnum.OBJECT_END.getId());
    }

    @ParametersAreNonnullByDefault
    private void writeType(DataOutputStream writer, @Nonnull NamespacedKey namespace, Type type) throws IOException {
        Validate.notNull(writer, "DataOutputStream cannot be null!!");
        Validate.notNull(namespace, "NamespacedKey cannot be null!");
        Validate.notNull(type, "Type cannot be null!");

        // We write the "id" of the type and the name.
        // This means we can read the type and the naming correctly
        System.out.println("writeType(" + namespace + ", "
            + type.getTypeEnum().name() + " - " + type.getTypeEnum().getId() + ')');
        writer.writeByte(type.getTypeEnum().getId());

        // Write the name of the type - OBJECT_END will never have a name
        if (type.getTypeEnum() != TypeEnum.OBJECT_END) {
            writer.writeUTF(namespace.toString());
        }

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
                BinaryUtils.writeArray(writer, ((ByteArrayType) type).getValue());
                break;
            case SHORT_ARRAY:
                BinaryUtils.writeArray(writer, ((ShortArrayType) type).getValue());
                break;
            case INT_ARRAY:
                BinaryUtils.writeArray(writer, ((IntArrayType) type).getValue());
                break;
            case DOUBLE_ARRAY:
                BinaryUtils.writeArray(writer, ((DoubleArrayType) type).getValue());
                break;
            case FLOAT_ARRAY:
                BinaryUtils.writeArray(writer, ((FloatArrayType) type).getValue());
                break;
            case LONG_ARRAY:
                BinaryUtils.writeArray(writer, ((LongArrayType) type).getValue());
                break;
            case STRING_ARRAY:
                BinaryUtils.writeArray(writer, ((StringArrayType) type).getValue());
                break;
            case OBJECT:
                final DataObject dataObject = ((DataObjectType) type).getValue();

                writeObject(writer, dataObject);
                break;

            case OBJECT_END:
                // Nothing to do in here
                break;
            default:
                break;
        }
    }
}
