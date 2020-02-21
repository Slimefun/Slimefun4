package io.github.thebusybiscuit.slimefun4.implementation.items.androids;

import org.bukkit.block.Block;

public class AndroidEntity {

	private final ProgrammableAndroid android;
	private final Block b;

	public AndroidEntity(ProgrammableAndroid android, Block b) {
		this.android = android;
		this.b = b;
	}

	public ProgrammableAndroid getAndroid() {
		return this.android;
	}

	public Block getBlock() {
		return b;
	}

}
