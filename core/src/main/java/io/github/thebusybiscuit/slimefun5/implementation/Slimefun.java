package io.github.thebusybiscuit.slimefun5.implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.thebusybiscuit.slimefun5.storage.Storage;
import io.github.thebusybiscuit.slimefun5.storage.backend.BlockStorageBackend;
import io.github.thebusybiscuit.slimefun5.storage.backend.jdbc.JdbcBackend;
import io.github.thebusybiscuit.slimefun5.storage.backend.jdbc.MySqlDialect;
import io.github.thebusybiscuit.slimefun5.storage.backend.jdbc.MySqlProvider;
import io.github.thebusybiscuit.slimefun5.storage.backend.jdbc.StorageBackendConfig;
import io.github.thebusybiscuit.slimefun5.storage.backend.legacy.LegacyFileBackend;
import io.github.thebusybiscuit.slimefun5.storage.backend.legacy.LegacyStorage;
import io.github.thebusybiscuit.slimefun5.storage.backend.migration.MigrationService;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import io.github.bakedlibs.dough.config.Config;
import io.github.bakedlibs.dough.protection.ProtectionManager;
import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.api.exceptions.TagMisconfigurationException;
import io.github.thebusybiscuit.slimefun5.api.geo.GEOResource;
import io.github.thebusybiscuit.slimefun5.api.gps.GPSNetwork;
import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.core.SlimefunRegistry;
import io.github.thebusybiscuit.slimefun5.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun5.core.multiblocks.MultiBlockOwnership;
import io.github.thebusybiscuit.slimefun5.core.networks.NetworkManager;
import io.github.thebusybiscuit.slimefun5.core.services.AnalyticsService;
import io.github.thebusybiscuit.slimefun5.core.services.AutoSavingService;
import io.github.thebusybiscuit.slimefun5.core.services.BackupService;
import io.github.thebusybiscuit.slimefun5.core.services.BlockDataService;
import io.github.thebusybiscuit.slimefun5.core.services.CustomItemDataService;
import io.github.thebusybiscuit.slimefun5.core.services.CustomTextureService;
import io.github.thebusybiscuit.slimefun5.core.services.LocalizationService;
import io.github.thebusybiscuit.slimefun5.core.services.localization.GuideBookDisplay;
import io.github.thebusybiscuit.slimefun5.core.services.localization.EnchantTranslationService;
import io.github.thebusybiscuit.slimefun5.core.guide.categories.DefaultGuideCategories;
import io.github.thebusybiscuit.slimefun5.core.guide.categories.GuideCategoryRegistry;
import io.github.thebusybiscuit.slimefun5.core.services.localization.ItemTranslationService;
import io.github.thebusybiscuit.slimefun5.core.services.localization.MenuTranslationService;
import io.github.thebusybiscuit.slimefun5.core.services.localization.PacketTranslationService;
import io.github.thebusybiscuit.slimefun5.core.services.localization.TranslationCoverageService;
import io.github.thebusybiscuit.slimefun5.core.services.MetricsService;
import io.github.thebusybiscuit.slimefun5.core.services.MinecraftRecipeService;
import io.github.thebusybiscuit.slimefun5.core.services.PerWorldSettingsService;
import io.github.thebusybiscuit.slimefun5.core.services.PermissionsService;
import io.github.thebusybiscuit.slimefun5.core.services.ThreadService;
import io.github.thebusybiscuit.slimefun5.core.services.UpdaterService;
import io.github.thebusybiscuit.slimefun5.core.services.github.GitHubService;
import io.github.thebusybiscuit.slimefun5.core.services.holograms.HologramsService;
import io.github.thebusybiscuit.slimefun5.core.services.profiler.SlimefunProfiler;
import io.github.thebusybiscuit.slimefun5.core.services.sounds.SoundService;
import io.github.thebusybiscuit.slimefun5.implementation.items.altar.AncientAltar;
import io.github.thebusybiscuit.slimefun5.implementation.items.altar.AncientPedestal;
import io.github.thebusybiscuit.slimefun5.implementation.items.backpacks.Cooler;
import io.github.thebusybiscuit.slimefun5.implementation.items.magical.BeeWings;
import io.github.thebusybiscuit.slimefun5.implementation.items.tools.GrapplingHook;
import io.github.thebusybiscuit.slimefun5.implementation.items.weapons.SeismicAxe;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.AncientAltarListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.AutoCrafterListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.BackpackListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.BeeWingsListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.ArmorEquipListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.BlockListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.BlockPhysicsListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.EnderArmorListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.ButcherAndroidListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.CargoNodeListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.CoolerListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.DeathpointListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.DebugFishListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.DispenserListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.ElytraImpactListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.EnhancedFurnaceListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.ExplosionsListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.GadgetsListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.GrapplingHookListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.HopperListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.ItemDropListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.ItemPickupListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.JoinListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.MiddleClickListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.MiningAndroidListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.MultiBlockListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.MultiBlockRedstoneListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.NetworkListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.PlayerProfileListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.RadioactivityListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.SeismicAxeListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.SlimefunBootsListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.SlimefunBowListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.SlimefunGuideListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.SlimefunItemConsumeListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.SlimefunItemHitListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.SlimefunItemInteractListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.SoulboundListener;
import io.github.thebusybiscuit.slimefun5.core.guide.installer.AddonInstallerMenu;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiPage;
import io.github.thebusybiscuit.slimefun5.core.guide.wiki.WikiText;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.TalismanBlockDropListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.TalismanListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.VillagerTradingListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.crafting.AnvilListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.crafting.BrewingStandListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.crafting.CartographyTableListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.crafting.CauldronListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.crafting.CraftingTableListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.crafting.GrindstoneListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.crafting.SmithingTableListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.entity.BeeListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.entity.EntityInteractionListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.entity.FireworksListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.entity.IronGolemListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.entity.MobDropListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.entity.PiglinListener;
import io.github.thebusybiscuit.slimefun5.implementation.listeners.entity.WitherListener;
import io.github.thebusybiscuit.slimefun5.implementation.resources.GEOResourcesSetup;
import io.github.thebusybiscuit.slimefun5.implementation.setup.PostSetup;
import io.github.thebusybiscuit.slimefun5.implementation.setup.ResearchSetup;
import io.github.thebusybiscuit.slimefun5.implementation.setup.SlimefunItemSetup;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.SlimefunStartupTask;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.TickerTask;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.armor.RadiationTask;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.armor.RainbowArmorTask;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.armor.SlimefunArmorTask;
import io.github.thebusybiscuit.slimefun5.implementation.tasks.armor.SolarHelmetTask;
import io.github.thebusybiscuit.slimefun5.integrations.IntegrationsManager;
import io.github.thebusybiscuit.slimefun5.utils.NumberUtils;
import io.github.thebusybiscuit.slimefun5.utils.tags.SlimefunTag;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.MenuListener;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.UniversalBlockMenu;

