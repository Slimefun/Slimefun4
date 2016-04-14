package me.mrCookieSlime.Slimefun.Android;

import org.bukkit.block.Block;

public class AndroidObject {
	
	ProgrammableAndroid android;
	Block b;
	
	public AndroidObject(ProgrammableAndroid android, Block b) {
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
