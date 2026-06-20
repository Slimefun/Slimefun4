# In-Game Addon Installer Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add an in-game installer reachable from the Slimefun Guide settings that installs, updates, and (in a dev environment) builds-from-branch any Slimefun5 addon and Slimefun core itself.

**Architecture:** A new `core/guide/installer` package with pure-data + service layers (catalog, GitHub release/branch services, source builder, environment detector, install-state persistence, an orchestration facade) under three `ChestMenu` screens. All network/git/Gradle work runs on Bukkit's async scheduler; jars are staged to `/plugins` (new install) or the Bukkit update folder (update/self-update) and applied on restart — never hot-loaded.

**Tech Stack:** Java 8 (universal-jar floor: no `var`/`record`/switch-expr/`List.of`/text blocks), Bukkit API (1.8.8 floor), `HttpURLConnection` + Gson (matching `GitHubConnector`), `me.mrCookieSlime...ChestMenu`, `CustomItemStack`, `XMaterial`/`MaterialCompat`, `YamlConfiguration`, `ProcessBuilder`.

---

## ⚠️ Verification model (read first)

This project's unit tests are **disabled** project-wide — MockBukkit requires Java 25, incompatible with the Java-8 universal-jar floor. The established quality gate is:

- **Per task:** `./gradlew :core:compileJava -q` exits 0 (run from `core/Slimefun5/`).
- **Final:** a 1.8.8 boot test + a manual in-game dry-run (Task 13).

So tasks are **not** written test-first. Each implementation step is followed by a **compile-verification** step. Do not invent JUnit tests — they cannot run here. Keep functions small and pure where possible so the compile gate is meaningful.

**Conventions all tasks must follow:**
- Package root: `io.github.thebusybiscuit.slimefun5.core.guide.installer`.
- Java 8 only. Use `com.cryptomorin.xseries.XMaterial` + `io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat.stack(XMaterial)` for materials (returns a `Material`).
- Item stacks via `io.github.bakedlibs.dough.items.CustomItemStack.create(Material, String, String...)`.
- Plugin instance: `io.github.thebusybiscuit.slimefun5.implementation.Slimefun.instance()` (`@Nullable`), logger `Slimefun.logger()`.
- Async: `Slimefun.instance().getServer().getScheduler().runTaskAsynchronously(Slimefun.instance(), runnable)`; back to main thread: `runTask(Slimefun.instance(), runnable)`.
- Data folder: `Slimefun.instance().getDataFolder()` → `plugins/Slimefun`.
- Commit message convention (project rule): `type(scope): summary`, no body unless large.
- Branch: `feature/java8-universal-jar` (already checked out).

Working directory for all gradle/git commands: `F:/Documents/GitHub/slimefun/core/Slimefun5`.

---

## File Structure

| File | Responsibility |
|---|---|
| `AddonCatalog.java` | Static catalog of installable entries + dependency resolution + the permission constant. Pure data. |
| `InstallState.java` | Persists per-entry install method/version/restart-pending to `addon-installer.yml`. |
| `InstallTargets.java` | Resolves write targets (new install vs update folder) + data-folder sub-dirs (sources, logs). |
| `AddonReleaseService.java` | GitHub `releases/latest` fetch (version + jar asset URL) + jar download. |
| `EnvironmentDetector.java` | Detects `git` + a JDK (`javac`) for Mode B gating. |
| `BranchService.java` | GitHub `branches` fetch for the branch picker. |
| `AddonSourceBuilder.java` | Reusable source cache: clone/fetch/checkout + Gradle build + jar extraction. |
| `AddonInstaller.java` | Orchestration facade: dependency resolution, async install/build, building-set, chat feedback. |
| `AddonInstallerMenu.java` | Screen 1 — grid of entries with status badges. |
| `AddonDetailMenu.java` | Screen 2 — one entry's detail + action buttons. |
| `BranchSelectMenu.java` | Screen 3 — paginated branch picker. |
| `SlimefunGuideSettings.java` (modify) | Slot 47 → installer, permission-gated. |
| `plugin.yml` (modify) | Declare `slimefun.installer` permission, default op. |
| `messages.yml` (modify) | Installer menu titles. |

`InstallTargets.java` and `AddonInstaller.java` are additions beyond the spec's enumerated list — small, focused helpers that keep the menus and services thin.

---

### Task 1: AddonCatalog (pure data + dependency resolution)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonCatalog.java`

- [ ] **Step 1: Create the catalog**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cryptomorin.xseries.XMaterial;

/**
 * The static source of truth for everything the in-game installer can install: Slimefun core
 * plus every known Slimefun5 addon. Mirrors the repo list in scripts/run.ps1.
 */
public final class AddonCatalog {

    /** Permission required to use the installer. Declared in plugin.yml, default op. */
    public static final String PERMISSION = "slimefun.installer";

    /** The owner/org all entries live under on GitHub. */
    public static final String OWNER = "Slimefun5";

    /** The id of the Slimefun core entry. Treated specially (uses full releases, self-update). */
    public static final String CORE_ID = "slimefun";

    /** A single installable entry. */
    public static final class Entry {

        private final String id;
        private final String repo;
        private final String displayName;
        private final XMaterial icon;
        private final List<String> dependencies;
        private final boolean core;

        Entry(String id, String repo, String displayName, XMaterial icon, List<String> dependencies, boolean core) {
            this.id = id;
            this.repo = repo;
            this.displayName = displayName;
            this.icon = icon;
            this.dependencies = dependencies;
            this.core = core;
        }

