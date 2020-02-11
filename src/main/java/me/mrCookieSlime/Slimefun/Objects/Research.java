package me.mrCookieSlime.Slimefun.Objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.events.ResearchUnlockEvent;
import io.github.thebusybiscuit.slimefun4.core.guide.GuideSettings;
import io.github.thebusybiscuit.slimefun4.implementation.setup.ResearchSetup;
import io.github.thebusybiscuit.slimefun4.utils.FireworkUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * Statically handles researches. Represents a research, which is bound to one
 * {@link SlimefunItem} or more and require XP levels to unlock this/these item(s).
 * 
 * See {@link ResearchSetup} for the built-in researches.
 * 
 * @author TheBusyBiscuit
 */
public class Research implements Keyed {

	private static final int[] RESEARCH_PROGRESS = {23, 44, 57, 92};

	private final NamespacedKey key;
	private final int id;
	private String name;
	private int cost;
	
	private final List<SlimefunItem> items = new LinkedList<>();
	private boolean enabled = true;

	/**
	 * The constructor for a Research.
	 * 
	 * Create a new research by calling {@link #Research(int, String, int)}, then
	 * bind this research to the Slimefun items you want by calling
	 * {@link #addItems(SlimefunItem...)}. Once you're finished, call {@link #register()}
	 * to register it.
	 * 
	 * To speed up, directly setup the research by calling 
	 * {@link Slimefun#registerResearch(Research, org.bukkit.inventory.ItemStack...)}.
	 * 
	 * @param key	A unique identifier for this research
	 * @param id 	old way of identifying researches
	 * @param name Display name of the research
	 * @param cost Cost in XP levels to unlock the research
	 * 
	 */
	public Research(NamespacedKey key, int id, String name, int defaultCost) {
		this.key = key;
		this.id = id;
		this.name = name;
		this.cost = defaultCost;
	}
	
	@Override
	public NamespacedKey getKey() {
		return key;
	}
	
	public boolean isEnabled() {
		return SlimefunPlugin.getSettings().researchesEnabled && enabled;
	}

	/**
	 * Gets the ID of the research.
	 * 
	 * @return ID of the research
	 * 
	 * @since 4.0
	 */
	public int getID() {
		return id;
	}
	
	public String getName(Player p) {
		String localized = SlimefunPlugin.getLocal().getResearchName(p, key);
		return localized != null ? localized: name;
	}

	/**
	 * Gets the cost in XP levels to unlock the research.
	 * 
	 * @return The cost in XP levels of the research
	 * @since 4.1.10
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Sets the cost in XP levels to unlock the research.
	 * 
	 * @param cost Cost in XP levels
	 * 
	 * @since 4.1.10
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * Bind the specified Slimefun items to the research.
	 * 
	 * @param items {@link SlimefunItem} to bind to the research
	 * 
	 * @since 4.0
	 */
	public void addItems(SlimefunItem... items) {
		for (SlimefunItem item : items) {
			if (item != null) item.bindToResearch(this);
		}
	}

	/**
	 * Gets the list of the Slimefun items bound to the research.
	 * 
	 * @return the Slimefun items bound to the research
	 * 
	 * @since 4.0
	 */
	public List<SlimefunItem> getAffectedItems() {
		return items;
	}

	/**
	 * Convenience method to check if the player unlocked this research.
	 * 
	 * @param p Player to check
	 * @return true if he unlocked the research, otherwise false
	 * 
	 * @since 4.0
	 * @see #hasUnlocked(UUID)
	 */
	@Deprecated
	public boolean hasUnlocked(Player p) {
		return hasUnlocked(p.getUniqueId());
	}

	/**
	 * Checks if the player unlocked this research.
	 * 
	 * @param uuid UUID of the player to check
	 * @return true if he unlocked the research, otherwise false
	 * 
	 * @since 4.0
	 * @see #hasUnlocked(Player)
	 */
	@Deprecated
	public boolean hasUnlocked(UUID uuid) {
		return PlayerProfile.fromUUID(uuid).hasUnlocked(this);
	}

	/**
	 * Checks if the player can unlock this research.
	 * 
	 * @param p Player to check
	 * @return true if he can unlock the research, otherwise false
	 * 
	 * @since 4.1.10
	 */
	public boolean canUnlock(Player p) {
		if (!isEnabled()) return true;
		return (p.getGameMode() == GameMode.CREATIVE && SlimefunPlugin.getSettings().researchesFreeInCreative) || p.getLevel() >= this.cost;
	}

	/**
	 * Unlocks the research for the specified player.
	 * 
	 * @param p Player to unlock the research
	 * @param instant Whether to unlock the research instantly
	 * 
	 * @since 4.0
	 */
	public void unlock(final Player p, boolean instant) {
		if (!instant) {
			Slimefun.runSync(() -> {
				p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 1F);
				SlimefunPlugin.getLocal().sendMessage(p, "messages.research.progress", true, msg -> msg.replace("%research%", getName(p)).replace("%progress%", "0%"));
			}, 10L);
		}
		
