# In-Game Central Wiki — Design Spec (2026-06-21)

## Goal
Every Slimefun item — core **and** addon — has in-game documentation reachable from the guide.
No part of the plugin links to an external website. The wiki layers an authored **explanation**
(what the item does, how to use it, tips) plus **"used in"** navigation on top of the recipe view the
guide already renders.

## Key insight (why this is tractable)
`SurvivalSlimefunGuide.displayItem()` (SurvivalSlimefunGuide.java:650) already renders an item's
recipe grid, the producing machine icon (`RecipeType.getItem`), the output, and clickable ingredients.
We REUSE that. The wiki adds the parts that don't exist today:
1. An authored prose **explanation** per item (the docs that addon wikis stopped maintaining).
2. A **"Used in"** list (no reverse-recipe API exists; we build a cached index).
3. In-game navigation so all of it is reachable without leaving Minecraft.

## Architecture
New package `core/guide/wiki/`, mirroring `core/guide/installer/` conventions.

### Components
1. **`WikiText`** — the prose layer.
   - Holds, per item id (and per mechanic/category id), an optional list of explanation lines.
   - Loaded at enable from a bundled resource `core/src/main/resources/wiki/items.yml`
     (`<ITEM_ID>: ["line 1", "line 2", …]`) and `wiki/mechanics.yml` (hub topics).
   - Addon-facing API so addons enrich their own items:
     `Slimefun.getWikiText().set(String id, List<String> lines)` and a convenience
     `SlimefunItem#setWikiText(String... lines)`.
   - `get(String id)` returns the authored lines, or an **auto-generated** fallback:
     a short line derived from the item's group + recipe type (e.g. *"A {group} item crafted with a
     {recipeType}."*). Trivial items therefore still get a sensible page with zero authoring.

2. **`ReverseRecipeIndex`** — `item id → list of SlimefunItem that consume it`.
   - Built lazily on first wiki open, cached (volatile), invalidated never (registry is static post-enable).
   - Scans `Slimefun.getRegistry().getEnabledSlimefunItems()`, each `getRecipe()` (ItemStack[9]) plus
     `MultiBlockMachine.getRecipes()` inputs, matching with `SlimefunItem.getByItem()` to map an
     ingredient stack back to its id (so vanilla ingredients are ignored, Slimefun ones are linked).

3. **`WikiPage` (ChestMenu)** — the per-item wiki screen.
   - Layout: header (item icon + name) top-center; the **explanation** prose as an info book/paper item
     with the lines as lore; a **machine stats** entry for `EnergyNetComponent`/`AContainer` items
     (capacity, energy/tick, speed); a **"View recipe"** button that calls the guide's existing
     `displayItem` for this item; a **"Used in"** row of clickable item icons (paged) from
     `ReverseRecipeIndex`; clickable related items navigate to their own `WikiPage`. Back/breadcrumb at slot 0.
   - All icons version-safe (XMaterial via MaterialCompat); no enchant-glow.

4. **`WikiIndex` (ChestMenu)** — central entry point.
   - Reuses the themed groups already in the guide: theme → category → item, but each item click opens
     its `WikiPage` (not the recipe view). Includes the guide's search; search hits open `WikiPage`.

### Integration / entry points
- **Per-item wiki button** (SurvivalSlimefunGuide.java:618-624, slot 8): instead of
  `ChatUtils.sendURL(wiki.get())`, open `WikiPage` for the item. The button shows for **every** item
  now (not only ones with a `getWikipage()`), since every item has at least an auto-generated page.
- **Settings "Wiki" button** (SlimefunGuideSettings.java:160): open `WikiIndex` instead of the GitHub URL.

### External-link removal ("no external website")
Replace/remove every `ChatUtils.sendURL` docs/repo link in the guide:
- SurvivalSlimefunGuide.java:622 (per-item wiki) → open `WikiPage` (above).
- SlimefunGuideSettings.java:160 (wiki) → open `WikiIndex`.
- SlimefunGuideSettings.java:143 (source) and :199 (issues) → **removed** (button replaced with
  background, like the slot-47 non-op fix).
- PlayerLanguageOption.java:94 (Translating-Slimefun) → **removed** (the link line dropped; the
  language picker itself stays).
- `PostSetup`/`/wiki.json` external-URL wiring (`addOfficialWikipage`) is retired; `getWikipage()`
  stays as an optional page-name hint but is no longer rendered as a URL.
- **NOT touched:** `ContributorsMenu.java:62` (links to each contributor's GitHub profile) — that's
  attribution, not documentation. Flagged for the user; left clickable for now.

## Prose content (v1 scope)
- **Authored prose up front for the high-value items:** all machines and multiblocks, electric
  generators/capacitors, androids, geo-miners, key tools/weapons/armor, notable gadgets, and a set of
  **mechanic hub pages** (Research, Energy/Cargo Nets, Multiblocks, Androids, Geo-Mining). Authored via
  `wiki/items.yml` + `wiki/mechanics.yml`.
- **Auto-generated fallback** for trivial items (dusts, ingots, gems, intermediate components).
- Addons ship their own prose via the API (none required for the system to work).

## Group 4 (theming) — folded in here
No fixed item-count threshold. Concretely: **SlimefunAdvancements should not sit in Misc** — give it a
proper home. Since "advancements" are a progression mechanic, surface them via their own themed group
(or a dedicated entry) rather than Misc. Review other addons currently landing in Misc and rehome any
that clearly fit an existing theme; leave the rest. This is a small per-addon `setTheme` adjustment.

## Testing / acceptance
- `:core:compileJava` exits 0; `:core:shadowJar` produces the jar.
- Boot on a dev server (manual): clicking the wiki button on a machine shows its explanation + stats +
  "used in"; clicking a used-in icon navigates; the settings Wiki button opens the index; no button in
  the guide opens a browser/URL.
- Auto-generated fallback renders for an item with no authored prose.

## Out of scope (v1)
- Multiblock build-orientation diagrams.
- Authored prose for every trivial item.
- Reverse index invalidation (registry is immutable after enable).