/**
 * This is the main class of Slimefun.
 * This is where all the magic starts, take a look around.
 *
 * @author TheBusyBiscuit
 */
public class Slimefun extends JavaPlugin implements SlimefunAddon {

    /**
     * This is the Java version we recommend server owners to use.
     * This does not necessarily mean that it's the minimum version
     * required to run Slimefun.
     */
    private static final int RECOMMENDED_JAVA_VERSION = 17;

    /**
     * Our static instance of {@link Slimefun}.
     * Make sure to clean this up in {@link #onDisable()}!
     */
    private static Slimefun instance;

    /**
     * Keep track of which {@link MinecraftVersion} we are on.
     */
    private MinecraftVersion minecraftVersion = MinecraftVersion.UNKNOWN;

    /**
     * Keep track of whether this is a fresh install or a regular boot up.
     */
    private boolean isNewlyInstalled = false;

    // Various things we need
    private final SlimefunRegistry registry = new SlimefunRegistry();
    private final WikiText wikiText = new WikiText();
    private final SlimefunCommand command = new SlimefunCommand(this);
    private final TickerTask ticker = new TickerTask();

    // Services - Systems that fulfill certain tasks, treat them as a black box
    private final CustomItemDataService itemDataService = new CustomItemDataService(this, "slimefun_item");
    private final BlockDataService blockDataService = new BlockDataService(this, "slimefun_block");
    private final CustomTextureService textureService = new CustomTextureService(new Config(this, "item-models.yml"));
    private final GitHubService gitHubService = new GitHubService("Slimefun5/Slimefun5");
    private final UpdaterService updaterService = new UpdaterService(this, getDescription().getVersion(), getFile());
    private final MetricsService metricsService = new MetricsService(this);
    private final AutoSavingService autoSavingService = new AutoSavingService();
    private final BackupService backupService = new BackupService();
    private final PermissionsService permissionsService = new PermissionsService(this);
    private final PerWorldSettingsService worldSettingsService = new PerWorldSettingsService(this);
    private final MinecraftRecipeService recipeService = new MinecraftRecipeService(this);
    private final HologramsService hologramsService = new HologramsService(this);
    private final SoundService soundService = new SoundService(this);
    private final ThreadService threadService = new ThreadService(this);
    private final AnalyticsService analyticsService = new AnalyticsService(this);
    private final ItemTranslationService itemTranslationService = new ItemTranslationService();
    private final GuideCategoryRegistry guideCategoryRegistry = new GuideCategoryRegistry();
    private final EnchantTranslationService enchantTranslationService = new EnchantTranslationService();
    private final MenuTranslationService menuTranslationService = new MenuTranslationService();
    private final TranslationCoverageService translationCoverageService = new TranslationCoverageService();
    private final GuideBookDisplay guideBookDisplay = new GuideBookDisplay();
    private PacketTranslationService packetTranslationService;

    // Some other things we need
    private final IntegrationsManager integrations = new IntegrationsManager(this);
    private final SlimefunProfiler profiler = new SlimefunProfiler();
    private final GPSNetwork gpsNetwork = new GPSNetwork(this);

    // Even more things we need
    private NetworkManager networkManager;
    private LocalizationService local;
    private MultiBlockOwnership multiBlockOwnership;

    // Important config files for Slimefun
    private final Config config = new Config(this);
    private final Config items = new Config(this, "Items.yml");
    private final Config researches = new Config(this, "Researches.yml");

    // Data storage
    private Storage playerStorage;
    private BlockStorageBackend blockStorageBackend;
    private long bootTimestamp;
    private MigrationService storageMigration;

    // Listeners that need to be accessed elsewhere
    private final GrapplingHookListener grapplingHookListener = new GrapplingHookListener();
    private final BackpackListener backpackListener = new BackpackListener();
    private final SlimefunBowListener bowListener = new SlimefunBowListener();

    /**
     * This constructor is invoked by Bukkit and within unit tests.
     * Therefore we need to figure out if we're within unit tests or not.
     */
    public Slimefun() {
        super();

        // Check that we got loaded by MockBukkit rather than Bukkit's loader
        // TODO: This is very much a hack and we can hopefully move to a more native way in the future
        String classLoaderPackage = getClassLoader().getClass().getPackage().getName();
        if (classLoaderPackage.startsWith("be.seeseemelk.mockbukkit") || classLoaderPackage.startsWith("org.mockbukkit.mockbukkit")) {
            minecraftVersion = MinecraftVersion.UNIT_TEST;
        }
    }

    /**
     * This is called when the {@link Plugin} has been loaded and enabled on a {@link Server}.
     */
    @Override
    public void onEnable() {
        setInstance(this);

        if (isUnitTest()) {
            // We handle Unit Tests seperately.
            onUnitTestStart();
        } else if (isVersionUnsupported()) {
            // We wanna ensure that the Server uses a compatible version of Minecraft.
            getServer().getPluginManager().disablePlugin(this);
        } else {
            // The Environment has been validated.
            onPluginStart();
        }
    }

