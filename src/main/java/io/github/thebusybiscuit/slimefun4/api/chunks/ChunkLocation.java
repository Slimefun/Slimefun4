package io.github.thebusybiscuit.slimefun4.api.chunks;

import java.util.Objects;

import org.bukkit.Chunk;
import org.bukkit.World;

public final class ChunkLocation {
	
	private final World world;
	private final int x;
	private final int z;
	
	public ChunkLocation(Chunk chunk) {
		this(chunk.getWorld(), chunk.getX(), chunk.getZ());
	}
	
	public ChunkLocation(World world, int x, int z) {
		this.world = world;
		this.x = x;
		this.z = z;
	}
	
	public World getWorld() {
		return world;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChunkLocation) {
			ChunkLocation l = (ChunkLocation) obj;
			return world.getUID().equals(l.getWorld().getUID()) && l.getX() == x && l.getZ() == z;
		}
		else return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(world, x, z);
	}
	
	@Override
	public String toString() {
		return world.getName() + " - Chunk [" + x + " | " + z + "]";
	}

}