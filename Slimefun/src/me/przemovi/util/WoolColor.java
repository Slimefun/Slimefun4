package me.przemovi.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class WoolColor {
	public Material getColoredWool(int i) {
		if(i == 0) {
			return Material.WHITE_WOOL;
		} else if(i == 1) {
			return Material.ORANGE_WOOL;
		} else if(i == 2) {
			return Material.MAGENTA_WOOL;
		} else if(i == 3) {
			return Material.LIGHT_BLUE_WOOL;
		} else if(i == 4) {
			return Material.YELLOW_WOOL;
		} else if(i == 5) {
			return Material.LIME_WOOL;
		} else if(i == 6) {
			return Material.PINK_WOOL;
		} else if(i == 7) {
			return Material.GRAY_WOOL;
		} else if(i == 8) {
			return Material.LIGHT_GRAY_WOOL;
		} else if(i == 9) {
			return Material.CYAN_WOOL;
		} else if(i == 10) {
			return Material.PURPLE_WOOL;
		} else if(i == 11) {
			return Material.BLUE_WOOL;
		} else if(i == 12) {
			return Material.BROWN_WOOL;
		} else if(i == 13) {
			return Material.GREEN_WOOL;
		} else if(i == 14) {
			return Material.RED_WOOL;
		} else if(i == 15) {
			return Material.BLACK_WOOL;
		}
		return null;
	}
}
