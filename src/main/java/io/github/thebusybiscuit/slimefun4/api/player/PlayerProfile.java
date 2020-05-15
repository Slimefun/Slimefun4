package io.github.thebusybiscuit.slimefun4.api.player;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.api.items.HashedArmorpiece;
import io.github.thebusybiscuit.slimefun4.core.guide.GuideHistory;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;

/**
 * A class that can store a Player's {@link Research} progress for caching purposes.
 * It also holds the backpacks of a {@link Player}.
 * 
 * @author TheBusyBiscuit
 * 
 * @see Research
 * @see PlayerBackpack
 *
 */
public final class PlayerProfile {

    private final UUID uuid;
    private final String name;
    private final Config cfg;

    private boolean dirty = false;
    private boolean markedForDeletion = false;

    private final Set<Research> researches = new HashSet<>();
    private final Map<Integer, PlayerBackpack> backpacks = new HashMap<>();
    private final GuideHistory guideHistory = new GuideHistory(this);

    private final HashedArmorpiece[] armor = { new HashedArmorpiece(), new HashedArmorpiece(), new HashedArmorpiece(), new HashedArmorpiece() };

    private PlayerProfile(OfflinePlayer p) {
        this.uuid = p.getUniqueId();
        this.name = p.getName();

        cfg = new Config(new File("data-storage/Slimefun/Players/" + uuid.toString() + ".yml"));

        for (Research research : SlimefunPlugin.getRegistry().getResearches()) {
            if (cfg.contains("researches." + research.getID())) {
                researches.add(research);
            }
        }
    }

    public HashedArmorpiece[] getArmor() {
        return armor;
    }

    public Config getConfig() {
        return cfg;
    }

    /**
     * This returns the {@link UUID} this {@link PlayerProfile} is linked to.
     * 
     * @return The {@link UUID} of our {@link PlayerProfile}
     */
    public UUID getUUID() {
        return uuid;
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
        for (PlayerBackpack backpack : backpacks.values()) {
            backpack.save();
        }

        cfg.save();
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
    public void setResearched(Research research, boolean unlock) {
        Validate.notNull(research, "Research must not be null!");
        dirty = true;

        if (unlock) {
            cfg.setValue("researches." + research.getID(), true);
            researches.add(research);
        }
        else {
            cfg.setValue("researches." + research.getID(), null);
            researches.remove(research);
        }
    }

    /**
     * This method returns whether the {@link Player} has unlocked the given {@link Research}
     * 
     * @param research
     *            The {@link Research} that is being queried
     * @return Whether this {@link Research} has been unlocked
     */
    public boolean hasUnlocked(Research research) {
        if (research == null) {
            // No Research, no restriction
            return true;
        }

        return !research.isEnabled() || researches.contains(research);
    }

    /**
     * This Method will return all Researches that this {@link Player} has unlocked
     * 
     * @return A {@code Hashset<Research>} of all Researches this {@link Player} has unlocked
     */
    public Set<Research> getResearches() {
        return researches;
    }

    /**
     * Call this method if the Player has left.
     * The profile can then be removed from RAM.
     */
    public void markForDeletion() {
        this.markedForDeletion = true;
    }

    /**
     * Call this method if this Profile has unsaved changes.
     */
    public void markDirty() {
        this.dirty = true;
    }

    public PlayerBackpack createBackpack(int size) {
        IntStream stream = IntStream.iterate(0, i -> i + 1).filter(i -> !cfg.contains("backpacks." + i + ".size"));
        int id = stream.findFirst().getAsInt();

        PlayerBackpack backpack = new PlayerBackpack(this, id, size);
        backpacks.put(id, backpack);

        return backpack;
    }

    public Optional<PlayerBackpack> getBackpack(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Backpacks cannot have negative ids!");
        }

        PlayerBackpack backpack = backpacks.get(id);

        if (backpack != null) {
            return Optional.of(backpack);
        }
        else if (cfg.contains("backpacks." + id + ".size")) {
            backpack = new PlayerBackpack(this, id);
            backpacks.put(id, backpack);
            return Optional.of(backpack);
        }

        return Optional.empty();
    }

    public String getTitle() {
        List<String> titles = SlimefunPlugin.getRegistry().getResearchRanks();

        float fraction = (float) researches.size() / SlimefunPlugin.getRegistry().getResearches().size();
        int index = (int) (fraction * (titles.size() - 1));

        return titles.get(index);
    }

