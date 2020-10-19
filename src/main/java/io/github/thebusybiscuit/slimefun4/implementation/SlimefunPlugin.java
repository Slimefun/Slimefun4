package io.github.thebusybiscuit.slimefun4.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.scheduler.BukkitTask;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.cscorelib2.protection.ProtectionManager;
import io.github.thebusybiscuit.cscorelib2.reflection.ReflectionUtils;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.api.exceptions.TagMisconfigurationException;
import io.github.thebusybiscuit.slimefun4.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.SlimefunRegistry;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.networks.NetworkManager;
import io.github.thebusybiscuit.slimefun4.core.services.AutoSavingService;
import io.github.thebusybiscuit.slimefun4.core.services.BackupService;
import io.github.thebusybiscuit.slimefun4.core.services.BlockDataService;
import io.github.thebusybiscuit.slimefun4.core.services.CustomItemDataService;
import io.github.thebusybiscuit.slimefun4.core.services.CustomTextureService;
import io.github.thebusybiscuit.slimefun4.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun4.core.services.MetricsService;
import io.github.thebusybiscuit.slimefun4.core.services.MinecraftRecipeService;
import io.github.thebusybiscuit.slimefun4.core.services.PerWorldSettingsService;
import io.github.thebusybiscuit.slimefun4.core.services.PermissionsService;
import io.github.thebusybiscuit.slimefun4.core.services.UpdaterService;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubService;
import io.github.thebusybiscuit.slimefun4.core.services.plugins.ThirdPartyPluginService;
import io.github.thebusybiscuit.slimefun4.core.services.profiler.SlimefunProfiler;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientAltar;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.Cooler;
import io.github.thebusybiscuit.slimefun4.implementation.items.electric.reactors.Reactor;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GrapplingHook;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.SeismicAxe;
import io.github.thebusybiscuit.slimefun4.implementation.items.weapons.VampireBlade;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.AncientAltarListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BackpackListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BeeListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BlockListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.BlockPhysicsListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.CargoNodeListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.CoolerListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.DeathpointListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.DebugFishListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.DispenserListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.ElytraCrashListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.EnhancedFurnaceListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.EntityInteractionListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.ExplosionsListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.FireworksListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.GadgetsListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.GrapplingHookListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.IronGolemListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.ItemPickupListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.MobDropListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.MultiBlockListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.PiglinListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.PlayerProfileListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SeismicAxeListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SlimefunBootsListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SlimefunBowListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SlimefunGuideListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SlimefunItemConsumeListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SlimefunItemListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.SoulboundListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.TalismanListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.VampireBladeListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.VillagerTradingListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.WitherListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.WorldListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.AnvilListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.BrewingStandListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.CartographyTableListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.CauldronListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.CraftingTableListener;
import io.github.thebusybiscuit.slimefun4.implementation.listeners.crafting.GrindstoneListener;
import io.github.thebusybiscuit.slimefun4.implementation.resources.GEOResourcesSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.PostSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.ResearchSetup;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.ArmorTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.SlimefunStartupTask;
import io.github.thebusybiscuit.slimefun4.implementation.tasks.TickerTask;
import io.github.thebusybiscuit.slimefun4.utils.tags.SlimefunTag;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

/**
 * This is the main class of Slimefun.
 * This is where all the magic starts, take a look around.
 * Feel like home.
 *
 * @author TheBusyBiscuit
 */
public final class SlimefunPlugin extends JavaPlugin implements SlimefunAddon {

    private static SlimefunPlugin instance;

    private MinecraftVersion minecraftVersion = MinecraftVersion.UNKNOWN;
    private boolean isNewlyInstalled = false;

    private final SlimefunRegistry registry = new SlimefunRegistry();
    private final TickerTask ticker = new TickerTask();
    private final SlimefunCommand command = new SlimefunCommand(this);

