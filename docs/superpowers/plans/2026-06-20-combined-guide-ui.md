# Combined Guide UI (Phase 1) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make the Slimefun Guide present Slimefun + all addons as one themed library (themes → categories → items), with a per-player menu to hide addons, and finish the unused Settings slot.

**Architecture:** A theme layer rides on Slimefun's existing `FlexItemGroup` extension point. Each `ItemGroup` carries an optional theme id (declared by its owner; unset → Misc). At guide-open time the top level lists transient `ThemeItemGroup`s (one per non-empty theme); opening one lists that theme's categories; opening a category is the existing untouched flow. A per-player hidden-addon set (stored via `PdcCompat`) filters categories and search.

**Tech Stack:** Java 8, Bukkit/Spigot 1.8.8 API floor, Slimefun guide framework (`ChestMenu`, `ItemGroup`, `FlexItemGroup`), XSeries, `PdcCompat`/`MaterialCompat` compat helpers.

> **Verification note:** This fork **disables the test tasks** (`compileTestJava`/`test` are `enabled = false` in `core/build.gradle.kts` — MockBukkit needs Java 25+, incompatible with the Java 8 toolchain). There is no runnable unit-test harness, so each task is gated on **compile** (`./gradlew :core:compileJava -q` → `EXIT=0`) and the feature is verified by a **boot test** in the final task. Do not add JUnit tests; they would never run.

> **Key import gotcha:** `ItemGroup`, `FlexItemGroup`, and `PdcCompat` all use the **relocated** key type `io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey` — NOT `org.bukkit.NamespacedKey`. Every new file that builds a key MUST import the relocated one (mirror `DefaultItemGroups`).

**Working dir for all commands:** `F:/Documents/GitHub/slimefun/core/Slimefun5`
**Branch:** `feature/java8-universal-jar` (author `finn@birich.de`; never override identity).

---

## File Structure

**New files (all under `core/src/main/java/io/github/thebusybiscuit/slimefun5/`):**
- `core/guide/themes/GuideTheme.java` — enum catalogue of themes (id, name, icon, order).
- `core/guide/themes/ThemeItemGroup.java` — `FlexItemGroup` representing one theme at the top level.
- `core/guide/themes/ThemeRegistry.java` — buckets categories into themes, builds the theme groups for a player.
- `core/guide/AddonVisibility.java` — per-player hidden-addon set, persisted via `PdcCompat`.
- `core/guide/options/AddonVisibilityMenu.java` — the toggle menu opened from Settings.

**Modified files:**
- `api/items/ItemGroup.java` — add the optional theme id field + accessors.
- `implementation/guide/SurvivalSlimefunGuide.java` — top level returns themes; add `openThemeContents`; search respects visibility.
- `implementation/guide/CheatSheetSlimefunGuide.java` — move its override from `getVisibleItemGroups` to `collectVisibleCategories`.
- `core/guide/options/SlimefunGuideSettings.java` — slot 51 opens `AddonVisibilityMenu`.
- `implementation/setup/DefaultItemGroups.java` — constructor tags Slimefun's own categories.
- `src/main/resources/languages/en/messages.yml` — theme names + addon-visibility menu strings.

---

## Task 1: Add the theme id to `ItemGroup`

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/api/items/ItemGroup.java`

- [ ] **Step 1: Add the field + accessors**

In `ItemGroup` (after the existing `tier` field; the class already imports `javax.annotation.Nullable`), add:

```java
    /**
     * The optional guide-theme id this {@link ItemGroup} belongs to (see GuideTheme). Declared by the
     * category's owner (Slimefun or an addon). {@code null} = untagged, which the guide buckets into Misc.
     */
    @Nullable
    private String themeId;

    /**
     * Tags this {@link ItemGroup} with a guide-theme id. Fluent; safe to call once during setup.
     *
     * @param themeId the theme id, or {@code null} to clear
     * @return this {@link ItemGroup}
     */
    @Nonnull
    public ItemGroup setTheme(@Nullable String themeId) {
        this.themeId = themeId;
        return this;
    }

    /**
     * @return the guide-theme id, or {@code null} if untagged.
     */
    @Nullable
    public String getThemeId() {
        return themeId;
    }