        @Nonnull
        public String getId() {
            return id;
        }

        /** The GitHub repository name (without owner). */
        @Nonnull
        public String getRepo() {
            return repo;
        }

        /** "Slimefun5/Repo" — the full GitHub slug. */
        @Nonnull
        public String getSlug() {
            return OWNER + '/' + repo;
        }

        @Nonnull
        public String getDisplayName() {
            return displayName;
        }

        @Nonnull
        public XMaterial getIcon() {
            return icon;
        }

        /** Ids of hard dependencies that must also be installed. */
        @Nonnull
        public List<String> getDependencies() {
            return dependencies;
        }

        public boolean isCore() {
            return core;
        }
    }

    private static final Map<String, Entry> ENTRIES = new LinkedHashMap<>();

    static {
        // Core, pinned first.
        register(new Entry(CORE_ID, "Slimefun5", "Slimefun", XMaterial.BLAZE_POWDER, empty(), true));

        // Library (a dependency of others, also installable on its own).
        register(new Entry("infinitylib", "InfinityLib", "InfinityLib", XMaterial.BOOK, empty(), false));

        // Addons. Dependencies reference ids declared above.
        register(new Entry("infinityexpansion", "InfinityExpansion", "Infinity Expansion", XMaterial.NETHER_STAR, deps("infinitylib"), false));
        register(new Entry("networks", "Networks", "Networks", XMaterial.HOPPER, deps("infinitylib", "infinityexpansion"), false));
        register(new Entry("exoticgarden", "ExoticGarden", "Exotic Garden", XMaterial.MELON, empty(), false));
        register(new Entry("dynatech", "DynaTech", "DynaTech", XMaterial.FURNACE, empty(), false));
        register(new Entry("galactifun", "Galactifun", "Galactifun", XMaterial.FIREWORK_ROCKET, empty(), false));
        register(new Entry("slimetinker", "SlimeTinker", "Slime Tinker", XMaterial.ANVIL, empty(), false));
        register(new Entry("fluffymachines", "FluffyMachines", "Fluffy Machines", XMaterial.PISTON, empty(), false));
        register(new Entry("litexpansion", "LiteXpansion", "LiteXpansion", XMaterial.REDSTONE, empty(), false));
        register(new Entry("sensibletoolbox", "SensibleToolbox", "Sensible Toolbox", XMaterial.IRON_PICKAXE, empty(), false));
        register(new Entry("chestterminal", "ChestTerminal", "Chest Terminal", XMaterial.CHEST, empty(), false));
        register(new Entry("extragear", "ExtraGear", "Extra Gear", XMaterial.DIAMOND_CHESTPLATE, empty(), false));
        register(new Entry("luckyblocks", "LuckyBlocks", "Lucky Blocks", XMaterial.GOLD_BLOCK, empty(), false));
        register(new Entry("missilewarfare", "MissileWarfare", "Missile Warfare", XMaterial.TNT, empty(), false));
        register(new Entry("slimefunadvancements", "SlimefunAdvancements", "Slimefun Advancements", XMaterial.KNOWLEDGE_BOOK, empty(), false));
    }

    private AddonCatalog() {}

    private static void register(Entry entry) {
        ENTRIES.put(entry.getId(), entry);
    }

    private static List<String> empty() {
        return Collections.emptyList();
    }

    private static List<String> deps(String... ids) {
        return Collections.unmodifiableList(Arrays.asList(ids));
    }

    /** All entries in declared order (core first). */
    @Nonnull
    public static List<Entry> getEntries() {
        return new ArrayList<>(ENTRIES.values());
    }

    @Nullable
    public static Entry getById(@Nonnull String id) {
        return ENTRIES.get(id);
    }

    /**
     * Returns the given entry's full hard-dependency closure (transitive), in install order
     * (dependencies before dependents), excluding the entry itself. Used to auto-install deps.
     */
    @Nonnull
    public static List<Entry> resolveDependencies(@Nonnull Entry entry) {
        List<Entry> ordered = new ArrayList<>();
        collectDependencies(entry, ordered);
        return ordered;
    }

    private static void collectDependencies(Entry entry, List<Entry> ordered) {
        for (String depId : entry.getDependencies()) {
            Entry dep = ENTRIES.get(depId);

            if (dep != null && !containsId(ordered, dep.getId())) {
                collectDependencies(dep, ordered);

                if (!containsId(ordered, dep.getId())) {
                    ordered.add(dep);
                }
            }
        }
    }

