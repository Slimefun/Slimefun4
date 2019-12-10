package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

public class ContributionsConnector extends GitHubConnector {

	private static final Pattern nameFormat = Pattern.compile("[\\w_]+");

	// GitHub Bots that do not count as Contributors
	// (includes "invalid-email-address" because it is an invalid contributor)
	private static final List<String> blacklist = Arrays.asList(
		"invalid-email-address",
		"renovate-bot",
		"ImgBotApp",
		"TheBusyBot",
		"imgbot"
	);

	// Matches a GitHub name with a Minecraft name.
	private static final Map<String, String> aliases = new HashMap<>();

	// Should probably be switched to UUIDs at some point...
	static {
		aliases.put("WalshyDev", "HumanRightsAct");
		aliases.put("J3fftw1", "_lagpc_");
		aliases.put("ajan-12", "ajan_12");
		aliases.put("LinoxGH", "ajan_12");
		aliases.put("NihilistBrew", "ma1yang2");
		aliases.put("NihilistBrew", "ma1yang2");
		aliases.put("mrcoffee1026", "mr_coffee1026");
		aliases.put("BluGhostYT", "CyberPatriot");
	}
	
	private final String prefix;
	private final String repository;
	private final String role;
	
	public ContributionsConnector(String prefix, String repository, String role) {
		this.prefix = prefix;
		this.repository = repository;
		this.role = role;
	}
	
	@Override
	public void onSuccess(JsonElement element) {
		computeContributors(element.getAsJsonArray());
	}
	
	@Override
	public String getRepository() {
		return repository;
	}
	
	@Override
	public String getFileName() {
		return prefix + "_contributors";
	}

	@Override
	public String getURLSuffix() {
		return "/contributors?per_page=100";
	}

	private void computeContributors(JsonArray array) {
	    for (int i = 0; i < array.size(); i++) {
	    	JsonObject object = array.get(i).getAsJsonObject();

	    	String name = object.get("login").getAsString();
	    	int commits = object.get("contributions").getAsInt();
	    	String profile = object.get("html_url").getAsString();

	    	if (nameFormat.matcher(name).matches() && !blacklist.contains(name)) {
	    		Contributor contributor = SlimefunPlugin.getUtilities().contributors.computeIfAbsent(
	    				name,
						key -> new Contributor(aliases.getOrDefault(name, name), profile)
				);
	    		contributor.setContribution(role, commits);
	    	}
	    }
	}
}