```

(If `Nullable`/`Nonnull` are not already imported, add `import javax.annotation.Nullable;` and `import javax.annotation.Nonnull;`.)

- [ ] **Step 2: Compile**

Run: `./gradlew :core:compileJava -q ; echo "EXIT=$?"`
Expected: `EXIT=0`

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/api/items/ItemGroup.java
git commit -m "feat(guide): add optional theme id to ItemGroup"
```

---

## Task 2: Create the `GuideTheme` catalogue

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/themes/GuideTheme.java`

- [ ] **Step 1: Create the enum**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.themes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cryptomorin.xseries.XMaterial;

/**
 * The fixed catalogue of top-level guide themes. Every {@link io.github.thebusybiscuit.slimefun5.api.items.ItemGroup}
 * is bucketed into exactly one of these (its declared theme, else {@link #MISC}). Icons are all materials
 * that exist on Minecraft 1.8 so the guide renders on the legacy floor.
 */
public enum GuideTheme {

    WEAPONS("weapons", "&cWeapons", XMaterial.DIAMOND_SWORD, 10),
    TOOLS("tools", "&aTools", XMaterial.DIAMOND_PICKAXE, 20),
    ARMOR("armor", "&bArmor", XMaterial.DIAMOND_CHESTPLATE, 30),
    MACHINES("machines", "&6Machines", XMaterial.FURNACE, 40),
    ENERGY_TECH("energy_tech", "&eEnergy & Tech", XMaterial.REDSTONE, 50),
    RESOURCES("resources", "&fResources", XMaterial.IRON_INGOT, 60),
    MAGIC("magic", "&dMagic", XMaterial.ENCHANTED_BOOK, 70),
    FOOD("food", "&2Food & Farming", XMaterial.BREAD, 80),
    LOGISTICS("logistics", "&3Logistics", XMaterial.CHEST, 90),
    DECORATION("decoration", "&5Decoration", XMaterial.PAINTING, 100),
    MISC("misc", "&7Misc", XMaterial.CHEST_MINECART, 110);

    private final String id;
    private final String defaultName;
    private final XMaterial icon;
    private final int order;

    GuideTheme(String id, String defaultName, XMaterial icon, int order) {
        this.id = id;
        this.defaultName = defaultName;
        this.icon = icon;
        this.order = order;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    @Nonnull
    public String getDefaultName() {
        return defaultName;
    }

    @Nonnull
    public XMaterial getIcon() {
        return icon;
    }

    public int getOrder() {
        return order;
    }

    @Nullable
    public static GuideTheme byId(@Nullable String id) {
        if (id == null) {
            return null;
        }

        for (GuideTheme theme : values()) {
            if (theme.id.equals(id)) {
                return theme;
            }
        }

        return null;
    }
}
```

- [ ] **Step 2: Compile**

Run: `./gradlew :core:compileJava -q ; echo "EXIT=$?"`
Expected: `EXIT=0`

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/themes/GuideTheme.java
git commit -m "feat(guide): add GuideTheme catalogue"
```

---

## Task 3: Create `AddonVisibility` (per-player store)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/AddonVisibility.java`

Uses `PdcCompat.getString(Object holder, NamespacedKey key)` / `setString(Object holder, NamespacedKey key, String value)` (verified signatures), with the **relocated** `NamespacedKey`.

- [ ] **Step 1: Create the class**

