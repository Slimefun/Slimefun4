package me.mrCookieSlime.Slimefun;

import java.io.File;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.cscorelib2.players.MinecraftAccount;
import io.github.thebusybiscuit.cscorelib2.players.MinecraftAccount.TooManyRequestsException;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionManager;
import io.github.thebusybiscuit.cscorelib2.recipes.RecipeSnapshot;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import io.github.thebusybiscuit.cscorelib2.updater.BukkitUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.GitHubBuildsUpdater;
import io.github.thebusybiscuit.cscorelib2.updater.Updater;
import io.github.thebusybiscuit.slimefun4.core.services.BlockDataService;
import io.github.thebusybiscuit.slimefun4.core.services.CustomItemDataService;
import io.github.thebusybiscuit.slimefun4.core.services.CustomTextureService;
import io.github.thebusybiscuit.slimefun4.core.services.MetricsService;
import io.github.thebusybiscuit.slimefun4.core.services.github.Contributor;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubConnector;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubService;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.PluginUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.GEO.resources.NetherIceResource;
import me.mrCookieSlime.Slimefun.GEO.resources.OilResource;
import me.mrCookieSlime.Slimefun.GEO.resources.UraniumResource;
import me.mrCookieSlime.Slimefun.GPS.GPSNetwork;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AReactor;
import me.mrCookieSlime.Slimefun.Objects.tasks.ArmorTask;
import me.mrCookieSlime.Slimefun.Setup.CSCoreLibLoader;
import me.mrCookieSlime.Slimefun.Setup.Files;
import me.mrCookieSlime.Slimefun.Setup.MiscSetup;
import me.mrCookieSlime.Slimefun.Setup.ResearchSetup;
import me.mrCookieSlime.Slimefun.Setup.SlimefunLocalization;
import me.mrCookieSlime.Slimefun.Setup.SlimefunSetup;
import me.mrCookieSlime.Slimefun.Setup.WikiSetup;
import me.mrCookieSlime.Slimefun.ancient_altar.AncientAltarListener;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunBackup;
import me.mrCookieSlime.Slimefun.api.TickerTask;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.autosave.BlockAutoSaver;
import me.mrCookieSlime.Slimefun.autosave.PlayerAutoSaver;
import me.mrCookieSlime.Slimefun.commands.SlimefunCommand;
import me.mrCookieSlime.Slimefun.commands.SlimefunTabCompleter;
import me.mrCookieSlime.Slimefun.hooks.SlimefunHooks;
import me.mrCookieSlime.Slimefun.listeners.AndroidKillingListener;
import me.mrCookieSlime.Slimefun.listeners.ArmorListener;
import me.mrCookieSlime.Slimefun.listeners.AutonomousToolsListener;
import me.mrCookieSlime.Slimefun.listeners.BackpackListener;
import me.mrCookieSlime.Slimefun.listeners.BlockListener;
import me.mrCookieSlime.Slimefun.listeners.BowListener;
import me.mrCookieSlime.Slimefun.listeners.CoolerListener;
import me.mrCookieSlime.Slimefun.listeners.DamageListener;
import me.mrCookieSlime.Slimefun.listeners.FurnaceListener;
import me.mrCookieSlime.Slimefun.listeners.GearListener;
import me.mrCookieSlime.Slimefun.listeners.GuideOnJoinListener;
import me.mrCookieSlime.Slimefun.listeners.ItemListener;
import me.mrCookieSlime.Slimefun.listeners.ItemPickupListener;
import me.mrCookieSlime.Slimefun.listeners.NetworkListener;
import me.mrCookieSlime.Slimefun.listeners.PlayerQuitListener;
import me.mrCookieSlime.Slimefun.listeners.TalismanListener;
import me.mrCookieSlime.Slimefun.listeners.TeleporterListener;
import me.mrCookieSlime.Slimefun.listeners.ToolListener;
import me.mrCookieSlime.Slimefun.listeners.WorldListener;
import me.mrCookieSlime.Slimefun.utils.Settings;
import me.mrCookieSlime.Slimefun.utils.Utilities;

public final class SlimefunPlugin extends JavaPlugin {

	public static SlimefunPlugin instance;

	private RecipeSnapshot recipeSnapshot;
	
	private final CustomItemDataService itemDataService = new CustomItemDataService(this, "slimefun_item");
	private final CustomTextureService textureService = new CustomTextureService(this);
	private final BlockDataService blockDataService = new BlockDataService(this, "slimefun_block");
	private final GitHubService gitHubService = new GitHubService("TheBusyBiscuit/Slimefun4");
	
	private TickerTask ticker;
	private SlimefunLocalization local;
	private Config researches;
	private Config items;
	private Config whitelist;
	private Config config;
	
