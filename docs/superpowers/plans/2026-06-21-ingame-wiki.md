# In-Game Central Wiki — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: superpowers:subagent-driven-development.
> Steps use checkbox (`- [ ]`) syntax. NOTE: unit tests are disabled in this repo (MockBukkit needs
> Java 25). The gate for every task is `./gradlew :core:compileJava -q` exit 0 (and `:core:shadowJar`
> for the final task). Verify compile after each task instead of running unit tests.

**Goal:** An in-game, no-external-links wiki: per-item explanation prose + "used in" navigation layered
on the guide's existing recipe view, reachable from the item wiki button and a settings entry.

**Architecture:** new `core/guide/wiki/` package — `WikiText` (prose layer + auto fallback + addon API),
`ReverseRecipeIndex` (cached item→consumers), `WikiPage` (per-item ChestMenu), `WikiIndex` (entry point).
Reuses `SurvivalSlimefunGuide.displayItem` for recipe rendering. All external `sendURL` docs links removed.

**Tech Stack:** Java 8, Bukkit/Spigot API, Slimefun core, XMaterial via MaterialCompat. No var/records/
switch-expr/List.of. Follow `core/guide/installer/` style (synchronized state, @Nonnull, no enchant-glow).

---

### Task 1: `WikiText` prose layer + bundled resources + addon API

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/wiki/WikiText.java`
- Create: `core/src/main/resources/wiki/items.yml` (seed with a few entries; bulk content comes later)
- Create: `core/src/main/resources/wiki/mechanics.yml` (seed: research, energy, cargo, multiblocks)
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/api/items/SlimefunItem.java` (add `setWikiText(String...)`)
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/Slimefun.java` (hold a `WikiText` instance + `getWikiText()` accessor, load resources at enable)

- [ ] **Step 1:** Create `WikiText` with: `synchronized Map<String,List<String>>` store; `set(String id, List<String> lines)`; `setMechanic(String id, List<String> lines)`; `boolean has(String id)`; `List<String> get(SlimefunItem item)` returning authored lines if present else an auto-generated fallback (`"A " + group + " item."` + recipe-type line via `item.getRecipeType()`); `loadBundled()` reading the two YAML resources via `YamlConfiguration.loadConfiguration(new InputStreamReader(getResource(...)))`. Mirror `InstallState` style (synchronized, @Nonnull, Slimefun.logger() on failure).
- [ ] **Step 2:** Add `Slimefun.getWikiText()` static accessor + a private field set in onEnable (before `registerListeners`), call `wikiText.loadBundled()`.
- [ ] **Step 3:** Add `SlimefunItem.setWikiText(String... lines)` delegating to `Slimefun.getWikiText().set(getId(), Arrays.asList(lines))`. @Nonnull, returns void.
- [ ] **Step 4:** Seed `items.yml` with 3 example entries (e.g. `ENHANCED_FURNACE`, `ELECTRIC_SMELTERY`, `GRIND_STONE`) and `mechanics.yml` with `research`, `energy`, `cargo`, `multiblocks`.
- [ ] **Step 5:** Run `./gradlew :core:compileJava -q` — expect exit 0. Commit: `feat(wiki): WikiText prose layer + bundled resources + addon API`.

### Task 2: `ReverseRecipeIndex`

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/wiki/ReverseRecipeIndex.java`

- [ ] **Step 1:** Implement a class with a `volatile Map<String,List<SlimefunItem>>` cache and
  `List<SlimefunItem> getConsumers(SlimefunItem target)` that builds the full index on first call
  (double-checked locking): iterate `Slimefun.getRegistry().getEnabledSlimefunItems()`; for each, scan
  `getRecipe()` (skip nulls) and, if it is a `MultiBlockMachine`, its `getRecipes()` inputs; map each
  ingredient stack to its producer via `SlimefunItem.getByItem(stack)`; if non-null, add the consuming
  item under that ingredient id. Dedupe consumers per id.
- [ ] **Step 2:** `./gradlew :core:compileJava -q` exit 0. Commit: `feat(wiki): cached reverse-recipe index for "used in"`.

