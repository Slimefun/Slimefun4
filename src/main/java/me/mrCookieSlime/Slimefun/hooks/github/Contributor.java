package me.mrCookieSlime.Slimefun.hooks.github;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Represents a contributor on Slimefun4's GitHub repository.
 *
 * @since 4.1.6
 */
public class Contributor {
	
	private static final String PLACEHOLDER_HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZiYTYzMzQ0ZjQ5ZGQxYzRmNTQ4OGU5MjZiZjNkOWUyYjI5OTE2YTZjNTBkNjEwYmI0MGE1MjczZGM4YzgyIn19fQ==";

	private final String ghName;
	private final String mcName;
	private String profileLink;
	private final ConcurrentMap<String, Integer> contributions = new ConcurrentHashMap<>();

	// This field is nullable.
	// null = "Texture was not pulled yet or failed to pull, it will try again next time"
	// empty Optional = "No Texture could be found for this person.
	private Optional<String> headTexture;
	
	public Contributor(String name, String profile) {
		this.ghName = profile.substring(profile.lastIndexOf('/') + 1);
		this.mcName = name;
		this.profileLink = profile;
	}
	
	public void setContribution(String role, int commits) {
		contributions.put(role, commits);
	}

	/**
	 * Returns the name of this contributor.
	 *
	 * @return the name of this contributor
	 * @since 4.1.13
	 */
	public String getName() {
		return this.ghName;
	}

	/**
	 * Returns the MC name of the contributor. 
	 * This may be the same as {@link #getName()}.
	 *
	 * @return The MC username of this contributor.
	 */
	public String getMinecraftName() {
		return this.mcName;
	}

	/**
	 * Returns the link to the GitHub profile of this contributor.
	 *
	 * @return the GitHub profile of this contributor.
	 * @since 4.1.13
	 */
	public String getProfile() {
		return this.profileLink;
	}
	
	public Map<String, Integer> getContributions() {
		return contributions;
	}
	
	/**
	 * Returns this Creator's head texture.
	 * If no texture could be found, or it hasn't been pulled yet, 
	 * then it will return a placeholder texture.
	 * 
	 * @return A Base64-Head Texture
	 */
	public String getTexture() {
		if (headTexture == null || !headTexture.isPresent()) {
			return PLACEHOLDER_HEAD;
		}
		else {
			return headTexture.get();
		}
	}

	/**
	 * This method will return whether this instance of {@link Contributor} has
	 * pulled a texture yet.
	 * 
	 * @return	Whether this {@link Contributor} has been assigned a texture yet
	 */
	public boolean hasTexture() {
		return headTexture != null;
	}

	public void setTexture(Optional<String> skin) {
		headTexture = skin;
	}
	
	public int getTotalContributions() {
		return contributions.values().stream().mapToInt(Integer::intValue).sum();
	}
	
	public int index() {
		return -getTotalContributions();
	}
}
