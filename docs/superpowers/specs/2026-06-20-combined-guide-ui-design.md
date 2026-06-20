# Combined Guide UI — Phase 1 Design

**Date:** 2026-06-20
**Status:** Approved for planning
**Scope:** Phase 1 of the guide rework (guide UX). Phase 2 (in-game addon installer) is a separate spec.

## Problem

With many addons installed, the Slimefun Guide flattens Slimefun's and every addon's categories into one paginated list (`MAX_ITEM_GROUPS = 36` per page in `SurvivalSlimefunGuide`). The result is many pages of unstructured category icons. The Cheat Sheet inherits the same flat list and is worse (it also shows hidden groups). The Settings menu also has an unfinished placeholder (slot 51, the "work-in-progress" paper) with an empty click handler.

## Goals

1. Present Slimefun + all addons as **one coherent themed library** rather than "Slimefun + a pile of addons".
2. Keep it clean regardless of how many addons are installed.
3. Let each player curate which addons appear in *their* guide.
4. Finish the unused Settings slot by repurposing it.
5. Fix the Cheat Sheet's clutter (falls out of the same change).

## Non-Goals (explicitly out of scope for Phase 1)

- The in-game addon installer (download release / compile-from-branch) — **Phase 2**, separate spec.
- Tagging the 15 Slimefun5 addons' categories with themes — **follow-up pass** after Phase 1 lands. In Phase 1 they fall into **Misc**.
- Any change to recipe display, research-locking, GEO/altar/etc.
- Server-wide / admin visibility configuration (the toggle is per-player only).

## Key Decisions (from brainstorming)

| Decision | Choice |
|---|---|
| Layout | Unified **themed** categories (not foldered per-addon) |
| Theme depth | Theme → categories → items (theme is a grouping layer; existing category pages untouched) |
| Theme assignment | Declared by the category owner (Slimefun / each addon); undeclared → **Misc**. No name-guessing heuristic. |
| Toggle scope | **Per-player**, saved in player data |
| Toggle effect | Hide that addon's categories **and** items entirely (from guide browse + search) |
| Toggle entry point | Settings menu only, repurposing slot 51 |
| Phase 1 tagging | Core mechanism + Slimefun's own categories; addons → Misc until a follow-up tags them |

## Architecture

The mechanism rides on Slimefun's existing `FlexItemGroup` extension point — a guide category that owns its `open()` behaviour and that `getVisibleItemGroups()` already supports. The theme layer is therefore **non-invasive**: it changes only what the *top level* of the guide lists.

### Components

