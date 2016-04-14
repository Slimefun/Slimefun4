package me.mrCookieSlime.Slimefun.Objects;

import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public interface SlimefunBlockHandler {
	
	void onPlace(Player p, Block b, SlimefunItem item);

	boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason);
}
