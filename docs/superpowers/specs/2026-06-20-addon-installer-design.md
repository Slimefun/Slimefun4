# In-Game Addon Installer — Design (Guide Rework Phase 2)

**Date:** 2026-06-20
**Branch:** `feature/java8-universal-jar`
**Status:** Approved design, ready for implementation planning

## Goal

Add an in-game installer, reachable from the Slimefun Guide settings, that lets a
permitted player install, update, and (in a dev environment) build-from-branch any
Slimefun5 addon **and Slimefun core itself** — without leaving the game or touching the
server console.

## Context

Phase 1 (combined themed guide) is complete: the guide top level shows themes, addons
declare their themes, and a per-player Addon Visibility menu lives at settings slot 51.

Phase 2 repurposes **settings slot 47** — currently a link to the wiki "Addons" page
(`SlimefunGuideSettings.addHeader`, the `Material.BOOKSHELF` item) — into the installer
entry point.

The dev workspace already enumerates the 15 addon repos in `scripts/run.ps1`
(`$availableAddons`, all under the `Slimefun5/` org). Core already performs GitHub API
calls via `GitHubService`.

## Scope

In scope (this build):

- **Mode A — install latest release**: download a GitHub release jar to the server. Works
  on any server.
- **Mode B — compile from a branch**: dev-only; clone/fetch the repo, run Gradle, place the
  built jar. Gated on git + JDK + Gradle being present.
- **Self-update of Slimefun core**, treated as a first-class catalog entry, via the Bukkit
  update folder.
- **Unified Update** action that works the same regardless of how an addon was installed.
- **Automatic dependency resolution** for hard dependencies (e.g. InfinityLib).

Out of scope:

- Uninstalling addons in-game (manual file deletion remains the path).
- Configuring individual addon settings.
- Runtime hot-loading of plugins (explicitly rejected — see Activation Model).

## Activation Model

Bukkit plugin jars are file-locked by the running JVM (notably on Windows) and Slimefun
addons cannot be safely hot-loaded at runtime (research / item-group registration order,
inter-addon dependencies). Therefore **no runtime class loading**. Two write targets:

- **New install** (addon not currently loaded): write to `/plugins/<name>.jar`.
- **Update / self-update** (plugin already loaded, including Slimefun core): write to the
  server's **update folder**, which Bukkit swaps in on the next start, matching by the
  `plugin.yml` name regardless of filename. The update folder sidesteps the live-jar file
  lock and makes self-overwrite safe.

The update folder is a CraftBukkit feature inherited by every Bukkit fork (Spigot, Paper,
Purpur, …) — i.e. every server that can run Slimefun. Its name is configurable in
`bukkit.yml`, so the installer **must resolve it through the API**, never hardcode it:

```java
File updateDir = Bukkit.getServer().getUpdateFolderFile(); // honors bukkit.yml; default plugins/update
```

`getUpdateFolderFile()` exists since early Bukkit API, so it is safe on the 1.8.8 floor. The
directory is created if absent.

After any successful stage, the addon is flagged **restart-pending** and the player is told
to restart. State survives restarts (see `InstallState`).

## Architecture

New package: `io.github.thebusybiscuit.slimefun5.core.guide.installer`

| Component | Responsibility |
|---|---|
| `AddonCatalog` | Static source of truth: every installable entry — Slimefun core (`Slimefun5/Slimefun5`) plus the 15 addons. Each entry holds id, `owner/repo`, display name, icon (`XMaterial`), and a hard-dependency id list. Mirrors `run.ps1`'s `$availableAddons`. |
| `AddonReleaseService` | Async. Queries `repos/<owner>/<repo>/releases/latest`; for core uses the latest **full** release (excludes `gh-v*` prereleases), for addons the latest release. Resolves the jar asset URL + version tag; downloads jars. Reuses the `GitHubService` HTTP/connector pattern. |
| `BranchService` | Async. Lists a repo's branches via `repos/<owner>/<repo>/branches` for the Mode B branch picker. |
| `AddonSourceBuilder` | Mode B. Manages a **reusable** source cache under the Slimefun plugin data folder: `<dataFolder>/addon-sources/<repo>`. First run `git clone`; later `git fetch` + checkout the chosen branch. Runs the repo's Gradle wrapper to build, locates the produced jar, copies it to the correct write target. Dev-gated. |
| `EnvironmentDetector` | Detects `git` on PATH, a JDK (`javac` resolvable), and a usable Gradle wrapper in a cloned repo. Mode B UI appears only when all are present. |
| `InstallState` | Persists per-entry install method (`RELEASE` / `BRANCH`), the installed version-or-branch, and a restart-pending flag. Stored as `<dataFolder>/addon-installer.yml`. Powers correct status badges across restarts and the unified Update action. |
| `AddonInstallerMenu` | Screen 1: grid of catalog entries with status badges. |
| `AddonDetailMenu` | Screen 2: one entry's detail + action buttons. |
| `BranchSelectMenu` | Screen 3: paginated branch picker for Mode B. |

