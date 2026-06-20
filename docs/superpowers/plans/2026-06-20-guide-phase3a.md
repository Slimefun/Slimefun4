# Guide Phase 3a Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Four bounded guide-usability improvements — installer keeps the UI open, Addon Visibility moves to the main menu (redesigned, ≥1 enforced), single-subcategory passthrough, and search results show `Theme ▸ Category`.

**Architecture:** Targeted edits to existing guide classes; no new files. Builds on Phase 1 (themes) + Phase 2 (installer).

**Tech Stack:** Java 8 floor (no `var`/records/switch-expr/`List.of`/text blocks), Bukkit (1.8.8 floor), `ChestMenu`, `CustomItemStack`, `XMaterial`/`MaterialCompat`.

---

## ⚠️ Verification model

Unit tests are disabled project-wide (MockBukkit needs Java 25). Per-task gate: `./gradlew :core:compileJava -q` exits 0 (run from `core/Slimefun5/`). Final gate: boot test (Task 6). No JUnit tests. Commit convention: `type(scope): summary`. Branch `feature/java8-universal-jar`. Spec: `docs/superpowers/specs/2026-06-20-guide-phase3a-design.md`.

**Note on glow:** the spec mentioned an enchant glow on the visibility toggles. Enchantment constants are version-fragile on the 1.8→26.x universal jar, so this plan uses lime/gray **stained-glass panes + colored ✔/✖ text** instead — a clean, version-safe visual. No glow.

---

### Task 1: Installer keeps the UI open (Feature 1)

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonDetailMenu.java`
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/BranchSelectMenu.java`

- [ ] **Step 1: Re-open the detail menu instead of closing on install**

In `AddonDetailMenu.java`, the install/update click handler (slot 29) currently closes the inventory. Replace:
```java
        menu.addMenuClickHandler(29, (pl, slot, item, action) -> {
            inst.installRelease(pl, entry);
            pl.closeInventory();
            return false;
        });
```
with:
```java
        menu.addMenuClickHandler(29, (pl, slot, item, action) -> {
            inst.installRelease(pl, entry);
            // Keep the guide open; re-render so the header badge shows "Working…".
            open(pl, guide, entry);
            return false;
        });
```

- [ ] **Step 2: Re-open the detail menu instead of closing on branch build**

In `BranchSelectMenu.java`, the branch-select click handler currently closes the inventory. Replace:
```java
            menu.addMenuClickHandler(i - page * 36 + 9, (pl, slot, item, action) -> {
                inst().buildFromBranch(pl, entry, branch, System.currentTimeMillis());
                pl.closeInventory();
                return false;
            });
```
with:
```java
            menu.addMenuClickHandler(i - page * 36 + 9, (pl, slot, item, action) -> {
                inst().buildFromBranch(pl, entry, branch, System.currentTimeMillis());
                // Keep the guide open; return to the detail menu (header badge shows "Working…").
                AddonDetailMenu.open(pl, guide, entry);
                return false;
            });
```

- [ ] **Step 3: Compile**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 4: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/AddonDetailMenu.java core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/installer/BranchSelectMenu.java
git commit -m "feat(installer): keep the guide open after starting an install/build"
```

---

### Task 2: Search results show `Theme ▸ Category` (Feature 4)

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java`

- [ ] **Step 1: Add the GuideTheme import**

In the import block of `SurvivalSlimefunGuide.java`, add (next to the existing `core.guide.themes.*` imports):
```java
import io.github.thebusybiscuit.slimefun5.core.guide.themes.GuideTheme;
```

- [ ] **Step 2: Enrich the search-result lore**

In `openSearch`, replace the lore line that currently reads:
```java
                ItemStack itemstack = CustomItemStack.create(slimefunItem.getItem(), meta -> {
                    ItemGroup itemGroup = slimefunItem.getItemGroup();
                    meta.setLore(Arrays.asList("", ChatColor.DARK_GRAY + "⇨ " + ChatColor.WHITE + itemGroup.getDisplayName(p)));
                    VersionedItemFlag.addFlags(meta, VersionedItemFlag.HIDE_ATTRIBUTES, VersionedItemFlag.HIDE_ENCHANTS, VersionedItemFlag.HIDE_ADDITIONAL_TOOLTIP);
                });
```
with:
```java
                ItemStack itemstack = CustomItemStack.create(slimefunItem.getItem(), meta -> {
                    ItemGroup itemGroup = slimefunItem.getItemGroup();
                    GuideTheme theme = GuideTheme.byId(itemGroup.getThemeId());
                    if (theme == null) {
                        theme = GuideTheme.MISC;
                    }
                    String themeName = Slimefun.getLocalization().getMessage(p, "guide.themes." + theme.getId());
                    meta.setLore(Arrays.asList("", ChatColor.DARK_GRAY + "⇨ " + ChatColor.WHITE + themeName + ChatColor.GRAY + " ▸ " + ChatColor.WHITE + itemGroup.getDisplayName(p)));
                    VersionedItemFlag.addFlags(meta, VersionedItemFlag.HIDE_ATTRIBUTES, VersionedItemFlag.HIDE_ENCHANTS, VersionedItemFlag.HIDE_ADDITIONAL_TOOLTIP);
                });
```
(`GuideTheme.byId` returns null for a null/unknown theme id, so we fall back to `GuideTheme.MISC`. `guide.themes.<id>` keys already exist in `messages.yml`.)