    /**
     * This is our start method for a Unit Test environment.
     */
    private void onUnitTestStart() {
        local = new LocalizationService(this, "", null);
        networkManager = new NetworkManager(200);
        command.register();
        registry.load(this, config);
        loadTags();
        soundService.reload(false);
        // TODO: What do we do if tests want to use another storage backend (e.g. testing new feature on legacy + sql)?
        // Do we have a way to override this?
        playerStorage = new LegacyStorage();

        // Unit tests always use flat-file storage regardless of config (the H2-default inversion
        // must not create a DB during tests).
        blockStorageBackend = new LegacyFileBackend();
    }

    /**
     * This is our start method for a correct Slimefun installation.
     */
    private void onPluginStart() {
        bootTimestamp = System.currentTimeMillis();
        long timestamp = System.nanoTime();
        Logger logger = getLogger();

        // Check if Paper (<3) is installed
        if (PaperLib.isPaper()) {
            logger.log(Level.INFO, "Paper was detected! Performance optimizations have been applied.");
        } else {
            PaperLib.suggestPaper(this);
        }

        // Check if CS-CoreLib is installed (it is no longer needed)
        if (getServer().getPluginManager().getPlugin("CS-CoreLib") != null) {
            StartupWarnings.discourageCSCoreLib(logger);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Encourage newer Java version
        if (NumberUtils.getJavaVersion() < RECOMMENDED_JAVA_VERSION) {
            StartupWarnings.oldJavaVersion(logger, RECOMMENDED_JAVA_VERSION);
        }

        // Legacy Minecraft versions (1.8 - 1.15) are supported by this fork but only lightly tested, so
        // warn the admin they are on experimental ground.
        if (minecraftVersion.isBefore(MinecraftVersion.MINECRAFT_1_16)) {
            StartupWarnings.experimentalVersion(logger, minecraftVersion.getName());
        }

        // If the server has no "data-storage" folder, it's _probably_ a new install. So mark it for metrics.
        isNewlyInstalled = !new File("data-storage/Slimefun").exists();

        // Creating all necessary Folders
        logger.log(Level.INFO, "Creating directories...");
        createDirectories();

        // Load the multiblock ownership store (redstone auto-craft owner gating)
        multiBlockOwnership = new MultiBlockOwnership(this);
        multiBlockOwnership.load();

        // Load various config settings into our cache
        registry.load(this, config);

        // One-time config migration: configs from older builds carry upstream Slimefun 4's default chat
        // prefix (dough only writes defaults for MISSING keys, so our 'Slimefun 5' default never replaced
        // it). Bump ONLY that exact stale default to the current one; a customised prefix is left as-is.
        if ("&a&lSlimefun 4&7> ".equals(config.getString("options.chat-prefix"))) {
            config.setValue("options.chat-prefix", "&a&lSlimefun 5&7> ");
            config.save();
        }

        // Set up localization
        logger.log(Level.INFO, "Loading language files...");
        String chatPrefix = config.getString("options.chat-prefix");
        String serverDefaultLanguage = config.getString("options.language");
        local = new LocalizationService(this, chatPrefix, serverDefaultLanguage);

        int networkSize = config.getInt("networks.max-size");

        // Make sure that the network size is a valid input
        if (networkSize < 1) {
            logger.log(Level.WARNING, "Your 'networks.max-size' setting is misconfigured! It must be at least 1, it was set to: {0}", networkSize);
            networkSize = 1;
        }

        networkManager = new NetworkManager(networkSize, config.getBoolean("networks.enable-visualizer"), config.getBoolean("networks.delete-excess-items"));

        // Data storage
        playerStorage = new LegacyStorage();
        logger.log(Level.INFO, "Using legacy storage for player data");

        StorageBackendConfig.Backend backend = StorageBackendConfig.backend();

        try {
            switch (backend) {
                case MYSQL:
                    blockStorageBackend = new JdbcBackend(new MySqlDialect(), new MySqlProvider(
                        StorageBackendConfig.mysqlUrl(), StorageBackendConfig.mysqlUser(), StorageBackendConfig.mysqlPassword()));
                    logger.log(Level.INFO, "Using MySQL database storage for block data");
                    break;
                case H2:
                    blockStorageBackend = new JdbcBackend(StorageBackendConfig.h2Url());
                    logger.log(Level.INFO, "Using H2 database storage for block data");
                    break;
                case LEGACY:
                default:
                    blockStorageBackend = new LegacyFileBackend();
                    logger.log(Level.INFO, "Using legacy (flat-file) storage for block data");
                    break;
            }
        } catch (Exception | LinkageError e) {
            // The database driver is downloaded on demand (not shaded); a fresh offline server with no
            // cached driver, or a bad MySQL config, must not stop the plugin from enabling. Fall back to
            // flat-file storage for this boot and log loudly so the operator can fix it.
            logger.log(Level.SEVERE, e, () -> "Could not initialise the " + backend + " storage backend; "
                + "falling back to legacy (flat-file) storage for this boot. If this server has no internet "
                + "access, place the driver jar in plugins/Slimefun/libraries/ manually.");
            blockStorageBackend = new LegacyFileBackend();
        }

        if (blockStorageBackend instanceof JdbcBackend) {
            storageMigration = new MigrationService((JdbcBackend) blockStorageBackend, bootTimestamp);
        }

        // Setting up bStats and analytics. Metrics is OFF by default on this fork: the module still
        // reports to upstream Slimefun's bStats project, not this fork. (options.metrics-service)
        if (config.getBoolean("options.metrics-service")) {
            new Thread(metricsService::start, "Slimefun Metrics").start();
        }

        analyticsService.start();

        // Starting the Auto-Updater
        if (config.getBoolean("options.auto-update")) {
            logger.log(Level.INFO, "Starting Auto-Updater...");
            updaterService.start();
        } else {
            updaterService.disable();
        }

        // Registering all GEO Resources
        logger.log(Level.INFO, "Loading GEO-Resources...");
        GEOResourcesSetup.setup();

        logger.log(Level.INFO, "Loading Tags...");
        loadTags();

        logger.log(Level.INFO, "Loading items...");
        loadItems();

        logger.log(Level.INFO, "Loading researches...");
        loadResearches();

        registry.setResearchingEnabled(getResearchCfg().getBoolean("enable-researching"));
        PostSetup.setupWiki();

        logger.log(Level.INFO, "Loading in-game wiki text...");
        wikiText.loadBundled();

        DefaultGuideCategories.registerInto(guideCategoryRegistry);

        logger.log(Level.INFO, "Loading item translations...");
        itemTranslationService.loadBundled();
        itemTranslationService.canonicalizeToId();
        enchantTranslationService.loadBundled();
        menuTranslationService.loadBundled();

        // Pre-warm the balance caches (heavy per-item recipe-tree effort walks) once, after all addons have
        // registered their items. Runs ASYNC (off the main thread): doing it on the main thread froze the
        // server for a couple of seconds ~10s after boot - which is exactly when an admin first opens the
        // guide. The caches are ConcurrentHashMaps so a concurrent installer-open read is safe.
        getServer().getScheduler().runTaskLaterAsynchronously(this,
            () -> io.github.thebusybiscuit.slimefun5.core.balance.BalanceService.instance().warmCache(), 210L);

        logger.log(Level.INFO, "Registering listeners...");
        registerListeners();
        packetTranslationService = new PacketTranslationService(this);
        guideBookDisplay.build();

        // Initiating various Stuff and all items with a slight delay (0ms after the Server finished loading)
        runSync(new SlimefunStartupTask(this, () -> {
            textureService.register(registry.getAllSlimefunItems(), true);
            permissionsService.register(registry.getAllSlimefunItems(), true);
            soundService.reload(true);

            // This try/catch should prevent buggy Spigot builds from blocking item loading
            try {
                recipeService.refresh();
            } catch (Exception | LinkageError x) {
                logger.log(Level.SEVERE, x, () -> "An Exception occurred while iterating through the Recipe list on Minecraft Version " + minecraftVersion.getName() + " (Slimefun v" + getVersion() + ")");
            }

            // Now that every addon has enabled, drop "restart to apply" flags for ones that loaded
            AddonInstallerMenu.installer().reconcileRestartFlags();

            // Check installer-managed addons/core for updates and log + notify admins (also on join)
            AddonInstallerMenu.installer().checkForUpdatesOnStartup();

            // Stamp/verify the block-data storage-format marker (side-car; never touches the .sfb format)
            io.github.thebusybiscuit.slimefun5.storage.StorageFormat.checkAndStamp();

            // Pre-build the wiki's reverse-recipe index once here so the first player click is instant
            WikiPage.warmUpIndex();

            // Dev helper: dump the English baseline of every registered block menu (core + addons) for
            // translation, when explicitly requested. Disabled by default.
            if (config.getBoolean("guide.dump-menu-baseline") || Boolean.getBoolean("slimefun.dumpMenuBaseline")) {
                menuTranslationService.dumpBaseline(new File(getDataFolder(), "menus-baseline.yml"));
                itemTranslationService.dumpUntranslated(new File(getDataFolder(), "untranslated-items.yml"), java.util.Arrays.asList("de"));
            }

        }), 0);

        // Setting up our commands
        try {
            command.register();
        } catch (Exception | LinkageError x) {
            logger.log(Level.SEVERE, "An Exception occurred while registering the /slimefun command", x);
        }

        // Armor Update Task
        if (config.getBoolean("options.enable-armor-effects")) {
            new SlimefunArmorTask().schedule(this, config.getInt("options.armor-update-interval") * 20L);
            if (config.getBoolean("options.enable-radiation")) {
                new RadiationTask().schedule(this, config.getInt("options.radiation-update-interval") * 20L);
            }
            new RainbowArmorTask().schedule(this, config.getInt("options.rainbow-armor-update-interval") * 20L);
            new SolarHelmetTask().schedule(this, config.getInt("options.armor-update-interval") * 20L);
        } else if (config.getBoolean("options.enable-radiation")) {
            logger.log(Level.WARNING, "Cannot enable radiation while armor effects are disabled.");
        }

        // Starting our tasks
        autoSavingService.start(this, config.getInt("options.auto-save-delay-in-minutes"));
        hologramsService.start();
        ticker.start(this);

        // Loading integrations
        logger.log(Level.INFO, "Loading Third-Party plugin integrations...");
        integrations.start();
        gitHubService.start(this);

        // Hooray!
        logger.log(Level.INFO, "Slimefun has finished loading in {0}", getStartupTime(timestamp));
        // Build marker: if this line is missing from the console, the server is running an older jar.
        logger.log(Level.INFO, "[fork build 2026-07-01] bug-report relay fallback + slot-lock active");
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/Slimefun5/Slimefun5/issues";
    }

    /**
     * This method gets called when the {@link Plugin} gets disabled.
     * Most often it is called when the {@link Server} is shutting down or reloading.
     */
    @Override
    public void onDisable() {
        // Slimefun never loaded successfully, so we don't even bother doing stuff here
        if (instance() == null || minecraftVersion == MinecraftVersion.UNIT_TEST) {
            return;
        }

        // Cancel all tasks from this plugin immediately
        Bukkit.getScheduler().cancelTasks(this);

        // Finishes all started movements/removals of block data
        try {
            ticker.halt();
            ticker.run();
        } catch (Exception x) {
            getLogger().log(Level.SEVERE, x, () -> "Something went wrong while disabling the ticker task for Slimefun v" + getDescription().getVersion());
        }

        // Kill our Profiler Threads
        profiler.kill();

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

        // Save all "universal" inventories (ender chests for example)
        for (UniversalBlockMenu menu : registry.getUniversalInventories().values()) {
            menu.save();
        }

        // Flush any multiblock ownership changes still inside the debounce window
        if (multiBlockOwnership != null) {
            multiBlockOwnership.save();
        }

        // Tear down the block storage backend (H2 connection close; no-op for legacy) now that
        // every flush above has gone through it. Null-guarded: onDisable can run before the backend
        // is assigned (unsupported-version / missing-CS-CoreLib early exits both re-enter onDisable).
        if (blockStorageBackend != null) {
            blockStorageBackend.close();
        }

        // Create a new backup zip
        if (config.getBoolean("options.backup-data")) {
            backupService.run();
        }

        // Close and unload any resources from our Metrics Service
        metricsService.cleanUp();

        // Terminate our Plugin instance
        setInstance(null);

        /**
         * Close all inventories on the server to prevent item dupes
         * (Incase some idiot uses /reload)
         */
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.closeInventory();
        }
    }