    // Services - Systems that fulfill certain tasks, treat them as a black box
    private final CustomItemDataService itemDataService = new CustomItemDataService(this, "slimefun_item");
    private final BlockDataService blockDataService = new BlockDataService(this, "slimefun_block");
    private final CustomTextureService textureService = new CustomTextureService(new Config(this, "item-models.yml"));
    private final GitHubService gitHubService = new GitHubService("Slimefun/Slimefun4");
    private final UpdaterService updaterService = new UpdaterService(this, getDescription().getVersion(), getFile());
    private final MetricsService metricsService = new MetricsService(this);
    private final AutoSavingService autoSavingService = new AutoSavingService();
    private final BackupService backupService = new BackupService();
    private final PermissionsService permissionsService = new PermissionsService(this);
    private final PerWorldSettingsService worldSettingsService = new PerWorldSettingsService(this);
    private final ThirdPartyPluginService thirdPartySupportService = new ThirdPartyPluginService(this);
    private final MinecraftRecipeService recipeService = new MinecraftRecipeService(this);
    private final SlimefunProfiler profiler = new SlimefunProfiler();
    private LocalizationService local;

    private GPSNetwork gpsNetwork;
    private NetworkManager networkManager;
    private ProtectionManager protections;

    // Important config files for Slimefun
    private final Config config = new Config(this);
    private final Config items = new Config(this, "Items.yml");
    private final Config researches = new Config(this, "Researches.yml");

    // Listeners that need to be accessed elsewhere
    private final GrapplingHookListener grapplingHookListener = new GrapplingHookListener();
    private final BackpackListener backpackListener = new BackpackListener();
    private final SlimefunBowListener bowListener = new SlimefunBowListener();

    public SlimefunPlugin() {
        super();
    }