	private GPSNetwork gps;
	private ProtectionManager protections;
	private Utilities utilities;
	private Settings settings;
	private SlimefunHooks hooks;
	
	// Supported Versions of Minecraft
	private final String[] supported = {"v1_14_", "v1_15_"};

	@Override
	public void onEnable() {
		if (new CSCoreLibLoader(this).load()) {

			String currentVersion = ReflectionUtils.getVersion();

			if (currentVersion.startsWith("v")) {
				boolean compatibleVersion = false;
				StringBuilder versions = new StringBuilder();

				int i = 0;
				for (String version: supported) {
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
			getLogger().log(Level.INFO, "Loading Files...");
			Files files = new Files();
			files.cleanup();

			getLogger().log(Level.INFO, "Loading Config...");

			// Setup config.yml
			PluginUtils utils = new PluginUtils(this);
			utils.setupConfig();
			config = utils.getConfig();
			settings = new Settings(config);

			// Loading all extra configs
			researches = new Config(files.researches);
			items = new Config(files.items);
			whitelist = new Config(files.whitelist);

			// Setup messages.yml
			local = new SlimefunLocalization(this);
			
			// Setting up other stuff
			utilities = new Utilities();
			gps = new GPSNetwork();
			
			// Setting up bStats
			new MetricsService(this);

			// Setting up the Auto-Updater
			Updater updater;
			
			if (getDescription().getVersion().equals("UNOFFICIAL")) {
				// This Server is using a modified build that is not a public release.
				getLogger().log(Level.WARNING, "It looks like you are using an unofficially modified build of Slimefun!");
				getLogger().log(Level.WARNING, "Auto-Updates have been disabled, this build is not considered safe.");
				getLogger().log(Level.WARNING, "Do not report bugs encountered in this Version of Slimefun.");
			}
			if (getDescription().getVersion().startsWith("DEV - ")) {
				// If we are using a development build, we want to switch to our custom 
				updater = new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/Slimefun4/master");
			}
			else if (getDescription().getVersion().startsWith("RC - ")) {
				// If we are using a development build, we want to switch to our custom 
				updater = new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/Slimefun4/stable", "RC - ");
			}
			else {
				// We are using an official build, use the BukkitDev Updater
				updater = new BukkitUpdater(this, getFile(), 53485);
			}

			if (updater != null && config.getBoolean("options.auto-update")) {
				updater.start();
			}

			// Creating all necessary Folders
			String[] storage = {"blocks", "stored-blocks", "stored-inventories", "stored-chunks", "universal-inventories", "waypoints", "block-backups"};
			String[] general = {"scripts", "generators", "error-reports", "cache/github"};
			for (String s : storage) createDir("data-storage/Slimefun/" + s);
			for (String s : general) createDir("plugins/Slimefun/" + s);

			getLogger().log(Level.INFO, "Loading Items...");
			MiscSetup.setupItemSettings();
			
			try {
				SlimefunSetup.setupItems();
			} catch (Exception x) {
				getLogger().log(Level.SEVERE, "An Error occured while initializing SlimefunItems for Slimefun " + Slimefun.getVersion(), x);
			}
			
			MiscSetup.loadDescriptions();

			getLogger().log(Level.INFO, "Loading Researches...");
			ResearchSetup.setupResearches();
			
			settings.researchesEnabled = getResearchCfg().getBoolean("enable-researching");
			settings.smelteryFireBreakChance = (int) Slimefun.getItemValue("SMELTERY", "chance.fireBreak");

			MiscSetup.setupMisc();
			WikiSetup.addWikiPages(this);
			textureService.setup(utilities.allItems);

			getLogger().log(Level.INFO, "Loading World Generators...");

			// Generating Oil as an OreGenResource (its a cool API)
			OreGenSystem.registerResource(new OilResource());
			OreGenSystem.registerResource(new NetherIceResource());
			OreGenSystem.registerResource(new UraniumResource());

			// Setting up GitHub Connectors...
			gitHubService.connect(config.getBoolean("options.print-out-github-data-retrieving"));

			// All Slimefun Listeners
			new ArmorListener(this);
			new ItemListener(this);
			new BlockListener(this);
			new GearListener(this);
			new AutonomousToolsListener(this);
			new DamageListener(this);
			new BowListener(this);
			new ToolListener(this);
			new FurnaceListener(this);
			new TeleporterListener(this);
			new AndroidKillingListener(this);
			new NetworkListener(this);
			new ItemPickupListener(this);

			// Toggleable Listeners for performance
			if (config.getBoolean("items.talismans")) new TalismanListener(this);
			if (config.getBoolean("items.backpacks")) new BackpackListener(this);
			if (config.getBoolean("items.coolers")) new CoolerListener(this);

			// Handle Slimefun Guide being given on Join
			if (config.getBoolean("options.give-guide-on-first-join")) new GuideOnJoinListener(this);

			// Load/Unload Worlds in Slimefun
			new WorldListener(this);

			// Clear the Slimefun Guide History upon Player Leaving
			new PlayerQuitListener(this);

			// Initiating various Stuff and all Items with a slightly delay (0ms after the Server finished loading)
			getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
				recipeSnapshot = new RecipeSnapshot(this);
				protections = new ProtectionManager(getServer());
				MiscSetup.loadItems(settings);

				for (World world: Bukkit.getWorlds()) {
					new BlockStorage(world);
				}

				if (SlimefunItem.getByID("ANCIENT_ALTAR") != null) new AncientAltarListener((SlimefunPlugin) instance);
			}, 0);
			
			SlimefunCommand command = new SlimefunCommand(this);

			getCommand("slimefun").setExecutor(command);
			getCommand("slimefun").setTabCompleter(new SlimefunTabCompleter(command));

			// Armor Update Task
			if (config.getBoolean("options.enable-armor-effects")) {
				getServer().getScheduler().runTaskTimerAsynchronously(this, new ArmorTask(), 0L, config.getInt("options.armor-update-interval") * 20L);
			}

			ticker = new TickerTask();

			getServer().getScheduler().runTaskTimer(this, new PlayerAutoSaver(), 2000L, settings.blocksAutoSaveDelay * 60L * 20L);

			// Starting all ASYNC Tasks
			getServer().getScheduler().runTaskTimerAsynchronously(this, new BlockAutoSaver(), 2000L, settings.blocksAutoSaveDelay * 60L * 20L);
			getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
				try {
					ticker.run();
				}
				catch(Exception x) {
					getLogger().log(Level.SEVERE, "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + Slimefun.getVersion(), x);
					ticker.abortTick();
				}
			}, 100L, config.getInt("URID.custom-ticker-delay"));

			getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
				gitHubService.getConnectors().forEach(GitHubConnector::pullFile);

				for (Contributor contributor : gitHubService.getContributors().values()) {
					if (!contributor.hasTexture()) {
						try {
							Optional<UUID> uuid = MinecraftAccount.getUUID(contributor.getMinecraftName());

							if (uuid.isPresent()) {
								Optional<String> skin = MinecraftAccount.getSkin(uuid.get());
								contributor.setTexture(skin);
							}
							else {
								contributor.setTexture(Optional.empty());
							}
						}
						catch(IllegalArgumentException x) {
							// There cannot be a texture found because it is not a valid MC username
							contributor.setTexture(Optional.empty());
						}
						catch(TooManyRequestsException x) {
							break;
						}
					}
				}
			}, 80L, 60 * 60 * 20L);

			// Hooray!
			getLogger().log(Level.INFO, "Finished!");
			hooks = new SlimefunHooks(this);
			
			utilities.oreWasherOutputs = new ItemStack[] {SlimefunItems.IRON_DUST, SlimefunItems.GOLD_DUST, SlimefunItems.ALUMINUM_DUST, SlimefunItems.COPPER_DUST, SlimefunItems.ZINC_DUST, SlimefunItems.TIN_DUST, SlimefunItems.LEAD_DUST, SlimefunItems.SILVER_DUST, SlimefunItems.MAGNESIUM_DUST};

			// Do not show /sf elevator command in our Log, it could get quite spammy
			CSCoreLib.getLib().filterLog("([A-Za-z0-9_]{3,16}) issued server command: /sf elevator (.{0,})");
		}
		else {
			getCommand("slimefun").setExecutor((sender, cmd, label, args) -> {
				sender.sendMessage("You have forgotten to install CS-CoreLib! Slimefun is disabled.");
				sender.sendMessage("https://dev.bukkit.org/projects/cs-corelib");
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
		
		for (World world: Bukkit.getWorlds()) {
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
		
		for (UniversalBlockMenu menu: utilities.universalInventories.values()) {
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

		for (Player p: Bukkit.getOnlinePlayers()) {
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

	public static Config getResearchCfg() {
		return instance.researches;
	}

	public static Config getItemCfg() {
		return instance.items;
	}

	public static Config getWhitelist() {
		return instance.whitelist;
	}

	@Deprecated
	public static int randomize(int max) {
		if (max < 1) return 0;
		return CSCoreLib.randomizer().nextInt(max);
	}

	@Deprecated
	public static boolean chance(int max, int percentage) {
		if (max < 1) return false;
		return CSCoreLib.randomizer().nextInt(max) <= percentage;
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
	
	public static Settings getSettings() {
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

	public static SlimefunLocalization getLocal() {
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
