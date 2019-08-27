package me.mrCookieSlime.Slimefun.Objects.handlers;

import me.mrCookieSlime.Slimefun.Objects.MultiBlock;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface MultiBlockInteractionHandler extends ItemHandler {
	
	boolean onInteract(Player p, MultiBlock mb, Block b);
	
	default String toCodename() {
		return "MultiBlockInteractionHandler";
	}
}