    private static boolean containsId(List<Entry> list, String id) {
        for (Entry e : list) {
            if (e.getId().equals(id)) {
                return true;
            }
        }

        return false;
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0, no output.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonCatalog.java
git commit -m "feat(installer): add addon catalog with dependency resolution"
```

---

### Task 2: InstallState (persistence)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/InstallState.java`

- [ ] **Step 1: Create the install-state store**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.file.YamlConfiguration;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Persists what the installer has staged: per entry id, the install method, the installed
 * version-or-branch, and whether a restart is pending. Stored as plugins/Slimefun/addon-installer.yml.
 *
 * This reflects what the installer DID, not what is currently loaded — "is it loaded" is asked
 * separately via Bukkit's plugin manager.
 */
public final class InstallState {

    public enum Method {
        RELEASE,
        BRANCH
    }

    /** An immutable record of one staged install. */
    public static final class Record {

        private final Method method;
        private final String version;
        private final boolean restartPending;

        public Record(Method method, String version, boolean restartPending) {
            this.method = method;
            this.version = version;
            this.restartPending = restartPending;
        }

        @Nonnull
        public Method getMethod() {
            return method;
        }

        /** Release tag (RELEASE) or branch name (BRANCH). */
        @Nonnull
        public String getVersion() {
            return version;
        }

        public boolean isRestartPending() {
            return restartPending;
        }
    }

    private final File file;
    private final YamlConfiguration config;

    public InstallState() {
        File dataFolder = Slimefun.instance().getDataFolder();
        this.file = new File(dataFolder, "addon-installer.yml");
        this.config = file.exists() ? YamlConfiguration.loadConfiguration(file) : new YamlConfiguration();
    }

    @Nullable
    public Record get(@Nonnull String id) {
        if (!config.contains(id)) {
            return null;
        }

        String methodName = config.getString(id + ".method", Method.RELEASE.name());
        Method method;

        try {
            method = Method.valueOf(methodName);
        } catch (IllegalArgumentException e) {
            method = Method.RELEASE;
        }

        String version = config.getString(id + ".version", "");
        boolean restartPending = config.getBoolean(id + ".restart-pending", false);
        return new Record(method, version, restartPending);
    }

    /** Records a staged install and immediately persists. */
    public void set(@Nonnull String id, @Nonnull Method method, @Nonnull String version, boolean restartPending) {
        config.set(id + ".method", method.name());
        config.set(id + ".version", version);
        config.set(id + ".restart-pending", restartPending);
        save();
    }

    private void save() {
        try {
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            config.save(file);
        } catch (IOException e) {
            Slimefun.logger().log(Level.WARNING, "Failed to save addon-installer.yml: {0}", e.getMessage());
        }
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/InstallState.java
git commit -m "feat(installer): persist install state to addon-installer.yml"
```

---

### Task 3: InstallTargets (write-target + dir resolution)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/InstallTargets.java`

- [ ] **Step 1: Create the target resolver**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Resolves where jars and working files go.
 *
 * New installs land directly in /plugins; updates (and any self-update of an already-loaded
 * plugin, including Slimefun core) land in the server's update folder, which Bukkit swaps in on
 * the next start — sidestepping the live-jar file lock. The update folder name is configurable in
 * bukkit.yml, so it is always resolved through the API, never hardcoded.
 */
public final class InstallTargets {

    private InstallTargets() {}

    /** The /plugins directory (parent of the Slimefun data folder). */
    @Nonnull
    public static File pluginsDir() {
        return Slimefun.instance().getDataFolder().getParentFile();
    }

    /**
     * The directory a staged jar should be written to.
     *
     * @param alreadyLoaded
     *            whether a plugin with this entry's name is currently loaded — if so we update via
     *            the update folder; otherwise it is a fresh install into /plugins.
     */
    @Nonnull
    public static File targetDir(boolean alreadyLoaded) {
        if (alreadyLoaded) {
            File updateDir = Bukkit.getServer().getUpdateFolderFile();

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }

            return updateDir;
        }

        return pluginsDir();
    }

    /** Reusable source-cache root: plugins/Slimefun/addon-sources. */
    @Nonnull
    public static File sourcesDir() {
        File dir = new File(Slimefun.instance().getDataFolder(), "addon-sources");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }

    /** Build-log directory: plugins/Slimefun/installer-logs. */
    @Nonnull
    public static File logsDir() {
        File dir = new File(Slimefun.instance().getDataFolder(), "installer-logs");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return dir;
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/InstallTargets.java
git commit -m "feat(installer): resolve write targets and working directories"
```

---

### Task 4: AddonReleaseService (GitHub releases + jar download)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonReleaseService.java`

Reference: HTTP pattern matches `core/services/github/GitHubConnector.java` (Java 8 `HttpURLConnection`, `User-Agent`, Gson `JsonElement`, `JsonUtils.parseString`).

- [ ] **Step 1: Create the release service**

```java
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

            if (name.endsWith(".jar") && !name.contains("-sources") && !name.contains("-javadoc")) {
                return asset.has("browser_download_url") ? asset.get("browser_download_url").getAsString() : null;
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

        try {
            URL url = new URI(jarUrl).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
        }
    }

    @Nullable
    private static JsonElement get(@Nonnull String endpoint) {
        try {
            URL url = new URI(endpoint).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonReleaseService.java
git commit -m "feat(installer): fetch latest GitHub release and download jars"
```

---

### Task 5: EnvironmentDetector (Mode B gating)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/EnvironmentDetector.java`

- [ ] **Step 1: Create the detector**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;

/**
 * Detects whether the dev-only "build from a branch" mode is possible: git on PATH and a JDK
 * (a javac executable). The Gradle wrapper ships inside each cloned repo, so no separate Gradle
 * install is required. The result is cached for the JVM lifetime — these do not change at runtime.
 */
public final class EnvironmentDetector {

    private static Boolean cached;

    private EnvironmentDetector() {}

    /** True when both git and a JDK are available, enabling Mode B. */
    public static boolean canBuildFromSource() {
        if (cached == null) {
            cached = Boolean.valueOf(hasGit() && hasJdk());
        }

        return cached.booleanValue();
    }

    private static boolean hasGit() {
        return canRun("git", "--version");
    }

    private static boolean hasJdk() {
        // Prefer JAVA_HOME/bin/javac (the server may run on a JRE whose PATH lacks javac), then PATH.
        String javaHome = System.getProperty("java.home");

        if (javaHome != null) {
            String exe = isWindows() ? "javac.exe" : "javac";
            File javac = new File(new File(javaHome, "bin"), exe);

            if (javac.isFile()) {
                return true;
            }

            // java.home may point at a JRE nested in a JDK; check the parent too.
            File parentJavac = new File(new File(new File(javaHome).getParentFile(), "bin"), exe);

            if (parentJavac.isFile()) {
                return true;
            }
        }

        return canRun("javac", "-version");
    }

    private static boolean canRun(String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            // Drain output so the process can exit, then wait.
            try (java.io.InputStream in = process.getInputStream()) {
                byte[] buffer = new byte[1024];

                while (in.read(buffer) != -1) {
                    // discard
                }
            }

            return process.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os != null && os.toLowerCase(java.util.Locale.ROOT).contains("win");
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/EnvironmentDetector.java
git commit -m "feat(installer): detect git+JDK for build-from-source mode"
```

---

### Task 6: BranchService (GitHub branches)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/BranchService.java`

- [ ] **Step 1: Create the branch service**

```java
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
        try {
            URL url = new URI(endpoint).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/BranchService.java
git commit -m "feat(installer): list repository branches via GitHub API"
```

---

### Task 7: AddonSourceBuilder (clone/fetch + Gradle build)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonSourceBuilder.java`

- [ ] **Step 1: Create the source builder**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Builds an entry from a git branch into a jar, using a reusable source cache under
 * plugins/Slimefun/addon-sources/<repo>. First build clones; later builds fetch + hard-reset to
 * the chosen branch. Runs the repo's Gradle wrapper. Blocking — call off the main thread.
 */
public final class AddonSourceBuilder {

    /** The outcome of a build attempt. */
    public static final class Result {

        private final boolean success;
        private final File jar;
        private final File logFile;

        Result(boolean success, File jar, File logFile) {
            this.success = success;
            this.jar = jar;
            this.logFile = logFile;
        }

        public boolean isSuccess() {
            return success;
        }

        /** The produced jar (only when success). */
        @Nullable
        public File getJar() {
            return jar;
        }

        /** The full build log, always written. */
        @Nonnull
        public File getLogFile() {
            return logFile;
        }
    }

    /**
     * Clones/updates the repo to the given branch and runs `gradlew assemble`.
     *
     * @param timestamp
     *            a caller-supplied timestamp string for the log file name (Date.now() is unavailable
     *            in some contexts; callers pass System.currentTimeMillis()).
     */
    @Nonnull
    public Result build(@Nonnull AddonCatalog.Entry entry, @Nonnull String branch, long timestamp) {
        File repoDir = new File(InstallTargets.sourcesDir(), entry.getRepo());
        File logFile = new File(InstallTargets.logsDir(), entry.getRepo() + "-" + timestamp + ".log");

        boolean prepared = prepareSource(entry, branch, repoDir, logFile);

        if (!prepared) {
            return new Result(false, null, logFile);
        }

        boolean built = runGradle(repoDir, logFile);

        if (!built) {
            return new Result(false, null, logFile);
        }

        File jar = findBuiltJar(repoDir);
        return new Result(jar != null, jar, logFile);
    }

    private boolean prepareSource(AddonCatalog.Entry entry, String branch, File repoDir, File logFile) {
        String cloneUrl = "https://github.com/" + entry.getSlug() + ".git";

        if (new File(repoDir, ".git").isDirectory()) {
            return run(repoDir, logFile, "git", "fetch", "--depth", "1", "origin", branch)
                && run(repoDir, logFile, "git", "checkout", "-B", branch, "origin/" + branch)
                && run(repoDir, logFile, "git", "reset", "--hard", "origin/" + branch);
        }

        // Fresh clone (parent dir of repoDir is the sources root, which exists).
        return run(InstallTargets.sourcesDir(), logFile, "git", "clone", "--depth", "1", "--branch", branch, cloneUrl, entry.getRepo());
    }

    private boolean runGradle(File repoDir, File logFile) {
        String wrapper = EnvironmentDetector.isWindows() ? "gradlew.bat" : "./gradlew";
        return run(repoDir, logFile, wrapper, "assemble", "--no-daemon");
    }

    /**
     * Finds the built jar in build/libs, preferring a jar with no -sources/-javadoc classifier and
     * the newest modification time.
     */
    @Nullable
    private File findBuiltJar(File repoDir) {
        File libs = new File(repoDir, "build/libs");
        File[] jars = libs.listFiles((dir, name) -> name.endsWith(".jar") && !name.contains("-sources") && !name.contains("-javadoc"));

        if (jars == null || jars.length == 0) {
            return null;
        }

        File newest = jars[0];

        for (File jar : jars) {
            if (jar.lastModified() > newest.lastModified()) {
                newest = jar;
            }
        }

        return newest;
    }

    /** Runs a command in workingDir, appending combined stdout/stderr to logFile. */
    private boolean run(File workingDir, File logFile, String... command) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(workingDir);
            builder.redirectErrorStream(true);
            builder.redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));
            Process process = builder.start();
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            return false;
        }
    }

    /** Reads the last n lines of a log file for a chat summary. */
    @Nonnull
    public static java.util.List<String> tail(@Nonnull File logFile, int n) {
        java.util.LinkedList<String> lines = new java.util.LinkedList<>();

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(logFile), java.nio.charset.StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);

                if (lines.size() > n) {
                    lines.removeFirst();
                }
            }
        } catch (IOException e) {
            // Best-effort; return whatever we have.
        }

        return lines;
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonSourceBuilder.java
git commit -m "feat(installer): build addons from a git branch into a jar"
```

---

### Task 8: AddonInstaller (orchestration facade)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonInstaller.java`

This is the single entry point the menus call. It owns the transient "building/downloading" set, resolves dependencies, runs work async, updates `InstallState`, and messages the player.

- [ ] **Step 1: Create the facade**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * Orchestrates installs/updates/builds: dependency resolution, async download or build, state
 * updates, and player feedback. Menus call this; it never touches the UI directly beyond chat
 * messages.
 */
public final class AddonInstaller {

    private final AddonReleaseService releaseService = new AddonReleaseService();
    private final AddonSourceBuilder sourceBuilder = new AddonSourceBuilder();
    private final InstallState state;

    /** Entry ids with an install/build currently in flight, for the "working" badge. */
    private final Set<String> inProgress = ConcurrentHashMap.newKeySet();

    public AddonInstaller(@Nonnull InstallState state) {
        this.state = state;
    }

    @Nonnull
    public InstallState getState() {
        return state;
    }

    public boolean isInProgress(@Nonnull String id) {
        return inProgress.contains(id);
    }

    /** True when a plugin matching the entry's display/jar name is currently loaded. */
    public boolean isLoaded(@Nonnull AddonCatalog.Entry entry) {
        if (entry.isCore()) {
            return true;
        }

        for (Plugin plugin : Slimefun.instance().getServer().getPluginManager().getPlugins()) {
            if (plugin.getName().equalsIgnoreCase(entry.getRepo())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Installs (or updates) an entry from its latest release, plus any missing hard dependencies.
     * Runs entirely off the main thread; messages the player on completion.
     */
    public void installRelease(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry) {
        if (!inProgress.add(entry.getId())) {
            return;
        }

        runAsync(() -> {
            List<AddonCatalog.Entry> toInstall = new ArrayList<>();

            for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
                if (!isLoaded(dep)) {
                    toInstall.add(dep);
                }
            }

            toInstall.add(entry);

            List<String> staged = new ArrayList<>();
            boolean failure = false;

            for (AddonCatalog.Entry target : toInstall) {
                AddonReleaseService.ReleaseInfo info = releaseService.fetchLatest(target);

                if (info == null) {
                    message(player, ChatColor.RED + "✖ " + target.getDisplayName() + " is unavailable (no release / GitHub unreachable).");
                    failure = true;
                    break;
                }

                File dir = InstallTargets.targetDir(isLoaded(target));
                boolean ok = releaseService.downloadJar(info.getJarUrl(), dir, target.getRepo() + ".jar");

                if (!ok) {
                    message(player, ChatColor.RED + "✖ Failed to download " + target.getDisplayName() + ".");
                    failure = true;
                    break;
                }

                state.set(target.getId(), InstallState.Method.RELEASE, info.getTag(), true);
                staged.add(target.getDisplayName() + " " + info.getTag());
            }

            inProgress.remove(entry.getId());

            if (!failure) {
                message(player, ChatColor.GREEN + "✔ Staged: " + String.join(", ", staged) + ChatColor.GRAY + " — restart the server to apply.");
            }
        });
    }

    /**
     * Builds an entry from a branch and stages the resulting jar. Dependencies are NOT built from
     * source — they are installed from their latest release if missing.
     */
    public void buildFromBranch(@Nonnull Player player, @Nonnull AddonCatalog.Entry entry, @Nonnull String branch, long timestamp) {
        if (!inProgress.add(entry.getId())) {
            return;
        }

        runAsync(() -> {
            // Release-install any missing hard dependencies first.
            for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
                if (!isLoaded(dep)) {
                    AddonReleaseService.ReleaseInfo info = releaseService.fetchLatest(dep);

                    if (info != null) {
                        File dir = InstallTargets.targetDir(false);
                        releaseService.downloadJar(info.getJarUrl(), dir, dep.getRepo() + ".jar");
                        state.set(dep.getId(), InstallState.Method.RELEASE, info.getTag(), true);
                    }
                }
            }

            message(player, ChatColor.YELLOW + "⚙ Building " + entry.getDisplayName() + " from " + branch + "… this can take a few minutes.");
            AddonSourceBuilder.Result result = sourceBuilder.build(entry, branch, timestamp);

            if (!result.isSuccess() || result.getJar() == null) {
                message(player, ChatColor.RED + "✖ Build failed. Log: " + result.getLogFile().getPath());

                for (String line : AddonSourceBuilder.tail(result.getLogFile(), 15)) {
                    message(player, ChatColor.DARK_GRAY + line);
                }

                inProgress.remove(entry.getId());
                return;
            }

            File dir = InstallTargets.targetDir(isLoaded(entry));
            File dest = new File(dir, entry.getRepo() + ".jar");
            boolean copied = copy(result.getJar(), dest);
            inProgress.remove(entry.getId());

            if (copied) {
                state.set(entry.getId(), InstallState.Method.BRANCH, branch, true);
                message(player, ChatColor.GREEN + "✔ Built " + entry.getDisplayName() + " (" + branch + ") — restart the server to apply.");
            } else {
                message(player, ChatColor.RED + "✖ Build succeeded but staging the jar failed.");
            }
        });
    }

    private static boolean copy(File from, File to) {
        try {
            if (to.exists() && !to.delete()) {
                return false;
            }

            try (java.io.InputStream in = new java.io.FileInputStream(from); java.io.OutputStream out = new java.io.FileOutputStream(to)) {
                byte[] buffer = new byte[8192];
                int read;

                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            }

            return true;
        } catch (java.io.IOException e) {
            return false;
        }
    }

    private static void runAsync(Runnable runnable) {
        Slimefun.instance().getServer().getScheduler().runTaskAsynchronously(Slimefun.instance(), runnable);
    }

    private static void message(Player player, String text) {
        Slimefun.instance().getServer().getScheduler().runTask(Slimefun.instance(), () -> {
            if (player.isOnline()) {
                player.sendMessage(text);
            }
        });
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonInstaller.java
git commit -m "feat(installer): orchestrate installs, updates and branch builds"
```

---

### Task 9: AddonInstallerMenu (grid screen)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonInstallerMenu.java`

Reference: pagination/menu pattern matches `core/guide/options/ContributorsMenu.java` and `AddonVisibilityMenu.java`. A single shared `AddonInstaller` instance is created once and reused (releases are fetched fresh each open via the detail menu / badges).

- [ ] **Step 1: Create the grid menu**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.core.guide.options.SlimefunGuideSettings;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Screen 1 of the installer: a grid of Slimefun core + every addon with a status badge in lore.
 * Clicking an entry opens {@link AddonDetailMenu}.
 */
public final class AddonInstallerMenu {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 50, 51, 52, 53 };

    /** Shared installer state for the JVM lifetime — created lazily on first open. */
    private static AddonInstaller installer;

    private AddonInstallerMenu() {}

    @Nonnull
    public static AddonInstaller installer() {
        if (installer == null) {
            installer = new AddonInstaller(new InstallState());
        }

        return installer;
    }

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        menu.addItem(49, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back to Settings"));
        menu.addMenuClickHandler(49, (pl, slot, item, action) -> {
            SlimefunGuideSettings.openSettings(pl, guide);
            return false;
        });

        AddonInstaller inst = installer();
        List<AddonCatalog.Entry> entries = AddonCatalog.getEntries();

        int slot = 9;

        for (AddonCatalog.Entry entry : entries) {
            if (slot >= 45) {
                break;
            }

            menu.addItem(slot, icon(p, inst, entry));
            menu.addMenuClickHandler(slot, (pl, sl, item, action) -> {
                AddonDetailMenu.open(pl, guide, entry);
                return false;
            });
            slot++;
        }

        menu.open(p);
    }

    @Nonnull
    private static ItemStack icon(Player p, AddonInstaller inst, AddonCatalog.Entry entry) {
        String badge = StatusBadges.badge(inst, entry);
        return CustomItemStack.create(MaterialCompat.stack(entry.getIcon()),
            "&f" + entry.getDisplayName(),
            "",
            badge,
            "",
            "&7⇨ &eClick for details");
    }
}
```

- [ ] **Step 2: Create the shared badge helper**

This is used by both menu screens, so it lives in its own tiny class.

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/StatusBadges.java`

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import javax.annotation.Nonnull;

/**
 * Computes the one-line status badge shown for an entry, from local state only (no network).
 * The "update available" comparison against the latest release happens in the detail menu, which
 * fetches live; the grid shows installed/loaded/working/restart-pending from local knowledge.
 */
final class StatusBadges {

    private StatusBadges() {}

    @Nonnull
    static String badge(@Nonnull AddonInstaller inst, @Nonnull AddonCatalog.Entry entry) {
        if (inst.isInProgress(entry.getId())) {
            return "&e⚙ Working…";
        }

        InstallState.Record record = inst.getState().get(entry.getId());

        if (record != null && record.isRestartPending()) {
            return "&b↻ Restart to apply (" + record.getVersion() + ")";
        }

        if (entry.isCore()) {
            return "&a✔ Installed v" + io.github.thebusybiscuit.slimefun5.implementation.Slimefun.getVersion();
        }

        if (inst.isLoaded(entry)) {
            return "&a✔ Installed";
        }

        // Local-only "requires X": list hard dependencies that aren't loaded yet. (They are auto-
        // installed on click, but surfacing the requirement up front matches the spec's badge table.)
        java.util.List<String> missing = new java.util.ArrayList<>();

        for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
            if (!inst.isLoaded(dep)) {
                missing.add(dep.getDisplayName());
            }
        }

        if (!missing.isEmpty()) {
            return "&7＋ Not installed &8(needs " + String.join(", ", missing) + ")";
        }

        return "&7＋ Not installed";
    }
}
```

- [ ] **Step 3: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 4: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonInstallerMenu.java core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/StatusBadges.java
git commit -m "feat(installer): add installer grid menu with status badges"
```

---

### Task 10: AddonDetailMenu (per-entry actions)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonDetailMenu.java`

- [ ] **Step 1: Create the detail menu**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Screen 2: one entry's status and actions. The "Install/Update" action (Mode A) is always
 * available; "Build from branch" (Mode B) appears only when the dev environment supports it.
 * The button label switches between "Install" and "Update" based on whether the plugin is loaded;
 * the actual latest-release lookup happens when the player clicks (inside AddonInstaller, async),
 * so opening the menu makes no network call.
 */
public final class AddonDetailMenu {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53 };

    private AddonDetailMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull AddonCatalog.Entry entry) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        AddonInstaller inst = AddonInstallerMenu.installer();

        menu.addItem(0, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back"));
        menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
            AddonInstallerMenu.open(pl, guide);
            return false;
        });

