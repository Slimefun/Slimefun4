package me.mrCookieSlime.Slimefun.hooks;

import me.mrCookieSlime.Slimefun.SlimefunStartup;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public final class SlimefunHooks {
	
	private boolean exoticGarden = false;
	private boolean coreProtect = false;
	private boolean clearLag = false;
	private boolean worldEdit = false;
	private boolean placeHolderAPI = false;
	
	private CoreProtectAPI coreProtectAPI;
	
	public SlimefunHooks(SlimefunStartup plugin) {
		if (plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			placeHolderAPI = true;
			new PlaceholderAPIHook().register();
		}
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
			if (plugin.getServer().getPluginManager().isPluginEnabled("ClearLag")) {
				clearLag = true;
				new ClearLagHook(plugin);
			}
			
			exoticGarden = plugin.getServer().getPluginManager().isPluginEnabled("ExoticGarden"); // Had to do it this way, otherwise it seems disabled.
			
			if (plugin.getServer().getPluginManager().isPluginEnabled("CoreProtect")) {
				coreProtectAPI = ((CoreProtect) plugin.getServer().getPluginManager().getPlugin("CoreProtect")).getAPI();
			}

			// WorldEdit Hook to clear Slimefun Data upon //set 0 //cut or any other equivalent
			if (plugin.getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
				try {
					Class.forName("com.sk89q.worldedit.extent.Extent");
					worldEdit = true;
					new WorldEditHook();
					System.out.println("[Slimefun] Successfully hooked into WorldEdit!");
				} catch (Exception x) {
					System.err.println("[Slimefun] Failed to hook into WorldEdit!");
					System.err.println("[Slimefun] Maybe consider updating WorldEdit or Slimefun?");
				}
			}
		});
	}
	
	public boolean isExoticGardenInstalled() {
		return exoticGarden;
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
	
	public CoreProtectAPI getCoreProtectAPI() {
		return coreProtectAPI;
	}
	
}
