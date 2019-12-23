package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.ChatColor;

public enum Radioactivity {
	
	MODERATE(ChatColor.YELLOW),
	HIGH(ChatColor.DARK_GREEN),
	VERY_HIGH(ChatColor.RED),
	VERY_DEADLY(ChatColor.DARK_RED);
	
	private final ChatColor color;
	
	private Radioactivity(ChatColor color) {
		this.color = color;
	}
	
	public String getLore() {
		return ChatColor.GREEN + "\u2622" + ChatColor.GRAY + " Radiation level: " + color + toString().replace('_', ' ');
	}
	
	public int getLevel() {
		return ordinal() + 1;
	}

}
