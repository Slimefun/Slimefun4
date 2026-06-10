package io.github.thebusybiscuit.slimefun5.implementation.items.androids;

import org.bukkit.block.Block;

public class AndroidInstance {

    private final ProgrammableAndroid android;
    private final Block b;

    public AndroidInstance(ProgrammableAndroid android, Block b) {
        this.android = android;
        this.b = b;
    }

    public ProgrammableAndroid getAndroid() {
        return android;
    }

    public Block getBlock() {
        return b;
    }

}