```java
package io.github.thebusybiscuit.slimefun5.core.guide;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.PdcCompat;

/**
 * Per-player set of addon ids (category {@code NamespacedKey} namespaces) hidden from that player's guide.
 * Persisted via {@link PdcCompat} as a single comma-separated string, so it works on every Minecraft
 * version (PDC on 1.14+, legacy YAML fallback on 1.8-1.13). Default: empty (everything visible).
 */
public final class AddonVisibility {

    private static final NamespacedKey KEY = new NamespacedKey(Slimefun.instance(), "guide_hidden_addons");

    private AddonVisibility() {}

    @Nonnull
    public static Set<String> getHidden(@Nonnull Player p) {
        String raw = PdcCompat.getString(p, KEY);

        if (raw == null || raw.isEmpty()) {
            return new HashSet<>();
        }

        return new HashSet<>(Arrays.asList(raw.split(",")));
    }

    public static boolean isHidden(@Nonnull Player p, @Nonnull String addonId) {
        return getHidden(p).contains(addonId.toLowerCase(Locale.ROOT));
    }

    public static void setHidden(@Nonnull Player p, @Nonnull String addonId, boolean hidden) {
        Set<String> hiddenSet = getHidden(p);
        String id = addonId.toLowerCase(Locale.ROOT);

        if (hidden) {
            hiddenSet.add(id);
        } else {
            hiddenSet.remove(id);
        }

        PdcCompat.setString(p, KEY, String.join(",", hiddenSet));
    }
}
```

- [ ] **Step 2: Compile**

Run: `./gradlew :core:compileJava -q ; echo "EXIT=$?"`
Expected: `EXIT=0`
(If compile complains that `PdcCompat.getString` takes a different parameter type, open `utils/compatibility/PdcCompat.java` and match the exact signature — it is `getString(Object, NamespacedKey)` / `setString(Object, NamespacedKey, String)` with the relocated `NamespacedKey`.)

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/AddonVisibility.java
git commit -m "feat(guide): add per-player AddonVisibility store"
```

---

## Task 4: Create `ThemeItemGroup` (the FlexItemGroup)

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/themes/ThemeItemGroup.java`

- [ ] **Step 1: Create the class**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.themes;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.libraries.keys.NamespacedKey;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.api.items.groups.FlexItemGroup;
import io.github.thebusybiscuit.slimefun5.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideImplementation;
import io.github.thebusybiscuit.slimefun5.core.guide.SlimefunGuideMode;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.guide.SurvivalSlimefunGuide;

/**
 * A top-level guide entry representing one {@link GuideTheme}. Built transiently per guide-open from the
 * player's currently-visible categories, so it never pollutes the global item-group registry. Clicking it
 * lists the theme's member categories via {@link SurvivalSlimefunGuide#openThemeContents}.
 */
public class ThemeItemGroup extends FlexItemGroup {

    private final GuideTheme theme;
    private final List<ItemGroup> categories;

    public ThemeItemGroup(@Nonnull GuideTheme theme, @Nonnull ItemStack icon, @Nonnull List<ItemGroup> categories) {
        super(new NamespacedKey(Slimefun.instance(), "theme_" + theme.getId()), icon, theme.getOrder());
        this.theme = theme;
        this.categories = categories;
    }

    @Nonnull
    public GuideTheme getTheme() {
        return theme;
    }

    @Nonnull
    public List<ItemGroup> getCategories() {
        return categories;
    }

    @Override
    public boolean isVisible(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        return true;
    }

    @Override
    public void open(Player p, PlayerProfile profile, SlimefunGuideMode mode) {
        SlimefunGuideImplementation guide = Slimefun.getRegistry().getSlimefunGuide(mode);

        if (guide instanceof SurvivalSlimefunGuide) {
            ((SurvivalSlimefunGuide) guide).openThemeContents(profile, this, 1);
        }
    }
}
```

- [ ] **Step 2: Compile**

Run: `./gradlew :core:compileJava -q ; echo "EXIT=$?"`
Expected: it will FAIL with `cannot find symbol: method openThemeContents` — that method is added in Task 6. This is expected; proceed to Task 5 and 6, then compile.

- [ ] **Step 3: Commit (after Task 6 makes it compile — see Task 6 Step 4). Do not commit a non-compiling tree.**

> Note: Tasks 4, 5, 6 are interdependent (theme group ↔ registry ↔ guide method). Create all three, then compile and commit together in Task 6.

---

## Task 5: Create `ThemeRegistry`

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/themes/ThemeRegistry.java`

- [ ] **Step 1: Create the class**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.themes;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun5.core.guide.AddonVisibility;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