- [ ] **Step 3: Compile**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 4: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java
git commit -m "feat(guide): show Theme > Category location in search results"
```

---

### Task 3: Single-subcategory passthrough (Feature 3)

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java`
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/api/items/groups/NestedItemGroup.java`

- [ ] **Step 1: Theme passthrough in openThemeContents**

In `SurvivalSlimefunGuide.java`, replace the top of `openThemeContents`:
```java
    public void openThemeContents(@Nonnull PlayerProfile profile, @Nonnull ThemeItemGroup themeGroup, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        if (isSurvivalMode()) {
            profile.getGuideHistory().add(themeGroup, page);
        }

        List<ItemGroup> categories = themeGroup.getCategories();
```
with:
```java
    public void openThemeContents(@Nonnull PlayerProfile profile, @Nonnull ThemeItemGroup themeGroup, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        List<ItemGroup> categories = themeGroup.getCategories();

        // A theme with a single category opens that category directly. The empty theme view is skipped
        // and not added to history, so back-navigation returns to the main menu.
        if (categories.size() == 1) {
            openItemGroup(profile, categories.get(0), 1);
            return;
        }

        if (isSurvivalMode()) {
            profile.getGuideHistory().add(themeGroup, page);
        }
```

- [ ] **Step 2: Nested-group passthrough in NestedItemGroup.open**

In `NestedItemGroup.java`, replace:
```java
    @Override
    @ParametersAreNonnullByDefault
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        openGuide(p, profile, mode, 1);
    }
```
with:
```java
    @Override
    @ParametersAreNonnullByDefault
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        // A nested group with a single sub-group opens it directly, skipping the one-item menu.
        if (subGroups.size() == 1) {
            SlimefunGuide.openItemGroup(profile, subGroups.get(0), mode, 1);
            return;
        }

        openGuide(p, profile, mode, 1);
    }
```
(`subGroups` is the existing field; `SlimefunGuide.openItemGroup` is already used elsewhere in this file, so no new import.)

- [ ] **Step 3: Compile**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 4: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java core/src/main/java/io/github/thebusybiscuit/slimefun5/api/items/groups/NestedItemGroup.java
git commit -m "feat(guide): open single-child themes and nested groups directly"
```

---

### Task 4: Redesign AddonVisibilityMenu + enforce ≥1 shown (Feature 2, part A)

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/AddonVisibilityMenu.java`

- [ ] **Step 1: Add imports**

Ensure these imports are present in `AddonVisibilityMenu.java` (add any missing):
```java
import org.bukkit.ChatColor;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuide;
```
Remove the now-unused `SlimefunGuideSettings` import if present.

- [ ] **Step 2: Back button returns to the guide, add an info header, restyle toggles, enforce ≥1**

Replace the body of `open(...)` from the back-button block through the end of the addon loop with:
```java
        menu.addItem(49, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK), "&e⇦ Back"));
        menu.addMenuClickHandler(49, (pl, slot, item, action) -> {
            SlimefunGuide.openGuide(pl, guide);
            return false;
        });

        // Info header explaining the toggle + the "at least one" rule.
        menu.addItem(4, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOK),
            "&eAddon Visibility",
            "",
            "&7Click an addon to toggle whether it",
            "&7appears in your guide (browse + search).",
            "",
            "&7At least one addon must stay shown."));
        menu.addMenuClickHandler(4, ChestMenuUtils.getEmptyClickHandler());

        // Map of addon id (the category NamespacedKey namespace, lowercased) -> display name. A map also
        // dedupes in case Slimefun lists itself among the installed addons.
        Map<String, String> addons = new LinkedHashMap<>();
        addons.put("slimefun", "Slimefun");
        for (Plugin addon : Slimefun.getInstalledAddons()) {
            addons.put(addon.getName().toLowerCase(), addon.getName());
        }

        int slot = 9;
        for (Map.Entry<String, String> entry : addons.entrySet()) {
            if (slot >= 45) {
                Slimefun.logger().warning("[Guide] Addon Visibility menu is full; "
                    + (addons.size() - (slot - 9)) + " addon(s) beyond the first " + (slot - 9) + " are not listed.");
                break;
            }

            String addonId = entry.getKey();
            boolean visible = !AddonVisibility.isHidden(p, addonId);
            ItemStack icon = CustomItemStack.create(
                MaterialCompat.stack(visible ? XMaterial.LIME_STAINED_GLASS_PANE : XMaterial.GRAY_STAINED_GLASS_PANE),
                (visible ? "&a" : "&7") + entry.getValue(),
                "",
                visible ? "&a✔ Shown" : "&c✖ Hidden",
                "",
                "&7⇨ &eClick to toggle");

            menu.addItem(slot, icon);
            menu.addMenuClickHandler(slot, (pl, sl, item, action) -> {
                // Enforce at least one shown: refuse to hide the last visible addon.
                if (visible) {
                    int shown = 0;
                    for (String id : addons.keySet()) {
                        if (!AddonVisibility.isHidden(pl, id)) {
                            shown++;
                        }
                    }
                    if (shown <= 1) {
                        pl.sendMessage(ChatColor.RED + "At least one addon must stay visible.");
                        return false;
                    }
                }

                AddonVisibility.setHidden(pl, addonId, visible);
                open(pl, guide);
                return false;
            });
            slot++;
        }

        menu.open(p);
