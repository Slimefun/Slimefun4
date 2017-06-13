package me.mrCookieSlime.Slimefun.holograms;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class ArmorStandFactory {

	@Deprecated
	public static ArmorStand createHidden(Location l) {
		return me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory.createHidden(l);
	}

	@Deprecated
	public static ArmorStand createSmall(Location l, ItemStack item, EulerAngle arm, float yaw) {
		return me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory.createSmall(l, item, arm, yaw);
	}

	@Deprecated
	public static ArmorStand createSmall(Location l, ItemStack head, float yaw) {
		return me.mrCookieSlime.CSCoreLibPlugin.general.World.ArmorStandFactory.createSmall(l, head, yaw);
	}

}