/**
 * Buckets the guide's visible categories into {@link GuideTheme}s and builds the transient
 * {@link ThemeItemGroup} list shown at the guide's top level. A category's theme is its declared theme id,
 * or {@link GuideTheme#MISC} when untagged. Categories owned by an addon the player has hidden
 * (see {@link AddonVisibility}) are dropped. Empty themes are omitted.
 */
public final class ThemeRegistry {

    private ThemeRegistry() {}

    @Nonnull
    public static GuideTheme themeOf(@Nonnull ItemGroup group) {
        GuideTheme theme = GuideTheme.byId(group.getThemeId());
        return theme != null ? theme : GuideTheme.MISC;
    }

    /**
     * Builds the ordered list of non-empty {@link ThemeItemGroup}s for a player, given the raw set of
     * categories the guide would otherwise show.
     */
    @Nonnull
    public static List<ItemGroup> buildThemeGroups(@Nonnull Player p, @Nonnull List<ItemGroup> categories) {
        Map<GuideTheme, List<ItemGroup>> byTheme = new EnumMap<>(GuideTheme.class);

        for (ItemGroup group : categories) {
            String addonId = group.getKey().getNamespace();

            if (AddonVisibility.isHidden(p, addonId)) {
                continue;
            }

            byTheme.computeIfAbsent(themeOf(group), k -> new ArrayList<>()).add(group);
        }

        List<ItemGroup> result = new ArrayList<>();

        for (GuideTheme theme : GuideTheme.values()) {
            List<ItemGroup> members = byTheme.get(theme);

            if (members != null && !members.isEmpty()) {
                String name = Slimefun.getLocalization().getMessage(p, "guide.themes." + theme.getId());

                if (name == null || name.startsWith("guide.themes.")) {
                    name = theme.getDefaultName();
                }

                ItemStack icon = CustomItemStack.create(MaterialCompat.stack(theme.getIcon()), name,
                    "", "&7Categories: &e" + members.size(), "", "&7⇨ &eClick to open");
                result.add(new ThemeItemGroup(theme, icon, members));
            }
        }

        return result;
    }
}
```

- [ ] **Step 2: Compile** — deferred to Task 6 (depends on Task 6's guide method). Proceed to Task 6.

---

## Task 6: Wire the guide top level to themes

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java`
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/CheatSheetSlimefunGuide.java`

- [ ] **Step 1: Rename the raw collector and make `getVisibleItemGroups` return themes**

In `SurvivalSlimefunGuide.java`, the current method is:

```java
    protected @Nonnull List<ItemGroup> getVisibleItemGroups(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        List<ItemGroup> groups = new LinkedList<>();

        for (ItemGroup group : Slimefun.getRegistry().getAllItemGroups()) {
            try {
                if (group instanceof FlexItemGroup) {
                    FlexItemGroup flexItemGroup = (FlexItemGroup) group;                    if (flexItemGroup.isVisible(p, profile, getMode())) {
                        groups.add(group);
                    }
                } else if (!group.isHidden(p)) {
                    groups.add(group);
                }
            } catch (Exception | LinkageError x) {
                SlimefunAddon addon = group.getAddon();

                if (addon != null) {
                    addon.getLogger().log(Level.SEVERE, x, () -> "Could not display item group: " + group);
                } else {
                    Slimefun.logger().log(Level.SEVERE, x, () -> "Could not display item group: " + group);
                }
            }
        }

        return groups;
    }
