package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun5.utils.JsonUtils;

/**
 * Lists the branch names of a repository via the GitHub API, for the build-from-branch picker.
 * Blocking — call off the main thread.
 */
public final class BranchService {

    private static final String API_URL = "https://api.github.com/";
    private static final String USER_AGENT = "Slimefun5 (https://github.com/Slimefun)";
    private static final int TIMEOUT = 10_000;

    /**
     * @return branch names (up to 100), or an empty list on failure. Never null.
     */
    @Nonnull
    public List<String> fetchBranches(@Nonnull AddonCatalog.Entry entry) {
        String endpoint = API_URL + "repos/" + entry.getSlug() + "/branches?per_page=100";
        JsonElement response = get(endpoint);

        if (response == null || !response.isJsonArray()) {
            return Collections.emptyList();
        }

        JsonArray array = response.getAsJsonArray();
        List<String> branches = new ArrayList<>();

        for (JsonElement element : array) {
            JsonObject branch = element.getAsJsonObject();

            if (branch.has("name")) {
                branches.add(branch.get("name").getAsString());
            }
        }

        return branches;
    }

    @Nullable
    private static JsonElement get(@Nonnull String endpoint) {
        HttpURLConnection connection = null;

        try {
            URL url = new URI(endpoint).toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);

            int status = connection.getResponseCode();

            if (status < 200 || status >= 300) {
                return null;
            }

            return JsonUtils.parseString(readBody(connection.getInputStream()));
        } catch (Exception e) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Nonnull
    private static String readBody(@Nullable InputStream stream) throws IOException {
        if (stream == null) {
            return "";
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        }
    }
}
