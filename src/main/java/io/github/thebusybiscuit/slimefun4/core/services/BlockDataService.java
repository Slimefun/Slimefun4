package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Optional;

import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;

public class BlockDataService {
	
	private final NamespacedKey namespacedKey;
	
	public BlockDataService(Plugin plugin, String key) {
		namespacedKey = new NamespacedKey(plugin, key);
	}
	
	public void setBlockData(TileState tileEntity, String value) {
		PersistentDataAPI.setString(tileEntity, namespacedKey, value);
		tileEntity.update();
	}
	
	public Optional<String> getBlockData(TileState tileEntity) {
		return PersistentDataAPI.getOptionalString(tileEntity, namespacedKey);
	}

}