		PlayerProfile.get(p, profile -> {
			if (!profile.hasUnlocked(this)) {
				Runnable runnable = () -> {
					profile.setResearched(this, true);
					SlimefunPlugin.getLocal().sendMessage(p, "messages.unlocked", true, msg -> msg.replace("%research%", getName(p)));
					
					if (SlimefunPlugin.getSettings().researchFireworksEnabled && (!PersistentDataAPI.hasByte(p, GuideSettings.FIREWORKS_KEY) || PersistentDataAPI.getByte(p, GuideSettings.FIREWORKS_KEY) == (byte) 1)) {
						FireworkUtils.launchRandom(p, 1);
					}
				};
				
				Slimefun.runSync(() -> {
					ResearchUnlockEvent event = new ResearchUnlockEvent(p, this);
					Bukkit.getPluginManager().callEvent(event);
					
					if (!event.isCancelled()) {
						if (instant) {
							runnable.run();
						}
						else if (SlimefunPlugin.getRegistry().getCurrentlyResearchingPlayers().add(p.getUniqueId())) {
							SlimefunPlugin.getLocal().sendMessage(p, "messages.research.start", true, msg -> msg.replace("%research%", getName(p)));
							
							for (int i = 1; i < RESEARCH_PROGRESS.length + 1; i++) {
								int j = i;
								
								Slimefun.runSync(() -> {
									p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 1F);
									SlimefunPlugin.getLocal().sendMessage(p, "messages.research.progress", true, msg -> msg.replace("%research%", getName(p)).replace("%progress%", RESEARCH_PROGRESS[j - 1] + "%"));
								}, i * 20L);
							}
							
							Slimefun.runSync(() -> {
								runnable.run();
								SlimefunPlugin.getRegistry().getCurrentlyResearchingPlayers().remove(p.getUniqueId());
							}, (RESEARCH_PROGRESS.length + 1) * 20L);
						}
					}
				});
			}
		});
	}

	/**
	 * Registers the research.
	 * 
	 * @since 4.0
	 */
	public void register() {
		SlimefunPlugin.getResearchCfg().setDefaultValue("enable-researching", true);

		if (SlimefunPlugin.getResearchCfg().contains(this.getID() + ".enabled") && !SlimefunPlugin.getResearchCfg().getBoolean(this.getID() + ".enabled")) {
			Iterator<SlimefunItem> iterator = items.iterator();
			while (iterator.hasNext()) {
				SlimefunItem item = iterator.next();
				if (item != null) item.bindToResearch(null);
				iterator.remove();
			}
			return;
		}

		SlimefunPlugin.getResearchCfg().setDefaultValue(this.getID() + ".cost", this.getCost());
		SlimefunPlugin.getResearchCfg().setDefaultValue(this.getID() + ".enabled", true);
		
		this.cost = SlimefunPlugin.getResearchCfg().getInt(this.getID() + ".cost");
		this.enabled = SlimefunPlugin.getResearchCfg().getBoolean(this.getID() + ".enabled");

		SlimefunPlugin.getRegistry().getResearches().add(this);
		SlimefunPlugin.getRegistry().getResearchIds().add(this);
		
		if (SlimefunPlugin.getSettings().printOutLoading) {
			Slimefun.getLogger().log(Level.INFO, "Loaded Research \"{0}\"", name);
		}
	}

	/**
	 * Gets if the specified player is currently unlocking a research.
	 * 
	 * @param p Player to check
	 * @return true if the player is unlocking a research, otherwise false
	 * 
	 * @since 4.0
	 */
	public static boolean isResearching(Player p) {
		return SlimefunPlugin.getRegistry().getCurrentlyResearchingPlayers().contains(p.getUniqueId());
	}

	/**
	 * Gets the title of the specified player.
	 * 
	 * @param p Player to get the rank
	 * @param researched List of the player's unlocked researches
	 * @return the title of the specified player
	 * 
	 * @since 4.0
	 * @see #sendStats(CommandSender, Player)
	 */
	@Deprecated
	public static String getTitle(Player p, Collection<Research> researched) {
		return PlayerProfile.get(p).getTitle();
	}

	/**
	 * Attempts to get the research with the given ID.
	 * 
	 * @param id ID of the research to get
	 * @return Research if found, or null
	 * 
	 * @since 4.0
	 */
	public static Research getByID(int id) {
		for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
			if (research.getID() == id) return research;
		}
		return null;
	}

	/**
	 * Gets the list of unlocked researches for a player using his UUID.
	 * 
	 * @param uuid UUID of the player
	 * @return the list of unlocked researches for the player
	 * 
	 * @since 4.0
	 * @see #getResearches(String)
	 */
	@Deprecated
	public static List<Research> getResearches(UUID uuid) {
		List<Research> researched = new ArrayList<>();
		for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
			if (research.hasUnlocked(uuid)) researched.add(research);
		}
		return researched;
	}
	
	@Override
	public String toString() {
		return "Research {" + id + ',' + name + "}";
	}
}