package io.github.thebusybiscuit.slimefun4.core.researching;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.events.ResearchUnlockEvent;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun4.core.services.localization.Language;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.setup.ResearchSetup;
import io.github.thebusybiscuit.slimefun4.utils.FireworkUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * Represents a research, which is bound to one
 * {@link SlimefunItem} or more and requires XP levels to unlock said item(s).
 * 
 * @author TheBusyBiscuit
 * 
 * @see ResearchSetup
 * @see ResearchUnlockEvent
 * 
 */
public class Research implements Keyed {

    private static final int[] RESEARCH_PROGRESS = { 23, 44, 57, 92 };
    private static final String PLACEHOLDER_RESEARCH = "%research%";

    private final NamespacedKey key;
    private final int id;
    private final String name;
    private boolean enabled = true;
    private int cost;

    private final List<SlimefunItem> items = new LinkedList<>();

    /**
     * The constructor for a {@link Research}.
     * 
     * Create a new research, then bind this research to the Slimefun items you want by calling
     * {@link #addItems(SlimefunItem...)}. Once you're finished, call {@link #register()}
     * to register it.
     * 
     * @param key
     *            A unique identifier for this {@link Research}
     * @param id
     *            old way of identifying researches
     * @param defaultName
     *            The displayed name of this {@link Research}
     * @param defaultCost
     *            The Cost in XP levels to unlock this {@link Research}
     * 
     */
    public Research(@Nonnull NamespacedKey key, int id, @Nonnull String defaultName, int defaultCost) {
        Validate.notNull(key, "A NamespacedKey must be provided");
        Validate.notNull(defaultName, "A default name must be specified");

        this.key = key;
        this.id = id;
        this.name = defaultName;
        this.cost = defaultCost;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * This method returns whether this {@link Research} is enabled.
     * {@code false} can mean that this particular {@link Research} was disabled or that
     * researches altogether have been disabled.
     * 
     * @return Whether this {@link Research} is enabled or not
     */
    public boolean isEnabled() {
        return SlimefunPlugin.getRegistry().isResearchingEnabled() && enabled;
    }

    /**
     * Gets the ID of this {@link Research}.
     * This is the old way of identifying Researches, use a {@link NamespacedKey} in the future.
     * 
     * @deprecated Numeric Ids for Researches are deprecated, use {@link #getKey()} for identification instead.
     * 
     * @return The ID of this {@link Research}
     */
    @Deprecated
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
    @Nonnull
    public String getName(@Nonnull Player p) {
        String localized = SlimefunPlugin.getLocalization().getResearchName(p, key);
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
        if (cost < 0) {
            throw new IllegalArgumentException("Research cost must be zero or greater!");
        }

        this.cost = cost;
    }

    /**
     * Bind the specified {@link SlimefunItem SlimefunItems} to this {@link Research}.
     * 
     * @param items
     *            Instances of {@link SlimefunItem} to bind to this {@link Research}
     */
    public void addItems(SlimefunItem... items) {
        for (SlimefunItem item : items) {
            if (item != null) {
                item.setResearch(this);
            }
        }
    }

    /**
     * Bind the specified ItemStacks to this {@link Research}.
     * 
     * @param items
     *            Instances of {@link ItemStack} to bind to this {@link Research}
     * 
     * @return The current instance of {@link Research}
     */
    @Nonnull
    public Research addItems(ItemStack... items) {
        for (ItemStack item : items) {
            SlimefunItem sfItem = SlimefunItem.getByItem(item);

            if (sfItem != null) {
                sfItem.setResearch(this);
            }
        }

        return this;
    }

    /**
     * Lists every {@link SlimefunItem} that is bound to this {@link Research}.
     * 
     * @return The Slimefun items bound to this {@link Research}.
     */
    @Nonnull
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
    public boolean canUnlock(@Nonnull Player p) {
        if (!isEnabled()) {
            return true;
        }

        boolean creativeResearch = p.getGameMode() == GameMode.CREATIVE && SlimefunPlugin.getRegistry().isFreeCreativeResearchingEnabled();
        return creativeResearch || p.getLevel() >= cost;
    }

    /**
     * This unlocks this {@link Research} for the given {@link Player} without any form of callback.
     * 
     * @param p
     *            The {@link Player} who should unlock this {@link Research}
     * @param instant
     *            Whether to unlock it instantly
     */
    public void unlock(@Nonnull Player p, boolean instant) {
        unlock(p, instant, pl -> {});
    }

    /**
     * Unlocks this {@link Research} for the specified {@link Player}.
     * 
     * @param p
     *            The {@link Player} for which to unlock this {@link Research}
     * @param instant
     *            Whether to unlock this {@link Research} instantly
     * @param callback
     *            A callback which will be run when the {@link Research} animation completed
     */
    public void unlock(@Nonnull Player p, boolean instant, @Nonnull Consumer<Player> callback) {
        if (!instant) {
            SlimefunPlugin.runSync(() -> {
                p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 1F);
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.research.progress", true, msg -> msg.replace(PLACEHOLDER_RESEARCH, getName(p)).replace("%progress%", "0%"));
            }, 10L);
        }

        PlayerProfile.get(p, profile -> {
            if (!profile.hasUnlocked(this)) {
                SlimefunPlugin.runSync(() -> {
                    ResearchUnlockEvent event = new ResearchUnlockEvent(p, this);
                    Bukkit.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        if (instant) {
                            finishResearch(p, profile, callback);
                        } else if (SlimefunPlugin.getRegistry().getCurrentlyResearchingPlayers().add(p.getUniqueId())) {
                            SlimefunPlugin.getLocalization().sendMessage(p, "messages.research.start", true, msg -> msg.replace(PLACEHOLDER_RESEARCH, getName(p)));
                            playResearchAnimation(p);

                            SlimefunPlugin.runSync(() -> {
                                finishResearch(p, profile, callback);
                                SlimefunPlugin.getRegistry().getCurrentlyResearchingPlayers().remove(p.getUniqueId());
                            }, (RESEARCH_PROGRESS.length + 1) * 20L);
                        }
                    }
                });
            }
        });
    }

    private void finishResearch(@Nonnull Player p, @Nonnull PlayerProfile profile, @Nonnull Consumer<Player> callback) {
        profile.setResearched(this, true);
        SlimefunPlugin.getLocalization().sendMessage(p, "messages.unlocked", true, msg -> msg.replace(PLACEHOLDER_RESEARCH, getName(p)));
        callback.accept(p);

        if (SlimefunPlugin.getRegistry().isResearchFireworkEnabled() && SlimefunGuideSettings.hasFireworksEnabled(p)) {
            FireworkUtils.launchRandom(p, 1);
        }
    }

    private void playResearchAnimation(@Nonnull Player p) {
        for (int i = 1; i < RESEARCH_PROGRESS.length + 1; i++) {
            int j = i;

            SlimefunPlugin.runSync(() -> {
                p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 0.7F, 1F);
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.research.progress", true, msg -> msg.replace(PLACEHOLDER_RESEARCH, getName(p)).replace("%progress%", RESEARCH_PROGRESS[j - 1] + "%"));
            }, i * 20L);
        }
    }

    /**
     * Registers this {@link Research}.
     */
    public void register() {
        SlimefunPlugin.getResearchCfg().setDefaultValue("enable-researching", true);

        String path = key.getNamespace() + '.' + key.getKey();

        if (SlimefunPlugin.getResearchCfg().contains(path + ".enabled") && !SlimefunPlugin.getResearchCfg().getBoolean(path + ".enabled")) {
            for (SlimefunItem item : new ArrayList<>(items)) {
                if (item != null) {
                    item.setResearch(null);
                }
            }

            enabled = false;
            return;
        }

        SlimefunPlugin.getResearchCfg().setDefaultValue(path + ".cost", getCost());
        SlimefunPlugin.getResearchCfg().setDefaultValue(path + ".enabled", true);

        setCost(SlimefunPlugin.getResearchCfg().getInt(path + ".cost"));
        enabled = true;

        SlimefunPlugin.getRegistry().getResearches().add(this);
    }

    /**
     * Attempts to get a {@link Research} with the given {@link NamespacedKey}.
     * 
     * @param key
     *            the {@link NamespacedKey} of the {@link Research} you are looking for
     * 
     * @return An {@link Optional} with or without the found {@link Research}
     */
    @Nonnull
    public static Optional<Research> getResearch(@Nullable NamespacedKey key) {
        if (key == null) {
            return Optional.empty();
        }

        for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
            if (research.getKey().equals(key)) {
                return Optional.of(research);
            }
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Research (" + getKey() + ')';
    }
}