    public void sendStats(CommandSender sender) {
        Set<Research> researched = getResearches();
        int levels = researched.stream().mapToInt(Research::getCost).sum();
        int totalResearches = SlimefunPlugin.getRegistry().getResearches().size();

        float progress = Math.round(((researched.size() * 100.0F) / totalResearches) * 100.0F) / 100.0F;

        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&7Statistics for Player: &b" + name));
        sender.sendMessage("");
        sender.sendMessage(ChatColors.color("&7Title: " + ChatColor.AQUA + getTitle()));
        sender.sendMessage(ChatColors.color("&7Research Progress: " + NumberUtils.getColorFromPercentage(progress) + progress + " &r% " + ChatColor.YELLOW + '(' + researched.size() + " / " + totalResearches + ')'));
        sender.sendMessage(ChatColors.color("&7Total XP Levels spent: " + ChatColor.AQUA + levels));
    }

    /**
     * This returns the {@link Player} who this {@link PlayerProfile} belongs to.
     * If the {@link Player} is offline, null will be returned.
     * 
     * @return The {@link Player} of this {@link PlayerProfile} or null
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(getUUID());
    }

    /**
     * This returns the {@link GuideHistory} of this {@link Player}.
     * It is basically that player's browsing history.
     * 
     * @return The {@link GuideHistory} of this {@link Player}
     */
    public GuideHistory getGuideHistory() {
        return guideHistory;
    }

    public static boolean fromUUID(UUID uuid, Consumer<PlayerProfile> callback) {
        return get(Bukkit.getOfflinePlayer(uuid), callback);
    }

    /**
     * Get the PlayerProfile for a player asynchronously.
     *
     * @param p
     *            The player who's profile to retrieve
     * @param callback
     *            The callback with the PlayerProfile
     * 
     * @return If the player was cached or not.
     */
    public static boolean get(OfflinePlayer p, Consumer<PlayerProfile> callback) {
        UUID uuid = p.getUniqueId();
        PlayerProfile profile = SlimefunPlugin.getRegistry().getPlayerProfiles().get(uuid);

        if (profile != null) {
            callback.accept(profile);
            return true;
        }

        Bukkit.getScheduler().runTaskAsynchronously(SlimefunPlugin.instance, () -> {
            PlayerProfile pp = new PlayerProfile(p);
            SlimefunPlugin.getRegistry().getPlayerProfiles().put(uuid, pp);
            callback.accept(pp);
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
    public static boolean request(OfflinePlayer p) {
        if (!SlimefunPlugin.getRegistry().getPlayerProfiles().containsKey(p.getUniqueId())) {
            // Should probably prevent multiple requests for the same profile in the future
            Bukkit.getScheduler().runTaskAsynchronously(SlimefunPlugin.instance, () -> {
                PlayerProfile pp = new PlayerProfile(p);
                SlimefunPlugin.getRegistry().getPlayerProfiles().put(p.getUniqueId(), pp);
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
    public static Optional<PlayerProfile> find(OfflinePlayer p) {
        return Optional.ofNullable(SlimefunPlugin.getRegistry().getPlayerProfiles().get(p.getUniqueId()));
    }

    public static Iterator<PlayerProfile> iterator() {
        return SlimefunPlugin.getRegistry().getPlayerProfiles().values().iterator();
    }

    public static void getBackpack(ItemStack item, Consumer<PlayerBackpack> callback) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasLore()) {
            return;
        }

        OptionalInt id = OptionalInt.empty();
        String uuid = "";

        for (String line : item.getItemMeta().getLore()) {
            if (line.startsWith(ChatColors.color("&7ID: ")) && line.indexOf('#') != -1) {
                String[] splitLine = PatternUtils.HASH.split(line);

                if (PatternUtils.NUMERIC.matcher(splitLine[1]).matches()) {
                    id = OptionalInt.of(Integer.parseInt(splitLine[1]));
                    uuid = splitLine[0].replace(ChatColors.color("&7ID: "), "");
                }
            }
        }

        if (id.isPresent()) {
            int number = id.getAsInt();
            fromUUID(UUID.fromString(uuid), profile -> {
                Optional<PlayerBackpack> backpack = profile.getBackpack(number);

                if (backpack.isPresent()) {
                    callback.accept(backpack.get());
                }
            });
        }
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PlayerProfile && uuid.equals(((PlayerProfile) obj).uuid);
    }

    @Override
    public String toString() {
        return "PlayerProfile {" + uuid + "}";
    }

}
