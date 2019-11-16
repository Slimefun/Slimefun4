package me.mrCookieSlime.Slimefun.hooks;

import java.util.logging.Level;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public final class SlimefunHooks {
	
	private SlimefunPlugin plugin;
	
	private boolean exoticGarden = false;
	private boolean emeraldEnchants = false;
	private boolean coreProtect = false;
	private boolean clearLag = false;
	private boolean worldEdit = false;
	private boolean placeHolderAPI = false;
	
	public SlimefunHooks(SlimefunPlugin plugin) {
		this.plugin = plugin;
		
		if (isPluginInstalled("PlaceholderAPI")) {
			placeHolderAPI = true;
			new PlaceholderAPIHook().register();
		}
		
		/*
		 *  These Items are not marked as soft-dependencies and 
		 *  therefore need to be loaded after the Server has finished 
		 *  loading all plugins
		 */
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			if (isPluginInstalled("ClearLag")) {
				clearLag = true;
				new ClearLagHook(plugin);
			}
			
			exoticGarden = isPluginInstalled("ExoticGarden");
			emeraldEnchants = isPluginInstalled("EmeraldEnchants");

			// WorldEdit Hook to clear Slimefun Data upon //set 0 //cut or any other equivalent
			if (isPluginInstalled("WorldEdit")) {
				try {
					Class.forName("com.sk89q.worldedit.extent.Extent");
					worldEdit = true;
					new WorldEditHook();
				} catch (Exception x) {
					Slimefun.getLogger().log(Level.WARNING, "无法 Hook WorldEdit!");
					Slimefun.getLogger().log(Level.WARNING, "请尝试更新 WorldEdit / Slimefun");
				}
			}
		});
	}
	
	private boolean isPluginInstalled(String hook) {
		if (plugin.getServer().getPluginManager().isPluginEnabled(hook)) {
			Slimefun.getLogger().log(Level.INFO, "成功 Hook 插件: " + hook);
			return true;
		}
		else {
			return false;
		}
	}

	public boolean isExoticGardenInstalled() {
		return exoticGarden;
	}
	
	public boolean isEmeraldEnchantsInstalled() {
		return emeraldEnchants;
	}

	public boolean isCoreProtectInstalled() {
		return coreProtect;
	}

	public boolean isClearLagInstalled() {
		return clearLag;
	}

	public boolean isWorldEditInstalled() {
		return worldEdit;
	}

	public boolean isPlaceholderAPIInstalled() {
		return placeHolderAPI;
	}
}
