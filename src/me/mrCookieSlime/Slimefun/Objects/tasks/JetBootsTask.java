package me.mrCookieSlime.Slimefun.Objects.tasks;

import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.mrCookieSlime.CSCoreLibPlugin.general.Player.PlayerInventory;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;

public class JetBootsTask extends SlimefunTask {
	private double speed;

	public JetBootsTask(Player p, double speed) {
		super(p);
		this.speed = speed;
	}

	@Override
	void executeTask() {
		Player p = Bukkit.getPlayer(uuid);
		float cost = 0.075F;
		float charge = ItemEnergy.getStoredEnergy(p.getInventory().getBoots());
		double accuracy = Double.parseDouble(new DecimalFormat("##.##").format(speed - 0.7).replace(',', '.'));
		if (charge >= cost) {
			p.getInventory().setBoots(ItemEnergy.chargeItem(p.getInventory().getBoots(), -cost));
			PlayerInventory.update(p);

			p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TNT_PRIMED, (float) 0.25, 1);
			p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 1, 1);
			p.setFallDistance(0F);
			double gravity = 0.04;
			double offset = new Random().nextInt(2) == 1 ? accuracy: -accuracy;
			Vector vector = new Vector(p.getEyeLocation().getDirection().getX() * speed + offset, gravity, p.getEyeLocation().getDirection().getZ() * speed  - offset);

			p.setVelocity(vector);
		}
		else Bukkit.getScheduler().cancelTask(id);
	}
}