        List<String> deps = new ArrayList<>();

        for (AddonCatalog.Entry dep : AddonCatalog.resolveDependencies(entry)) {
            if (!inst.isLoaded(dep)) {
                deps.add(dep.getDisplayName());
            }
        }

        // Header (the entry itself).
        List<String> headerLore = new ArrayList<>();
        headerLore.add("");
        headerLore.add(StatusBadges.badge(inst, entry));

        if (!deps.isEmpty()) {
            headerLore.add("&7Will also install: &f" + String.join(", ", deps));
        }

        menu.addItem(13, CustomItemStack.create(MaterialCompat.stack(entry.getIcon()), "&f" + entry.getDisplayName(), headerLore.toArray(new String[0])));
        menu.addMenuClickHandler(13, ChestMenuUtils.getEmptyClickHandler());

        // Action: install/update from latest release (always available).
        String label = inst.isLoaded(entry) ? "&aUpdate to latest release" : "&aInstall latest release";
        menu.addItem(29, CustomItemStack.create(MaterialCompat.stack(XMaterial.LIME_DYE), label, "", "&7Downloads the latest GitHub release", "&7and stages it for the next restart.", "", "&7⇨ &eClick"));
        menu.addMenuClickHandler(29, (pl, slot, item, action) -> {
            inst.installRelease(pl, entry);
            pl.closeInventory();
            return false;
        });

