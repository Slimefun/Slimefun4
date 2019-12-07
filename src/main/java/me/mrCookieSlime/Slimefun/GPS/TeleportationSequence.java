package me.mrCookieSlime.Slimefun.GPS;

import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.mrCookieSlime.CSCoreLibPlugin.general.World.TitleBuilder;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.TitleBuilder.TitleType;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class TeleportationSequence {
	
	private TeleportationSequence() {}
	
	public static void start(UUID uuid, int complexity, Location source, Location destination, boolean resistance) {
		SlimefunPlugin.getUtilities().teleporterUsers.add(uuid);
		
		updateProgress(uuid, getSpeed(complexity, source, destination), 1, source, destination, resistance);
	}
	
	public static int getSpeed(int complexity, Location source, Location destination) {
		int speed = complexity / 200;
		if (speed > 50) speed = 50;
		speed = speed - (distance(source, destination) / 200);
		
		return speed < 1 ? 1: speed;
	}
	
	private static int distance(Location source, Location destination) {
		if (source.getWorld().getName().equals(destination.getWorld().getName())) {
			int distance = (int) source.distance(destination);
			return distance > 8000 ? 8000: distance;
		}
		else return 8000;
	}
	
	private static boolean isValid(Player p, Location source) {
		return p != null && p.getLocation().distanceSquared(source) < 2.0;
	}
	
	private static void cancel(UUID uuid, Player p) {
		SlimefunPlugin.getUtilities().teleporterUsers.remove(uuid);
		
		if (p != null) {
			try {
				TitleBuilder title = (TitleBuilder) new TitleBuilder(20, 60, 20).addText(ChatColor.translateAlternateColorCodes('&', "&4Teleportation cancelled"));
				TitleBuilder subtitle = (TitleBuilder) new TitleBuilder(20, 60, 20).addText(ChatColor.translateAlternateColorCodes('&', "&40%"));
				
				title.send(TitleType.TITLE, p);
				subtitle.send(TitleType.SUBTITLE, p);
			} catch(Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occured while cancelling a Teleportation Sequence for Slimefun " + Slimefun.getVersion(), x);
			}
		}
	}
	
	private static void updateProgress(final UUID uuid, final int speed, final int progress, final Location source, final Location destination, final boolean resistance) {
		Player p = Bukkit.getPlayer(uuid);
		if (isValid(p, source)) {
			try {
				if (progress > 99) {
					TitleBuilder title = (TitleBuilder) new TitleBuilder(20, 60, 20).addText(ChatColor.translateAlternateColorCodes('&', "&3Teleported!"));
					TitleBuilder subtitle = (TitleBuilder) new TitleBuilder(20, 60, 20).addText(ChatColor.translateAlternateColorCodes('&', "&b100%"));
					
					title.send(TitleType.TITLE, p);
					subtitle.send(TitleType.SUBTITLE, p);
					
					p.teleport(destination);
					
					if (resistance) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 600, 20));
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lYou have been given 30 Seconds of Invulnerability!"));
					}
					
					destination.getWorld().spawnParticle(Particle.PORTAL,new Location(destination.getWorld(), destination.getX(), destination.getY() + 1, destination.getZ()),progress * 2, 0.2F, 0.8F, 0.2F );
					destination.getWorld().playSound(destination, Sound.BLOCK_BEACON_ACTIVATE, 1F, 1F);
					SlimefunPlugin.getUtilities().teleporterUsers.remove(uuid);
				}
				else {
					TitleBuilder title = (TitleBuilder) new TitleBuilder(0, 60, 0).addText(ChatColor.translateAlternateColorCodes('&', "&3Teleporting..."));
					TitleBuilder subtitle = (TitleBuilder) new TitleBuilder(0, 60, 0).addText(ChatColor.translateAlternateColorCodes('&', "&b" + progress + "%"));
					
					title.send(TitleType.TITLE, p);
					subtitle.send(TitleType.SUBTITLE, p);
					
					source.getWorld().spawnParticle(Particle.PORTAL, source, progress * 2, 0.2F, 0.8F, 0.2F);
					source.getWorld().playSound(source, Sound.BLOCK_BEACON_AMBIENT, 1F, 0.6F);
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> updateProgress(uuid, speed, progress + speed, source, destination, resistance), 10L);
				}
			} catch (Exception x) {
				Slimefun.getLogger().log(Level.SEVERE, "An Error occured during a Teleportation Sequence for Slimefun " + Slimefun.getVersion(), x);
			}
		}
		else cancel(uuid, p);
	}

}
