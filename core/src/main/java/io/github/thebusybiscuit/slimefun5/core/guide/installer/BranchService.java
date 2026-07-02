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

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.JsonUtils;

/**
 * Lists the branch names of a repository via the GitHub API, for the build-from-branch picker.
 * Blocking — call off the main thread.
 */
public final class BranchService {

    private static final String API_URL = "https://api.github.com/";
    private static final String USER_AGENT = "Slimefun5 (https://github.com/Slimefun)";
    private static final int TIMEOUT = 10_000;

    /** Why a branch fetch produced no usable list — so the menu can explain instead of just "none". */
    public enum Status { OK, RATE_LIMITED, UNREACHABLE }

    /** The outcome of a branch fetch: a status plus the branch names (empty unless status is OK). */
    public static final class Result {
        private final Status status;
        private final List<String> branches;

        Result(Status status, List<String> branches) {
            this.status = status;
            this.branches = branches;
        }

        @Nonnull
        public Status getStatus() {
            return status;
        }

        @Nonnull
        public List<String> getBranches() {
            return branches;
        }
    }

    /**
     * @return the fetch result: OK with branch names (up to 100), or RATE_LIMITED / UNREACHABLE with an
     *         empty list. Never null.
     */
    @Nonnull
    public Result fetchBranches(@Nonnull AddonCatalog.Entry entry) {
        String endpoint = API_URL + "repos/" + entry.getSlug() + "/branches?per_page=100";
        HttpURLConnection connection = null;

        try {
            URL url = new URI(endpoint).toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            String token = token();

            if (token != null) {
                connection.setRequestProperty("Authorization", "token " + token);
            }

            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);

            int status = connection.getResponseCode();

            if (status == 403 && "0".equals(connection.getHeaderField("X-RateLimit-Remaining"))) {
                return new Result(Status.RATE_LIMITED, Collections.emptyList());
            }

            if (status < 200 || status >= 300) {
                return new Result(Status.UNREACHABLE, Collections.emptyList());
            }

            JsonElement response = JsonUtils.parseString(readBody(connection.getInputStream()));

            if (response == null || !response.isJsonArray()) {
                return new Result(Status.UNREACHABLE, Collections.emptyList());
            }

            JsonArray array = response.getAsJsonArray();
            List<String> branches = new ArrayList<>();

            for (JsonElement element : array) {
                JsonObject branch = element.getAsJsonObject();

                if (branch.has("name")) {
                    branches.add(branch.get("name").getAsString());
                }
            }

            return new Result(Status.OK, branches);
        } catch (Exception e) {
            return new Result(Status.UNREACHABLE, Collections.emptyList());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /** Optional token from config.yml (shared with the installer), raising the API limit to 5000/hour. */
    @Nullable
    private static String token() {
        String token = Slimefun.getCfg().getString("installer.github-token");
        return token != null && !token.trim().isEmpty() ? token.trim() : null;
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