        // Action: build from branch (Mode B, dev only).
        if (EnvironmentDetector.canBuildFromSource()) {
            menu.addItem(33, CustomItemStack.create(MaterialCompat.stack(XMaterial.WHEAT_SEEDS), "&bBuild from a branch", "", "&7Clone & compile a chosen branch", "&7(developer environments only).", "", "&7⇨ &eClick to pick a branch"));
            menu.addMenuClickHandler(33, (pl, slot, item, action) -> {
                BranchSelectMenu.open(pl, guide, entry, 0);
                return false;
            });
        }

        menu.open(p);
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonDetailMenu.java
git commit -m "feat(installer): add per-addon detail menu with install/build actions"
```

---

### Task 11: BranchSelectMenu (branch picker)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/BranchSelectMenu.java`

- [ ] **Step 1: Create the branch picker**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.installer;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import com.cryptomorin.xseries.XMaterial;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * Screen 3: a paginated list of the repo's branches. Branches are fetched async, then the menu is
 * rendered on the main thread. Selecting a branch starts the build via {@link AddonInstaller}.
 */
public final class BranchSelectMenu {

    private BranchSelectMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide, @Nonnull AddonCatalog.Entry entry, int page) {
        // Fetch branches off the main thread, then render.
        Slimefun.instance().getServer().getScheduler().runTaskAsynchronously(Slimefun.instance(), () -> {
            BranchService service = new BranchService();
            List<String> branches = service.fetchBranches(entry);
            Slimefun.instance().getServer().getScheduler().runTask(Slimefun.instance(), () -> render(p, guide, entry, branches, page));
        });
    }

