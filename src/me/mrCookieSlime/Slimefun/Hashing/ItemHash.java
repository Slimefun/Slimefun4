package me.mrCookieSlime.Slimefun.Hashing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class ItemHash {
	
	public static MessageDigest digest;
	public static int LENGTH;
	public static Map<String, SlimefunItem> map = new HashMap<>();
	
	static {
		try {
			digest = MessageDigest.getInstance("SHA");
			LENGTH = hash("The Busy Biscuit").length();
		} catch (NoSuchAlgorithmException e) {
			System.out.println("FATAL Security ERROR - Slimefun was disabled.");
			Bukkit.getPluginManager().disablePlugin(SlimefunStartup.instance);
			e.printStackTrace();
		}
	}
	
	public static String hash(String input) {
		digest.update(input.getBytes());
		byte[] hash = digest.digest();
		return new BigInteger(1, hash).toString(16);
	}
	
	public static String toString(SlimefunItem item) {
		StringBuilder builder = new StringBuilder(LENGTH * 2);
		
		for (char c: item.getHash().toCharArray()) {
			builder.append('§');
			builder.append(c);
		}
		
		return builder.toString();
	}
	public static SlimefunItem fromString(String input) {
		if (input == null || input.length() != LENGTH * 2) return null;
		
		String hex = input.replaceAll("§", "");
		
		if (hex.length() != LENGTH || !map.containsKey(hex)) return null;
		
		return map.get(hex);
	}
	
	public static void register(SlimefunItem item) {
		String hash = hash(item.getID());
		
		if (map.containsKey(hash) && !item.getID().equals(map.get(hash).getHash())) {
			System.out.println("FATAL Security ERROR - Slimefun was disabled.");
			Bukkit.getPluginManager().disablePlugin(SlimefunStartup.instance);
			throw new IllegalStateException("Hash Collision: " + hash);
		}
		
		item.setHash(hash);
		map.put(hash, item);
	}

}