    /**
     * This is a private internal method to set the de-facto instance of {@link Slimefun}.
     * Having this as a seperate method ensures the seperation between static and non-static fields.
     * It also makes sonarcloud happy :)
     * Only ever use it during {@link #onEnable()} or {@link #onDisable()}.
     * 
     * @param pluginInstance
     *            Our instance of {@link Slimefun} or null
     */
    private static void setInstance(@Nullable Slimefun pluginInstance) {
        instance = pluginInstance;
    }

    /**
     * This returns the time it took to load Slimefun (given a starting point).
     * 
     * @param timestamp
     *            The time at which we started to load Slimefun.
     * 
     * @return The total time it took to load Slimefun (in ms or s)
     */
    private @Nonnull String getStartupTime(long timestamp) {
        long ms = (System.nanoTime() - timestamp) / 1000000;

        if (ms > 1000) {
            return NumberUtils.roundDecimalNumber(ms / 1000.0) + 's';
        } else {
            return NumberUtils.roundDecimalNumber(ms) + "ms";
        }
    }

    /**
     * This method checks if this is currently running in a unit test
     * environment.
     * 
     * @return Whether we are inside a unit test
     */
    public boolean isUnitTest() {
        return minecraftVersion == MinecraftVersion.UNIT_TEST;
    }

