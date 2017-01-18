package me.mrCookieSlime.Slimefun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.PluginUtils;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.CSCoreLibPlugin.general.Clock;
import me.mrCookieSlime.CSCoreLibPlugin.general.Reflection.ReflectionUtils;
import me.mrCookieSlime.CSCoreLibSetup.CSCoreLibLoader;
import me.mrCookieSlime.Slimefun.AncientAltar.Pedestals;
import me.mrCookieSlime.Slimefun.Commands.SlimefunCommand;
import me.mrCookieSlime.Slimefun.Commands.SlimefunTabCompleter;
import me.mrCookieSlime.Slimefun.GEO.OreGenSystem;
import me.mrCookieSlime.Slimefun.GEO.Resources.NetherIceResource;
import me.mrCookieSlime.Slimefun.GEO.Resources.OilResource;
import me.mrCookieSlime.Slimefun.GPS.Elevator;
import me.mrCookieSlime.Slimefun.GitHub.Contributor;
import me.mrCookieSlime.Slimefun.GitHub.GitHubConnector;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.MultiBlock;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunArmorPiece;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AContainer;
import me.mrCookieSlime.Slimefun.Setup.Files;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.MiscSetup;
import me.mrCookieSlime.Slimefun.Setup.ResearchSetup;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.Setup.SlimefunSetup;
import me.mrCookieSlime.Slimefun.URID.AutoSavingTask;
import me.mrCookieSlime.Slimefun.URID.URID;
import me.mrCookieSlime.Slimefun.WorldEdit.WESlimefunManager;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.TickerTask;
import me.mrCookieSlime.Slimefun.api.energy.ChargableBlock;
import me.mrCookieSlime.Slimefun.api.energy.EnergyNet;
import me.mrCookieSlime.Slimefun.api.energy.ItemEnergy;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.CargoNet;
import me.mrCookieSlime.Slimefun.listeners.AncientAltarListener;
import me.mrCookieSlime.Slimefun.listeners.AndroidKillingListener;
import me.mrCookieSlime.Slimefun.listeners.ArmorListener;
import me.mrCookieSlime.Slimefun.listeners.AutonomousToolsListener;
import me.mrCookieSlime.Slimefun.listeners.BackpackListener;
import me.mrCookieSlime.Slimefun.listeners.BlockListener;
import me.mrCookieSlime.Slimefun.listeners.BowListener;
import me.mrCookieSlime.Slimefun.listeners.ClearLaggIntegration;
import me.mrCookieSlime.Slimefun.listeners.CoolerListener;
import me.mrCookieSlime.Slimefun.listeners.DamageListener;
import me.mrCookieSlime.Slimefun.listeners.FurnaceListener;
import me.mrCookieSlime.Slimefun.listeners.GearListener;
import me.mrCookieSlime.Slimefun.listeners.ItemListener;
import me.mrCookieSlime.Slimefun.listeners.TalismanListener;
import me.mrCookieSlime.Slimefun.listeners.TeleporterListener;
import me.mrCookieSlime.Slimefun.listeners.ToolListener;

public class SlimefunStartup extends JavaPlugin {

	public static SlimefunStartup instance;

	static PluginUtils utils;
	static Config researches;
	static Config items;
	static Config whitelist;
	static Config config;

	public static TickerTask ticker;

	private boolean clearlag = false;
	private boolean exoticGarden = false;