```

Replace it with these two methods (rename the body to `collectVisibleCategories`, and add a themed `getVisibleItemGroups` — but **skip our own `ThemeItemGroup`s** to avoid double-wrapping, and skip the raw categories so only themes show at the top level):

```java
    /**
     * Collects the raw {@link ItemGroup} categories the guide would show (before the theme layer). Override
     * point for guide variants (e.g. the cheat sheet shows more). Excludes our transient ThemeItemGroups.
     */
    protected @Nonnull List<ItemGroup> collectVisibleCategories(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        List<ItemGroup> groups = new LinkedList<>();

        for (ItemGroup group : Slimefun.getRegistry().getAllItemGroups()) {
            try {
                if (group instanceof ThemeItemGroup) {
                    continue;
                } else if (group instanceof FlexItemGroup) {
                    FlexItemGroup flexItemGroup = (FlexItemGroup) group;
                    if (flexItemGroup.isVisible(p, profile, getMode())) {
                        groups.add(group);
                    }
                } else if (!group.isHidden(p)) {
                    groups.add(group);
                }
            } catch (Exception | LinkageError x) {
                SlimefunAddon addon = group.getAddon();

                if (addon != null) {
                    addon.getLogger().log(Level.SEVERE, x, () -> "Could not display item group: " + group);
                } else {
                    Slimefun.logger().log(Level.SEVERE, x, () -> "Could not display item group: " + group);
                }
            }
        }

        return groups;
    }

    /**
     * The top-level entries shown on the guide's main menu: one {@link ThemeItemGroup} per non-empty theme,
     * built from {@link #collectVisibleCategories}. Raw categories are reachable only through their theme.
     */
    protected @Nonnull List<ItemGroup> getVisibleItemGroups(@Nonnull Player p, @Nonnull PlayerProfile profile) {
        return ThemeRegistry.buildThemeGroups(p, collectVisibleCategories(p, profile));
    }
```

Add imports near the other guide imports in `SurvivalSlimefunGuide.java`:

```java
import io.github.thebusybiscuit.slimefun5.core.guide.themes.GuideTheme;
import io.github.thebusybiscuit.slimefun5.core.guide.themes.ThemeItemGroup;
import io.github.thebusybiscuit.slimefun5.core.guide.themes.ThemeRegistry;
```

- [ ] **Step 2: Add `openThemeContents`**

In `SurvivalSlimefunGuide.java`, add this method directly after `openMainMenu(...)` (it mirrors `openMainMenu`'s paging but lists the theme's categories and supports a back button):

```java
    /**
     * Opens the contents of a single theme: a paginated grid of that theme's member categories, with a
     * back button to the main menu. Pushed onto guide history so back-navigation from a category returns here.
     */
    public void openThemeContents(@Nonnull PlayerProfile profile, @Nonnull ThemeItemGroup themeGroup, int page) {
        Player p = profile.getPlayer();

        if (p == null) {
            return;
        }

        if (isSurvivalMode()) {
            profile.getGuideHistory().add(themeGroup, page);
        }

        List<ItemGroup> categories = themeGroup.getCategories();

        ChestMenu menu = create(p);
        createHeader(p, profile, menu);
        addBackButton(menu, 1, p, profile);

        int index = 9;
        int target = (MAX_ITEM_GROUPS * (page - 1)) - 1;

        while (target < (categories.size() - 1) && index < MAX_ITEM_GROUPS + 9) {
            target++;
            showItemGroup(menu, p, profile, categories.get(target), index);
            index++;
        }

        int pages = target == categories.size() - 1 ? page : (categories.size() - 1) / MAX_ITEM_GROUPS + 1;

        menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
        menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
            int next = page - 1;

            if (next != page && next > 0) {
                openThemeContents(profile, themeGroup, next);
            }

            return false;
        });

        menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
        menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
            int next = page + 1;

            if (next != page && next <= pages) {
                openThemeContents(profile, themeGroup, next);
            }

            return false;
        });

        menu.open(p);
    }