    /**
     * This method checks for the {@link MinecraftVersion} of the {@link Server}.
     * If the version is unsupported, a warning will be printed to the console.
     *
     * @return Whether the {@link MinecraftVersion} is unsupported
     */
    private boolean isVersionUnsupported() {
        try {
            // First check if they still use the unsupported CraftBukkit software.
            if (!PaperLib.isSpigot() && Bukkit.getName().equals("CraftBukkit")) {
                StartupWarnings.invalidServerSoftware(getLogger());
                return true;
            }

            
            // Now check the actual Version of Minecraft
            int version = PaperLib.getMinecraftVersion();
            int patchVersion = PaperLib.getMinecraftPatchVersion();
            
            if (version == 0) {
                java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("MC: (\\d+)\\.(\\d+)").matcher(Bukkit.getVersion());
                if (matcher.find()) {
                    version = Integer.parseInt(matcher.group(1));
                    patchVersion = Integer.parseInt(matcher.group(2));
                }
            }


            if (version > 0) {
                // Check all supported versions of Minecraft
                for (MinecraftVersion supportedVersion : MinecraftVersion.values()) {
                    if (supportedVersion.isMinecraftVersion(version, patchVersion)) {
                        minecraftVersion = supportedVersion;
                        return false;
                    }
                }

                // Looks like you are using an unsupported Minecraft Version
                StartupWarnings.invalidMinecraftVersion(getLogger(), version, getDescription().getVersion());
                return true;
            } else {
                getLogger().log(Level.WARNING, "We could not determine the version of Minecraft you were using? ({0})", Bukkit.getVersion());

                /*
                 * If we are unsure about it, we will assume "supported".
                 * They could be using a non-Bukkit based Software which still
                 * might support Bukkit-based plugins.
                 * Use at your own risk in this case.
                 */
                return false;
            }
        } catch (Exception | LinkageError x) {
            getLogger().log(Level.SEVERE, x, () -> "Error: Could not determine Environment or version of Minecraft for Slimefun v" + getDescription().getVersion());

            // We assume "unsupported" if something went wrong.
            return true;
        }
    }

    /**
     * This private method gives us a {@link Collection} of every {@link MinecraftVersion}
     * that Slimefun is compatible with (as a {@link String} representation).
     * <p>
     * Example:
     * 
     * <pre>
     * { 1.14.x, 1.15.x, 1.16.x }
     * </pre>
     * 
     * @return A {@link Collection} of all compatible minecraft versions as strings
     */
    static @Nonnull Collection<String> getSupportedVersions() {
        List<String> list = new ArrayList<>();

        for (MinecraftVersion version : MinecraftVersion.values()) {
            if (!version.isVirtual()) {
                list.add(version.getName());
            }
        }

        return list;
    }

    /**
     * This method creates all necessary directories (and sub directories) for Slimefun.
     */
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

