package io.github.thebusybiscuit.slimefun4.core.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public final class FireworkUtils {
	
	private static final Color[] colors = {Color.AQUA, Color.BLACK, Color.BLUE, Color.FUCHSIA, Color.GRAY, Color.GREEN, Color.LIME, Color.MAROON, Color.NAVY, Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.SILVER, Color.TEAL, Color.WHITE, Color.YELLOW};
	
	private FireworkUtils() {}
	
	public static void launchFirework(Location l, Color color) {
		Firework fw = (Firework)l.getWorld().spawnEntity(l, EntityType.FIREWORK);
		
		FireworkMeta meta = fw.getFireworkMeta();
	    FireworkEffect effect = getRandomEffect(ThreadLocalRandom.current(), color);
	    meta.addEffect(effect);
	    meta.setPower(ThreadLocalRandom.current().nextInt(2) + 1);
	    fw.setFireworkMeta(meta);
	}
	
	public static Firework createFirework(Location l, Color color) {
		Firework fw = (Firework)l.getWorld().spawnEntity(l, EntityType.FIREWORK);
		FireworkMeta meta = fw.getFireworkMeta();
	    FireworkEffect effect = FireworkEffect.builder().flicker(ThreadLocalRandom.current().nextBoolean()).withColor(color).with(ThreadLocalRandom.current().nextInt(3) + 1 == 1 ? Type.BALL: Type.BALL_LARGE).trail(ThreadLocalRandom.current().nextBoolean()).build();
	    meta.addEffect(effect);
	    meta.setPower(ThreadLocalRandom.current().nextInt(2) + 1);
	    fw.setFireworkMeta(meta);
	    return fw;
	}
	
	public static void launchRandom(Player p, int amount) {
		for (int i = 0; i < amount; i++) {
			Location l = p.getLocation().clone();
			l.setX(l.getX() + ThreadLocalRandom.current().nextInt(amount));
			l.setX(l.getX() - ThreadLocalRandom.current().nextInt(amount));
			l.setZ(l.getZ() + ThreadLocalRandom.current().nextInt(amount));
			l.setZ(l.getZ() - ThreadLocalRandom.current().nextInt(amount));
			
            launchFirework(l, getRandomColor());
		}
	}
	
	public static FireworkEffect getRandomEffect(Random random, Color color) {
		return FireworkEffect.builder()
			.flicker(random.nextBoolean())
			.withColor(color)
			.with(random.nextBoolean() ? Type.BALL: Type.BALL_LARGE)
			.trail(random.nextBoolean())
		.build();
	}
	
	private static Color getRandomColor() {
		return colors[ThreadLocalRandom.current().nextInt(colors.length)];
	}
}