1. **`GuideTheme` (`extends FlexItemGroup`)** — one per theme. Its `open(player, profile, mode)` renders a paginated grid of *its member categories* (filtered by the player's visibility toggles), each clickable into the existing `openItemGroup(...)`. It resolves the active guide implementation via `Slimefun.getRegistry().getSlimefunGuide(mode)` so it works for both Survival and Cheat Sheet.

2. **Theme tag on `ItemGroup`** — an optional theme key. Backwards-compatible: a new field/setter defaulting to "unset". Slimefun's own categories set it in `DefaultItemGroups`. Addons set it on their own categories (Phase-1 follow-up). Unset → Misc.

3. **`ThemeRegistry`** — given the live category registry and a player/profile, buckets every visible category into its theme (declared theme, else Misc), and exposes:
   - `getThemes()` → ordered list of non-empty `GuideTheme`s for a given player (a theme with zero visible categories is hidden).
   - `getCategories(theme, player, profile)` → that theme's visible, non-hidden categories.

4. **Guide top-level swap** — `SurvivalSlimefunGuide.getVisibleItemGroups(...)` returns the **theme groups** instead of the raw categories. Raw categories are reachable only through their theme. Everything below the top level (`openItemGroup`, recipe pages, research-locking) is unchanged.

5. **Per-player addon visibility store** — a set of hidden addon ids per player, persisted via the existing version-safe `PdcCompat` (which already has a legacy-YAML fallback for player/UUID holders on 1.8) as a delimited string under a single guide key (e.g. `guide_hidden_addons`). Default: empty (all addons visible). `AddonVisibility` exposes `isHidden(player, addonId)`, `setHidden(player, addonId, hidden)`, and `getHidden(player)`.

6. **"Addon Visibility" settings entry** — replaces the empty slot 51 in `SlimefunGuideSettings`. Opens a paginated menu listing Slimefun core + each installed addon (`Slimefun.getInstalledAddons()`), each with an on/off toggle, plus an "all on / all off" shortcut. Toggling writes to the per-player store.

### Theme set (tunable)

`⚔ Weapons` · `🔧 Tools` · `🛡 Armor` · `⚙ Machines` · `⚡ Energy & Tech` · `⛏ Resources` · `✨ Magic` · `🍞 Food & Farming` · `📦 Logistics` (cargo / networks / GPS / terminals) · `🎨 Decoration` · `❓ Misc`.

Each theme has a stable id, a display name (localizable), an icon, and a sort order. The set is defined in one place (an enum or registry) so it can be adjusted without touching navigation code.

Slimefun's own categories map as: `weapons`→Weapons; `tools`→Tools; `items`(Useful Items)→Misc/Tools; `basic_machines`→Machines; `food`→Food & Farming; `armor`→Armor; `magical_items`/`magical_gadgets`→Magic; `magical_armor`→Armor (or Magic); `misc`→Misc; `tech_misc`(Technical Components)/`technical_gadgets`/`electricity`/`androids`/`gps`→Energy & Tech; `cargo`→Logistics; `resources`→Resources; seasonal groups → Misc (still gated to their month by `SeasonalItemGroup`). Exact assignments are confirmed during implementation; all are one-line tags in `DefaultItemGroups`.

## Navigation Flow

```
Main guide
  └─ Themes (GuideTheme grid)            ← getVisibleItemGroups() now returns these
       └─ Categories in that theme        ← GuideTheme.open() renders these (visibility-filtered)
            └─ Items + recipes            ← existing openItemGroup(), unchanged
Settings & Info
  └─ slot 51 "Addon Visibility"          ← repurposed
       └─ Addon on/off toggle list (per-player)
```

Back navigation uses the existing `GuideHistory`; a theme page pushes onto history like a category does today.

## Search

`SurvivalSlimefunGuide` search must exclude items whose owning addon is hidden for that player. The search result filter gains a visibility check keyed on the item's addon id. (Hidden = entirely absent from results, matching the toggle effect.)

## Cheat Sheet

`CheatSheetSlimefunGuide extends SurvivalSlimefunGuide`, so swapping the top level to themes is inherited automatically; it spawns items on click exactly as now. Per-player visibility applies there too. No cheat-sheet-specific code beyond what it already overrides.

## Edge Cases

- **Empty theme** → hidden from the top level (no empty folders).
- **`LockedItemGroup`** → still shown inside its theme with the lock/parent-requirement behaviour unchanged.
- **Addon-defined `FlexItemGroup`** (custom guide pages) → treated as a category; gets a theme tag or Misc; its own `open()` still runs when clicked.
- **`SeasonalItemGroup`** → only appears (in its theme) during its month, as today.
- **No addons installed** → themes still group Slimefun's own categories; this is strictly cleaner than today, never worse.
- **All addons hidden by a player** → only Slimefun core themes/categories show.

## Files

**New (core):**
- `core/guide/themes/GuideTheme.java` — the `FlexItemGroup` theme.
- `core/guide/themes/GuideThemes.java` (or enum) — the theme set + ids/icons/order.
- `core/guide/themes/ThemeRegistry.java` — category→theme bucketing.
- `core/guide/AddonVisibility.java` — per-player hidden-addon store (read/write via `PdcCompat`).
- `core/guide/options/AddonVisibilityMenu.java` — the toggle menu.

**Changed (core):**
- `ItemGroup` — optional theme tag (field + setter/getter; default unset).
- `implementation/guide/SurvivalSlimefunGuide` — `getVisibleItemGroups()` returns themes; search filter respects visibility.
- `core/guide/options/SlimefunGuideSettings` — slot 51 opens the Addon Visibility menu.
- `implementation/setup/DefaultItemGroups` — tag Slimefun's own categories.
- `languages/en/messages.yml` — theme names, the addon-visibility menu strings, repurpose `guide.work-in-progress` (or add new keys).

## Testing

- Boot 1.8.8 and a modern version; open the survival guide → confirm themes show, drill into a theme → categories → items + recipes.
- Toggle an addon off → its categories vanish from the themed browse **and** search; toggle on → reappear. Confirm the choice persists across guide reopen and relog (per-player store).
- Open the Cheat Sheet → same themed layout; click spawns items.
- Open Settings → slot 51 is now "Addon Visibility", not the WIP paper.
- With zero addons, confirm Slimefun-only themes render cleanly.
- Verify no regression to recipe pages, research-locking, seasonal gating.

## Rollout

Single-repo change (core). Build core, boot-test, commit on `feature/java8-universal-jar`. The 15-addon theme-tagging follow-up and the Phase 2 installer are tracked separately.