    private static void render(Player p, ItemStack guide, AddonCatalog.Entry entry, List<String> branches, int page) {
        ChestMenu menu = new ChestMenu(Slimefun.getLocalization().getMessage(p, "guide.title.installer"));
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, 0, 2, 3, 4, 5, 6, 7, 8, 45, 47, 48, 49, 50, 51, 53);

        menu.addItem(1, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back"));
        menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
            AddonDetailMenu.open(pl, guide, entry);
            return false;
        });

        if (branches.isEmpty()) {
            menu.addItem(22, CustomItemStack.create(MaterialCompat.stack(XMaterial.BARRIER), "&cNo branches found", "", "&7Couldn't reach GitHub or the repo has no branches."));
            menu.addMenuClickHandler(22, ChestMenuUtils.getEmptyClickHandler());
            menu.open(p);
            return;
        }

        for (int i = page * 36; i < branches.size() && i < (page + 1) * 36; i++) {
            String branch = branches.get(i);
            menu.addItem(i - page * 36 + 9, CustomItemStack.create(MaterialCompat.stack(XMaterial.WHEAT_SEEDS), "&a" + branch, "", "&7⇨ &eClick to build this branch"));
            menu.addMenuClickHandler(i - page * 36 + 9, (pl, slot, item, action) -> {
                inst().buildFromBranch(pl, entry, branch, System.currentTimeMillis());
                pl.closeInventory();
                return false;
            });
        }

        int pages = (branches.size() - 1) / 36 + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page + 1, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            if (page > 0) {
                render(pl, guide, entry, branches, page - 1);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page + 1, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            if (page + 1 < pages) {
                render(pl, guide, entry, branches, page + 1);
            }

            return false;
        });

        menu.open(p);
    }

    private static AddonInstaller inst() {
        return AddonInstallerMenu.installer();
    }
}
```

- [ ] **Step 2: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/BranchSelectMenu.java
git commit -m "feat(installer): add paginated branch picker for build-from-source"
```

