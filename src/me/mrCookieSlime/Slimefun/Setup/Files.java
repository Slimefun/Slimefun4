package me.mrCookieSlime.Slimefun.Setup;

import java.io.File;

public class Files {
	
	public static File RESEARCHES = new File("plugins/Slimefun/Researches.yml");
	public static File CONFIG = new File("plugins/Slimefun/config.yml");
	public static File ITEMS = new File("plugins/Slimefun/Items.yml");
	public static File DATABASE = new File("data-storage/Slimefun/Players");
	public static File WHITELIST = new File("plugins/Slimefun/whitelist.yml");
	public static File MYSQL = new File("plugins/Slimefun/mysql.yml");
	
	public static void cleanup() {
		if (!RESEARCHES.exists()) {
			System.err.println("###############################################");
			System.err.println("############## = -  INFO  - = #################");
			System.err.println("###############################################");
			System.err.println("      ");
			System.err.println("Slimefun Warning:");
			System.err.println("         ");
			System.err.println("Slimefun has detected that your Files are either");
			System.err.println("outdated or do not exist. We generated new Files");
			System.err.println("instead otherwise Slimefun would not work. If you");
			System.err.println("have used Slimefun before, your Settings are now");
			System.err.println("gone. But therefore Slimefun works!");
			delete(new File("plugins/Slimefun"));
			delete(new File("data-storage/Slimefun"));
		}
		
		if (!DATABASE.exists()) {
			DATABASE.mkdirs();
		}
	}
	
	public static void delete(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File current: files) {
				if (current.isDirectory()) {
					delete(current);
				}
				else {
					current.delete();
				}
			}
		}
		folder.delete();
	}

}