### Task 3: `WikiPage` per-item menu

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/wiki/WikiPage.java`

- [ ] **Step 1:** Static `open(Player p, ItemStack guide, SlimefunItem item)` building a `ChestMenu`
  titled from the item name. Draw a background border. Slot for the item icon + name header. An
  info item (book/paper via MaterialCompat) whose lore is `Slimefun.getWikiText().get(item)`.
- [ ] **Step 2:** If `item instanceof EnergyNetComponent` (and/or `AContainer`), add a stats item with
  lore lines: capacity (`getCapacity()`), and for `AContainer` energy/tick (`getEnergyConsumption()`)
  + speed (`getSpeed()`). Guard each with instanceof; skip cleanly otherwise.
- [ ] **Step 3:** Add a "View recipe" button that calls the guide's `displayItem` for this item
  (obtain the survival guide via `Slimefun.getRegistry().getSlimefunGuide(SlimefunGuideMode.SURVIVAL_MODE)`
  and the player's profile; match how SlimefunGuideSettings opens guide screens).
- [ ] **Step 4:** Add a paged "Used in" row: for each consumer from `ReverseRecipeIndex.getConsumers(item)`,
  place its icon; click opens `WikiPage.open` for that consumer. Page with prev/next if > row capacity.
- [ ] **Step 5:** Back button at slot 0 returns to the previous screen (pass the opener via a parameter
  or return to `WikiIndex`). `./gradlew :core:compileJava -q` exit 0.
  Commit: `feat(wiki): per-item WikiPage (explanation, stats, used-in, navigation)`.

### Task 4: `WikiIndex` entry point + guide integration + link removal

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/wiki/WikiIndex.java`
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java`
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/SlimefunGuideSettings.java`
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/PlayerLanguageOption.java`

- [ ] **Step 1:** `WikiIndex.open(Player, ItemStack guide)` — present the themed groups (reuse the
  guide's visible item-group logic); clicking a group drills to its items; clicking an item opens
  `WikiPage`. Keep it simple: a paged grid of all enabled item groups → items. Include a search button
  that opens the guide's search but routes hits to `WikiPage` (if routing is hard, omit search in v1
  and note it).
- [ ] **Step 2:** SurvivalSlimefunGuide.java:618-624 — replace the slot-8 wiki button so it shows for
  every item and its click calls `WikiPage.open(pl, guide, item)`. Remove the `getWikipage()` gate and
  the `ChatUtils.sendURL` call.
- [ ] **Step 3:** SlimefunGuideSettings.java:160 — wiki button opens `WikiIndex.open`. Lines 143
  (source) and 199 (issues) — remove the buttons; add those slots to `BACKGROUND_SLOTS` (like slot 47).
- [ ] **Step 4:** PlayerLanguageOption.java:94 — remove the `sendURL` translating-link line (keep the
  language option otherwise). Remove now-unused imports.
- [ ] **Step 5:** `./gradlew :core:compileJava -q` exit 0.
  Commit: `feat(wiki): WikiIndex entry point; route guide wiki buttons in-game; remove external links`.

### Task 5: Group 4 — rehome SlimefunAdvancements out of Misc

**Files:**
- Modify: the SlimefunAdvancements addon's setup (in `F:\Documents\GitHub\slimefun\addons\SlimefunAdvancements`)
  where its guide group's `setTheme(...)` is called (Phase-1 set it to `misc`).

- [ ] **Step 1:** Locate the `setTheme("misc")` (or untagged) call on SlimefunAdvancements' top-level
  guide group. Change it to a more fitting theme. Since advancements are progression, use an existing
  theme that fits best (review `GuideTheme` ids); if none fits, leave a dedicated theme id and add a
  `guide.themes.<id>` localization key in core `messages.yml`.
- [ ] **Step 2:** Review other addons that land in Misc (`grep setTheme("misc")` across addons); rehome
  any that clearly fit an existing theme, leave the rest.
- [ ] **Step 3:** Build-verify the changed addon(s) (`gradlew shadowJar`). Commit each repo (no push).

### Task 6: Final verification + prose content authoring

- [ ] **Step 1:** `./gradlew :core:shadowJar -q` exit 0; confirm `wiki/items.yml` + `wiki/mechanics.yml`
  are bundled in the jar.
- [ ] **Step 2:** Bulk-author `wiki/items.yml` prose for the high-value items (machines, multiblocks,
  generators, androids, geo-miners, notable tools/weapons/armor/gadgets) and `wiki/mechanics.yml` hubs.
  (Done via parallel subagents keyed off `SlimefunItems` ids; auto-fallback covers the rest.)
- [ ] **Step 3:** `./gradlew :core:compileJava -q` + `:core:shadowJar` exit 0.
  Commit: `content(wiki): author explanations for core machines, items, and mechanics`.
