package me.mrCookieSlime.Slimefun;

import io.github.starwishsama.utils.ProtectionChecker;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionManager;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkManager;
import io.github.thebusybiscuit.slimefun4.core.SlimefunRegistry;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.services.*;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubService;
import io.github.thebusybiscuit.slimefun4.core.services.metrics.MetricsService;
import io.github.thebusybiscuit.slimefun4.core.services.plugins.ThirdPartyPluginService;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.*;
import io.github.thebusybiscuit.slimefun4.implementation.resources.GEOResourcesSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.PostSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.ResearchSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;
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

/**
 * This is the main class of Slimefun.
 * This is where all the magic starts, take a look around.
 * Feel like home.
 *
 * @author TheBusyBiscuit
 */
public final class SlimefunPlugin extends JavaPlugin implements SlimefunAddon {

    public static SlimefunPlugin instance;

    private final SlimefunRegistry registry = new SlimefunRegistry();
    private MinecraftVersion minecraftVersion = MinecraftVersion.UNKNOWN;

    // Services - Systems that fulfill certain tasks, treat them as a black box
    private final CustomItemDataService itemDataService = new CustomItemDataService(this, "slimefun_item");
    private final BlockDataService blockDataService = new BlockDataService(this, "slimefun_block");
    private final CustomTextureService textureService = new CustomTextureService(this);
    private final GitHubService gitHubService = new GitHubService("TheBusyBiscuit/Slimefun4");
    private final UpdaterService updaterService = new UpdaterService();
    private final MetricsService metricsService = new MetricsService(this);
    private final AutoSavingService autoSavingService = new AutoSavingService();
    private final BackupService backupService = new BackupService();
    private final PermissionsService permissionsService = new PermissionsService(this);
    private final ThirdPartyPluginService thirdPartySupportService = new ThirdPartyPluginService(this);
    private final MinecraftRecipeService recipeService = new MinecraftRecipeService(this);
    private LocalizationService local;

    private GPSNetwork gpsNetwork;
    private NetworkManager networkManager;
    private ProtectionManager protections;

    private TickerTask ticker;
    private SlimefunCommand command;

    private Config researches;
    private Config items;
    private Config whitelist;
    private Config config;

    // Listeners that need to be accessed elsewhere
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
            getLogger().log(Level.INFO, "加载文件中...");
            createDirectories();

            getLogger().log(Level.INFO, "加载配置中...");

            // Setup config.yml
            config = new Config(this);
            registry.load(config);

            // Loading all extra configs
            researches = new Config(this, "Researches.yml");
            items = new Config(this, "Items.yml");
            whitelist = new Config(this, "whitelist.yml");

            // Setup various config files
            textureService.load();
            permissionsService.load();
            local = new LocalizationService(this, config.getString("options.language"));

            // Setting up Network classes
            networkManager = new NetworkManager(config.getInt("options.max-network-size"));

            // Setting up other stuff
            gpsNetwork = new GPSNetwork();

            // Setting up bStats
            metricsService.start();

            // 汉化版不提供自动更新服务

            // Registering all GEO Resources
            getLogger().log(Level.INFO, "加载地形资源中...");
            GEOResourcesSetup.setup();

            getLogger().log(Level.INFO, "加载物品中...");
            PostSetup.setupItemSettings();

            try {
                SlimefunItemSetup.setup(this);
            } catch (Throwable x) {
                getLogger().log(Level.SEVERE, "An Error occured while initializing SlimefunItems for Slimefun " + getVersion(), x);
            }

            getLogger().log(Level.INFO, "加载研究项目中...");

            try {
                ResearchSetup.setupResearches();
            } catch (Throwable x) {
                getLogger().log(Level.SEVERE, "An Error occured while initializing Slimefun Researches for Slimefun " + getVersion(), x);
            }

            registry.setResearchingEnabled(getResearchCfg().getBoolean("enable-researching"));

            PostSetup.setupWiki();

            // Setting up GitHub Connectors...
            gitHubService.connect(false);

            // Residence & PlotSquared Listener
            new ProtectionChecker(this);

