package io.github.thebusybiscuit.slimefun4.api.player;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.thebusybiscuit.slimefun4.storage.data.PlayerData;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import io.github.bakedlibs.dough.common.ChatColors;
import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.api.events.AsyncProfileLoadEvent;
import io.github.thebusybiscuit.slimefun4.api.gps.Waypoint;
import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.api.researches.Research;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectionType;
import io.github.thebusybiscuit.slimefun4.core.attributes.ProtectiveArmor;
import io.github.thebusybiscuit.slimefun4.core.guide.GuideHistory;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.armor.SlimefunArmorPiece;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;

/**
 * A class that can store a Player's {@link Research} progress for caching purposes.
 * It also holds the backpacks of a {@link Player}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Research
 * @see Waypoint
 * @see PlayerBackpack
 * @see HashedArmorpiece
 *
 */
public class PlayerProfile {

    private final UUID ownerId;
    private final String name;

    private final Config configFile;

    private boolean dirty = false;
    private boolean markedForDeletion = false;

    private final GuideHistory guideHistory = new GuideHistory(this);

    private final HashedArmorpiece[] armor = { new HashedArmorpiece(), new HashedArmorpiece(), new HashedArmorpiece(), new HashedArmorpiece() };

    private final PlayerData data;

    protected PlayerProfile(@Nonnull OfflinePlayer p, PlayerData data) {
        this.ownerId = p.getUniqueId();
        this.name = p.getName();
        this.data = data;

        configFile = new Config("data-storage/Slimefun/Players/" + ownerId.toString() + ".yml");
    }

    /**
     * This method provides a fast way to access the armor of a {@link Player}.
     * It returns a cached version, represented by {@link HashedArmorpiece}.
     * 
     * @return The cached armor for this {@link Player}
     */
    public @Nonnull HashedArmorpiece[] getArmor() {
        return armor;
    }

    /**
     * This returns the {@link Config} which is used to store the data.
     * Only intended for internal usage.
     * 
     * @return The {@link Config} associated with this {@link PlayerProfile}
     *
     * @deprecated Look at {@link PlayerProfile#getPlayerData()} instead for reading data.
     */
    @Deprecated
    public @Nonnull Config getConfig() {
        return configFile;
    }

    /**
     * This returns the {@link UUID} this {@link PlayerProfile} is linked to.
     * 
     * @return The {@link UUID} of our {@link PlayerProfile}
     */
    public @Nonnull UUID getUUID() {
        return ownerId;
    }

    /**
     * This method returns whether the {@link Player} has logged off.
     * If this is true, then the Profile can be removed from RAM.
     * 
     * @return Whether the Profile is marked for deletion
     */
    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    /**
     * This method returns whether the Profile has unsaved changes
     * 
     * @return Whether there are unsaved changes
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * This method will save the Player's Researches and Backpacks to the hard drive
     */
    public void save() {
        Slimefun.getPlayerStorage().savePlayerData(this.ownerId, this.data);
        dirty = false;
    }

    /**
     * This method sets the Player's "researched" status for this Research.
     * Use the boolean to unlock or lock the {@link Research}
     * 
     * @param research
     *            The {@link Research} that should be unlocked or locked
     * @param unlock
     *            Whether the {@link Research} should be unlocked or locked
     */
    public void setResearched(@Nonnull Research research, boolean unlock) {
        Validate.notNull(research, "Research must not be null!");
        dirty = true;

        if (unlock) {
            data.addResearch(research);
        } else {
            data.removeResearch(research);
        }
    }

    /**
     * This method returns whether the {@link Player} has unlocked the given {@link Research}
     * 
     * @param research
     *            The {@link Research} that is being queried
     * 
     * @return Whether this {@link Research} has been unlocked
     */
    public boolean hasUnlocked(@Nullable Research research) {
        if (research == null) {
            // No Research, no restriction
            return true;
        }

        return !research.isEnabled() || data.getResearches().contains(research);
    }

    /**
     * This method returns whether this {@link Player} has unlocked all {@link Research Researches}.
     * 
     * @return Whether they unlocked every {@link Research}
     */
    public boolean hasUnlockedEverything() {
        for (Research research : Slimefun.getRegistry().getResearches()) {
            // If there is a single Research not unlocked: They haven't unlocked everything.
            if (!hasUnlocked(research)) {
                return false;
            }
        }

        // Player has everything unlocked - Hooray!
        return true;
    }

    /**
     * This Method will return all Researches that this {@link Player} has unlocked
     * 
     * @return A {@code Hashset<Research>} of all Researches this {@link Player} has unlocked
     */
    public @Nonnull Set<Research> getResearches() {
        return ImmutableSet.copyOf(this.data.getResearches());
    }

    /**
     * This returns a {@link List} of all {@link Waypoint Waypoints} belonging to this
     * {@link PlayerProfile}.
     * 
     * @return A {@link List} containing every {@link Waypoint}
     */
    public @Nonnull List<Waypoint> getWaypoints() {
        return ImmutableList.copyOf(this.data.getWaypoints());
    }

