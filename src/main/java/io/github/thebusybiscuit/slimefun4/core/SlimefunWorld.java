package io.github.thebusybiscuit.slimefun4.core;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;

import io.github.thebusybiscuit.cscorelib2.collections.OptionalMap;
import io.github.thebusybiscuit.slimefun4.api.blocks.BlockLocation;
import io.github.thebusybiscuit.slimefun4.api.blocks.SlimefunBlock;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class SlimefunWorld {
	
	private final UUID uuid;
	private final OptionalMap<BlockLocation, SlimefunBlock> blocks = new OptionalMap<>(HashMap::new);

	public SlimefunWorld(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * This method will return the {@link UUID} of this instance.
	 * The {@link UUID} will be the same for {@link SlimefunWorld} and the corresponding instance
	 * of {@link World}.
	 * 
	 * @return		This world's {@link UUID}
	 */
	public UUID getUUID() {
		return uuid;
	}
	
	/**
	 * This method will reliably get the instance of {@link SlimefunBlock} associated with the given
	 * {@link Block}.
	 * 
	 * @param b		The {@link Block} to query
	 * @return		An {@link Optional} of the requested {@link SlimefunBlock}, empty if none was found
	 */
	public Optional<SlimefunBlock> getBlock(Block b) {
		if (b.getState() instanceof TileState) {
			Optional<String> blockData = SlimefunPlugin.getBlockDataService().getBlockData((TileState) b.getState());
			
			if (blockData.isPresent()) {
				return Optional.of(new SlimefunBlock(blockData.get()));
			}
		}
		
		return blocks.get(new BlockLocation(b));
	}
	
	public boolean isBlock(Block b, String id) {
		if (id == null) {
			throw new IllegalArgumentException("Cannot check blocks for id: null");
		}
		
		Optional<SlimefunBlock> block = getBlock(b);
		
		return block.isPresent() && block.get().getID().equals(id);
	}
}