---

### Task 12: Wire-up (settings slot 47 + permission + messages)

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/SlimefunGuideSettings.java:162-179`
- Modify: `core/src/main/resources/plugin.yml` (permissions block, end of file)
- Modify: `core/src/main/resources/languages/en/messages.yml:151` (guide.title block)

- [ ] **Step 1: Replace the slot 47 wiki link with the permission-gated installer**

In `SlimefunGuideSettings.java`, replace the entire slot-47 block (the `menu.addItem(47, ...)` for `guide.title.addons` and its `addMenuClickHandler(47, ...)` that opens the wiki Addons URL — lines 162-179) with:

```java
        // @formatter:off
        if (p.hasPermission(io.github.thebusybiscuit.slimefun5.core.guide.installer.AddonCatalog.PERMISSION)) {
            menu.addItem(47, CustomItemStack.create(Material.BOOKSHELF,
                "&3" + locale.getMessage(p, "guide.title.installer"),
                "",
                "&7Install, update or build Slimefun and its",
                "&7addons without leaving the game.",
                "",
                "&7Installed on this Server: &b" + Slimefun.getInstalledAddons().size(),
                "",
                "&7⇨ &eClick to open the Addon Installer"));
            // @formatter:on

            menu.addMenuClickHandler(47, (pl, slot, item, action) -> {
                io.github.thebusybiscuit.slimefun5.core.guide.installer.AddonInstallerMenu.open(pl, guide);
                return false;
            });
        } else {
            // @formatter:off
            menu.addItem(47, CustomItemStack.create(Material.BOOKSHELF,
                "&3" + locale.getMessage(p, "guide.title.addons"),
                "",
                "&7Slimefun is huge. But its addons are what makes",
                "&7this plugin truly shine. Go check them out, some",
                "&7of them may be exactly what you were missing out on!",
                "",
                "&7Installed on this Server: &b" + Slimefun.getInstalledAddons().size(),
                "",
                "&7⇨ &eClick to see all available addons for Slimefun5"));
            // @formatter:on

            menu.addMenuClickHandler(47, (pl, slot, item, action) -> {
                pl.closeInventory();
                ChatUtils.sendURL(pl, "https://github.com/Slimefun5/Slimefun5/wiki/Addons");
                return false;
            });
        }
