package me.mrCookieSlime.Slimefun;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionManager;
import io.github.thebusybiscuit.cscorelib2.recipes.RecipeSnapshot;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkManager;
import io.github.thebusybiscuit.slimefun4.core.SlimefunRegistry;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.hooks.SlimefunHooks;
import io.github.thebusybiscuit.slimefun4.core.services.*;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubService;
import io.github.thebusybiscuit.slimefun4.core.services.metrics.MetricsService;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.*;
import io.github.thebusybiscuit.slimefun4.implementation.resources.GEOResourcesSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.MiscSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.ResearchSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.WikiSetup;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.ArmorTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.TickerTask;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AReactor;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import me.mrCookieSlime.Slimefun.utils.ConfigCache;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public final class SlimefunPlugin extends JavaPlugin implements SlimefunAddon {

	public static SlimefunPlugin instance;

	private RecipeSnapshot recipeSnapshot;
	
	private SlimefunRegistry registry;
	
	private final CustomItemDataService itemDataService = new CustomItemDataService(this, "slimefun_item");
	private final CustomTextureService textureService = new CustomTextureService(this);
	private final BlockDataService blockDataService = new BlockDataService(this, "slimefun_block");
	private final GitHubService gitHubService = new GitHubService("TheBusyBiscuit/Slimefun4");
	private final UpdaterService updaterService = new UpdaterService(this, getFile());
    private final AutoSavingService autoSavingService = new AutoSavingService();
    private final BackupService backupService = new BackupService();

	private TickerTask ticker;
	private LocalizationService local;
	private NetworkManager networkManager;
	private Config researches;
	private Config items;
	private Config whitelist;
	private Config config;

	private GPSNetwork gps;
	private ProtectionManager protections;
	private ConfigCache settings;
	private SlimefunHooks hooks;

	// Supported Versions of Minecraft
	private final String[] supported = {"v1_14_", "v1_15_"};
	
	private AncientAltarListener ancientAltarListener;
	private BackpackListener backpackListener;
    private GrapplingHookListener grapplingHookListener;
	private SlimefunBowListener bowListener;

	@Override
	public void onEnable() {
		if (getServer().getPluginManager().isPluginEnabled("CS-CoreLib")) {
			
			if (isVersionUnsupported()) {
				getServer().getPluginManager().disablePlugin(this);
				return;
			}
			
			instance = this;

			// Creating all necessary Folders
			getLogger().log(Level.INFO, "Loading Files...");
			String[] storage = {"Players", "blocks", "stored-blocks", "stored-inventories", "stored-chunks", "universal-inventories", "waypoints", "block-backups"};
			String[] general = {"scripts", "generators", "error-reports", "cache/github"};
			for (String file : storage) createDir("data-storage/Slimefun/" + file);
			for (String file : general) createDir("plugins/Slimefun/" + file);

			getLogger().log(Level.INFO, "正在加载配置...");

			// Setup config.yml
			config = new Config(this);
            registry = new SlimefunRegistry();
			settings = new ConfigCache(config);

			// Loading all extra configs
			researches = new Config(this, "Researches.yml");
			items = new Config(this, "Items.yml");
			whitelist = new Config(this, "whitelist.yml");

			// Setup messages.yml
			local = new LocalizationService(this, config.getString("options.language"));

			// Setting up Network classes
			networkManager = new NetworkManager(config.getInt("options.max-network-size"));
			
			// Setting up other stuff
			gps = new GPSNetwork();

			// Setting up bStats
			new MetricsService(this);

			// Starting the Auto-Updater
			if (config.getBoolean("options.auto-update")) {
				updaterService.start();
			}

            getLogger().log(Level.INFO, "正在加载世界生成器...");

            // Generating Oil as an OreGenResource (it iss a cool API)
            GEOResourcesSetup.setup();

            getLogger().log(Level.INFO, "正在加载物品...");
            MiscSetup.setupItemSettings();

            try {
                SlimefunItemSetup.setup(this);
            } catch (Throwable x) {
                getLogger().log(Level.SEVERE, "An Error occured while initializing SlimefunItems for Slimefun " + getVersion(), x);
            }

            getLogger().log(Level.INFO, "正在加载研究项目...");

            try {
                ResearchSetup.setupResearches();
            } catch (Throwable x) {
                getLogger().log(Level.SEVERE, "An Error occured while initializing Slimefun Researches for Slimefun " + getVersion(), x);
            }

            settings.researchesEnabled = getResearchCfg().getBoolean("enable-researching");

            MiscSetup.setupMisc();
            WikiSetup.addWikiPages(this);
            textureService.setup(registry.getAllSlimefunItems());

            // Setting up GitHub Connectors...
            gitHubService.connect(config.getBoolean("options.print-out-github-data-retrieving"));

            // All Slimefun Listeners
            new SlimefunBootsListener(this);
            new SlimefunItemListener(this);
            new BlockPhysicsListener(this);
            new MultiBlockListener(this);
            new GearListener(this);
            new DispenserListener(this);
            new EntityKillListener(this);
            new BlockListener(this);
            new EnhancedFurnaceListener(this);
            new TeleporterListener(this);
            new AndroidKillingListener(this);
            new NetworkListener(this);
            new ItemPickupListener(this);
            new DeathpointListener(this);
            new ExplosionsListener(this);
            new DebugFishListener(this);
            new VanillaMachinesListener(this);

			bowListener = new SlimefunBowListener(this);
			ancientAltarListener = new AncientAltarListener();
            grapplingHookListener = new GrapplingHookListener();

			// Toggleable Listeners for performance
			if (config.getBoolean("items.talismans")) new TalismanListener(this);
			if (config.getBoolean("items.soulbound")) new SoulboundListener(this);

			if (config.getBoolean("items.backpacks")) {
				backpackListener = new BackpackListener(this);
			}

			// Handle Slimefun Guide being given on Join
			new SlimefunGuideListener(this, config.getBoolean("options.give-guide-on-first-join"));

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

				if (SlimefunItem.getByID("ANCIENT_ALTAR") != null) {
					ancientAltarListener.load(this);
				}

                if (SlimefunItem.getByID("GRAPPLING_HOOK") != null) {
                    grapplingHookListener.load(this);
                }

				if (SlimefunItem.getByID("IGNITION_CHAMBER") != null) {
				    new IgnitionChamberListener(this);
                }

                if (SlimefunItem.getByID("BLADE_OF_VAMPIRES") != null) {
                    new VampireBladeListener(this);
                }
                if (SlimefunItem.getByID("COOLER") != null) {
                    new CoolerListener(this);
                }
            }, 0);

            // Setting up the command /sf and all subcommands
            new SlimefunCommand(this);

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
				catch (Exception x) {
					getLogger().log(Level.SEVERE, "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + getVersion(), x);
					ticker.abortTick();
				}
			}, 100L, config.getInt("URID.custom-ticker-delay"));

			gitHubService.start(this);

			// Hooray!
			getLogger().log(Level.INFO, "加载完成!");
			hooks = new SlimefunHooks(this);


			// Do not show /sf elevator command in our Log, it could get quite spammy
			CSCoreLib.getLib().filterLog("([A-Za-z0-9_]{3,16}) issued server command: /sf elevator (.{0,})");
		}
		else {
			getLogger().log(Level.INFO, "#################### - INFO - ####################");
			getLogger().log(Level.INFO, " ");
			getLogger().log(Level.INFO, "Slimefun 未能被加载.");
			getLogger().log(Level.INFO, "请下载并安装前置 CS-CoreLib.");
			getLogger().log(Level.INFO, "https://thebusybiscuit.github.io/builds/TheBusyBiscuit/CS-CoreLib/master/");

			getCommand("slimefun").setExecutor((sender, cmd, label, args) -> {
				sender.sendMessage("你忘记安装 CS-CoreLib 了! Slimefun 已被禁用.");
				sender.sendMessage("https://thebusybiscuit.github.io/builds/TheBusyBiscuit/CS-CoreLib/master/");
				return true;
			});
		}
	}

	private boolean isVersionUnsupported() {
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
				getLogger().log(Level.SEVERE, "### Slimefun 未能被正确加载");
				getLogger().log(Level.SEVERE, "###");
				getLogger().log(Level.SEVERE, "### Slimefun 与当前服务端版本不兼容!");
				getLogger().log(Level.SEVERE, "###");
				getLogger().log(Level.SEVERE, "### 你正在使用 Minecraft " + ReflectionUtils.getVersion());
				getLogger().log(Level.SEVERE, "### 但 Slimefun v" + getDescription().getVersion() + " 仅支持");
				getLogger().log(Level.SEVERE, "### Minecraft {0}", versions);
				getLogger().log(Level.SEVERE, "###");
				getLogger().log(Level.SEVERE, "### 请考虑使用关闭自动更新的旧版本或者升级服务端版本");
				return true;
			}
		}
		
		return false;
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
				getLogger().log(Level.SEVERE, "An Error occured while saving Slimefun-Blocks in World '" + world.getName() + "' for Slimefun " + getVersion(), x);
			}
		}

		for (UniversalBlockMenu menu : registry.getUniversalInventories().values()) {
			menu.save();
		}

        backupService.run();

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

	public static GPSNetwork getGPSNetwork() {
		return instance.gps;
	}

	public static SlimefunHooks getHooks() {
		return instance.hooks;
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
	
	public static String getVersion() {
		return instance.getDescription().getVersion();
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

    public static UpdaterService getUpdater() {
        return instance.updaterService;
    }

	public static GitHubService getGitHubService() {
		return instance.gitHubService;
	}
	
	public static SlimefunRegistry getRegistry() {
		return instance.registry;
	}
	
	public static NetworkManager getNetworkManager() {
		return instance.networkManager;
	}
	
	public static AncientAltarListener getAncientAltarListener() {
		return instance.ancientAltarListener;
	}

    public static GrapplingHookListener getGrapplingHookListener() {
        return instance.grapplingHookListener;
    }
	
	public static BackpackListener getBackpackListener() {
		return instance.backpackListener;
	}
	
	public static SlimefunBowListener getBowListener() {
		return instance.bowListener;
	}

	public static Set<Plugin> getInstalledAddons() {
		return Arrays.stream(instance.getServer().getPluginManager().getPlugins())
				.filter(plugin -> plugin.getDescription().getDepend().contains(instance.getName()) || plugin.getDescription().getSoftDepend().contains(instance.getName()))
				.collect(Collectors.toSet());
	}

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/Slimefun4/issues";
    }
}
