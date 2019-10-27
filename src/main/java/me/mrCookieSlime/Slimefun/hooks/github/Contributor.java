package me.mrCookieSlime.Slimefun.hooks.github;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

/**
 * Represents a contributor on Slimefun4's GitHub repository.
 *
 * @since 4.1.6
 */
public class Contributor {
	
	private String name;
	private String job;
	private String profile;
	private int commits;

	public Contributor(String name, String job, int commits) {
		this.name = name;
		this.job = job;
		this.commits = commits;
	}

	/**
	 * Returns the name of this contributor.
	 *
	 * @return the name of this contributor
	 * @since 4.1.13
	 */
	public String getName() 	{	return this.name;		}

	/**
	 * Returns the job of this contributor.
	 * It can be {@code Author} or {@code Head Artist}.
	 *
	 * @return the job of this contributor
	 * @since 4.1.13
	 */
	public String getJob() 		{	return this.job;		}

	/**
	 * Returns the link to the GitHub profile of this contributor.
	 *
	 * @return the GitHub profile of this contributor.
	 * @since 4.1.13
	 */
	public String getProfile() 	{	return this.profile;	}

	/**
	 * Returns the number of commits to the Slimefun4's repository of this contributor.
	 *
	 * @return the number of commits of this contributor.
	 * @since 4.1.13
	 */
	public int getCommits() 	{	return this.commits;	}

	/**
	 * Sets the link to the GitHub profile of this contributor.
	 *
	 * @param profile the link to the GitHub profile of this contributor
	 *
	 * @since 4.1.13
	 */
	protected void setProfile(String profile) {
		this.profile = profile;
	}
	
	/**
	 * Returns this Creator's head texture
	 * @return A Base64-Head Texture
	 */
	public String getTexture() {
		return SlimefunPlugin.getUtilities().contributorHeads.get(name);
	}
}