```

`showItemGroup`, `addBackButton`, `create`, `createHeader`, `MAX_ITEM_GROUPS`, and `ChestMenuUtils` are all already members/imports of this class — no new imports beyond Step 1. (`GuideTheme` import from Step 1 may be unused after this; remove it if the compiler warns — `ThemeItemGroup` and `ThemeRegistry` are used.)

- [ ] **Step 3: Point the cheat sheet's override at the new collector**

In `CheatSheetSlimefunGuide.java`, the current override is `protected List<ItemGroup> getVisibleItemGroups(...)`. Rename it to `collectVisibleCategories` so the cheat sheet still broadens the raw set but inherits the themed top level:

Change the method signature line from:

```java
    @Override
    protected List<ItemGroup> getVisibleItemGroups(@Nonnull Player p, @Nonnull PlayerProfile profile) {
```

to:

```java
    @Override
    protected List<ItemGroup> collectVisibleCategories(@Nonnull Player p, @Nonnull PlayerProfile profile) {
```

(The method body — iterating `getAllItemGroups()` and adding non-flex or visible-flex groups — stays as-is. Add `if (group instanceof ThemeItemGroup) continue;` as the first check inside the loop to mirror the base, and add `import io.github.thebusybiscuit.slimefun5.core.guide.themes.ThemeItemGroup;`.)

- [ ] **Step 4: Compile (Tasks 4–6 together)**

Run: `./gradlew :core:compileJava -q ; echo "EXIT=$?"`
Expected: `EXIT=0`

- [ ] **Step 5: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/themes/ \
        core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java \
        core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/CheatSheetSlimefunGuide.java
git commit -m "feat(guide): show themes at top level, categories inside each theme"
```

---

## Task 7: Filter search by addon visibility

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java` (the `openSearch` loop, ~line 357-362)

- [ ] **Step 1: Add the visibility guard**

The current loop condition in `openSearch` is:

```java
            if (!slimefunItem.isHidden() && isItemGroupAccessible(p, slimefunItem) && isSearchFilterApplicable(slimefunItem, searchTerm)) {
```

Change it to also skip items whose owning addon the player has hidden:

```java
            if (!slimefunItem.isHidden()
                && !AddonVisibility.isHidden(p, slimefunItem.getItemGroup().getKey().getNamespace())
                && isItemGroupAccessible(p, slimefunItem)
                && isSearchFilterApplicable(slimefunItem, searchTerm)) {
```

Add the import:

```java
import io.github.thebusybiscuit.slimefun5.core.guide.AddonVisibility;
```

- [ ] **Step 2: Compile**

Run: `./gradlew :core:compileJava -q ; echo "EXIT=$?"`
Expected: `EXIT=0`
(If `slimefunItem.getItemGroup()` is not the accessor name, check `SlimefunItem` — it is `getItemGroup()`.)

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/guide/SurvivalSlimefunGuide.java
git commit -m "feat(guide): exclude hidden addons from guide search"
```

---

## Task 8: Repurpose Settings slot 51 → Addon Visibility menu

**Files:**
- Create: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/AddonVisibilityMenu.java`
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/SlimefunGuideSettings.java` (slot 51)

- [ ] **Step 1: Create the menu**

```java
package io.github.thebusybiscuit.slimefun5.core.guide.options;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cryptomorin.xseries.XMaterial;

import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.core.guide.AddonVisibility;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;

/**
 * A per-player menu listing every installed addon with an on/off toggle controlling whether that addon's
 * categories and items appear in the player's guide (browse + search). Backed by {@link AddonVisibility}.
 */
public final class AddonVisibilityMenu {

    private static final int[] BORDER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 50, 51, 52, 53 };

    private AddonVisibilityMenu() {}

    public static void open(@Nonnull Player p, @Nonnull ItemStack guide) {
        ChestMenu menu = new ChestMenu("Addon Visibility");
        menu.setEmptySlotsClickable(false);
        ChestMenuUtils.drawBackground(menu, BORDER);

        // Back to the settings menu.
        menu.addItem(49, CustomItemStack.create(MaterialCompat.stack(XMaterial.ENCHANTED_BOOK),
            "&e⇦ Back to Settings"));
        menu.addMenuClickHandler(49, (pl, slot, item, action) -> {
            SlimefunGuideSettings.openSettings(pl, guide);
            return false;
        });

        List<String> addonIds = new ArrayList<>();
        addonIds.add("slimefun");
        for (SlimefunAddon addon : Slimefun.getInstalledAddons()) {
            addonIds.add(addon.getJavaPlugin().getName().toLowerCase());
        }

        int slot = 9;
        for (String addonId : addonIds) {
            if (slot >= 45) {
                break; // one page is enough for Slimefun's addon count; pagination is a later refinement
            }

            boolean visible = !AddonVisibility.isHidden(p, addonId);
            String state = visible ? "&aShown" : "&cHidden";
            ItemStack icon = CustomItemStack.create(
                MaterialCompat.stack(visible ? XMaterial.LIME_DYE : XMaterial.GRAY_DYE),
                "&f" + addonId, "", "&7State: " + state, "", "&7⇨ &eClick to toggle");

            menu.addItem(slot, icon);
            menu.addMenuClickHandler(slot, (pl, sl, item, action) -> {
                AddonVisibility.setHidden(pl, addonId, visible); // flip current state
                open(pl, guide);
                return false;
            });
            slot++;
        }

        menu.open(p);
    }
}
```

(If `XMaterial.LIME_DYE`/`GRAY_DYE` resolve to `null` on 1.8, `MaterialCompat.stack` already falls back safely — no crash. `SlimefunAddon.getJavaPlugin()` is the correct accessor.)

- [ ] **Step 2: Wire slot 51 in `SlimefunGuideSettings`**

In `SlimefunGuideSettings.java`, the current slot-51 block is:

```java
        menu.addItem(51, CustomItemStack.create(XMaterial.TOTEM_OF_UNDYING.parseMaterial(), ChatColor.RED + locale.getMessage(p, "guide.work-in-progress")), (pl, slot, item, action) -> {
            // Add something here
            return false;
        });
```

Replace it with:

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

Add the imports to `SlimefunGuideSettings.java`:

```java
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;
```

(`AddonVisibilityMenu` is in the same package `core.guide.options`, so no import needed. `CustomItemStack`, `XMaterial`, `ChatColor` are already imported.)

- [ ] **Step 3: Compile**

Run: `./gradlew :core:compileJava -q ; echo "EXIT=$?"`
Expected: `EXIT=0`

- [ ] **Step 4: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/AddonVisibilityMenu.java \
        core/src/main/java/io/github/thebusybiscuit/slimefun5/core/guide/options/SlimefunGuideSettings.java
git commit -m "feat(guide): finish settings slot 51 as the Addon Visibility menu"
```

---

## Task 9: Tag Slimefun's own categories with themes

**Files:**
- Modify: `core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/setup/DefaultItemGroups.java`

`DefaultItemGroups` is a class of `final` field initializers with no constructor. Add a constructor that tags each group (runs when the class is instantiated during setup; the theme tag is only read at guide-open time, so timing is safe).

- [ ] **Step 1: Add the constructor + import**

Add the import:

```java
import io.github.thebusybiscuit.slimefun5.core.guide.themes.GuideTheme;
```

Add this constructor inside the class, immediately before the closing brace `}` (after the `rickFlexGroup` field):

```java
    DefaultItemGroups() {
        weapons.setTheme(GuideTheme.WEAPONS.getId());
        tools.setTheme(GuideTheme.TOOLS.getId());
        usefulItems.setTheme(GuideTheme.MISC.getId());
        basicMachines.setTheme(GuideTheme.MACHINES.getId());
        food.setTheme(GuideTheme.FOOD.getId());
        armor.setTheme(GuideTheme.ARMOR.getId());
        magicalResources.setTheme(GuideTheme.MAGIC.getId());
        magicalGadgets.setTheme(GuideTheme.MAGIC.getId());
        magicalArmor.setTheme(GuideTheme.ARMOR.getId());
        misc.setTheme(GuideTheme.MISC.getId());
        technicalComponents.setTheme(GuideTheme.ENERGY_TECH.getId());
        technicalGadgets.setTheme(GuideTheme.ENERGY_TECH.getId());
        resources.setTheme(GuideTheme.RESOURCES.getId());
        electricity.setTheme(GuideTheme.ENERGY_TECH.getId());
        androids.setTheme(GuideTheme.ENERGY_TECH.getId());
        cargo.setTheme(GuideTheme.LOGISTICS.getId());
        gps.setTheme(GuideTheme.ENERGY_TECH.getId());
        christmas.setTheme(GuideTheme.MISC.getId());
        valentinesDay.setTheme(GuideTheme.MISC.getId());
        easter.setTheme(GuideTheme.MISC.getId());
        birthday.setTheme(GuideTheme.MISC.getId());
        halloween.setTheme(GuideTheme.MISC.getId());
    }
```

(Field names are taken verbatim from `DefaultItemGroups`. `setTheme` on a `LockedItemGroup`/`SeasonalItemGroup` field works because `setTheme` is inherited from `ItemGroup` and is called as a statement, not assigned.)

- [ ] **Step 2: Compile**

Run: `./gradlew :core:compileJava -q ; echo "EXIT=$?"`
Expected: `EXIT=0`

- [ ] **Step 3: Commit**

```bash
git add core/src/main/java/io/github/thebusybiscuit/slimefun5/implementation/setup/DefaultItemGroups.java
git commit -m "feat(guide): tag Slimefun's own categories with themes"
```

---

## Task 10: Add localization strings

**Files:**
- Modify: `core/src/main/resources/languages/en/messages.yml`

- [ ] **Step 1: Add the theme names + the addon-visibility title**

Under the `guide:` → `title:` block (which already has `wiki`, `addons`, etc.), add:

```yaml
    addon-visibility: 'Addon Visibility'
```

Under the `guide:` block, add a new `themes:` section (sibling of `title:`), matching the ids in `GuideTheme`:

```yaml
  themes:
    weapons: '&cWeapons'
    tools: '&aTools'
    armor: '&bArmor'
    machines: '&6Machines'
    energy_tech: '&eEnergy & Tech'
    resources: '&fResources'
    magic: '&dMagic'
    food: '&2Food & Farming'
    logistics: '&3Logistics'
    decoration: '&5Decoration'
    misc: '&7Misc'
```

- [ ] **Step 2: Compile (resource change — confirm the build still assembles)**

Run: `./gradlew :core:compileJava -q ; echo "EXIT=$?"`
Expected: `EXIT=0`

- [ ] **Step 3: Commit**

```bash
git add core/src/main/resources/languages/en/messages.yml
git commit -m "feat(guide): add theme names + addon-visibility localization"
```

---

## Task 11: Boot-test milestone

**Files:** none (verification only)

- [ ] **Step 1: Build the full jar**

Run: `./gradlew :core:shadowJar -q ; echo "EXIT=$?"`
Expected: `EXIT=0`

- [ ] **Step 2: Boot 1.8.8 (legacy floor) with addons, in the background**

Run: `./gradlew runServer -PmcVersion=1.8.8 -Paddons=Slimefun5/InfinityExpansion@feature/java8-universal-jar,Slimefun5/Networks@feature/java8-universal-jar -PlocalAddons` (or use `scripts/run.ps1` and pick 1.8.8 + a couple addons).
Watch the log for: `Slimefun has finished loading`, no exceptions from `SurvivalSlimefunGuide`/`ThemeRegistry`/`ThemeItemGroup`/`AddonVisibility` during enable.

- [ ] **Step 3: In-game verification checklist (user-driven; the agent reports the log is clean and lists these for the user)**

  - Open the survival guide → top level shows **themes** (Weapons, Tools, Machines, …), not a flat category list.
  - Click a theme → its categories appear; click a category → items + recipes work exactly as before.
  - Open **Settings & Info** → slot 51 is now **"Addon Visibility"** (a bookshelf), not the "work-in-progress" paper.
  - Open Addon Visibility → toggle an addon off → its categories disappear from the themed browse **and** from search; toggle on → reappear. Reopen the guide / relog → the choice persisted.
  - Open the **Cheat Sheet** guide → same themed layout; clicking an item still spawns it.
  - Confirm no regressions: research-locking, recipe pages, seasonal categories.

- [ ] **Step 4: Stop the server and kill any lingering forked JVM** (per the project's runServer gotcha), then report results.

---

## Done

Phase 1 complete: the guide is a combined themed library with per-player addon visibility, the cheat sheet inherits it, and the unused settings slot is finished. Follow-ups (separate specs/plans): tag the 15 addons' categories with themes; Phase 2 in-game addon installer.
