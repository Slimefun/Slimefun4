package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.github.thebusybiscuit.slimefun5.utils.JsonUtils;

/**
 * Talks to the GitHub Releases API for a single entry: finds the latest release's version tag and
 * downloadable jar asset, and downloads jars. All methods are blocking — call them off the main
 * thread.
 */
public final class AddonReleaseService {

    private static final String API_URL = "https://api.github.com/";
    private static final String USER_AGENT = "Slimefun5 (https://github.com/Slimefun)";
    private static final int TIMEOUT = 10_000;

    /** The resolved latest release of an entry. */
    public static final class ReleaseInfo {

        private final String tag;
        private final String jarUrl;

        ReleaseInfo(String tag, String jarUrl) {
            this.tag = tag;
            this.jarUrl = jarUrl;
        }

        @Nonnull
        public String getTag() {
            return tag;
        }

        @Nonnull
        public String getJarUrl() {
            return jarUrl;
        }
    }

    /**
     * Fetches the latest release for an entry. For core we use /releases/latest, which excludes
     * prereleases (the gh-v* GitHub-only builds), so it returns the latest stable v* release.
     * Addons use the same endpoint.
     *
     * @return the resolved release, or null if there is no release, no jar asset, or the request failed.
     */
    @Nullable
    public ReleaseInfo fetchLatest(@Nonnull AddonCatalog.Entry entry) {
        String endpoint = API_URL + "repos/" + entry.getSlug() + "/releases/latest";
        JsonElement response = get(endpoint);

        if (response == null || !response.isJsonObject()) {
            return null;
        }

        JsonObject obj = response.getAsJsonObject();

        if (!obj.has("tag_name") || !obj.has("assets")) {
            return null;
        }

        String tag = obj.get("tag_name").getAsString();
        String jarUrl = findJarAsset(obj.getAsJsonArray("assets"));

        if (jarUrl == null) {
            return null;
        }

        return new ReleaseInfo(tag, jarUrl);
    }

    @Nullable
    private static String findJarAsset(@Nonnull JsonArray assets) {
        for (JsonElement element : assets) {
            JsonObject asset = element.getAsJsonObject();
            String name = asset.has("name") ? asset.get("name").getAsString() : "";

            if (name.endsWith(".jar") && !name.contains("-sources") && !name.contains("-javadoc") && asset.has("browser_download_url")) {
                return asset.get("browser_download_url").getAsString();
            }
        }

        return null;
    }

    /**
     * Downloads a jar to the target directory under the given file name. Writes to a .tmp file then
     * atomically renames into place, so a failed download never leaves a half-written jar.
     *
     * @return true on success.
     */
    public boolean downloadJar(@Nonnull String jarUrl, @Nonnull File targetDir, @Nonnull String fileName) {
        File tmp = new File(targetDir, fileName + ".tmp");
        File dest = new File(targetDir, fileName);
        HttpURLConnection connection = null;

        try {
            URL url = new URI(jarUrl).toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(TIMEOUT);
            connection.setReadTimeout(TIMEOUT);

            try (InputStream in = connection.getInputStream(); FileOutputStream out = new FileOutputStream(tmp)) {
                byte[] buffer = new byte[8192];
                int read;

                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }

            if (dest.exists() && !dest.delete()) {
                tmp.delete();
                return false;
            }

            return tmp.renameTo(dest);
        } catch (IOException | URISyntaxException e) {
            tmp.delete();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
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
