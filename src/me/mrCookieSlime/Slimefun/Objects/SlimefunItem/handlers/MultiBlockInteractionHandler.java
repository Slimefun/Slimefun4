package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.mrCookieSlime.Slimefun.Objects.MultiBlock;

@Deprecated
public abstract class MultiBlockInteractionHandler extends ItemHandler {
	
	public abstract boolean onInteract(Player p, MultiBlock mb, Block b);
	
	@Override
	public String toCodename() {
		return "MultiBlockInteractionHandler";
	}
}