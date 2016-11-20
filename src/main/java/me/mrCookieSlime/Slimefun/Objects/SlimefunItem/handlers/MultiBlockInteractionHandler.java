package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.handlers;

import me.mrCookieSlime.Slimefun.Objects.MultiBlock;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class MultiBlockInteractionHandler extends ItemHandler {
	
	public abstract boolean onInteract(Player p, MultiBlock mb, Block b);
	
	@Override
	public String toCodename() {
		return "MultiBlockInteractionHandler";
	}
}
