package me.mrCookieSlime.Slimefun.Objects;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.data.PersistentDataAPI;
import io.github.thebusybiscuit.slimefun4.api.events.ResearchUnlockEvent;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
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
 * @author TheBusyBiscuit
 * 
 * @see ResearchSetup
 * @see ResearchUnlockEvent
 * 
 */
public class Research implements Keyed {

    private static final int[] RESEARCH_PROGRESS = { 23, 44, 57, 92 };

    private final NamespacedKey key;
    private final int id;
    private String name;
    private int cost;

    private final List<SlimefunItem> items = new LinkedList<>();
    private boolean enabled = true;

    /**
     * The constructor for a {@link Research}.
     * 
     * Create a new research, then bind this research to the Slimefun items you want by calling
     * {@link #addItems(SlimefunItem...)}. Once you're finished, call {@link #register()}
     * to register it.
     * 
     * To speed up, directly setup the research by calling
     * {@link Slimefun#registerResearch(Research, org.bukkit.inventory.ItemStack...)}.
     * 
     * @param key
     *            A unique identifier for this {@link Research}
     * @param id
     *            old way of identifying researches
     * @param name
     *            The displayed name of this {@link Research}
     * @param defaultCost
     *            The Cost in XP levels to unlock this {@link Research}
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

    /**
     * This method returns whether this {@link Research} is enabled.
     * {@code false} can mean that this particular {@link Research} was disabled or that
     * researches alltogether have been disabled.
     * 
     * @return Whether this {@link Research} is enabled or not
     */
    public boolean isEnabled() {
        return SlimefunPlugin.getSettings().researchesEnabled && enabled;
    }

    /**
     * Gets the ID of this {@link Research}.
     * This is the old way of identifying Researches, use a {@link NamespacedKey} in the future.
     * 
     * @return The ID of this {@link Research}
     */
    public int getID() {
        return id;
    }

    /**
     * This method gives you a localized name for this {@link Research}.
     * The name is automatically taken from the currently selected {@link Language} of
     * the specified {@link Player}.
     * 
     * @param p
     *            The {@link Player} to translate this name for.
     * @return The localized Name of this {@link Research}.
     */
    public String getName(Player p) {
        String localized = SlimefunPlugin.getLocal().getResearchName(p, key);
        return localized != null ? localized : name;
    }

    /**
     * Gets the cost in XP levels to unlock this {@link Research}.
     * 
     * @return The cost in XP levels for this {@link Research}
     */
    public int getCost() {
        return cost;
    }

    /**
     * Sets the cost in XP levels to unlock this {@link Research}.
     * 
     * @param cost
     *            The cost in XP levels
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Bind the specified Slimefun items to this {@link Research}.
     * 
     * @param items
     *            Instances of {@link SlimefunItem} to bind to this {@link Research}
     */
    public void addItems(SlimefunItem... items) {
        for (SlimefunItem item : items) {
            if (item != null) {
                item.bindToResearch(this);
            }
        }
    }

    /**
     * Lists every {@link SlimefunItem} that is bound to this {@link Research}.
     * 
     * @return The Slimefun items bound to this {@link Research}.
     */
    public List<SlimefunItem> getAffectedItems() {
        return items;
    }

    /**
     * Checks if the {@link Player} can unlock this {@link Research}.
     * 
     * @param p
     *            The {@link Player} to check
     * @return Whether that {@link Player} can unlock this {@link Research}
     */
    public boolean canUnlock(Player p) {
        if (!isEnabled()) return true;
        return (p.getGameMode() == GameMode.CREATIVE && SlimefunPlugin.getSettings().researchesFreeInCreative) || p.getLevel() >= this.cost;
    }

    /**
     * Unlocks this {@link Research} for the specified {@link Player}.
     * 
     * @param p
     *            The {@link Player} for which to unlock this {@link Research}
     * @param instant
     *            Whether to unlock the research instantly
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

                    if (SlimefunPlugin.getSettings().researchFireworksEnabled && (!PersistentDataAPI.hasByte(p, SlimefunGuideSettings.FIREWORKS_KEY) || PersistentDataAPI.getByte(p, SlimefunGuideSettings.FIREWORKS_KEY) == (byte) 1)) {
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
     * Registers this {@link Research}.
     */
    public void register() {
        SlimefunPlugin.getResearchCfg().setDefaultValue("enable-researching", true);

        String path = key.getNamespace() + '.' + key.getKey();
        migrate(id, path);

        if (SlimefunPlugin.getResearchCfg().contains(path + ".enabled") && !SlimefunPlugin.getResearchCfg().getBoolean(path + ".enabled")) {
            Iterator<SlimefunItem> iterator = items.iterator();
            while (iterator.hasNext()) {
                SlimefunItem item = iterator.next();
                if (item != null) item.bindToResearch(null);
                iterator.remove();
            }
            return;
        }

        SlimefunPlugin.getResearchCfg().setDefaultValue(path + ".cost", this.getCost());
        SlimefunPlugin.getResearchCfg().setDefaultValue(path + ".enabled", true);

        this.cost = SlimefunPlugin.getResearchCfg().getInt(path + ".cost");
        this.enabled = SlimefunPlugin.getResearchCfg().getBoolean(path + ".enabled");

        SlimefunPlugin.getRegistry().getResearches().add(this);
        SlimefunPlugin.getRegistry().getResearchIds().add(this);

        if (SlimefunPlugin.getSettings().printOutLoading) {
            Slimefun.getLogger().log(Level.INFO, "Loaded Research \"{0}\"", name);
        }
    }

    // Temporary migration method from ids to Namespaced Keys.
    private void migrate(int id, String path) {
        if (SlimefunPlugin.getResearchCfg().contains(id + ".enabled")) {
            SlimefunPlugin.getResearchCfg().setValue(path + ".enabled", SlimefunPlugin.getResearchCfg().getBoolean(id + ".enabled"));
        }

        if (SlimefunPlugin.getResearchCfg().contains(id + ".cost")) {
            SlimefunPlugin.getResearchCfg().setValue(path + ".cost", SlimefunPlugin.getResearchCfg().getInt(id + ".cost"));
        }

        SlimefunPlugin.getResearchCfg().setValue(String.valueOf(id), null);
    }

    /**
     * Attempts to get a {@link Research} with the given ID.
     * 
     * We will use {@link NamespacedKey} for this in the future.
     * 
     * @param id
     *            ID of the research to get
     * @return {@link Research} if found, or null
     */
    public static Research getByID(int id) {
        for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
            if (research.getID() == id) return research;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Research {" + id + ',' + name + "}";
    }
}