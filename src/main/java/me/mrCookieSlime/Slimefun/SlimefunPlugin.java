package me.mrCookieSlime.Slimefun;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionManager;
import io.github.thebusybiscuit.cscorelib2.recipes.RecipeSnapshot;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunTabCompleter;
import io.github.thebusybiscuit.slimefun4.core.services.AutoSavingService;
import io.github.thebusybiscuit.slimefun4.core.services.BlockDataService;
import io.github.thebusybiscuit.slimefun4.core.services.CustomItemDataService;
import io.github.thebusybiscuit.slimefun4.core.services.CustomTextureService;
import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun4.core.services.MetricsService;
import io.github.thebusybiscuit.slimefun4.core.services.UpdaterService;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubService;
import io.github.thebusybiscuit.slimefun4.implementation.geo.resources.NetherIceResource;
import io.github.thebusybiscuit.slimefun4.implementation.geo.resources.OilResource;
import io.github.thebusybiscuit.slimefun4.implementation.geo.resources.SaltResource;
import io.github.thebusybiscuit.slimefun4.implementation.geo.resources.UraniumResource;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AndroidKillingListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AutonomousToolsListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BackpackListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BlockListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.CoolerListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.EnhancedFurnaceListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.GearListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.GrapplingHookListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.ItemPickupListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.MultiBlockListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.NetworkListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.PlayerProfileListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SlimefunGuideListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SoulboundListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.TalismanListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.TeleporterListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.WaypointListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.WorldListener;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AReactor;
import me.mrCookieSlime.Slimefun.Objects.tasks.ArmorTask;
import me.mrCookieSlime.Slimefun.Setup.MiscSetup;
import me.mrCookieSlime.Slimefun.Setup.ResearchSetup;
import me.mrCookieSlime.Slimefun.Setup.SlimefunSetup;
import me.mrCookieSlime.Slimefun.Setup.WikiSetup;
import me.mrCookieSlime.Slimefun.ancient_altar.AncientAltarListener;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunBackup;
import me.mrCookieSlime.Slimefun.api.TickerTask;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.hooks.SlimefunHooks;
import me.mrCookieSlime.Slimefun.listeners.ArmorListener;
import me.mrCookieSlime.Slimefun.listeners.BowListener;
import me.mrCookieSlime.Slimefun.listeners.DamageListener;
import me.mrCookieSlime.Slimefun.listeners.ItemListener;
import me.mrCookieSlime.Slimefun.listeners.ToolListener;
import me.mrCookieSlime.Slimefun.utils.ConfigCache;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public final class SlimefunPlugin extends JavaPlugin {

	public static SlimefunPlugin instance;

	private RecipeSnapshot recipeSnapshot;
	
	private final CustomItemDataService itemDataService = new CustomItemDataService(this, "slimefun_item");
	private final CustomTextureService textureService = new CustomTextureService(this);
	private final BlockDataService blockDataService = new BlockDataService(this, "slimefun_block");
	private final GitHubService gitHubService = new GitHubService("TheBusyBiscuit/Slimefun4");
	private final AutoSavingService autoSavingService = new AutoSavingService();
	private final UpdaterService updaterService = new UpdaterService(this, getFile());
	
	private TickerTask ticker;
	private LocalizationService local;
	private Config researches;
	private Config items;
	private Config whitelist;
	private Config config;
	
	private GPSNetwork gps;
	private ProtectionManager protections;
	private Utilities utilities;
	private ConfigCache settings;
	private SlimefunHooks hooks;
	
	// Supported Versions of Minecraft
	private final String[] supported = {"v1_14_", "v1_15_"};

	@Override
	public void onEnable() {
		if (getServer().getPluginManager().isPluginEnabled("CS-CoreLib")) {

			String currentVersion = ReflectionUtils.getVersion();

			if (currentVersion.startsWith("v")) {
				boolean compatibleVersion = false;
				StringBuilder versions = new StringBuilder();

				int i = 0;
				for (String version : supported) {
					if (currentVersion.startsWith(version)) {
						compatibleVersion = true;
					}

					String s = version.substring(1).replaceFirst("_", ".").replace("_", ".X");
					if (i == 0) versions.append(s);
					else if (i == supported.length - 1) versions.append(" or ").append(s);
					else versions.append(", ").append(s);

					i++;
				}

				// Looks like you are using an unsupported Minecraft Version
				if (!compatibleVersion) {
					getLogger().log(Level.SEVERE, "### Slimefun was not installed correctly!");
					getLogger().log(Level.SEVERE, "###");
					getLogger().log(Level.SEVERE, "### You are using the wrong Version of Minecraft!");
					getLogger().log(Level.SEVERE, "###");
					getLogger().log(Level.SEVERE, "### You are using Minecraft " + ReflectionUtils.getVersion());
					getLogger().log(Level.SEVERE, "### but Slimefun v" + getDescription().getVersion() + " requires you to be using");
					getLogger().log(Level.SEVERE, "### Minecraft {0}", versions);
					getLogger().log(Level.SEVERE, "###");
					getLogger().log(Level.SEVERE, "### Please use an older Version of Slimefun and disable auto-updating");
					getLogger().log(Level.SEVERE, "### or consider updating your Server Software.");
					getServer().getPluginManager().disablePlugin(this);
					return;
				}
			}

			instance = this;
			
			// Creating all necessary Folders
			getLogger().log(Level.INFO, "Loading Files...");
			String[] storage = {"Players", "blocks", "stored-blocks", "stored-inventories", "stored-chunks", "universal-inventories", "waypoints", "block-backups"};
			String[] general = {"scripts", "generators", "error-reports", "cache/github"};
			for (String file : storage) createDir("data-storage/Slimefun/" + file);
			for (String file : general) createDir("plugins/Slimefun/" + file);

			getLogger().log(Level.INFO, "Loading Config...");

			// Setup config.yml
			config = new Config(this);
			settings = new ConfigCache(config);

			// Loading all extra configs
			researches = new Config(this, "Researches.yml");
			items = new Config(this, "Items.yml");
			whitelist = new Config(this, "whitelist.yml");

			// Setup messages.yml
			local = new LocalizationService(this);
			
			// Setting up other stuff
			utilities = new Utilities();
			gps = new GPSNetwork();
			
			// Setting up bStats
			new MetricsService(this);

			// Starting the Auto-Updater
			if (config.getBoolean("options.auto-update")) {
				updaterService.start();
			}

			getLogger().log(Level.INFO, "Loading Items...");
			MiscSetup.setupItemSettings();
			
			try {
				SlimefunSetup.setupItems();
			} catch (Exception x) {
				getLogger().log(Level.SEVERE, "An Error occured while initializing SlimefunItems for Slimefun " + Slimefun.getVersion(), x);
			}

			getLogger().log(Level.INFO, "Loading Researches...");
			ResearchSetup.setupResearches();
			
			settings.researchesEnabled = getResearchCfg().getBoolean("enable-researching");
			settings.smelteryFireBreakChance = (int) Slimefun.getItemValue("SMELTERY", "chance.fireBreak");

			MiscSetup.setupMisc();
			WikiSetup.addWikiPages(this);
			textureService.setup(utilities.allItems);

			getLogger().log(Level.INFO, "Loading World Generators...");

			// Generating Oil as an OreGenResource (it iss a cool API)
			OreGenSystem.registerResource(new OilResource());
			OreGenSystem.registerResource(new NetherIceResource());
			OreGenSystem.registerResource(new UraniumResource());
			OreGenSystem.registerResource(new SaltResource());

			// Setting up GitHub Connectors...
			gitHubService.connect(config.getBoolean("options.print-out-github-data-retrieving"));

			// All Slimefun Listeners
			new ArmorListener(this);
			new ItemListener(this);
			new BlockListener(this);
			new MultiBlockListener(this);
			new GearListener(this);
			new AutonomousToolsListener(this);
			new DamageListener(this);
			new BowListener(this);
			new ToolListener(this);
			new EnhancedFurnaceListener(this);
			new TeleporterListener(this);
			new AndroidKillingListener(this);
			new NetworkListener(this);
			new ItemPickupListener(this);
			new WaypointListener(this);

			// Toggleable Listeners for performance
			if (config.getBoolean("items.talismans")) new TalismanListener(this);
			if (config.getBoolean("items.backpacks")) new BackpackListener(this);
			if (config.getBoolean("items.coolers")) new CoolerListener(this);
			if (config.getBoolean("items.soulbound")) new SoulboundListener(this);

			// Handle Slimefun Guide being given on Join
			if (config.getBoolean("options.give-guide-on-first-join")) new SlimefunGuideListener(this);

			// Load/Unload Worlds in Slimefun
			new WorldListener(this);

			// Clear the Slimefun Guide History upon Player Leaving
			new PlayerProfileListener(this);

			// Initiating various Stuff and all Items with a slightly delay (0ms after the Server finished loading)
			Slimefun.runSync(() -> {
				recipeSnapshot = new RecipeSnapshot(this);
				protections = new ProtectionManager(getServer());
				MiscSetup.loadItems(settings);

				for (World world : Bukkit.getWorlds()) {
					new BlockStorage(world);
				}

				if (SlimefunItem.getByID("ANCIENT_ALTAR") != null) new AncientAltarListener(this);
				if (SlimefunItem.getByID("GRAPPLING_HOOK") != null) new GrapplingHookListener(this);
			}, 0);
			
			SlimefunCommand command = new SlimefunCommand(this);

			getCommand("slimefun").setExecutor(command);
			getCommand("slimefun").setTabCompleter(new SlimefunTabCompleter(command));

			// Armor Update Task
			if (config.getBoolean("options.enable-armor-effects")) {
				getServer().getScheduler().runTaskTimerAsynchronously(this, new ArmorTask(), 0L, config.getInt("options.armor-update-interval") * 20L);
			}

			ticker = new TickerTask();

			autoSavingService.start(this, config.getInt("options.auto-save-delay-in-minutes"));
			
			// Starting all ASYNC Tasks
			getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
				try {
					ticker.run();
				}
				catch(Exception x) {
					getLogger().log(Level.SEVERE, "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + Slimefun.getVersion(), x);
					ticker.abortTick();
				}
			}, 100L, config.getInt("URID.custom-ticker-delay"));
			
			gitHubService.start(this);

			// Hooray!
			getLogger().log(Level.INFO, "Finished!");
			hooks = new SlimefunHooks(this);
			
			utilities.oreWasherOutputs = new ItemStack[] {SlimefunItems.IRON_DUST, SlimefunItems.GOLD_DUST, SlimefunItems.ALUMINUM_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.ZINC_DUST, SlimefunItems.TIN_DUST, SlimefunItems.LEAD_DUST, SlimefunItems.SILVER_DUST, SlimefunItems.MAGNESIUM_DUST};

			// Do not show /sf elevator command in our Log, it could get quite spammy
			CSCoreLib.getLib().filterLog("([A-Za-z0-9_]{3,16}) issued server command: /sf elevator (.{0,})");
		}
		else {
			getLogger().log(Level.INFO, "#################### - INFO - ####################");
			getLogger().log(Level.INFO, " ");
			getLogger().log(Level.INFO, "Slimefun could not be loaded (yet).");
			getLogger().log(Level.INFO, "It appears that you have not installed CS-CoreLib.");
			getLogger().log(Level.INFO, "Please download and install CS-CoreLib manually:");
			getLogger().log(Level.INFO, "https://thebusybiscuit.github.io/builds/TheBusyBiscuit/CS-CoreLib/master/");
			
			getCommand("slimefun").setExecutor((sender, cmd, label, args) -> {
				sender.sendMessage("You have forgotten to install CS-CoreLib! Slimefun is disabled.");
				sender.sendMessage("https://thebusybiscuit.github.io/builds/TheBusyBiscuit/CS-CoreLib/master/");
				return true;
			});
		}
	}

	@Override
	public void onDisable() {
		// CS-CoreLib wasn't loaded, just disabling
		if (instance == null) return;
		
		Bukkit.getScheduler().cancelTasks(this);

		if (ticker != null) {
			// Finishes all started movements/removals of block data
			ticker.halt();
			ticker.run();
		}
		
		PlayerProfile.iterator().forEachRemaining(profile -> {
			if (profile.isDirty()) profile.save();
		});
		
		for (World world : Bukkit.getWorlds()) {
			try {
				BlockStorage storage = BlockStorage.getStorage(world);
				
				if (storage != null) {
					storage.save(true);
				}
				else {
					getLogger().log(Level.SEVERE, "Could not save Slimefun Blocks for World \"" + world.getName() + "\"");
				}
			} catch (Exception x) {
				getLogger().log(Level.SEVERE, "An Error occured while saving Slimefun-Blocks in World '" + world.getName() + "' for Slimefun " + Slimefun.getVersion());
			}
		}
		
		for (UniversalBlockMenu menu : utilities.universalInventories.values()) {
			menu.save();
		}
		
		SlimefunBackup.start();

		// Prevent Memory Leaks
		AContainer.processing = null;
		AContainer.progress = null;
		
		AGenerator.processing = null;
		AGenerator.progress = null;
		
		AReactor.processing = null;
		AReactor.progress = null;

		instance = null;

		for (Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}
	}

	private void createDir(String path) {
		File file = new File(path);
		if (!file.exists()) file.mkdirs();
	}

	public static Config getCfg() {
		return instance.config;
	}

	public static io.github.thebusybiscuit.cscorelib2.config.Config getResearchCfg() {
		return instance.researches;
	}

	public static Config getItemCfg() {
		return instance.items;
	}

	public static io.github.thebusybiscuit.cscorelib2.config.Config getWhitelist() {
		return instance.whitelist;
	}

	public GPSNetwork getGPS() {
		return gps;
	}

	public static SlimefunHooks getHooks() {
		return instance.hooks;
	}
	
	public static Utilities getUtilities() {
		return instance.utilities;
	}
	
	public static ConfigCache getSettings() {
		return instance.settings;
	}
	
	public static TickerTask getTicker() {
		return instance.ticker;
	}
	
	public static boolean isActive() {
		return instance != null;
	}

	public static ProtectionManager getProtectionManager() {
		return instance.protections;
	}

	public static LocalizationService getLocal() {
		return instance.local;
	}
	
	public static RecipeSnapshot getMinecraftRecipes() {
		return instance.recipeSnapshot;
	}
	
	public static CustomItemDataService getItemDataService() {
		return instance.itemDataService;
	}
	
	public static CustomTextureService getItemTextureService() {
		return instance.textureService;
	}
	
	public static BlockDataService getBlockDataService() {
		return instance.blockDataService;
	}

	public static GitHubService getGitHubService() {
		return instance.gitHubService;
	}

}