    /**
     * This method registers all of our {@link Listener Listeners}.
     */
    private void registerListeners() {
        // Old deprecated CS-CoreLib Listener
        register(() -> new MenuListener(this));

        register(() -> new SlimefunBootsListener(this));
        register(() -> new EnderArmorListener(this));
        // PlayerArmorChangeEvent is a Paper-only event; only register the listener when it is present.
        if (isClassPresent("com.destroystokyo.paper.event.player.PlayerArmorChangeEvent")) {
            register(() -> new ArmorEquipListener(this));
        }
        register(() -> new SlimefunItemInteractListener(this));
        register(() -> new SlimefunItemConsumeListener(this));
        register(() -> new BlockPhysicsListener(this));
        register(() -> new CargoNodeListener(this));
        register(() -> new MultiBlockListener(this));
        register(() -> new MultiBlockRedstoneListener(this));
        register(() -> new io.github.thebusybiscuit.slimefun5.core.guide.installer.AddonUpdateJoinListener(this));
        register(() -> new GadgetsListener(this));
        register(() -> new DispenserListener(this));
        register(() -> new BlockListener(this));
        register(() -> new EnhancedFurnaceListener(this));
        if (minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_12)) {
            register(() -> new ItemPickupListener(this));
        }
        register(() -> new ItemDropListener(this));
        register(() -> new DeathpointListener(this));
        register(() -> new ExplosionsListener(this));
        register(() -> new DebugFishListener(this));
        register(() -> new FireworksListener(this));
        register(() -> new WitherListener(this));
        register(() -> new IronGolemListener(this));
        register(() -> new EntityInteractionListener(this));
        register(() -> new MobDropListener(this));
        register(() -> new VillagerTradingListener(this));
        if (minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_9)) {
            register(() -> new ElytraImpactListener(this));
        }
        register(() -> new CraftingTableListener(this));
        register(() -> new AnvilListener(this));
        register(() -> new BrewingStandListener(this));
        register(() -> new CauldronListener(this));
        register(() -> new GrindstoneListener(this));
        register(() -> new CartographyTableListener(this));
        register(() -> new ButcherAndroidListener(this));
        register(() -> new MiningAndroidListener(this));
        register(() -> new NetworkListener(this, networkManager));
        register(() -> new HopperListener(this));
        register(() -> new TalismanListener(this));
        if (minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_13)) {
            register(() -> new TalismanBlockDropListener(this));
        }
        register(() -> new SoulboundListener(this));
        register(() -> new AutoCrafterListener(this));
        register(() -> new SlimefunItemHitListener(this));
        register(() -> new MiddleClickListener(this));
        if (minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_15)) {
            register(() -> new BeeListener(this));
            register(() -> new BeeWingsListener(this, (BeeWings) SlimefunItems.BEE_WINGS.getItem()));
        }
        if (minecraftVersion.isAtLeast(MinecraftVersion.MINECRAFT_1_16)) {
            register(() -> new PiglinListener(this));
            register(() -> new SmithingTableListener(this));
        }
        register(() -> new JoinListener(this));

        // Item-specific Listeners
        register(() -> new CoolerListener(this, (Cooler) SlimefunItems.COOLER.getItem()));
        register(() -> new SeismicAxeListener(this, (SeismicAxe) SlimefunItems.SEISMIC_AXE.getItem()));
        register(() -> new RadioactivityListener(this));
        register(() -> new AncientAltarListener(this, (AncientAltar) SlimefunItems.ANCIENT_ALTAR.getItem(), (AncientPedestal) SlimefunItems.ANCIENT_PEDESTAL.getItem()));
        register(() -> grapplingHookListener.register(this, (GrapplingHook) SlimefunItems.GRAPPLING_HOOK.getItem()));
        register(() -> bowListener.register(this));
        register(() -> backpackListener.register(this));

        // Handle Slimefun Guide being given on Join
        register(() -> new SlimefunGuideListener(this, config.getBoolean("guide.receive-on-first-join")));

        // Clear the Slimefun Guide History upon Player Leaving
        register(() -> new PlayerProfileListener(this));
    }

    /**
     * Registers a single {@link Listener}, tolerating failures caused by the universal Java-8 jar
     * running on a legacy server. A {@link Listener} whose event types or items only exist on newer
     * Minecraft versions throws {@link NoClassDefFoundError} (or another {@link LinkageError}) when
     * Bukkit inspects its handler methods; on modern servers this never triggers, but on legacy we
     * log and skip that one listener instead of aborting the whole plugin enable.
     *
     * @param listenerInit
     *            The action that constructs and registers the {@link Listener}
     */
    private void register(@Nonnull Runnable listenerInit) {
        try {
            listenerInit.run();
        } catch (LinkageError | RuntimeException e) {
            getLogger().log(Level.WARNING, e, () -> "Skipped a listener that is unavailable on this Minecraft version");
        }
    }

    private static boolean isClassPresent(@Nonnull String className) {
        try {
            Class.forName(className);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    /**
     * This (re)loads every {@link SlimefunTag}.
     */
    private void loadTags() {
        for (SlimefunTag tag : SlimefunTag.values()) {
            try {
                // Only reload "empty" (or unloaded) Tags
                if (tag.isEmpty()) {
                    tag.reload();
                }
            } catch (TagMisconfigurationException e) {
                getLogger().log(Level.SEVERE, e, () -> "Failed to load Tag: " + tag.name());
            }
        }
    }

    /**
     * This loads all of our items.
     */
    private void loadItems() {
        try {
            SlimefunItemSetup.setup(this);
        } catch (Exception | LinkageError x) {
            getLogger().log(Level.SEVERE, x, () -> "An Error occurred while initializing SlimefunItems for Slimefun " + getVersion());
        }
    }

    /**
     * This loads our researches.
     */
    private void loadResearches() {
        try {
            ResearchSetup.setupResearches();
        } catch (Exception | LinkageError x) {
            getLogger().log(Level.SEVERE, x, () -> "An Error occurred while initializing Slimefun Researches for Slimefun " + getVersion());
        }
    }

    /**
     * This returns the global instance of {@link Slimefun}.
     * This may return null if the {@link Plugin} was disabled.
     *
     * @return The {@link Slimefun} instance
     */
    public static @Nullable Slimefun instance() {
        return instance;
    }

    /**
     * This private static method allows us to throw a proper {@link Exception}
     * whenever someone tries to access a static method while the instance is null.
     * This happens when the method is invoked before {@link #onEnable()} or after {@link #onDisable()}.
     * <p>
     * Use it whenever a null check is needed to avoid a non-descriptive {@link NullPointerException}.
     */
    private static void validateInstance() {
        if (instance == null) {
            throw new IllegalStateException("Cannot invoke static method, Slimefun instance is null.");
        }
    }

    /**
     * This returns the {@link Logger} instance that Slimefun uses.
     * <p>
     * <strong>Any {@link SlimefunAddon} should use their own {@link Logger} instance!</strong>
     * 
     * @return Our {@link Logger} instance
     */
    public static @Nonnull Logger logger() {
        validateInstance();
        return instance.getLogger();
    }

    /**
     * This returns the version of Slimefun that is currently installed.
     *
     * @return The currently installed version of Slimefun
     */
    public static @Nonnull String getVersion() {
        validateInstance();
        return instance.getDescription().getVersion();
    }

    public static @Nonnull Config getCfg() {
        validateInstance();
        return instance.config;
    }

    public static @Nonnull Config getResearchCfg() {
        validateInstance();
        return instance.researches;
    }

    public static @Nonnull Config getItemCfg() {
        validateInstance();
        return instance.items;
    }

    /**
     * This returns our {@link GPSNetwork} instance.
     * The {@link GPSNetwork} is responsible for handling any GPS-related
     * operations and for managing any {@link GEOResource}.
     * 
     * @return Our {@link GPSNetwork} instance
     */
    public static @Nonnull GPSNetwork getGPSNetwork() {
        validateInstance();
        return instance.gpsNetwork;
    }

    public static @Nonnull TickerTask getTickerTask() {
        validateInstance();
        return instance.ticker;
    }

    /**
     * This returns the {@link LocalizationService} of Slimefun.
     *
     * @return The {@link LocalizationService} of Slimefun
     */
    public static @Nonnull LocalizationService getLocalization() {
        validateInstance();
        return instance.local;
    }

    /**
     * This method returns out {@link MinecraftRecipeService} for Slimefun.
     * This service is responsible for finding/identifying {@link Recipe Recipes}
     * from vanilla Minecraft.
     * 
     * @return Slimefun's {@link MinecraftRecipeService} instance
     */
    public static @Nonnull MinecraftRecipeService getMinecraftRecipeService() {
        validateInstance();
        return instance.recipeService;
    }

    public static @Nonnull CustomItemDataService getItemDataService() {
        validateInstance();
        return instance.itemDataService;
    }

    public static @Nonnull CustomTextureService getItemTextureService() {
        validateInstance();
        return instance.textureService;
    }

    public static @Nonnull PermissionsService getPermissionsService() {
        validateInstance();
        return instance.permissionsService;
    }

    public static @Nonnull BlockDataService getBlockDataService() {
        validateInstance();
        return instance.blockDataService;
    }

    /**
     * This method returns out world settings service.
     * That service is responsible for managing item settings per
     * {@link World}, such as disabling a {@link SlimefunItem} in a
     * specific {@link World}.
     * 
     * @return Our instance of {@link PerWorldSettingsService}
     */
    public static @Nonnull PerWorldSettingsService getWorldSettingsService() {
        validateInstance();
        return instance.worldSettingsService;
    }

    /**
     * This returns our {@link HologramsService} which handles the creation and
     * cleanup of any holograms.
     * 
     * @return Our instance of {@link HologramsService}
     */
    public static @Nonnull HologramsService getHologramsService() {
        validateInstance();
        return instance.hologramsService;
    }

    /**
     * This returns our {@link  SoundService} which handles the configuration of all sounds used in Slimefun
     *
     * @return Our instance of {@link SoundService}
     */
    @Nonnull
    public static SoundService getSoundService() {
        validateInstance();
        return instance.soundService;
    }

    /**
     * This returns our instance of {@link IntegrationsManager}.
     * This is responsible for managing any integrations with third party {@link Plugin plugins}.
     * 
     * @return Our instance of {@link IntegrationsManager}
     */
    public static @Nonnull IntegrationsManager getIntegrations() {
        validateInstance();
        return instance.integrations;
    }

    /**
     * This returns out instance of the {@link ProtectionManager}.
     * This bridge is used to hook into any third-party protection {@link Plugin}.
     * 
     * @return Our instanceof of the {@link ProtectionManager}
     */
    public static @Nonnull ProtectionManager getProtectionManager() {
        return getIntegrations().getProtectionManager();
    }

    /**
     * This method returns the {@link UpdaterService} of Slimefun.
     * It is used to handle automatic updates.
     *
     * @return The {@link UpdaterService} for Slimefun
     */
    public static @Nonnull UpdaterService getUpdater() {
        validateInstance();
        return instance.updaterService;
    }

    /**
     * This method returns the {@link MetricsService} of Slimefun.
     * It is used to handle sending metric information to bStats.
     *
     * @return The {@link MetricsService} for Slimefun
     */
    public static @Nonnull MetricsService getMetricsService() {
        validateInstance();
        return instance.metricsService;
    }

    /**
     * This method returns the {@link AnalyticsService} of Slimefun.
     * It is used to handle sending analytic information.
     *
     * @return The {@link AnalyticsService} for Slimefun
     */
    public static @Nonnull AnalyticsService getAnalyticsService() {
        validateInstance();
        return instance.analyticsService;
    }

    /**
     * This method returns the {@link ItemTranslationService} of Slimefun.
     * It translates item names and lore per language for display in Slimefun UIs.
     *
     * @return The {@link ItemTranslationService} for Slimefun
     */
    public static @Nonnull ItemTranslationService getItemTranslationService() {
        validateInstance();
        return instance.itemTranslationService;
    }

    /**
     * This method returns the {@link GuideCategoryRegistry} of Slimefun.
     * It holds the guide's top-level categories (core-registered + addon-registered).
     *
     * @return The {@link GuideCategoryRegistry} for Slimefun
     */
    public static @Nonnull GuideCategoryRegistry getGuideCategories() {
        validateInstance();
        return instance.guideCategoryRegistry;
    }

    /**
     * This method returns the {@link EnchantTranslationService} of Slimefun.
     * It resolves per-language enchantment display names for the packet lore renderer.
     *
     * @return The {@link EnchantTranslationService} for Slimefun
     */
    public static @Nonnull EnchantTranslationService getEnchantTranslationService() {
        validateInstance();
        return instance.enchantTranslationService;
    }

    /**
     * This method returns the {@link MenuTranslationService} of Slimefun.
     * It translates the decorative info items of block menus per language.
     *
     * @return The {@link MenuTranslationService} for Slimefun
     */
    public static @Nonnull MenuTranslationService getMenuTranslationService() {
        validateInstance();
        return instance.menuTranslationService;
    }

    /**
     * This method returns the {@link TranslationCoverageService} of Slimefun.
     * It combines item-unit and message-unit coverage into one honest, weighted translation percentage.
     *
     * @return The {@link TranslationCoverageService} for Slimefun
     */
    public static @Nonnull TranslationCoverageService getTranslationCoverageService() {
        validateInstance();
        return instance.translationCoverageService;
    }

    /**
     * This method returns the {@link GuideBookDisplay} of Slimefun.
     * It holds the boot-precomputed, per-language rendering of the Slimefun Guide book.
     *
     * @return The {@link GuideBookDisplay} for Slimefun
     */
    public static @Nonnull GuideBookDisplay getGuideBookDisplay() {
        validateInstance();
        return instance.guideBookDisplay;
    }

    /**
     * This method returns the {@link PacketTranslationService} of Slimefun, or null if packet
     * translation has not been set up yet (before it is constructed during boot) or is disabled.
     *
     * @return The {@link PacketTranslationService} for Slimefun, or null
     */
    public static @Nullable PacketTranslationService getPacketTranslationService() {
        validateInstance();
        return instance.packetTranslationService;
    }

    /**
     * This method returns the {@link GitHubService} of Slimefun.
     * It is used to retrieve data from GitHub repositories.
     *
     * @return The {@link GitHubService} for Slimefun
     */
    public static @Nonnull GitHubService getGitHubService() {
        validateInstance();
        return instance.gitHubService;
    }

    /**
     * This returns our {@link NetworkManager} which is responsible
     * for handling the Cargo and Energy networks.
     * 
     * @return Our {@link NetworkManager} instance
     */

    public static @Nonnull NetworkManager getNetworkManager() {
        validateInstance();
        return instance.networkManager;
    }

    public static @Nonnull SlimefunRegistry getRegistry() {
        validateInstance();
        return instance.registry;
    }

    /**
     * This returns our {@link MultiBlockOwnership} store, which tracks the owner of each multiblock
     * crafting table and gates the redstone auto-craft on that owner's unlocked research.
     *
     * @return Our {@link MultiBlockOwnership} instance
     */
    public static @Nonnull MultiBlockOwnership getMultiBlockOwnership() {
        validateInstance();
        return instance.multiBlockOwnership;
    }

    public static @Nonnull WikiText getWikiText() {
        validateInstance();
        return instance.wikiText;
    }

    public static @Nonnull GrapplingHookListener getGrapplingHookListener() {
        validateInstance();
        return instance.grapplingHookListener;
    }

    public static @Nonnull BackpackListener getBackpackListener() {
        validateInstance();
        return instance.backpackListener;
    }

    public static @Nonnull SlimefunBowListener getBowListener() {
        validateInstance();
        return instance.bowListener;
    }

    /**
     * The {@link Command} that was added by Slimefun.
     *
     * @return Slimefun's command
     */
    public static @Nonnull SlimefunCommand getCommand() {
        validateInstance();
        return instance.command;
    }

    /**
     * This returns our instance of the {@link SlimefunProfiler}, a tool that is used
     * to analyse performance and lag.
     *
     * @return The {@link SlimefunProfiler}
     */
    public static @Nonnull SlimefunProfiler getProfiler() {
        validateInstance();
        return instance.profiler;
    }

    /**
     * This returns the currently installed version of Minecraft.
     *
     * @return The current version of Minecraft
     */
    public static @Nonnull MinecraftVersion getMinecraftVersion() {
        validateInstance();
        return instance.minecraftVersion;
    }

    /**
     * This method returns whether this version of Slimefun was newly installed.
     * It will return true if this {@link Server} uses Slimefun for the very first time.
     *
     * @return Whether this is a new installation of Slimefun
     */
    public static boolean isNewlyInstalled() {
        validateInstance();
        return instance.isNewlyInstalled;
    }

    /**
     * This method returns a {@link Set} of every {@link Plugin} that lists Slimefun
     * as a required or optional dependency.
     * <p>
     * We will just assume this to be a list of our addons.
     *
     * @return A {@link Set} of every {@link Plugin} that is dependent on Slimefun
     */
    public static @Nonnull Set<Plugin> getInstalledAddons() {
        validateInstance();
        String pluginName = instance.getName();

        // @formatter:off - Collect any Plugin that (soft)-depends on Slimefun
        return Arrays.stream(instance.getServer().getPluginManager().getPlugins()).filter(plugin -> {
            PluginDescriptionFile description = plugin.getDescription();
            return description.getDepend().contains(pluginName) || description.getSoftDepend().contains(pluginName);
        }).collect(Collectors.toSet());
        // @formatter:on
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
    public static @Nullable BukkitTask runSync(@Nonnull Runnable runnable, long delay) {
        Validate.notNull(runnable, "Cannot run null");
        Validate.isTrue(delay >= 0, "The delay cannot be negative");

        // Run the task instantly within a Unit Test
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
    public static @Nullable BukkitTask runSync(@Nonnull Runnable runnable) {
        Validate.notNull(runnable, "Cannot run null");

        // Run the task instantly within a Unit Test
        if (getMinecraftVersion() == MinecraftVersion.UNIT_TEST) {
            runnable.run();
            return null;
        }

        if (instance == null || !instance.isEnabled()) {
            return null;
        }

        return instance.getServer().getScheduler().runTask(instance, runnable);
    }

    public static @Nonnull Storage getPlayerStorage() {
        return instance().playerStorage;
    }

    public static @Nonnull BlockStorageBackend getBlockStorageBackend() {
        return instance().blockStorageBackend;
    }

    public static @Nullable MigrationService getStorageMigration() {
        return instance == null ? null : instance.storageMigration;
    }

    /**
     * This method returns the {@link ThreadService} of Slimefun.
     * <b>Do not use this if you're an addon. Please make your own {@link ThreadService}.</b>
     *
     * @return The {@link ThreadService} for Slimefun
     */
    public static @Nonnull ThreadService getThreadService() {
        return instance().threadService;
    }
}