            // All Slimefun Listeners
            new SlimefunBootsListener(this);
            new SlimefunItemListener(this);
            new SlimefunItemConsumeListener(this);
            new BlockPhysicsListener(this);
            new MultiBlockListener(this);
            new GadgetsListener(this);
            new DispenserListener(this);
            new EntityKillListener(this);
            new BlockListener(this);
            new EnhancedFurnaceListener(this);
            new ItemPickupListener(this);
            new DeathpointListener(this);
            new ExplosionsListener(this);
            new DebugFishListener(this);
            new VanillaMachinesListener(this);
            new FireworksListener(this);
            new WitherListener(this);
            new IronGolemListener(this);

            bowListener = new SlimefunBowListener(this);
            ancientAltarListener = new AncientAltarListener();
            grapplingHookListener = new GrapplingHookListener();

            // Toggleable Listeners for performance
            if (config.getBoolean("items.talismans")) {
                new TalismanListener(this);
            }

            if (config.getBoolean("items.soulbound")) {
                new SoulboundListener(this);
            }

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
                textureService.register(registry.getAllSlimefunItems());
                permissionsService.register(registry.getAllSlimefunItems());

                recipeService.load();
                protections = new ProtectionManager(getServer());

                PostSetup.loadItems();

                for (World world : Bukkit.getWorlds()) {
                    new BlockStorage(world);
                }

                if (isEnabled("ANCIENT_ALTAR")) {
                    ancientAltarListener.load(this);
                }

                if (isEnabled("GRAPPLING_HOOK")) {
                    grapplingHookListener.load(this);
                }

                if (isEnabled("BLADE_OF_VAMPIRES")) {
                    new VampireBladeListener(this);
                }

                if (isEnabled("COOLER")) {
                    new CoolerListener(this);
                }

                if (isEnabled("ELEVATOR_PLATE", "GPS_ACTIVATION_DEVICE_SHARED", "GPS_ACTIVATION_DEVICE_PERSONAL")) {
                    new TeleporterListener(this);
                }

                if (isEnabled("PROGRAMMABLE_ANDROID_BUTCHER", "PROGRAMMABLE_ANDROID_2_BUTCHER", "PROGRAMMABLE_ANDROID_3_BUTCHER")) {
                    new ButcherAndroidListener(this);
                }