    /**
     * This adds the given {@link Waypoint} to the {@link List} of {@link Waypoint Waypoints}
     * of this {@link PlayerProfile}.
     * 
     * @param waypoint
     *            The {@link Waypoint} to add
     */
    public void addWaypoint(@Nonnull Waypoint waypoint) {
        this.data.addWaypoint(waypoint);
        markDirty();
    }

    /**
     * This removes the given {@link Waypoint} from the {@link List} of {@link Waypoint Waypoints}
     * of this {@link PlayerProfile}.
     * 
     * @param waypoint
     *            The {@link Waypoint} to remove
     */
    public void removeWaypoint(@Nonnull Waypoint waypoint) {
        this.data.removeWaypoint(waypoint);
        markDirty();
    }

    /**
     * Call this method if the Player has left.
     * The profile can then be removed from RAM.
     */
    public final void markForDeletion() {
        markedForDeletion = true;
    }

    /**
     * Call this method if this Profile has unsaved changes.
     */
    public final void markDirty() {
        dirty = true;
    }

    public @Nonnull PlayerBackpack createBackpack(int size) {
        int nextId = this.data.getBackpacks().size(); // Size is not 0 indexed so next ID can just be the current size

        PlayerBackpack backpack = PlayerBackpack.newBackpack(this.ownerId, nextId, size);
        this.data.addBackpack(backpack);

        markDirty();

        return backpack;
    }

    public @Nonnull Optional<PlayerBackpack> getBackpack(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Backpacks cannot have negative ids!");
        }

        PlayerBackpack backpack = data.getBackpack(id);

        if (backpack != null) {
            markDirty();
            return Optional.of(backpack);
        }