All network / git / Gradle work runs on Bukkit's **async scheduler**; menu redraws and
chat feedback are scheduled back onto the **main thread**. The server main thread never
blocks.

## Data Flow

1. Player (with permission) clicks settings slot 47 → `AddonInstallerMenu.open`.
2. Menu reads `InstallState` and renders immediately with cached/known status, then fires an
   async `AddonReleaseService` sweep for latest versions; badges refresh on completion.
3. Click an entry → `AddonDetailMenu`. Buttons:
   - **Install latest release** / **Update** (Mode A) — always available.
   - **Build from branch ▸** (Mode B) — only when `EnvironmentDetector` passes; opens
     `BranchSelectMenu`.
4. On install/update: resolve missing hard dependencies via `AddonCatalog`, download (Mode A)
   or build (Mode B) each jar to the correct write target, update `InstallState`, mark
   restart-pending, notify the player.

## Menu UX

**Screen 1 — `AddonInstallerMenu`:** Slimefun core pinned top-left, then the 15 addons.
Per-entry status badge in lore:

| Badge | Meaning |
|---|---|
| `✔ Installed v1.2.3` | Loaded, up to date |
| `⬆ Update available 1.2.3 → 1.3.0` | Newer release exists |
| `↻ Restart to apply` | Install/update staged, awaiting restart |
| `⚙ Building…` | Mode B Gradle build in progress |
| `＋ Not installed` | Available to install |
| `⚠ Requires InfinityLib` | Missing hard dependency |
| `✖ Unavailable` | No release / fetch failed |

**Screen 2 — `AddonDetailMenu`:** name, description, status, and action buttons (above).
Auto-resolved dependencies are listed ("Will also install: InfinityLib").

**Screen 3 — `BranchSelectMenu`:** paginated branch list from `BranchService`; selecting a
branch starts the async build.

**Feedback:** long ops show `⚙ Building…` / downloading; the player may close the menu and
receives a chat message on completion — success (`✔ InfinityExpansion 1.3.0 staged — restart
to apply`) or failure (`✖ Build failed — see plugins/Slimefun/installer-logs/…`). Reopening
the menu reflects new state.

## Access Control

A new permission **`slimefun.installer`**, default **op**. Players without it do not see the
installer button at slot 47 (slot falls back to the existing wiki link or an inert
background). Server owners grant it via any permissions plugin.

## Error Handling

- **GitHub unreachable** → badge `✖`, message "couldn't reach GitHub, try again later".
- **No jar asset on the release** → badge `✖ Unavailable`.
- **Download** → write to a `.tmp` file, then atomic-rename into place; discard `.tmp` on
  failure (never leave a half-written jar).
- **Gradle build failure** → retain the last ~15 log lines for the chat summary, write the
  full log to `<dataFolder>/installer-logs/<repo>-<timestamp>.log`, surface the path.
- **Permission missing** → button absent (defense in addition to hiding).
- **Mode B env missing** → Mode B button hidden; Mode A still works.

## Testing

Unit tests remain disabled project-wide (MockBukkit requires Java 25, incompatible with the
Java-8 floor). Verification gates:

1. `./gradlew :core:compileJava -q` exits 0.
2. 1.8.8 boot test: server enables, the installer opens from settings slot 47 (as op), grid
   renders with badges.
3. Manual dry-run: install one addon with no dependencies (e.g. LiteXpansion) — confirm the
   staged jar lands in `/plugins`, `addon-installer.yml` records it, and the badge shows
   `↻ Restart to apply`; restart and confirm the addon loads.
4. Dependency dry-run: install InfinityExpansion on a server without InfinityLib — confirm
   both jars stage.

## File Structure

- Create `core/.../core/guide/installer/AddonCatalog.java`
- Create `core/.../core/guide/installer/AddonReleaseService.java`
- Create `core/.../core/guide/installer/BranchService.java`
- Create `core/.../core/guide/installer/AddonSourceBuilder.java`
- Create `core/.../core/guide/installer/EnvironmentDetector.java`
- Create `core/.../core/guide/installer/InstallState.java`
- Create `core/.../core/guide/installer/AddonInstallerMenu.java`
- Create `core/.../core/guide/installer/AddonDetailMenu.java`
- Create `core/.../core/guide/installer/BranchSelectMenu.java`
- Modify `core/.../core/guide/options/SlimefunGuideSettings.java` (slot 47 → installer, permission-gated)
- Modify `core/src/main/resources/plugin.yml` (declare `slimefun.installer` permission, default op)
- Modify `core/src/main/resources/languages/en/messages.yml` (installer titles + status strings)
