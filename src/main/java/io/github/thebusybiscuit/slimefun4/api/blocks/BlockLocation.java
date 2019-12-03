package io.github.thebusybiscuit.slimefun4.api.blocks;

import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.block.Block;

public final class BlockLocation {
	
	private final int x;
	private final int y;
	private final int z;
	
	public BlockLocation(Block b) {
		this(b.getX(), b.getY(), b.getZ());
	}
	
	public BlockLocation(Location l) {
		this(l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}
	
	public BlockLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockLocation) {
			BlockLocation l = (BlockLocation) obj;
			return l.getX() == x && l.getY() == y && l.getZ() == z;
		}
		else return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}
	
	@Override
	public String toString() {
		return "[" + x + " | " + y + " | " + z + "]";
	}

}