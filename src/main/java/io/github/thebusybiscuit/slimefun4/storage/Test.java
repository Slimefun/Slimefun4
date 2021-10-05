package io.github.thebusybiscuit.slimefun4.storage;

import io.github.bakedlibs.dough.blocks.BlockPosition;
import io.github.thebusybiscuit.slimefun4.storage.implementation.BinaryStorage;

public class Test {

    public static void main(String[] args) {
        Storage storage = new BinaryStorage();

        storage.getBlock(new BlockPosition(null, 0, 0, 0))
            .setInt();
    }
}
