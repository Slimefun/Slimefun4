package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.github.thebusybiscuit.cscorelib2.players.MinecraftAccount;
import io.github.thebusybiscuit.cscorelib2.players.MinecraftAccount.TooManyRequestsException;
import io.github.thebusybiscuit.slimefun4.core.services.GitHubService;

public class GitHubTask implements Runnable {
	
	private final GitHubService gitHubService;

	public GitHubTask(GitHubService github) {
		gitHubService = github;
	}
	
	@Override
	public void run() {
		gitHubService.getConnectors().forEach(GitHubConnector::pullFile);
		
		// Store all queried usernames to prevent 429 responses for pinging the
		// same URL twice in one run.
		Map<String, String> skins = new HashMap<>();

		for (Contributor contributor : gitHubService.getContributors().values()) {
			if (!contributor.hasTexture()) {
				try {
					if (skins.containsKey(contributor.getMinecraftName())) {
						contributor.setTexture(skins.get(contributor.getMinecraftName()));
					}
					else {
						contributor.setTexture(grabTexture(skins, contributor.getMinecraftName()));
					}
				}
				catch(IllegalArgumentException x) {
					// There cannot be a texture found because it is not a valid MC username
					contributor.setTexture(null);
				}
				catch(TooManyRequestsException x) {
					break;
				}
			}
		}
	}

	private String grabTexture(Map<String, String> skins, String username) throws TooManyRequestsException {
		Optional<UUID> uuid = MinecraftAccount.getUUID(username);
		
		if (uuid.isPresent()) {
			Optional<String> skin = MinecraftAccount.getSkin(uuid.get());
			skins.put(username, skin.orElse(""));
			return skin.orElse(null);
		}
		else {
			return null;
		}
	}

}
