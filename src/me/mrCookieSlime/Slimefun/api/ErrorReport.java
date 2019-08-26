package me.mrCookieSlime.Slimefun.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Clock;
import me.mrCookieSlime.Slimefun.SlimefunStartup;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class ErrorReport {
	
	private File file;
	
	public ErrorReport(TickerTask task, Location l, SlimefunItem item, Exception x) {
		int try_count = 1;
		file = new File("plugins/Slimefun/error-reports/" + Clock.getFormattedTime() + ".err");
		
		while (file.exists()) {
			try_count += 1;
			file = new File("plugins/Slimefun/error-reports/" + Clock.getFormattedTime() + "(" + try_count + ").err");
		}
		
		PrintStream stream = null;
		
		try {
			stream = new PrintStream(file);
			stream.println();
			stream.println("Java Environment:");
			stream.println("  Operating System: " + System.getProperty("os.name"));
			stream.println("  Java Version: " + System.getProperty("java.version"));
			stream.println();
			stream.println("Server Software: " + Bukkit.getName());
			stream.println("  Build: " + Bukkit.getVersion());
			stream.println("  Minecraft: " + Bukkit.getBukkitVersion());
			stream.println();
			stream.println("Slimefun Environment:");
			stream.println("  CS-CoreLib v" + CSCoreLib.getLib().getDescription().getVersion());
			stream.println("  Slimefun v" + SlimefunStartup.instance.getDescription().getVersion());
			stream.println();

			List<String> plugins = new ArrayList<>();
			List<String> addons = new ArrayList<>();
			for (Plugin p: Bukkit.getPluginManager().getPlugins()) {
				if (Bukkit.getPluginManager().isPluginEnabled(p)) {
					plugins.add("  + " + p.getName() + " " + p.getDescription().getVersion());
					if (p.getDescription().getDepend().contains("Slimefun") || p.getDescription().getSoftDepend().contains("Slimefun"))
						addons.add("  + " + p.getName() + " " + p.getDescription().getVersion());
				}
				else {
					plugins.add("  - " + p.getName() + " " + p.getDescription().getVersion());
					if (p.getDescription().getDepend().contains("Slimefun") || p.getDescription().getSoftDepend().contains("Slimefun"))
						addons.add("  - " + p.getName() + " " + p.getDescription().getVersion());
				}
			}
			
			stream.println("Installed Addons (" + addons.size() + ")");
			addons.forEach(stream::println);
			
			stream.println();
			
			stream.println("Installed Plugins (" + plugins.size() + "):");
			plugins.forEach(stream::println);
			
			stream.println();
			stream.println("Block Info:");
			stream.println("  World: " + l.getWorld().getName());
			stream.println("  X: " + l.getBlockX());
			stream.println("  Y: " + l.getBlockY());
			stream.println("  Z: " + l.getBlockZ());
			stream.println("  Material: " + l.getBlock().getType());
			stream.println("  State: " + l.getBlock().getState().getClass().getName());
			stream.println();
			stream.println("Ticker-Info:");
			stream.println("  Type: " + (item.getBlockTicker().isSynchronized() ? "Synchronized": "Asynchronous"));
			stream.println("  Object Dump: " + task.toString());
			stream.println();
			stream.println("Slimefun Data:");
			stream.println("  ID: " + item.getID());
			stream.println("  Inventory: " + BlockStorage.getStorage(l.getWorld()).hasInventory(l));
			stream.println("  Data: " + BlockStorage.getBlockInfoAsJson(l));
			stream.println();
			stream.println("Stacktrace:");
			stream.println();
			x.printStackTrace(stream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (stream != null) stream.close();
		}
	}

	public File getFile() {
		return file;
	}

}