                if (isEnabled("ENERGY_REGULATOR", "CARGO_MANAGER")) {
                    new NetworkListener(this);
                }

            }, 0);

            // Setting up the command /sf and all subcommands
            command = new SlimefunCommand(this);

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
                } catch (Exception x) {
                    getLogger().log(Level.SEVERE, "An Exception was caught while ticking the Block Tickers Task for Slimefun v" + getVersion(), x);
                    ticker.abortTick();
                }
            }, 100L, config.getInt("URID.custom-ticker-delay"));

            gitHubService.start(this);

            // Hooray!
            getLogger().log(Level.INFO, "完成!");
            thirdPartySupportService.start();

            // Do not show /sf elevator command in our Log, it could get quite spammy
            CSCoreLib.getLib().filterLog("([A-Za-z0-9_]{3,16}) issued server command: /sf elevator (.{0,})");
        } else {
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

    private boolean isEnabled(String... itemIds) {
        for (String id : itemIds) {
            SlimefunItem item = SlimefunItem.getByID(id);

            if (item != null && !item.isDisabled()) {
                return true;
            }
        }
        return false;
    }

    private boolean isVersionUnsupported() {
        String currentVersion = ReflectionUtils.getVersion();

        if (currentVersion.startsWith("v")) {
            for (MinecraftVersion version : MinecraftVersion.values()) {
                if (version.matches(currentVersion)) {
                    minecraftVersion = version;
                    return false;
                }
            }

            // Looks like you are using an unsupported Minecraft Version
            getLogger().log(Level.SEVERE, "### Slimefun 未能被正确加载");
            getLogger().log(Level.SEVERE, "###");
            getLogger().log(Level.SEVERE, "### Slimefun 与当前服务端版本不兼容!");
            getLogger().log(Level.SEVERE, "###");
            getLogger().log(Level.SEVERE, "### 你正在使用 Minecraft " + ReflectionUtils.getVersion());
            getLogger().log(Level.SEVERE, "### 但 Slimefun v" + getDescription().getVersion() + " 仅支持");
            getLogger().log(Level.SEVERE, "### Minecraft {0}", String.join(" / ", MinecraftVersion.getSupportedVersions()));
            getLogger().log(Level.SEVERE, "###");
            getLogger().log(Level.SEVERE, "### 请考虑使用关闭自动更新的旧版本或者升级服务端版本");
            return true;
        }

        getLogger().log(Level.WARNING, "We could not determine the version of Minecraft you were using ({0})", currentVersion);
        return false;
    }

    @Override
    public void onDisable() {
        // CS-CoreLib wasn't loaded, just disabling
        if (instance == null) {
            return;
        }

        Bukkit.getScheduler().cancelTasks(this);

        if (ticker != null) {
            // Finishes all started movements/removals of block data
            ticker.halt();
            ticker.run();
        }

        PlayerProfile.iterator().forEachRemaining(profile -> {
            if (profile.isDirty()) {
                profile.save();
            }
        });

        for (World world : Bukkit.getWorlds()) {
            try {
                BlockStorage storage = BlockStorage.getStorage(world);

                if (storage != null) {
                    storage.save(true);
                } else {
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

    private void createDirectories() {
        String[] storage = {"Players", "blocks", "stored-blocks", "stored-inventories", "stored-chunks", "universal-inventories", "waypoints", "block-backups"};
        String[] general = {"scripts", "generators", "error-reports", "cache/github"};

        for (String file : storage) {
            createDir("data-storage/Slimefun/" + file);
        }

        for (String file : general) {
            createDir("plugins/Slimefun/" + file);
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

    public static GPSNetwork getGPSNetwork() {
        return instance.gpsNetwork;
    }

    public static TickerTask getTicker() {
        return instance.ticker;
    }

    /**
     * This returns the version of Slimefun that is currently installed.
     *
     * @return The currently installed version of Slimefun
     */
    public static String getVersion() {
        return instance.getDescription().getVersion();
    }

    public static ProtectionManager getProtectionManager() {
        return instance.protections;
    }

    public static LocalizationService getLocal() {
        return instance.local;
    }

    public static MinecraftRecipeService getMinecraftRecipes() {
        return instance.recipeService;
    }

    public static CustomItemDataService getItemDataService() {
        return instance.itemDataService;
    }

    public static CustomTextureService getItemTextureService() {
        return instance.textureService;
    }

    public static PermissionsService getPermissionsService() {
        return instance.permissionsService;
    }

    public static BlockDataService getBlockDataService() {
        return instance.blockDataService;
    }

    public static ThirdPartyPluginService getThirdPartySupportService() {
        return instance.thirdPartySupportService;
    }

    /**
     * This method returns the {@link UpdaterService} of Slimefun.
     * It is used to handle automatic updates.
     *
     * @return The {@link UpdaterService} for Slimefun
     */
    public static UpdaterService getUpdater() {
        return instance.updaterService;
    }

    /**
     * This method returns the {@link GitHubService} of Slimefun.
     * It is used to retrieve data from GitHub repositories.
     *
     * @return The {@link GitHubService} for Slimefun
     */
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

    /**
     * This method returns a {@link Set} of every {@link Plugin} that lists Slimefun
     * as a required or optional dependency.
     *
     * We will just assume this to be a list of our addons.
     *
     * @return A {@link Set} of every {@link Plugin} that is dependent on Slimefun
     */
    public static Set<Plugin> getInstalledAddons() {
        return Arrays.stream(instance.getServer().getPluginManager().getPlugins()).filter(plugin -> plugin.getDescription().getDepend().contains(instance.getName()) || plugin.getDescription().getSoftDepend().contains(instance.getName())).collect(Collectors.toSet());
    }

    public static SlimefunCommand getCommand() {
        return instance.command;
    }

    public static MinecraftVersion getMinecraftVersion() {
        return instance.minecraftVersion;
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
