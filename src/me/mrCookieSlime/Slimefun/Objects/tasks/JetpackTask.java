package me.mrCookieSlime.Slimefun.Objects.tasks;

import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class JetpackTask extends SlimefunTask {
	private double thrust;
	
	public JetpackTask(Player p, double thrust) {
		super(p);
		this.thrust = thrust;
	}
	
	@Override
	public void setID(int id) {
		this.id = id;
	}

	@Override
	void executeTask() {
		Player p = Bukkit.getPlayer(uuid);
		float cost = 0.08F;
		float charge = ItemEnergy.getStoredEnergy(p.getInventory().getChestplate());
		if (charge >= cost) {
			p.getInventory().setChestplate(ItemEnergy.chargeItem(p.getInventory().getChestplate(), -cost));
			PlayerInventory.update(p);

			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, (float) 0.25, 1);
			p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1, 1);
			p.setFallDistance(0F);
			Vector vector = new Vector(0, 1, 0);
			vector.multiply(thrust);
			vector.add(p.getEyeLocation().getDirection().multiply(0.2F));

			p.setVelocity(vector);
		}
		else Bukkit.getScheduler().cancelTask(id);
	}
}