    public SlimefunPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        minecraftVersion = MinecraftVersion.UNIT_TEST;
    }

    @Override
    public void onEnable() {
        instance = this;

        if (minecraftVersion == MinecraftVersion.UNIT_TEST) {
            local = new LocalizationService(this, "", null);
            gpsNetwork = new GPSNetwork();
            networkManager = new NetworkManager(200);
            command.register();
            registry.load(config);
        } else if (getServer().getPluginManager().isPluginEnabled("CS-CoreLib")) {
            getLogger().log(Level.INFO, "CS-CoreLib was detected!");
            long timestamp = System.nanoTime();
            PaperLib.suggestPaper(this);

            if (PaperLib.isPaper()) {
                getLogger().log(Level.INFO, "Paper was detected! Performance optimizations have been applied.");
            }

            // We wanna ensure that the Server uses a compatible version of Minecraft
            if (isVersionUnsupported()) {
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            // Disabling backwards-compatibility for fresh Slimefun installs
            if (!new File("data-storage/Slimefun").exists()) {
                config.setValue("options.backwards-compatibility", false);
                config.save();

                isNewlyInstalled = true;
            }

            // Creating all necessary Folders
            getLogger().log(Level.INFO, "Creating directories...");
            createDirectories();
            registry.load(config);

            // Set up localization
            getLogger().log(Level.INFO, "Loading language files...");
            local = new LocalizationService(this, config.getString("options.chat-prefix"), config.getString("options.language"));

            // Setting up Networks
            gpsNetwork = new GPSNetwork();

            int networkSize = config.getInt("networks.max-size");

            if (networkSize < 1) {
                getLogger().log(Level.WARNING, "Your 'networks.max-size' setting is misconfigured! It must be at least 1, it was set to: {0}", networkSize);
                networkSize = 1;
            }

            networkManager = new NetworkManager(networkSize);

            // Setting up bStats
            new Thread(metricsService::start, "Slimefun Metrics").start();

            // Starting the Auto-Updater
            if (config.getBoolean("options.auto-update")) {
                getLogger().log(Level.INFO, "Starting Auto-Updater...");
                updaterService.start();
            } else {
                updaterService.disable();
            }

            // Registering all GEO Resources
            getLogger().log(Level.INFO, "Loading GEO-Resources...");
            GEOResourcesSetup.setup();

            getLogger().log(Level.INFO, "Loading Tags...");
            loadTags();

            getLogger().log(Level.INFO, "Loading items...");
            loadItems();

            getLogger().log(Level.INFO, "Loading researches...");
            loadResearches();

            registry.setResearchingEnabled(getResearchCfg().getBoolean("enable-researching"));
            PostSetup.setupWiki();

            getLogger().log(Level.INFO, "Registering listeners...");
            registerListeners();

            // Initiating various Stuff and all items with a slight delay (0ms after the Server finished loading)
            runSync(new SlimefunStartupTask(this, () -> {
                protections = new ProtectionManager(getServer());
                textureService.register(registry.getAllSlimefunItems(), true);
                permissionsService.register(registry.getAllSlimefunItems(), true);

                // This try/catch should prevent buggy Spigot builds from blocking item loading
                try {
                    recipeService.refresh();
                } catch (Exception | LinkageError x) {
                    getLogger().log(Level.SEVERE, x, () -> "An Exception occurred while iterating through the Recipe list on Minecraft Version " + minecraftVersion.getName() + " (Slimefun v" + getVersion() + ")");
                }

            }), 0);

            // Setting up the command /sf and all subcommands
            command.register();

            // Armor Update Task
            if (config.getBoolean("options.enable-armor-effects")) {
                boolean radioactiveFire = config.getBoolean("options.burn-players-when-radioactive");
                getServer().getScheduler().runTaskTimerAsynchronously(this, new ArmorTask(radioactiveFire), 0L, config.getInt("options.armor-update-interval") * 20L);
            }

            autoSavingService.start(this, config.getInt("options.auto-save-delay-in-minutes"));
            ticker.start(this);

            getLogger().log(Level.INFO, "Loading Third-Party plugin integrations...");
            thirdPartySupportService.start();

            gitHubService.start(this);

            // Hooray!
            getLogger().log(Level.INFO, "Slimefun has finished loading in {0}", getStartupTime(timestamp));
        } else {
            instance = null;

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

    @Nonnull
    private String getStartupTime(long timestamp) {
        long ms = (System.nanoTime() - timestamp) / 1000000;

        if (ms > 1000) {
            return DoubleHandler.fixDouble(ms / 1000.0) + "s";
        } else {
            return DoubleHandler.fixDouble(ms) + "ms";
        }
    }

    /**
     * This method checks for the {@link MinecraftVersion} of the {@link Server}.
     * If the version is unsupported, a warning will be printed to the console.
     *
     * @return Whether the {@link MinecraftVersion} is unsupported
     */
    private boolean isVersionUnsupported() {
        String currentVersion = ReflectionUtils.getVersion();

        if (currentVersion.startsWith("v")) {
            for (MinecraftVersion version : MinecraftVersion.valuesCache) {
                if (version.matches(currentVersion)) {
                    minecraftVersion = version;
                    return false;
                }
            }

            // Looks like you are using an unsupported Minecraft Version
            getLogger().log(Level.SEVERE, "#############################################");
            getLogger().log(Level.SEVERE, "### Slimefun was not installed correctly!");
            getLogger().log(Level.SEVERE, "### You are using the wrong version of Minecraft!");
            getLogger().log(Level.SEVERE, "###");
            getLogger().log(Level.SEVERE, "### You are using Minecraft {0}", ReflectionUtils.getVersion());
            getLogger().log(Level.SEVERE, "### but Slimefun v{0} requires you to be using", getDescription().getVersion());
            getLogger().log(Level.SEVERE, "### Minecraft {0}", String.join(" / ", getSupportedVersions()));
            getLogger().log(Level.SEVERE, "#############################################");
            return true;
        }

        getLogger().log(Level.WARNING, "We could not determine the version of Minecraft you were using ({0})", currentVersion);
        return false;
    }

    @Nonnull
    private Collection<String> getSupportedVersions() {
        List<String> list = new ArrayList<>();

        for (MinecraftVersion version : MinecraftVersion.valuesCache) {
            if (version != MinecraftVersion.UNKNOWN) {
                list.add(version.getName());
            }
        }

        return list;
    }

    @Override
    public void onDisable() {
        // Slimefun never loaded successfully, so we don't even bother doing stuff here
        if (instance() == null || minecraftVersion == MinecraftVersion.UNIT_TEST) {
            return;
        }

        // Cancel all tasks from this plugin immediately
        Bukkit.getScheduler().cancelTasks(this);

        // Finishes all started movements/removals of block data
        ticker.halt();
        ticker.run();

        // Save all Player Profiles that are still in memory
        PlayerProfile.iterator().forEachRemaining(profile -> {
            if (profile.isDirty()) {
                profile.save();
            }
        });

        // Save all registered Worlds
        for (Map.Entry<String, BlockStorage> entry : getRegistry().getWorlds().entrySet()) {
            try {
                entry.getValue().saveAndRemove();
            } catch (Exception x) {
                getLogger().log(Level.SEVERE, x, () -> "An Error occurred while saving Slimefun-Blocks in World '" + entry.getKey() + "' for Slimefun " + getVersion());
            }
        }

        for (UniversalBlockMenu menu : registry.getUniversalInventories().values()) {
            menu.save();
        }

        // Create a new backup zip
        backupService.run();

        metricsService.cleanUp();

        // Prevent Memory Leaks
        // These static Maps should be removed at some point...
        AContainer.processing = null;
        AContainer.progress = null;

        AGenerator.processing = null;
        AGenerator.progress = null;

        Reactor.processing = null;
        Reactor.progress = null;

        instance = null;

        // Close all inventories on the server to prevent item dupes
        // (Incase some idiot uses /reload)
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.closeInventory();
        }
    }

    private void createDirectories() {
        String[] storageFolders = { "Players", "blocks", "stored-blocks", "stored-inventories", "stored-chunks", "universal-inventories", "waypoints", "block-backups" };
        String[] pluginFolders = { "scripts", "error-reports", "cache/github", "world-settings" };

        for (String folder : storageFolders) {
            File file = new File("data-storage/Slimefun", folder);

            if (!file.exists()) {
                file.mkdirs();
            }
        }

        for (String folder : pluginFolders) {
            File file = new File("plugins/Slimefun", folder);

            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    private void registerListeners() {
        new SlimefunBootsListener(this);
        new SlimefunItemListener(this);
        new SlimefunItemConsumeListener(this);
        new BlockPhysicsListener(this);
        new CargoNodeListener(this);
        new MultiBlockListener(this);
        new GadgetsListener(this);
        new DispenserListener(this);
        new BlockListener(this);
        new EnhancedFurnaceListener(this);
        new ItemPickupListener(this);
        new DeathpointListener(this);
        new ExplosionsListener(this);
        new DebugFishListener(this);
        new FireworksListener(this);
        new WitherListener(this);
        new IronGolemListener(this);
        new EntityInteractionListener(this);
        new MobDropListener(this);
        new VillagerTradingListener(this);
        new ElytraCrashListener(this);
        new CraftingTableListener(this);
        new AnvilListener(this);
        new BrewingStandListener(this);
        new CauldronListener(this);
        new GrindstoneListener(this);
        new CartographyTableListener(this);

        if (minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_15)) {
            new BeeListener(this);
        }

        if (minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            new PiglinListener(this);
        }

        // Item-specific Listeners
        new VampireBladeListener(this, (VampireBlade) SlimefunItems.BLADE_OF_VAMPIRES.getItem());
        new CoolerListener(this, (Cooler) SlimefunItems.COOLER.getItem());
        new SeismicAxeListener(this, (SeismicAxe) SlimefunItems.SEISMIC_AXE.getItem());
        new AncientAltarListener(this, (AncientAltar) SlimefunItems.ANCIENT_ALTAR.getItem(), (AncientPedestal) SlimefunItems.ANCIENT_PEDESTAL.getItem());
        grapplingHookListener.register(this, (GrapplingHook) SlimefunItems.GRAPPLING_HOOK.getItem());
        bowListener.register(this);
        backpackListener.register(this);

        // Toggleable Listeners for performance reasons
        if (config.getBoolean("items.talismans")) {
            new TalismanListener(this);
        }

        if (config.getBoolean("items.soulbound")) {
            new SoulboundListener(this);
        }

        // Handle Slimefun Guide being given on Join
        new SlimefunGuideListener(this, config.getBoolean("guide.receive-on-first-join"));

        // Load/Unload Worlds in Slimefun
        new WorldListener(this);

        // Clear the Slimefun Guide History upon Player Leaving
        new PlayerProfileListener(this);
    }

    private void loadTags() {
        for (SlimefunTag tag : SlimefunTag.valuesCache) {
            try {
                tag.reload();
            } catch (TagMisconfigurationException e) {
                getLogger().log(Level.SEVERE, e, () -> "Failed to load Tag: " + tag.name());
            }
        }
    }

    private void loadItems() {
        try {
            SlimefunItemSetup.setup(this);
        } catch (Exception | LinkageError x) {
            getLogger().log(Level.SEVERE, x, () -> "An Error occurred while initializing SlimefunItems for Slimefun " + getVersion());
        }
    }

    private void loadResearches() {
        try {
            ResearchSetup.setupResearches();
        } catch (Exception | LinkageError x) {
            getLogger().log(Level.SEVERE, x, () -> "An Error occurred while initializing Slimefun Researches for Slimefun " + getVersion());
        }
    }

    /**
     * This returns the global instance of {@link SlimefunPlugin}.
     * This may return null if the {@link Plugin} was disabled.
     * 
     * @return The {@link SlimefunPlugin} instance
     */
    @Nullable
    public static SlimefunPlugin instance() {
        return instance;
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

    public static TickerTask getTickerTask() {
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

    /**
     * This returns the {@link LocalizationService} of Slimefun.
     *
     * @return The {@link LocalizationService} of Slimefun
     */
    public static LocalizationService getLocalization() {
        return instance.local;
    }

    public static ProtectionManager getProtectionManager() {
        return instance.protections;
    }

    public static MinecraftRecipeService getMinecraftRecipeService() {
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
     * This method returns the {@link MetricsService} of Slimefun.
     * It is used to handle sending metric information to bStats.
     *
     * @return The {@link MetricsService} for Slimefun
     */
    public static MetricsService getMetricsService() {
        return instance.metricsService;
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
     * <p>
     * We will just assume this to be a list of our addons.
     *
     * @return A {@link Set} of every {@link Plugin} that is dependent on Slimefun
     */
    @Nonnull
    public static Set<Plugin> getInstalledAddons() {
        return Arrays.stream(instance.getServer().getPluginManager().getPlugins()).filter(plugin -> plugin.getDescription().getDepend().contains(instance.getName()) || plugin.getDescription().getSoftDepend().contains(instance.getName())).collect(Collectors.toSet());
    }

    /**
     * The {@link Command} that was added by Slimefun.
     *
     * @return Slimefun's command
     */
    public static SlimefunCommand getCommand() {
        return instance.command;
    }

    /**
     * This returns our instance of the {@link SlimefunProfiler}, a tool that is used
     * to analyse performance and lag.
     * 
     * @return The {@link SlimefunProfiler}
     */
    public static SlimefunProfiler getProfiler() {
        return instance.profiler;
    }

    /**
     * This returns the currently installed version of Minecraft.
     *
     * @return The current version of Minecraft
     */
    public static MinecraftVersion getMinecraftVersion() {
        return instance.minecraftVersion;
    }

    /**
     * This method returns whether this version of Slimefun was newly installed.
     * It will return true if this {@link Server} uses Slimefun for the very first time.
     * 
     * @return Whether this is a new installation of Slimefun
     */
    public static boolean isNewlyInstalled() {
        return instance.isNewlyInstalled;
    }

    public static String getCSCoreLibVersion() {
        return CSCoreLib.getLib().getDescription().getVersion();
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Slimefun/Slimefun4/issues";
    }

    /**
     * This method schedules a delayed synchronous task for Slimefun.
     * <strong>For Slimefun only, not for addons.</strong>
     * 
     * This method should only be invoked by Slimefun itself.
     * Addons must schedule their own tasks using their own {@link Plugin} instance.
     * 
     * @param runnable
     *            The {@link Runnable} to run
     * @param delay
     *            The delay for this task
     * 
     * @return The resulting {@link BukkitTask} or null if Slimefun was disabled
     */
    @Nullable
    public static BukkitTask runSync(@Nonnull Runnable runnable, long delay) {
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        if (getMinecraftVersion() == MinecraftVersion.UNIT_TEST) {
            runnable.run();
            return null;
        }

        if (instance == null || !instance.isEnabled()) {
            return null;
        }

        return instance.getServer().getScheduler().runTaskLater(instance, runnable, delay);
    }

    /**
     * This method schedules a synchronous task for Slimefun.
     * <strong>For Slimefun only, not for addons.</strong>
     * 
     * This method should only be invoked by Slimefun itself.
     * Addons must schedule their own tasks using their own {@link Plugin} instance.
     * 
     * @param runnable
     *            The {@link Runnable} to run
     * 
     * @return The resulting {@link BukkitTask} or null if Slimefun was disabled
     */
    @Nullable
    public static BukkitTask runSync(@Nonnull Runnable runnable) {
        Validate.notNull(runnable, "Cannot run null");

        if (getMinecraftVersion() == MinecraftVersion.UNIT_TEST) {
            runnable.run();
            return null;
        }

        if (instance == null || !instance.isEnabled()) {
            return null;
        }

        return instance.getServer().getScheduler().runTask(instance, runnable);
    }

}
