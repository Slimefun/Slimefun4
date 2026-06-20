# Backlog — guide / addons / infra (captured 2026-06-20)

Large mixed batch requested after Phase 3a. Decomposed into workstreams; each large item gets its
own brainstorm → spec → plan before implementation. **DONE** items already shipped this session.

## Group 1 — Core startup/listener fixes (bounded, no design)
- **B. 1.8/old-MC listener registration crashes.** On 1.8.8, registration logs ERRORs and one
  `NoClassDefFoundError` for events absent on the running version:
  `EntityPickupItemEvent` (1.12+: `ItemPickupListener`, `entity.PiglinListener`),
  `EntityToggleGlideEvent` (1.9+: `ElytraImpactListener`),
  `BlockDropItemEvent` (1.12+: uncaught NoClassDefFoundError in `Slimefun.registerListeners` ~line 659),
  `SmithItemEvent` (1.16+: `crafting.SmithingTableListener`). A "Skipped a listener that is
  unavailable" guard already exists for some; extend it so every version-gated listener is skipped
  cleanly (no ERROR/NoClassDefFoundError). SFAdvancements' `InventoryCriterionCompleter` hits the
  same `EntityPickupItemEvent` — addon-side, see Group 5.
- **D. "Restart to apply" never clears.** Phase 2 `InstallState.restartPending` stays true after a
  restart. On enable, reconcile: if an addon flagged restart-pending is now loaded (at its target
  version/branch), clear the flag so the installer badge updates.
- **E. DONE (0ab56dc).** Emptied settings slot 51 now shows background.
- **G. Op-gated entries should not exist for non-ops.** Settings slot 47 ("Addons for Slimefun5")
  currently falls back to a GitHub wiki link for non-permitted players; instead show nothing
  (background). Ties into Group 3 (no external links).

## Group 2 — Jar naming convention (core + 15 addons)
- **A.** Built jars should be named `<PluginName>-<version>.jar` (e.g. `Slimefun-5.0.0-UNOFFICIAL.jar`)
  by each project's own build (shadowJar `archiveBaseName`/`archiveVersion`), NOT renamed by
  runServer/the orchestrator. **Reverts the Phase-2 orchestrator rename** (copyAddonJar →
  `<PluginName>.jar`): orchestrator should copy the build's jar as-is. Touches core
  `build.gradle.kts` + every addon build.

## Group 3 — In-game documentation / central wiki (LARGE, needs brainstorm)
- **F.** A central in-game wiki: every plugin's docs viewable in-game; NO plugin links to an external
  website. Replace all external wiki/github links in the guide (settings wiki button, source,
  per-item wiki pages, slot 47) with in-game content. Major feature — own design cycle.
- Subsumes **G** (non-op entries) and informs **H**.

## Group 4 — Addon theming policy (small, per-addon)
- **C.** Addons should put items in **Misc** by default and only create their own category when Misc
  would be overloaded. Revisit the Phase-1 theme-tagging with this rule (some small addons currently
  declare dedicated categories).

## Group 5 — Addon bStats (multi-repo investigation + fixes)
- **I.** Several addon bStats projects are not submitting. Confirmed WORKING: Slimefun Lucky Blocks
  (31438). Reported NOT working (verify + fix): Slimefun Advancements (31436), Slimefun Warfare
  (31437), Slimefun Tech (31440), InfinityLib (31439). Others belong to upstream authors with
  existing playerbases — confirm but don't assume. **Re-point two:** SlimeTinker → Slimefun Tinker
  (31392), SensibleToolbox → Slimefun Toolbox (31393). Each addon's README should already carry the
  correct id (except those two). Investigate the SlimefunMetrics/MetricsModule wiring per addon
  (bStats id mismatch / metrics not started).

## Group 6 — Infra
- **H.** The GitHub wiki did not sync to `Slimefun5.wiki` (currently empty). Investigate the wiki
  repo / sync. May be moot if Group 3 makes docs in-game-only, but the public wiki may still be
  wanted.

## Suggested order
1. Group 1 (core fixes — active startup errors). 2. Group 2 (naming). 3. Group 5 (bStats).
4. Group 4 (theming policy). 5. Group 3 (in-game wiki — biggest, own design). 6. Group 6 (infra).
