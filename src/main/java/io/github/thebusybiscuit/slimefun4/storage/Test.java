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
            .setInt(NamespacedKey.minecraft("1"), 123)
            .setLongArray(NamespacedKey.minecraft("2"), new long[] {1, 2, 3, 4})
            .setBoolean(NamespacedKey.minecraft("3"), true)
            .setString(NamespacedKey.minecraft("4"), "peepoPog wow")
            .setByte(NamespacedKey.minecraft("5"), (byte) 1)
            .setDataObject(NamespacedKey.minecraft("6"), new DataObject()
                .setInt(NamespacedKey.minecraft("7"), 12)
            );

        new BinaryWriter(new File("test.dat"), CompressionType.OFF).write(dataObject);

        System.out.println(BinaryUtils.toString(dataObject, true));
    }
}
