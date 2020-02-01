package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.plugin.Plugin;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun4.core.services.github.ContributionsConnector;
import io.github.thebusybiscuit.slimefun4.core.services.github.Contributor;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubConnector;
import io.github.thebusybiscuit.slimefun4.core.services.github.GitHubTask;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;

public class GitHubService {

	private final String repository;
	private final Set<GitHubConnector> connectors;
	private final ConcurrentMap<String, Contributor> contributors;

	private boolean logging = false;

	private int issues = 0;
	private int pullRequests = 0;
	private int forks = 0;
	private int stars = 0;
	private int codeBytes = 0;
	private Date lastUpdate = new Date();

	public GitHubService(String repository) {
		this.repository = repository;

		connectors = new HashSet<>();
		contributors = new ConcurrentHashMap<>();
	}

	public void start(Plugin plugin) {
		plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new GitHubTask(this), 80L, 60 * 60 * 20L);
	}

	private void addDefaultContributors() {
		Contributor fuffles = new Contributor("Fuffles_");
		fuffles.setContribution("&dSkull Texture Artist", 0);
		contributors.put(fuffles.getName(), fuffles);
		
		// Translators - German
		addTranslator("TheBusyBiscuit", "de", false);
		
		// Translators - French
		addTranslator("JustDams", "D4ms_", "fr", true);
		addTranslator("edkerforne", "fr", true);
		addTranslator("tnthomastn", "fr", true);
		
		// Translators - Italian
		addTranslator("xXDOTTORXx", "it", true);
		
		// Translators - Latvian
		addTranslator("AgnisT", "lv", true);
		
		// Translators - Hungarian
		addTranslator("andris155", "hu", true);
		
		// Translators - Slovak
		addTranslator("KillerXCoder", "sk", true);
		addTranslator("PixelHotDog", "sk", true);
		
		// Translators - Spanish
		addTranslator("Luu7", "_Luu", "es", true);
		addTranslator("Vravinite", "es", true);
		addTranslator("NotUmBr4", "es", true);
		addTranslator("dbzjjoe", "es", true);
		
		// Translators - Swedish
		addTranslator("NihilistBrew", "ma1yang2", "sv", false);
		addTranslator("Tra-sh", "TurretTrash", "sv", true);
		
		// Translators - Dutch
		addTranslator("Dr4gonD", "nl", true);
		addTranslator("svr333", "nl", false);
		
		// Translators - Chinese (China)
		addTranslator("StarWishsama", "zh-CN", false);
	}

	private void addTranslator(String name, String language, boolean lock) {
		addTranslator(name, name, language, lock);
	}

	private void addTranslator(String name, String alias, String language, boolean lock) {
		Contributor contributor = contributors.computeIfAbsent(name, user -> new Contributor(alias, "https://github.com/" + user));
		contributor.setContribution("translator," + language, 0);
		
		if (lock) contributor.lock();
	}

	public void connect(boolean logging) {
		this.logging = logging;
		addDefaultContributors();

		connectors.add(new ContributionsConnector(this, "code", repository, "developer"));
		connectors.add(new ContributionsConnector(this, "wiki", "TheBusyBiscuit/Slimefun4-wiki", "wiki"));
		connectors.add(new ContributionsConnector(this, "resourcepack", "TheBusyBiscuit/Slimefun4-Resourcepack", "resourcepack"));

		connectors.add(new GitHubConnector(this) {

			@Override
			public void onSuccess(JsonElement element) {
				JsonObject object = element.getAsJsonObject();
				forks = object.get("forks").getAsInt();
				stars = object.get("stargazers_count").getAsInt();
				lastUpdate = NumberUtils.parseGitHubDate(object.get("pushed_at").getAsString());
			}

			@Override
			public String getRepository() {
				return repository;
			}

			@Override
			public String getFileName() {
				return "repo";
			}

			@Override
			public String getURLSuffix() {
				return "";
			}
		});

		connectors.add(new GitHubConnector(this) {

			@Override
			public void onSuccess(JsonElement element) {
				JsonArray array = element.getAsJsonArray();

				int issueCount = 0;
				int prCount = 0;

				for (JsonElement elem : array) {
					JsonObject obj = elem.getAsJsonObject();

					if (obj.has("pull_request")) prCount++;
					else issueCount++;
				}

				issues = issueCount;
				pullRequests = prCount;
			}

			@Override
			public String getRepository() {
				return repository;
			}

			@Override
			public String getFileName() {
				return "issues";
			}

			@Override
			public String getURLSuffix() {
				return "/issues";
			}
		});

		connectors.add(new GitHubConnector(this) {

			@Override
			public void onSuccess(JsonElement element) {
				JsonObject object = element.getAsJsonObject();
				codeBytes = object.get("Java").getAsInt();
			}

			@Override
			public String getRepository() {
				return repository;
			}

			@Override
			public String getFileName() {
				return "languages";
			}

			@Override
			public String getURLSuffix() {
				return "/languages";
			}
		});
	}

	public Set<GitHubConnector> getConnectors() {
		return connectors;
	}

	public ConcurrentMap<String, Contributor> getContributors() {
		return contributors;
	}

	public int getForks() {
		return forks;
	}

	public int getStars() {
		return stars;
	}

	public int getIssues() {
		return issues;
	}

	public int getPullRequests() {
		return pullRequests;
	}

	public int getCodeSize() {
		return codeBytes;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public boolean isLoggingEnabled() {
		return logging;
	}
}
