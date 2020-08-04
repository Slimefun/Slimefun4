package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import io.github.thebusybiscuit.cscorelib2.data.ComputedOptional;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;

/**
 * Represents a {@link Contributor} who contributed to a GitHub repository.
 *
 * @author TheBusyBiscuit
 * @author Walshy
 * 
 * @see GitHubService
 * 
 */
public class Contributor {

    private final String githubUsername;
    private final String minecraftUsername;
    private final String profileLink;

    private final ConcurrentMap<String, Integer> contributions = new ConcurrentHashMap<>();
    private final ComputedOptional<String> headTexture = ComputedOptional.createNew();

    private Optional<UUID> uuid = Optional.empty();
    private boolean locked = false;

    public Contributor(String username, String profile) {
        Validate.notNull(username, "Username must never be null!");
        Validate.notNull(profile, "The profile link must never be null!");
        githubUsername = profile.substring(profile.lastIndexOf('/') + 1);
        minecraftUsername = username;
        profileLink = profile;
    }

    public Contributor(String username) {
        Validate.notNull(username, "Username must never be null!");
        githubUsername = username;
        minecraftUsername = username;
        profileLink = null;
    }

    public void setContribution(String role, int commits) {
        if (!locked || role.startsWith("translator,")) {
            contributions.put(role, commits);
        }
    }

    /**
     * Returns the name of this contributor.
     *
     * @return the name of this contributor
     */
    public String getName() {
        return githubUsername;
    }

    /**
     * Returns the MC name of the contributor.
     * This may be the same as {@link #getName()}.
     *
     * @return The MC username of this contributor.
     */
    public String getMinecraftName() {
        return minecraftUsername;
    }

    /**
     * Returns a link to the GitHub profile of this {@link Contributor}.
     *
     * @return The GitHub profile of this {@link Contributor}
     */
    public String getProfile() {
        return profileLink;
    }

    public List<Map.Entry<String, Integer>> getContributions() {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(contributions.entrySet());
        list.sort(Comparator.comparingInt(entry -> -entry.getValue()));
        return list;
    }

    /**
     * This method gives you the amount of contributions this {@link Contributor}
     * has submmited in the name of the given role.
     * 
     * @param role
     *            The role for which to count the contributions.
     * @return The amount of contributions this {@link Contributor} submitted as the given role
     */
    public int getContributions(String role) {
        return contributions.getOrDefault(role, 0);
    }

    /**
     * This method sets the {@link UUID} for this {@link Contributor}.
     * 
     * @param uuid
     *            The {@link UUID} for this {@link Contributor}
     */
    public void setUniqueId(UUID uuid) {
        this.uuid = uuid == null ? Optional.empty() : Optional.of(uuid);
    }

    /**
     * This returns the {@link UUID} for this {@link Contributor}.
     * This {@link UUID} may be loaded from a cache.
     * 
     * @return The {@link UUID} of this {@link Contributor}
     */
    public Optional<UUID> getUniqueId() {
        return uuid;
    }

    /**
     * Returns this Creator's head texture.
     * If no texture could be found, or it hasn't been pulled yet,
     * then it will return a placeholder texture.
     * 
     * @return A Base64-Head Texture
     */
    public String getTexture() {
        if (!headTexture.isComputed() || !headTexture.isPresent()) {
            GitHubService github = SlimefunPlugin.getGitHubService();

            if (github != null) {
                String cached = github.getCachedTexture(githubUsername);
                return cached != null ? cached : HeadTexture.UNKNOWN.getTexture();
            }
            else {
                return HeadTexture.UNKNOWN.getTexture();
            }
        }
        else {
            return headTexture.get();
        }
    }

    /**
     * This method will return whether this instance of {@link Contributor} has
     * pulled a texture yet.
     * 
     * @return Whether this {@link Contributor} has been assigned a texture yet
     */
    public boolean hasTexture() {
        return headTexture.isComputed();
    }

    public void setTexture(String skin) {
        headTexture.compute(skin);
    }

    public int getTotalContributions() {
        return contributions.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int index() {
        return -getTotalContributions();
    }

    public String getDisplayName() {
        return ChatColor.GRAY + githubUsername + (!githubUsername.equals(minecraftUsername) ? ChatColor.DARK_GRAY + " (MC: " + minecraftUsername + ")" : "");
    }

    public void lock() {
        locked = true;
    }
}