```

(Permitted players get the installer; everyone else keeps the original wiki link, so no functionality is lost.)

- [ ] **Step 2: Declare the permission in plugin.yml**

Append to the `permissions:` block at the end of `core/src/main/resources/plugin.yml`:

```yaml
  slimefun.installer:
    description: Allows you to use the in-game Addon Installer
    default: op
```

- [ ] **Step 3: Add the menu title message**

In `core/src/main/resources/languages/en/messages.yml`, in the `guide.title` block (after the `addon-visibility:` line at line 151), add:

```yaml
    installer: 'Addon Installer'
```

- [ ] **Step 4: Verify it compiles**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 5: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/SlimefunGuideSettings.java core/src/main/resources/plugin.yml core/src/main/resources/languages/en/messages.yml
git commit -m "feat(installer): wire installer into guide settings slot 47"
```

---

### Task 13: Build, boot & dry-run verification

**Files:** none (verification only).

- [ ] **Step 1: Full core compile**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 2: Build the shadow jar**

Run: `./gradlew :core:shadowJar -q`
Expected: exit 0; a jar appears in `core/build/libs/`.

- [ ] **Step 3: 1.8.8 boot test**

Run the project's run script for 1.8.8 (e.g. `scripts/run.ps1`, selecting 1.8.8 and skipping addons), or the headless variant. Wait for Slimefun to enable.
Expected: server enables, no exceptions from the `installer` package in the log.

- [ ] **Step 4: In-game smoke test (as op)**

In-game: open the Slimefun Guide → Settings → slot 47 ("Addon Installer"). 
Expected: the grid opens; Slimefun core shows `✔ Installed v<version>`; addons show `＋ Not installed`; clicking one opens the detail menu with an "Install latest release" button.

- [ ] **Step 5: Release-install dry-run**

Click a no-dependency addon (e.g. LiteXpansion) → "Install latest release". 
Expected: chat shows `✔ Staged: LiteXpansion <tag> — restart…`; `plugins/LiteXpansion.jar` exists; `plugins/Slimefun/addon-installer.yml` records it with `restart-pending: true`. Restart and confirm LiteXpansion loads.

- [ ] **Step 6: Dependency dry-run**

Click InfinityExpansion → "Install latest release" on a server without InfinityLib. 
Expected: chat lists both InfinityLib and InfinityExpansion staged; both jars appear in `/plugins`.

- [ ] **Step 7: Commit any fixes**

If steps 3-6 surfaced fixes, commit them with `fix(installer): …`. Otherwise nothing to commit.

---

## Notes for the implementer

- **Rate limits & badge scope:** unauthenticated GitHub API allows 60 requests/hour per IP. The grid uses only local state (no per-entry API call), and detail/branch screens fetch on demand — so a normal session stays well under the limit. Do not add a startup sweep of all 16 release endpoints. Consequence for the spec's badge table: the **grid** shows the local-only badges (`✔ Installed`, `↻ Restart to apply`, `⚙ Working…`, `＋ Not installed [needs …]`). The network-dependent states (`⬆ Update available`, `✖ Unavailable`) surface at click time via the detail menu's Install/Update action and its chat feedback, not as live grid badges.
- **`Date.now()`/`new Date()`** are fine in normal plugin code; the `timestamp` parameter on `AddonSourceBuilder.build` exists only so callers pass `System.currentTimeMillis()` explicitly (keeps the builder pure/testable).
- **No hot-loading:** never call `PluginManager.loadPlugin` — staged jars apply on restart only. This is a deliberate spec decision.
- **Update vs install target** is decided by `AddonInstaller.isLoaded(entry)`: a loaded plugin (incl. core) → update folder; otherwise `/plugins`.
