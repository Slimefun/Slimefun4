package io.github.thebusybiscuit.slimefun4.core.services.github;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.mrCookieSlime.Slimefun.api.Slimefun;

class ContributionsConnector extends GitHubConnector {

    // GitHub Bots that do not count as Contributors
    // (includes "invalid-email-address" because it is an invalid contributor)
    private static final List<String> blacklist = Arrays.asList("invalid-email-address", "renovate-bot", "TheBusyBot", "ImgBotApp", "imgbot", "imgbot[bot]", "github-actions[bot]", "gitlocalize-app", "gitlocalize-app[bot]", "mt-gitlocalize");

    // Matches a GitHub name with a Minecraft name.
    private static final Map<String, String> aliases = new HashMap<>();

    // Should probably be switched to UUIDs at some point...
    static {
        aliases.put("WalshyDev", "HumanRightsAct");
        aliases.put("J3fftw1", "_lagpc_");
        aliases.put("ajan-12", "ajan_12");
        aliases.put("mrcoffee1026", "mr_coffee1026");
        aliases.put("Cyber-MC", "CyberPatriot");
        aliases.put("BurningBrimstone", "Bluedevil74");
        aliases.put("bverhoeven", "soczol");
        aliases.put("ramdon-person", "ramdon_person");
    }

    private final String prefix;
    private final String role;
    private final int page;

    private boolean finished = false;

    ContributionsConnector(GitHubService github, String prefix, int page, String repository, String role) {
        super(github, repository);

        this.prefix = prefix;
        this.page = page;
        this.role = role;
    }

    /**
     * This returns whether this {@link ContributionsConnector} has finished its task.
     * 
     * @return Whether it is finished
     */
    public boolean hasFinished() {
        return finished;
    }

    @Override
    public void onSuccess(JsonElement element) {
        finished = true;
        if (element.isJsonArray()) {
            computeContributors(element.getAsJsonArray());
        }
        else {
            Slimefun.getLogger().log(Level.WARNING, "Received an unusual answer from GitHub, possibly a timeout? ({0})", element);
        }
    }

    @Override
    public void onFailure() {
        finished = true;
    }

    @Override
    public String getFileName() {
        return prefix + "_contributors";
    }

    @Override
    public String getURLSuffix() {
        return "/contributors?per_page=100&page=" + page;
    }

    private void computeContributors(JsonArray array) {
        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();

            String name = object.get("login").getAsString();
            int commits = object.get("contributions").getAsInt();
            String profile = object.get("html_url").getAsString();

            if (!blacklist.contains(name)) {
                github.addContributor(aliases.getOrDefault(name, name), profile, role, commits);
            }
        }
    }
}