package me.mrCookieSlime.Slimefun.Setup;

import java.io.File;
import java.util.logging.Level;

import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class Files {
	
	public final File researches = new File("plugins/Slimefun/Researches.yml");
	public final File config = new File("plugins/Slimefun/config.yml");
	public final File items = new File("plugins/Slimefun/Items.yml");
	public final File database = new File("data-storage/Slimefun/Players");
	public final File whitelist = new File("plugins/Slimefun/whitelist.yml");
	
	public void cleanup() {
		if (!researches.exists()) {
			Slimefun.getLogger().log(Level.WARNING, "###############################################");
			Slimefun.getLogger().log(Level.WARNING, "############## = -  INFO  - = #################");
			Slimefun.getLogger().log(Level.WARNING, "###############################################");
			Slimefun.getLogger().log(Level.WARNING, " ");
			Slimefun.getLogger().log(Level.WARNING, "Slimefun Warning:");
			Slimefun.getLogger().log(Level.WARNING, " ");
			Slimefun.getLogger().log(Level.WARNING, "Slimefun has detected that your Files are either");
			Slimefun.getLogger().log(Level.WARNING, "outdated or do not exist. We generated new Files");
			Slimefun.getLogger().log(Level.WARNING, "instead otherwise Slimefun would not work. If you");
			Slimefun.getLogger().log(Level.WARNING, "have used Slimefun before, your Settings are now");
			Slimefun.getLogger().log(Level.WARNING, "gone. But therefore Slimefun works!");
			delete(new File("plugins/Slimefun"));
			delete(new File("data-storage/Slimefun"));
		}
		
		if (!database.exists()) {
			database.mkdirs();
		}
	}
	
	public boolean delete(File folder) {
		File[] files = folder.listFiles();
		if (files != null) {
			for (File current: files) {
				if (current.isDirectory()) {
					if (!delete(current)) return false;
				}
				else {
					if (!current.delete()) return false;
				}
			}
		}
		
		return folder.delete();
	}

}
