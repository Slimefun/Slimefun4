package me.mrCookieSlime.Slimefun.Objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.events.ResearchUnlockEvent;
import me.mrCookieSlime.CSCoreLibPlugin.general.Particles.FireworkShow;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Setup.ResearchSetup;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * Statically handles researches. Represents a research, which is bound to one
 * {@link SlimefunItem} or more and require XP levels to unlock this/these item(s).
 * <p>
 * See {@link #Research(int, String, int)} to create a research.
 * <p>
 * See {@link ResearchSetup} for the built-in researches.
 * 
 * @author TheBusyBiscuit
 * @since 4.0
 */
public class Research {

	private static final int[] RESEARCH_PROGRESS = {23, 44, 57, 92};

	private int id;
	private String name;
	private List<SlimefunItem> items;
	private int cost;
	private boolean enabled;

	/**
	 * The constructor for a Research.
	 * <p>
	 * Create a new research by calling {@link #Research(int, String, int)}, then
	 * bind this research to the Slimefun items you want by calling
	 * {@link #addItems(SlimefunItem...)}. Once you're finished, call {@link #register()}
	 * to register it.
	 * <p>
	 * To speed up, directly setup the research by calling 
	 * {@link Slimefun#registerResearch(Research, org.bukkit.inventory.ItemStack...)}.
	 * 
	 * @param id Unique integer ID for this research, used for {@link #getByID(int)} and to
	 *           register it in Researches.yml
	 * @param name Display name of the research
	 * @param cost Cost in XP levels to unlock the research
	 * 
	 * @since 4.0
	 */
	public Research(int id, String name, int cost) {
		this.id = id;
		this.name = name;
		this.cost = cost;
		this.items = new ArrayList<>();
		this.enabled = true;
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

	/**
	 * Gets the display name of the research.
	 * 
	 * @return The display name of the research
	 * 
	 * @since 4.0
	 */
	public String getName() {
		return name;
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
		for (SlimefunItem item: items) {
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
	 * Locks the research for the specified player.
	 * 
	 * @param p Player to lock the research
	 * 
	 * @since 4.0
	 */
	@Deprecated
	public void lock(Player p) {
		PlayerProfile.get(p).setResearched(this, false);
		SlimefunPlugin.getLocal().sendMessage(p, "commands.research.reset-target", true);
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
				SlimefunPlugin.getLocal().sendMessage(p, "messages.research.progress", true, msg -> msg.replace("%research%", getName()).replace("%progress%", "0%"));
			}, 10L);
		}
		
		PlayerProfile.get(p, profile -> {
			if (!profile.hasUnlocked(this)) {
				Runnable runnable = () -> {
					profile.setResearched(this, true);
					SlimefunPlugin.getLocal().sendMessage(p, "messages.unlocked", true, msg -> msg.replace("%research%", getName()));
					
					if (SlimefunPlugin.getCfg().getBoolean("options.research-unlock-fireworks")) {
						FireworkShow.launchRandom(p, 1);
					}
				};
				
				Slimefun.runSync(() -> {
					ResearchUnlockEvent event = new ResearchUnlockEvent(p, this);
					Bukkit.getPluginManager().callEvent(event);
					
					if (!event.isCancelled()) {
						if (instant) {
							runnable.run();
						}
						else if (!SlimefunPlugin.getUtilities().researching.contains(p.getUniqueId())){
							SlimefunPlugin.getUtilities().researching.add(p.getUniqueId());
							SlimefunPlugin.getLocal().sendMessage(p, "messages.research.start", true, msg -> msg.replace("%research%", getName()));
							
							for (int i = 1; i < RESEARCH_PROGRESS.length + 1; i++) {
								int j = i;
								
								Slimefun.runSync(() -> {
									p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 1F);
									SlimefunPlugin.getLocal().sendMessage(p, "messages.research.progress", true, msg -> msg.replace("%research%", getName()).replace("%progress%", RESEARCH_PROGRESS[j - 1] + "%"));
								}, i * 20L);
							}
							
							Slimefun.runSync(() -> {
								runnable.run();
								SlimefunPlugin.getUtilities().researching.remove(p.getUniqueId());
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

		SlimefunPlugin.getResearchCfg().setDefaultValue(this.getID() + ".name", this.getName());
		SlimefunPlugin.getResearchCfg().setDefaultValue(this.getID() + ".cost", this.getCost());
		SlimefunPlugin.getResearchCfg().setDefaultValue(this.getID() + ".enabled", true);

		this.name = SlimefunPlugin.getResearchCfg().getString(this.getID() + ".name");
		this.cost = SlimefunPlugin.getResearchCfg().getInt(this.getID() + ".cost");
		this.enabled = SlimefunPlugin.getResearchCfg().getBoolean(this.getID() + ".enabled");

		SlimefunPlugin.getUtilities().allResearches.add(this);
		if (SlimefunPlugin.getSettings().printOutLoading) {
			Slimefun.getLogger().log(Level.INFO, "Loaded Research \"" + this.getName() + "\"");
		}
	}

	/**
	 * Gets the list of all registered researches.
	 * 
	 * @return The list of registered researches
	 * 
	 * @since 4.0
	 * @see ResearchSetup
	 */
	public static List<Research> list() {
		return SlimefunPlugin.getUtilities().allResearches;
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
		return SlimefunPlugin.getUtilities().researching.contains(p.getUniqueId());
	}

	/**
	 * Sends the research statistics and title of the specified player to the command sender.
	 * 
	 * @param sender CommandSender to send the statistics
	 * @param p Player to get the statistics
	 * 
	 * @since 4.0
	 * @see #getTitle(Player, List)
	 */
	@Deprecated
	public static void sendStats(CommandSender sender, Player p) {
		PlayerProfile.get(p).sendStats(sender);
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
		for (Research research: list()) {
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
		for (Research r: list()) {
			if (r.hasUnlocked(uuid)) researched.add(r);
		}
		return researched;
	}

	/**
	 * Convenience method to get the list of unlocked researches
	 * for a player using his UUID (specified as a String).
	 * 
	 * @param uuid String representing the UUID of the player
	 * @return the list of unlocked researches for the player
	 * 
	 * @since 4.0
	 * @see #getResearches(UUID)
	 */
	@Deprecated
	public static List<Research> getResearches(String uuid) {
		return getResearches(UUID.fromString(uuid));
	}
	
	@Override
	public String toString() {
		return "Research {" + id + ',' + name + "}";
	}
}