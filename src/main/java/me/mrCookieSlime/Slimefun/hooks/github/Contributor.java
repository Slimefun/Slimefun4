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
	
	private String name;
	private String profile;
	private Optional<String> headTexture;
	private final ConcurrentMap<String, Integer> contributions = new ConcurrentHashMap<>();

	public Contributor(String name, String profile) {
		this.name = name;
		this.profile = profile;
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
		return this.name;
	}

	/**
	 * Returns the link to the GitHub profile of this contributor.
	 *
	 * @return the GitHub profile of this contributor.
	 * @since 4.1.13
	 */
	public String getProfile() {
		return this.profile;
	}
	
	public Map<String, Integer> getContributions() {
		return contributions;
	}
	
	/**
	 * Returns this Creator's head texture
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