	// Supported Versions of Minecraft
	final String[] supported = {"v1_9_", "v1_10_", "v1_11_", "PluginBukkitBridge"};

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		CSCoreLibLoader loader = new CSCoreLibLoader(this);
		if (loader.load()) {

			boolean compatibleVersion = false;

			for (String version: supported) {
				if (ReflectionUtils.getVersion().startsWith(version)) {
					compatibleVersion = true;
					break;
				}
			}

			// Looks like you are using an unsupported Minecraft Version
			if (!compatibleVersion) {
				System.err.println("### Slimefun failed to load!");
				System.err.println("###");
				System.err.println("### You are using the wrong Version of Minecraft!!!");
				System.err.println("###");
				System.err.println("### You are using Minecraft " + ReflectionUtils.getVersion());
				System.err.println("### but Slimefun v" + getDescription().getVersion() + " requires you to be using");
				System.err.println("### Minecraft 1.9.X or 1.10.X");
				System.err.println("###");
				System.err.println("### Please use an older Version of Slimefun and disable auto-updating");
				System.err.println("### or consider updating your Server Software.");
				getServer().getPluginManager().disablePlugin(this);
				return;
			}

			instance = this;
			System.out.println("[Slimefun] Loading Files...");
			Files.cleanup();

			System.out.println("[Slimefun] Loading Config...");

			utils = new PluginUtils(this);
			utils.setupConfig();

			// Loading all extra configs
			researches = new Config(Files.RESEARCHES);
			items = new Config(Files.ITEMS);
			whitelist = new Config(Files.WHITELIST);

			// Init Config, Updater, Metrics and messages.yml
			utils.setupUpdater(53485, getFile());
			utils.setupMetrics();
			utils.setupLocalization();
			config = utils.getConfig();
			Messages.local = utils.getLocalization();
			Messages.setup();

			// Creating all necessary Folders
			// TODO: Make a shortcut method such as createDir(path)
			if (!new File("data-storage/Slimefun/blocks").exists()) new File("data-storage/Slimefun/blocks").mkdirs();
			if (!new File("data-storage/Slimefun/stored-blocks").exists()) new File("data-storage/Slimefun/stored-blocks").mkdirs();
			if (!new File("data-storage/Slimefun/stored-inventories").exists()) new File("data-storage/Slimefun/stored-inventories").mkdirs();
			if (!new File("data-storage/Slimefun/universal-inventories").exists()) new File("data-storage/Slimefun/universal-inventories").mkdirs();
			if (!new File("data-storage/Slimefun/waypoints").exists()) new File("data-storage/Slimefun/waypoints").mkdirs();
			if (!new File("data-storage/Slimefun/block-backups").exists()) new File("data-storage/Slimefun/block-backups").mkdirs();
			if (!new File("plugins/Slimefun/scripts").exists()) new File("plugins/Slimefun/scripts").mkdirs();
			if (!new File("plugins/Slimefun/generators").exists()) new File("plugins/Slimefun/generators").mkdirs();
			if (!new File("plugins/Slimefun/error-reports").exists()) new File("plugins/Slimefun/error-reports").mkdirs();
			if (!new File("plugins/Slimefun/cache/github").exists()) new File("plugins/Slimefun/cache/github").mkdirs();

			SlimefunManager.plugin = this;

			System.out.println("[Slimefun] Loading Items...");
			MiscSetup.setupItemSettings();
			try {
				SlimefunSetup.setupItems();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			MiscSetup.loadDescriptions();

			System.out.println("[Slimefun] Loading Researches...");
			Research.enabled = getResearchCfg().getBoolean("enable-researching");
			ResearchSetup.setupResearches();

			MiscSetup.setupMisc();

			BlockStorage.info_delay = config.getInt("URID.info-delay");

			System.out.println("[Slimefun] Loading World Generators...");

			// Generating Oil as an OreGenResource (its a cool API)
			OreGenSystem.registerResource(new OilResource());
			OreGenSystem.registerResource(new NetherIceResource());
			
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

			// Toggleable Listeners for performance
			if (config.getBoolean("items.talismans")) new TalismanListener(this);
			if (config.getBoolean("items.backpacks")) new BackpackListener(this);
			if (config.getBoolean("items.coolers")) new CoolerListener(this);

			// Handle Slimefun Guide being given on Join
			// TODO: Move it to its own class, was too lazy
			if (config.getBoolean("options.give-guide-on-first-join")) {
				getServer().getPluginManager().registerEvents(new Listener() {

					@EventHandler
					public void onJoin(PlayerJoinEvent e) {
						if (!e.getPlayer().hasPlayedBefore()) {
							Player p = e.getPlayer();
							if (!getWhitelist().getBoolean(p.getWorld().getName() + ".enabled")) return;
							if (!getWhitelist().getBoolean(p.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE")) return;
							p.getInventory().addItem(SlimefunGuide.getItem(config.getBoolean("guide.default-view-book")));
						}
					}

				}, this);
			}

			// Load/Unload Worlds in Slimefun
			// TODO: Move it to its own class, was too lazy
			getServer().getPluginManager().registerEvents(new Listener() {

				@EventHandler
				public void onWorldLoad(WorldLoadEvent e) {
					BlockStorage.getForcedStorage(e.getWorld());

					SlimefunStartup.getWhitelist().setDefaultValue(e.getWorld().getName() + ".enabled", true);
					SlimefunStartup.getWhitelist().setDefaultValue(e.getWorld().getName() + ".enabled-items.SLIMEFUN_GUIDE", true);
					SlimefunStartup.getWhitelist().save();
				}

				@EventHandler
				public void onWorldUnload(WorldUnloadEvent e) {
					BlockStorage storage = BlockStorage.getStorage(e.getWorld());
					if (storage != null) storage.save(true);
					else System.err.println("[Slimefun] Could not save Slimefun Blocks for World \"" + e.getWorld().getName() + "\"");
				}

			}, this);

			// Clear the Slimefun Guide History upon Player Leaving
			// TODO: Move it to its own class, was too lazy
			getServer().getPluginManager().registerEvents(new Listener() {

				@EventHandler
				public void onDisconnect(PlayerQuitEvent e) {
					if (SlimefunGuide.history.containsKey(e.getPlayer().getUniqueId())) SlimefunGuide.history.remove(e.getPlayer().getUniqueId());
				}

			}, this);

			// Initiating various Stuff and all Items with a slightly delay (0ms after the Server finished loading)
			getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					Slimefun.emeraldenchants = getServer().getPluginManager().isPluginEnabled("EmeraldEnchants");
					SlimefunGuide.all_recipes = config.getBoolean("options.show-vanilla-recipes-in-guide");
					MiscSetup.loadItems();

					for (World world: Bukkit.getWorlds()) {
						new BlockStorage(world);
					}

					if (SlimefunItem.getByName("ANCIENT_ALTAR") != null) new AncientAltarListener((SlimefunStartup) instance);
				}
			}, 0);

			// WorldEdit Hook to clear Slimefun Data upon //set 0 //cut or any other equivalent
			if (getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
				try {
					Class.forName("com.sk89q.worldedit.extent.Extent");
					new WESlimefunManager();
					System.out.println("[Slimefun] Successfully hooked into WorldEdit!");
				} catch(Exception x) {
					System.err.println("[Slimefun] Failed to hook into WorldEdit!");
					System.err.println("[Slimefun] Maybe consider updating WorldEdit or Slimefun?");
				}
			}

			getCommand("slimefun").setExecutor(new SlimefunCommand(this));
			getCommand("slimefun").setTabCompleter(new SlimefunTabCompleter());

			// Armor Update Task
			if (config.getBoolean("options.enable-armor-effects")) {
				getServer().getScheduler().runTaskTimer(this, new Runnable() {

					@Override
					public void run() {
						for (Player p: Bukkit.getOnlinePlayers()) {
							for (ItemStack armor: p.getInventory().getArmorContents()) {
								if (armor != null) {
									if (Slimefun.hasUnlocked(p, armor, true)) {
										if (SlimefunItem.getByItem(armor) instanceof SlimefunArmorPiece) {
											for (PotionEffect effect: ((SlimefunArmorPiece) SlimefunItem.getByItem(armor)).getEffects()) {
												p.removePotionEffect(effect.getType());
												p.addPotionEffect(effect);
											}
										}
										if (SlimefunManager.isItemSimiliar(armor, SlimefunItem.getItem("SOLAR_HELMET"), false)) {
											if (p.getWorld().getTime() < 12300 || p.getWorld().getTime() > 23850) {
												if (p.getEyeLocation().getBlock().getLightFromSky() == 15) {
													ItemEnergy.chargeInventory(p, Float.valueOf(String.valueOf(Slimefun.getItemValue("SOLAR_HELMET", "charge-amount"))));
												}
											}
										}
									}
								}
							}

							for (ItemStack radioactive: SlimefunItem.radioactive) {
								if (p.getInventory().containsAtLeast(radioactive, 1) || SlimefunManager.isItemSimiliar(p.getInventory().getItemInOffHand(), radioactive, true)) {
									if (SlimefunManager.isItemSimiliar(SlimefunItems.SCUBA_HELMET, p.getInventory().getHelmet(), true) &&
										SlimefunManager.isItemSimiliar(SlimefunItems.HAZMATSUIT_CHESTPLATE, p.getInventory().getChestplate(), true) &&
										SlimefunManager.isItemSimiliar(SlimefunItems.HAZMATSUIT_LEGGINGS, p.getInventory().getLeggings(), true) &&
										SlimefunManager.isItemSimiliar(SlimefunItems.RUBBER_BOOTS, p.getInventory().getBoots(), true)) {

										break;
									}


									p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 400, 3));
									p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 400, 3));
									p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 400, 3));
									p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 3));
									p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 1));
									p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 400, 1));
									p.setFireTicks(400);
									break;
								}
							}
						}
					}
				}, 0L, config.getInt("options.armor-update-interval") * 20L);
			}

			ticker = new TickerTask();

			// Starting all ASYNC Tasks
			getServer().getScheduler().scheduleAsyncRepeatingTask(this, new AutoSavingTask(), 1200L, config.getInt("options.auto-save-delay-in-minutes") * 60L * 20L);
			getServer().getScheduler().scheduleAsyncRepeatingTask(this, ticker, 100L, config.getInt("URID.custom-ticker-delay"));
			
			getServer().getScheduler().scheduleAsyncDelayedTask(this, new GitHubConnector() {
				
				@Override
				public void onSuccess(JsonElement element) {
					JsonArray array = element.getAsJsonArray();
				    
				    for (int i = 0; i < array.size(); i++) {
				    	JsonObject object = array.get(i).getAsJsonObject();
				    	
				    	String name = object.get("login").getAsString();
				    	String job = "&cAuthor";
				    	int commits = object.get("contributions").getAsInt();
				    	String profile = object.get("html_url").getAsString();
				    	
				    	if (!name.equals("invalid-email-address")) {
				    		Contributor contributor = new Contributor(name, job, commits);
				    		contributor.profile = profile;
				    		SlimefunGuide.contributors.add(contributor);
				    	}
				    }
					SlimefunGuide.contributors.add(new Contributor("AquaLazuryt", "&6Lead Head Artist", 0));
				}
				
				@Override
				public void onFailure() {
					SlimefunGuide.contributors.add(new Contributor("TheBusyBiscuit", "&cAuthor", 3));
					SlimefunGuide.contributors.add(new Contributor("John000708", "&cAuthor", 2));
					SlimefunGuide.contributors.add(new Contributor("AquaLazuryt", "&6Lead Head Artist", 0));
				}
				
				@Override
				public String getRepository() {
					return "TheBusyBiscuit/Slimefun4";
				}
				
				@Override
				public String getFileName() {
					return "contributors";
				}

				@Override
				public String getURLSuffix() {
					return "/contributors";
				}
			});
			
			getServer().getScheduler().scheduleAsyncDelayedTask(this, new GitHubConnector() {
				
				@Override
				public void onSuccess(JsonElement element) {
					JsonObject object = element.getAsJsonObject();
					SlimefunGuide.issues = object.get("open_issues_count").getAsInt();
					SlimefunGuide.forks = object.get("forks").getAsInt();
				}
				
				@Override
				public void onFailure() {
				}
				
				@Override
				public String getRepository() {
					return "TheBusyBiscuit/Slimefun4";
				}
				
				@Override
				public String getFileName() {
					return "repo";
				}

				@Override
				public String getURLSuffix() {
					return "";
				}
			});
			
			getServer().getScheduler().scheduleAsyncDelayedTask(this, new GitHubConnector() {
				
				@Override
				public void onSuccess(JsonElement element) {
					JsonObject object = element.getAsJsonObject();
					SlimefunGuide.code_lines = object.get("Java").getAsInt();
				}
				
				@Override
				public void onFailure() {
				}
				
				@Override
				public String getRepository() {
					return "TheBusyBiscuit/Slimefun4";
				}
				
				@Override
				public String getFileName() {
					return "lines_of_code";
				}

				@Override
				public String getURLSuffix() {
					return "/languages";
				}
			});
			
			// Hooray!
			System.out.println("[Slimefun] Finished!");

			clearlag = getServer().getPluginManager().isPluginEnabled("ClearLag");

            Bukkit.getScheduler().scheduleSyncDelayedTask(this, new BukkitRunnable() {
                @Override
                public void run() {
                    exoticGarden = getServer().getPluginManager().isPluginEnabled("ExoticGarden"); //Had to do it this way, otherwise it seems disabled.
                }
            }, 0);

			if (clearlag) new ClearLaggIntegration(this);

			// Do not show /sf elevator command in our Log, it could get quite spammy
			CSCoreLib.getLib().filterLog("([A-Za-z0-9_]{3,16}) issued server command: /sf elevator (.{0,})");
		}
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);

		try {
			for (Map.Entry<Block, Block> entry: ticker.move.entrySet()) {
				BlockStorage._integrated_moveBlockInfo(entry.getKey(), entry.getValue());
			}
			ticker.move.clear();

			for (World world: Bukkit.getWorlds()) {
				BlockStorage storage = BlockStorage.getStorage(world);
				if (storage != null) storage.save(true);
				else System.err.println("[Slimefun] Could not save Slimefun Blocks for World \"" + world.getName() + "\"");
			}

			File folder = new File("data-storage/Slimefun/block-backups");
			List<File> backups = Arrays.asList(folder.listFiles());
			if (backups.size() > 20) {
				Collections.sort(backups, new Comparator<File>() {

					@Override
					public int compare(File f1, File f2) {
						try {
							return (int) (new SimpleDateFormat("yyyy-MM-dd-HH-mm").parse(f1.getName().replace(".zip", "")).getTime() - new SimpleDateFormat("yyyy-MM-dd-HH-mm").parse(f2.getName().replace(".zip", "")).getTime());
						} catch (ParseException e) {
							return 0;
						}
					}
				});

				for (int i = backups.size() - 20; i > 0; i--) {
					backups.get(i).delete();
				}
			}

			File file = new File("data-storage/Slimefun/block-backups/" + Clock.format(new Date()) + ".zip");
			byte[] buffer = new byte[1024];

			if (file.exists()) file.delete();

			try {
				file.createNewFile();

				ZipOutputStream output = new ZipOutputStream(new FileOutputStream(file));

				for (File f1: new File("data-storage/Slimefun/stored-blocks/").listFiles()) {
					for (File f: f1.listFiles()) {
						ZipEntry entry = new ZipEntry("stored-blocks/" + f1.getName() + "/" + f.getName());
						output.putNextEntry(entry);
						FileInputStream input = new FileInputStream(f);

						int length;
						while ((length = input.read(buffer)) > 0) {
							output.write(buffer, 0, length);
						}

						input.close();
						output.closeEntry();
					}
				}

				for (File f: new File("data-storage/Slimefun/universal-inventories/").listFiles()) {
					ZipEntry entry = new ZipEntry("universal-inventories/" + f.getName());
					output.putNextEntry(entry);
					FileInputStream input = new FileInputStream(f);

					int length;
					while ((length = input.read(buffer)) > 0) {
						output.write(buffer, 0, length);
					}

					input.close();
					output.closeEntry();
				}

				for (File f: new File("data-storage/Slimefun/stored-inventories/").listFiles()) {
					ZipEntry entry = new ZipEntry("stored-inventories/" + f.getName());
					output.putNextEntry(entry);
					FileInputStream input = new FileInputStream(f);

					int length;
					while ((length = input.read(buffer)) > 0) {
						output.write(buffer, 0, length);
					}

					input.close();
					output.closeEntry();
				}

				ZipEntry entry = new ZipEntry("stored-chunks/chunks.sfc");
				output.putNextEntry(entry);
				FileInputStream input = new FileInputStream(new File("data-storage/Slimefun/stored-chunks/chunks.sfc"));

				int length;
				while ((length = input.read(buffer)) > 0) {
					output.write(buffer, 0, length);
				}

				input.close();
				output.closeEntry();

				output.close();
				System.out.println("[Slimfun] Backed up Blocks to " + file.getName());
			} catch(IOException e) {
				e.printStackTrace();
			}
		} catch(Exception x) {
		}

		config = null;
		researches = null;
		items = null;
		whitelist = null;
		instance = null;
		Messages.local = null;
		Files.CONFIG = null;
		Files.DATABASE = null;
		Files.ITEMS = null;
		Files.RESEARCHES = null;
		Files.WHITELIST = null;
		MultiBlock.list = null;
		Research.list = null;
		Research.researching = null;
		SlimefunItem.all = null;
		SlimefunItem.items = null;
		SlimefunItem.map_name = null;
		SlimefunItem.handlers = null;
		SlimefunItem.radioactive = null;
		Variables.damage = null;
		Variables.jump = null;
		Variables.mode = null;
		SlimefunGuide.history = null;
		Variables.enchanting = null;
		Variables.backpack = null;
		Variables.soulbound = null;
		Variables.blocks = null;
		Variables.cancelPlace = null;
		Variables.arrows = null;
		SlimefunCommand.arguments = null;
		SlimefunCommand.descriptions = null;
		SlimefunCommand.tabs = null;
		URID.objects = null;
		URID.ids = null;
		SlimefunItem.blockhandler = null;
		BlockMenuPreset.presets = null;
		BlockStorage.loaded_tickers = null;
		BlockStorage.ticking_chunks = null;
		BlockStorage.worlds = null;
		ChargableBlock.capacitors = null;
		ChargableBlock.max_charges = null;
		AContainer.processing = null;
		AContainer.progress = null;
		Slimefun.guide_handlers = null;
		Pedestals.recipes = null;
		Elevator.ignored = null;
		EnergyNet.listeners = null;
		EnergyNet.machines_input = null;
		EnergyNet.machines_output = null;
		EnergyNet.machines_storage = null;
		CargoNet.faces = null;
		BlockStorage.universal_inventories = null;
		TickerTask.block_timings = null;
		OreGenSystem.map = null;
		SlimefunGuide.contributors = null;
		GitHubConnector.connectors = null;

		for (Player p: Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}
	}

	public static Config getCfg() {
		return config;
	}

	public static Config getResearchCfg() {
		return researches;
	}

	public static Config getItemCfg() {
		return items;
	}

	public static Config getWhitelist() {
		return whitelist;
	}

	public static int randomize(int max) {
		return CSCoreLib.randomizer().nextInt(max);
	}

	public static boolean chance(int max, int percentage) {
		return CSCoreLib.randomizer().nextInt(max) <= percentage;
	}

	public boolean isClearLagInstalled() {
		return clearlag;
	}

	public boolean isExoticGardenInstalled () {
		return exoticGarden;
	}
}
