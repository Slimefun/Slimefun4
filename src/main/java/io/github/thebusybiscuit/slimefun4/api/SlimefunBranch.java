package io.github.thebusybiscuit.slimefun4.api;

/**
 * This enum represents the branch this Slimefun build is on.
 * development or stable, unofficial or even unknown.
 * 
 * @author TheBusyBiscuit
 *
 */
public enum SlimefunBranch {
	
	DEVELOPMENT("master", true),
	STABLE("stable", true),
	UNOFFICIAL("Unknown", false),
	UNKNOWN("Unknown", false);
	
	private final String name;
	private final boolean official;
	
	private SlimefunBranch(String name, boolean official) {
		this.name = name;
		this.official = official;
	}
	
	public String getName() {
		return name;
	}

	public boolean isOfficial() {
		return official;
	}

}
