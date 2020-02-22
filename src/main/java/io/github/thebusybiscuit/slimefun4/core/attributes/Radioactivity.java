package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.ChatColor;

public enum Radioactivity {

	LOW(ChatColor.YELLOW, "低"),
	MODERATE(ChatColor.YELLOW, "中"),
	HIGH(ChatColor.DARK_GREEN, "高"),
	VERY_HIGH(ChatColor.RED, "极高"),
	VERY_DEADLY(ChatColor.DARK_RED, "致死");
	
	private final ChatColor color;
	private final String alias;
	
	private Radioactivity(ChatColor color, String alias) {
		this.color = color;
		this.alias = alias;
	}

	// Little tweak for Localize
	public String getLore() {
		return ChatColor.GREEN + "\u2622" + ChatColor.GRAY + " 辐射等级: " + color + alias;
	}
	
	public int getLevel() {
		return ordinal() + 1;
	}

}