```
This replaces the previous back-button block, the addon map, and the addon loop (everything between `ChestMenuUtils.drawBackground(...)` and the final `menu.open(p);`). Keep the existing `drawBackground` call and the `BORDER` constant as they are. Note slot 4 is in `BORDER`, so the info item is set after the background draw and overrides it.

- [ ] **Step 3: Compile**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 4: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/AddonVisibilityMenu.java
git commit -m "feat(guide): restyle addon visibility menu and require >=1 addon shown"
```

---

### Task 5: Move the Addon Visibility entry to the main menu (Feature 2, part B)

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java`
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/SlimefunGuideSettings.java`

- [ ] **Step 1: Add the visibility button to the main menu header**

In `SurvivalSlimefunGuide.java`, add the imports (if missing):
```java
import io.github.thebusybiscuit.slimefun5.core.guide.options.AddonVisibilityMenu;
```
(`com.cryptomorin.xseries.XMaterial`, `MaterialCompat`, and `CustomItemStack` are already imported.)

In `openMainMenu`, immediately after `createHeader(p, profile, menu);` add:
```java
        // Addon Visibility entry (main menu only).
        menu.addItem(4, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOKSHELF),
            "&3Addon Visibility",
            "",
            "&7Choose which addons appear in your guide.",
            "&7Hidden addons are removed from browsing",
            "&7and search — just for you.",
            "",
            "&7⇨ &eClick to manage"));
        menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
            AddonVisibilityMenu.open(pl, this.item);
            return false;
        });
```
(`this.item` is the guide `ItemStack` field on `SurvivalSlimefunGuide`.)

- [ ] **Step 2: Remove the Addon Visibility entry from Settings**

In `SlimefunGuideSettings.java`, delete the slot-51 block that opens `AddonVisibilityMenu`:
```java
        menu.addItem(51, CustomItemStack.create(MaterialCompat.stack(XMaterial.BOOKSHELF),
            "&3" + locale.getMessage(p, "guide.title.addon-visibility"),
            "",
            "&7Choose which addons appear in your guide.",
            "&7Hidden addons are removed from browsing",
            "&7and search — just for you.",
            "",
            "&7⇨ &eClick to manage"), (pl, slot, item, action) -> {
            AddonVisibilityMenu.open(pl, guide);
            return false;
        });
```
Delete that entire statement. Slot 51 is already in `BACKGROUND_SLOTS`, so it reverts to background automatically. If the `AddonVisibilityMenu` import becomes unused, remove it.

- [ ] **Step 3: Compile**

Run: `./gradlew :core:compileJava -q`
Expected: exit 0.

- [ ] **Step 4: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/SlimefunGuideSettings.java
git commit -m "feat(guide): move addon visibility entry from settings to main menu"
```

---

### Task 6: Build & boot verification

**Files:** none.

- [ ] **Step 1: Full compile + shadow jar**

Run: `./gradlew :core:compileJava -q` then `./gradlew :core:shadowJar -q`
Expected: both exit 0; a jar appears in `core/build/libs/`.

- [ ] **Step 2: Boot test (any version, e.g. via run.ps1)**

Open the guide as a player. Verify:
- Main menu shows the **Addon Visibility** button at slot 4 (top center); Settings no longer has it at slot 51.
- The visibility menu uses lime/gray panes; trying to hide the last shown addon is refused with a chat message.
- A theme containing a single category opens that category directly; *back* returns to the main menu.
- Search results show `⇨ Theme ▸ Category`.
- From the installer detail menu, starting an install no longer closes the guide.

- [ ] **Step 3: Commit any fixes**

Commit with `fix(guide): …` if the boot test surfaced issues; otherwise nothing to commit.

---

## Notes for the implementer

- Match the exact current text in each file before editing — Task 1's handlers and Task 4's loop were written in earlier phases and should match the snippets above, but verify.
- `AddonVisibilityMenu.open(Player, ItemStack)` is the existing signature; the `guide` ItemStack is only used to return to the guide on back.
- Do not touch `CheatSheetSlimefunGuide` — it inherits `openThemeContents`/search via `SurvivalSlimefunGuide`.
