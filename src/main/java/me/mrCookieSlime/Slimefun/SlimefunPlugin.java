package me.mrCookieSlime.Slimefun;

import io.github.starwishsama.extra.ProtectionChecker;
import io.github.starwishsama.extra.UpdateChecker;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionManager;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun4.api.network.NetworkManager;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
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
import io.github.thebusybiscuit.slimefun4.implementation.tasks.SlimefunStartupTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.TickerTask;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AReactor;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
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
    private MinecraftVersion minecraftVersion = MinecraftVersion.UNKNOWN;

    private final SlimefunRegistry registry = new SlimefunRegistry();
    private final TickerTask ticker = new TickerTask();
    private final SlimefunCommand command = new SlimefunCommand(this);

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
    private final PerWorldSettingsService worldSettingsService = new PerWorldSettingsService(this);
    private final ThirdPartyPluginService thirdPartySupportService = new ThirdPartyPluginService(this);
    private final MinecraftRecipeService recipeService = new MinecraftRecipeService(this);
    private LocalizationService local;

    private GPSNetwork gpsNetwork;
    private NetworkManager networkManager;
    private ProtectionManager protections;

    // Important config files for Slimefun
    private final Config config = new Config(this);
    private final Config items = new Config(this, "Items.yml");
    private final Config researches = new Config(this, "Researches.yml");

    // Listeners that need to be accessed elsewhere
    private final AncientAltarListener ancientAltarListener = new AncientAltarListener();
    private final GrapplingHookListener grapplingHookListener = new GrapplingHookListener();
    private final BackpackListener backpackListener = new BackpackListener();
    private final SlimefunBowListener bowListener = new SlimefunBowListener();

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().isPluginEnabled("CS-CoreLib")) {
            long timestamp = System.nanoTime();

            if (isVersionUnsupported()) {
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            instance = this;

            // Creating all necessary Folders
            getLogger().log(Level.INFO, "加载中...");
            createDirectories();
            registry.load(config);

            local = new LocalizationService(this, config.getString("options.language"));

            // Setting up Network
            gpsNetwork = new GPSNetwork();
            networkManager = new NetworkManager(config.getInt("options.max-network-size"));

            // Setting up bStats
            metricsService.start();

            // 汉化版不提供自动更新服务

            // Registering all GEO Resources
            getLogger().log(Level.INFO, "加载地形资源中...");
            GEOResourcesSetup.setup();

            getLogger().log(Level.INFO, "加载物品中...");
            loadItems();

            getLogger().log(Level.INFO, "加载研究项目中...");
            loadResearches();

            registry.setResearchingEnabled(getResearchCfg().getBoolean("enable-researching"));

            PostSetup.setupWiki();

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

            bowListener.register(this);

            // Toggleable Listeners for performance
            if (config.getBoolean("items.talismans")) {
                new TalismanListener(this);
            }

            if (config.getBoolean("items.soulbound")) {
                new SoulboundListener(this);
            }

            if (config.getBoolean("items.backpacks")) {
                backpackListener.register(this);
            }

            // Handle Slimefun Guide being given on Join
            new SlimefunGuideListener(this, config.getBoolean("options.give-guide-on-first-join"));

            // Load/Unload Worlds in Slimefun
            new WorldListener(this);

            // Clear the Slimefun Guide History upon Player Leaving
            new PlayerProfileListener(this);

            // Initiating various Stuff and all Items with a slightly delay (0ms after the Server finished loading)
            Slimefun.runSync(new SlimefunStartupTask(this, () -> {
                protections = new ProtectionManager(getServer());
                textureService.register(registry.getAllSlimefunItems());
                permissionsService.register(registry.getAllSlimefunItems());
                recipeService.refresh();
            }), 0);

            // Setting up the command /sf and all subcommands
            command.register();

            // Armor Update Task
            if (config.getBoolean("options.enable-armor-effects")) {
                getServer().getScheduler().runTaskTimerAsynchronously(this, new ArmorTask(), 0L, config.getInt("options.armor-update-interval") * 20L);
            }

            autoSavingService.start(this, config.getInt("options.auto-save-delay-in-minutes"));
            ticker.start(this);
            thirdPartySupportService.start();
            gitHubService.start(this);

            // Hooray!
            getLogger().log(Level.INFO, "Slimefun 加载完成, 耗时 {0}", getStartupTime(timestamp));

            if (config.getBoolean("options.update-check")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        getLogger().log(Level.INFO, UpdateChecker.getUpdateInfo());
                    }
                }.runTaskAsynchronously(this);
            }

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

    private String getStartupTime(long timestamp) {
        long ms = (System.nanoTime() - timestamp) / 1000000;

        if (ms > 1000) {
            return DoubleHandler.fixDouble(ms / 1000) + "s";
        } else {
            return DoubleHandler.fixDouble(ms) + "ms";
        }
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
            getLogger().log(Level.SEVERE, "### Minecraft {0}", String.join(" / ", getSupportedVersions()));
            getLogger().log(Level.SEVERE, "###");
            getLogger().log(Level.SEVERE, "### 请考虑使用关闭自动更新的旧版本或者升级服务端版本");
            return true;
        }

        getLogger().log(Level.WARNING, "我们无法识别出你在用的服务端版本 :( ({0})", currentVersion);
        return false;
    }

    private Collection<String> getSupportedVersions() {
        List<String> list = new ArrayList<>();

        for (MinecraftVersion version : MinecraftVersion.values()) {
            if (version != MinecraftVersion.UNKNOWN) {
                list.add(version.getName());
            }
        }

        return list;
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
                    getLogger().log(Level.SEVERE, "Could not save Slimefun Blocks for World \"{0}\"", world.getName());
                }
            } catch (Exception x) {
                getLogger().log(Level.SEVERE, x, () -> "An Error occured while saving Slimefun-Blocks in World '" + world.getName() + "' for Slimefun " + getVersion());
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
        String[] storageFolders = {"Players", "blocks", "stored-blocks", "stored-inventories", "stored-chunks", "universal-inventories", "waypoints", "block-backups"};
        String[] pluginFolders = {"scripts", "generators", "error-reports", "cache/github", "world-settings"};

        for (String folder : storageFolders) {
            File file = new File("data-storage/Slimefun", folder);
            if (!file.exists()) file.mkdirs();
        }

        for (String folder : pluginFolders) {
            File file = new File("plugins/Slimefun", folder);
            if (!file.exists()) file.mkdirs();
        }
    }

    private void loadItems() {
        try {
            SlimefunItemSetup.setup(this);
        } catch (Throwable x) {
            getLogger().log(Level.SEVERE, x, () -> "An Error occured while initializing SlimefunItems for Slimefun " + getVersion());
        }
    }

    private void loadResearches() {
        try {
            ResearchSetup.setupResearches();
        } catch (Throwable x) {
            getLogger().log(Level.SEVERE, x, () -> "An Error occured while initializing Slimefun Researches for Slimefun " + getVersion());
        }
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

    public static PerWorldSettingsService getWorldSettingsService() {
        return instance.worldSettingsService;
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

    public static String getCSCoreLibVersion() {
        return CSCoreLib.getLib().getDescription().getVersion();
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/Slimefun4/issues";
    }

}
