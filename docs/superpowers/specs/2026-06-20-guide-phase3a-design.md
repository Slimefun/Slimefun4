# Guide Rework Phase 3a — Design

**Date:** 2026-06-20
**Branch:** `feature/java8-universal-jar`
**Status:** Approved design, ready for implementation planning

## Goal

Four bounded guide-usability improvements. (A fifth request — items belonging to multiple
categories / full re-sort — is split out as a separate later design, **Phase 3b**, because it
changes the item model.)

## Context

Built on Phase 1 (themed guide) + Phase 2 (in-game installer). Relevant code:
`implementation/guide/SurvivalSlimefunGuide.java` (`openMainMenu`, `openThemeContents`,
`openItemGroup`, `openSearch`, `createHeader`), `core/guide/AddonVisibility.java` (per-player
hidden-addon set), `core/guide/options/AddonVisibilityMenu.java`, `core/guide/options/SlimefunGuideSettings.java`,
`core/guide/installer/{AddonDetailMenu,BranchSelectMenu}.java`.

## Feature 1 — Installer keeps the UI open

`AddonDetailMenu`'s Install/Update click handler currently calls `pl.closeInventory()`. Remove it;
after kicking off the async install, **re-open the detail menu** (`AddonDetailMenu.open(...)`) so its
header badge reflects `⚙ Working…` and the player keeps browsing. Same for the branch-build click in
`BranchSelectMenu` (re-open the detail menu rather than close). Chat completion feedback is unchanged.

## Feature 2 — Addon Visibility on the main menu, redesigned, ≥1 enforced

- **Entry point:** add a button at **header slot 4** of the top-level menu, set only inside
  `openMainMenu` (not in shared `createHeader`, so it appears solely on the main menu). It opens
  `AddonVisibilityMenu`.
- **Remove** the slot-51 Addon Visibility entry from `SlimefunGuideSettings.addHeader` (one obvious
  place). Slot 51 reverts to background.
- **Redesign `AddonVisibilityMenu`:** each addon is a **stained-glass-pane toggle** — lime pane +
  enchant glow + `&a✔ Shown` when visible; gray pane + `&c✖ Hidden` when hidden. Add a small info
  item at the top: "Click to toggle which addons appear in your guide. At least one must stay shown."
- **≥1 rule:** toggling the last shown addon to hidden is blocked. Computed from the menu's addon
  list (`slimefun` + installed addons) minus `AddonVisibility.getHidden(p)`: if hiding this addon
  would leave zero shown, deny and message the player ("At least one addon must stay visible").

## Feature 3 — Single-subcategory passthrough

- **Themes:** in `openThemeContents`, if `themeGroup.getCategories()` has exactly **1** entry, skip
  the theme grid and call `openItemGroup(profile, categories.get(0), 1)` directly. Do **not** push
  the theme onto guide history in that case, so *back* from the category returns to the main menu.
- **Nested groups:** apply the same to core's `NestedItemGroup` — when it has a single visible
  sub-group, open that sub-group directly. (InfinityLib's addon-side `MultiGroup` is out of scope;
  its groups have many children anyway.)

## Feature 4 — Search results show `Theme ▸ Category`

In `openSearch` (the result-lore builder, ~line 436), replace the single-line
`⇨ <category displayName>` with `⇨ <Theme> ▸ <Category>`:

- **Theme:** `slimefunItem.getItemGroup().getThemeId()` → `GuideTheme.byId(id)` localized name; if the
  group has no theme id, use the *Misc* theme name.
- **Category:** `itemGroup.getDisplayName(p)` (unchanged source).

Example: `⇨ Machines ▸ Infinity Expansion`. The existing `AddonVisibility.isHidden` filter on search
results is unchanged.

## Out of scope (Phase 3b, separate design)

Items belonging to 2+ categories (jetpack, solar helmet, jet boots, …), splitting existing menus,
and full re-sorting. This changes how items map to categories (currently exactly one `ItemGroup`
per item) and needs its own brainstorm.

## Files

- Modify: `core/guide/installer/AddonDetailMenu.java`, `core/guide/installer/BranchSelectMenu.java` (F1)
- Modify: `implementation/guide/SurvivalSlimefunGuide.java` (F2 main-menu button, F3 passthrough, F4 search lore)
- Modify: `core/guide/options/AddonVisibilityMenu.java` (F2 redesign + ≥1 rule)
- Modify: `core/guide/options/SlimefunGuideSettings.java` (F2 remove slot 51)
- Modify: `core/api/items/groups/NestedItemGroup.java` (F3 nested passthrough) — verify exact open path during planning
- Possibly modify: `core/src/main/resources/languages/en/messages.yml` (F2 button title / info text, if not hardcoded)

## Testing

Tests remain disabled (Java-8 floor). Gate on `:core:compileJava -q` clean + a boot test: main menu
shows the visibility button at slot 4; a single-category theme opens its category directly; search
shows `Theme ▸ Category`; installing from the detail menu no longer closes the guide; the visibility
menu refuses to hide the last shown addon.
