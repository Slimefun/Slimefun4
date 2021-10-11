package io.github.thebusybiscuit.slimefun4.storage;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.storage.implementation.BinaryStorage;
import io.github.thebusybiscuit.slimefun4.storage.implementation.binary.BinaryUtils;
import io.github.thebusybiscuit.slimefun4.storage.implementation.binary.BinaryWriter;
import io.github.thebusybiscuit.slimefun4.storage.implementation.binary.CompressionType;
import org.bukkit.NamespacedKey;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        Storage storage = new BinaryStorage();

        final DataObject dataObject = storage.getBlock(new BlockPosition(null, 0, 0, 0))
            .setInt(NamedKey.ofDefault("1"), 123)
            .setLongArray(NamedKey.ofDefault("2"), new long[] {1, 2, 3, 4})
            .setBoolean(NamedKey.ofDefault("3"), true)
            .setString(NamedKey.ofDefault("4"), "peepoPog wow")
            .setByte(NamedKey.ofDefault("5"), (byte) 1)
            .setDataObject(NamedKey.ofDefault("6"), new DataObject()
                .setInt(NamedKey.ofDefault("7"), 12)
            );

        new BinaryWriter(new File("test.dat"), CompressionType.NONE).write(dataObject);

        System.out.println(BinaryUtils.toString(dataObject, true));
    }
}