        return Optional.empty();
    }

    private int countNonEmptyResearches(@Nonnull Collection<Research> researches) {
        int count = 0;
        for (Research research : researches) {
            if (research.hasEnabledItems()) {
                count++;
            }
        }
        return count;
    }

    /**
     * This method gets the research title, as defined in {@code config.yml},
     * of this {@link PlayerProfile} based on the fraction
     * of unlocked {@link Research}es of this player.
     *
     * @return The research title of this {@link PlayerProfile}
     */
    public @Nonnull String getTitle() {
        List<String> titles = Slimefun.getRegistry().getResearchRanks();

        int allResearches = countNonEmptyResearches(Slimefun.getRegistry().getResearches());
        float fraction = (float) countNonEmptyResearches(getResearches()) / allResearches;
        int index = (int) (fraction * (titles.size() - 1));

        return titles.get(index);
    }

    /**
     * This sends the statistics for the specified {@link CommandSender}
     * to the {@link CommandSender}. This includes research title, research progress
     * and total xp spent.
     *
     * @param sender The {@link CommandSender} for which to get the statistics and send them to.
     */
    public void sendStats(@Nonnull CommandSender sender) {
        int unlockedResearches = countNonEmptyResearches(getResearches());
        int levels = getResearches().stream().mapToInt(Research::getCost).sum();
        int allResearches = countNonEmptyResearches(Slimefun.getRegistry().getResearches());

        float progress = Math.round(((unlockedResearches * 100.0F) / allResearches) * 100.0F) / 100.0F;

        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&7Statistics for Player: &b" + name));
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&7Title: " + ChatColor.AQUA + getTitle()));
        sender.sendMessage(ChatColors.color("&7Research Progress: " + NumberUtils.getColorFromPercentage(progress) + progress + " &r% " + ChatColor.YELLOW + '(' + unlockedResearches + " / " + allResearches + ')'));
        sender.sendMessage(ChatColors.color("&7Total XP Levels spent: " + ChatColor.AQUA + levels));
    }

    /**
     * This returns the {@link Player} who this {@link PlayerProfile} belongs to.
     * If the {@link Player} is offline, null will be returned.
     * 
     * @return The {@link Player} of this {@link PlayerProfile} or null
     */
    public @Nullable Player getPlayer() {
        return Bukkit.getPlayer(getUUID());
    }

    /**
     * This returns the {@link GuideHistory} of this {@link Player}.
     * It is basically that player's browsing history.
     * 
     * @return The {@link GuideHistory} of this {@link Player}
     */
    public @Nonnull GuideHistory getGuideHistory() {
        return guideHistory;
    }

    public static boolean fromUUID(@Nonnull UUID uuid, @Nonnull Consumer<PlayerProfile> callback) {
        return get(Bukkit.getOfflinePlayer(uuid), callback);
    }

    /**
     * Get the {@link PlayerProfile} for a {@link OfflinePlayer} asynchronously.
     *
     * @param p
     *            The {@link OfflinePlayer} who's {@link PlayerProfile} to retrieve
     * @param callback
     *            The callback with the {@link PlayerProfile}
     * 
     * @return If the {@link OfflinePlayer} was cached or not.
     */
    public static boolean get(@Nonnull OfflinePlayer p, @Nonnull Consumer<PlayerProfile> callback) {
        Validate.notNull(p, "Cannot get a PlayerProfile for: null!");

        UUID uuid = p.getUniqueId();
        PlayerProfile profile = Slimefun.getRegistry().getPlayerProfiles().get(uuid);

        if (profile != null) {
            callback.accept(profile);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(Slimefun.instance(), () -> {
            PlayerData data = Slimefun.getPlayerStorage().loadPlayerData(p.getUniqueId());

            AsyncProfileLoadEvent event = new AsyncProfileLoadEvent(new PlayerProfile(p, data));
            Bukkit.getPluginManager().callEvent(event);

            Slimefun.getRegistry().getPlayerProfiles().put(uuid, event.getProfile());
            callback.accept(event.getProfile());
        });

        return false;
    }

    /**
     * This requests an instance of {@link PlayerProfile} to be loaded for the given {@link OfflinePlayer}.
     * This method will return true if the {@link PlayerProfile} was already found.
     * 
     * @param p
     *            The {@link OfflinePlayer} to request the {@link PlayerProfile} for.
     * 
     * @return Whether the {@link PlayerProfile} was already loaded
     */
    public static boolean request(@Nonnull OfflinePlayer p) {
        Validate.notNull(p, "Cannot request a Profile for null");

        if (!Slimefun.getRegistry().getPlayerProfiles().containsKey(p.getUniqueId())) {
            // Should probably prevent multiple requests for the same profile in the future
            Bukkit.getScheduler().runTaskAsynchronously(Slimefun.instance(), () -> {
                PlayerData data = Slimefun.getPlayerStorage().loadPlayerData(p.getUniqueId());

                PlayerProfile pp = new PlayerProfile(p, data);
                Slimefun.getRegistry().getPlayerProfiles().put(p.getUniqueId(), pp);
            });

            return false;
        }

        return true;
    }

    /**
     * This method tries to search for a {@link PlayerProfile} of the given {@link OfflinePlayer}.
     * The result of this method is an {@link Optional}, if no {@link PlayerProfile} was found, an empty
     * {@link Optional} will be returned.
     * 
     * @param p
     *            The {@link OfflinePlayer} to get the {@link PlayerProfile} for
     * 
     * @return An {@link Optional} describing the result
     */
    public static @Nonnull Optional<PlayerProfile> find(@Nonnull OfflinePlayer p) {
        return Optional.ofNullable(Slimefun.getRegistry().getPlayerProfiles().get(p.getUniqueId()));
    }

    public static @Nonnull Iterator<PlayerProfile> iterator() {
        return Slimefun.getRegistry().getPlayerProfiles().values().iterator();
    }

    public static void getBackpack(@Nullable ItemStack item, @Nonnull Consumer<PlayerBackpack> callback) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return;
        }

        OptionalInt id = OptionalInt.empty();
        String uuid = "";

        for (String line : item.getItemMeta().getLore()) {
            if (line.startsWith(ChatColors.color("&7ID: ")) && line.indexOf('#') != -1) {
                String[] splitLine = CommonPatterns.HASH.split(line);

                if (CommonPatterns.NUMERIC.matcher(splitLine[1]).matches()) {
                    id = OptionalInt.of(Integer.parseInt(splitLine[1]));
                    uuid = splitLine[0].replace(ChatColors.color("&7ID: "), "");
                }
            }
        }

        if (id.isPresent()) {
            int number = id.getAsInt();

            fromUUID(UUID.fromString(uuid), profile -> {
                Optional<PlayerBackpack> backpack = profile.getBackpack(number);
                backpack.ifPresent(callback);
            });
        }
    }

    public boolean hasFullProtectionAgainst(@Nonnull ProtectionType type) {
        Validate.notNull(type, "ProtectionType must not be null.");

        int armorCount = 0;
        NamespacedKey setId = null;

        for (HashedArmorpiece armorpiece : armor) {
            Optional<SlimefunArmorPiece> armorPiece = armorpiece.getItem();
            if (armorPiece.isPresent() && armorPiece.get() instanceof ProtectiveArmor protectiveArmor) {
                for (ProtectionType protectionType : protectiveArmor.getProtectionTypes()) {
                    if (protectionType == type) {
                        if (!protectiveArmor.isFullSetRequired()) {
                            return true;
                        } else if (setId == null || setId.equals(protectiveArmor.getArmorSetId())) {
                            armorCount++;
                            setId = protectiveArmor.getArmorSetId();
                        }
                    }
                }
            }
        }

        return armorCount == 4;
    }

    public PlayerData getPlayerData() {
        return this.data;
    }

    @Override
    public int hashCode() {
        return ownerId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayerProfile profile && ownerId.equals(profile.ownerId);
    }

    @Override
    public String toString() {
        return "PlayerProfile {" + ownerId + "}";
    }

}
