# Item Internationalisation (i18n) — Design Spec (2026-06-21)

## Goal
Let Slimefun item **names and lore** be translated per language, for core and addons, with a
translation-coverage % surfaced per plugin in the language picker. Decisions (user-approved):

1. **Scope:** items shown inside **Slimefun UIs** (Guide, machine GUIs, search) render in the
   **viewing player's** language. **Physical** items (inventory/world) use the **server default**
   language. No packet rewriting.
2. **Source of truth:** today's hardcoded names/lore stay as the built-in **English fallback**.
   Other languages live in per-language files keyed by item id; addons ship their own. Missing →
   fall back to English. Coverage % = entries present vs total items.

## Architecture

### Files
- `core/src/main/resources/languages/<lang>/items.yml` — `<ITEM_ID>: { name: "...", lore: ["...","..."] }`.
  Core ships these (initially only a couple of languages as proof; community fills them in). Addons
  ship the same path in their own jar.
- `en` needs **no file** — English is the hardcoded default already baked into each `SlimefunItem`.

### `ItemTranslationService` (new, `core/services/localization/`)
- Loads, per language, a `Map<itemId, ItemTranslation{name, List<String> lore}>` from the core jar
  and from each addon that calls `registerTranslations(plugin)`.
- `getName(@Nullable String lang, SlimefunItem item)` → translated name, else the item's default
  (`item.getItemName()`).
- `getLore(@Nullable String lang, SlimefunItem item)` → translated lore, else the item's default lore.
- `getDisplayItem(Player p, SlimefunItem item)` → a clone of `item.getItem()` with name+lore replaced
  by the player-language translation (no-op when none → English). This is the single integration hook.
- `getCoverage(String lang)` → `Map<pluginName, int[]{translated, total}>` over all registered items
  (grouped by `item.getAddon().getName()`), for the %-UI.

### Integration points (per-player UI translation)
- The Guide item view: where a `SlimefunItem` is rendered for a player, pass through
  `getDisplayItem(p, item)`. Primary hook: `SurvivalSlimefunGuide` item-display + category item lists,
  and the WikiPage. Keep changes localized to display builders (don't touch item registration).
- **Physical items at server default:** Phase 2 — at registration, if the configured server language
  has a translation, bake it into the item. Phase 1 leaves physical items English.

### Translation-% UI (#9)
- `PlayerLanguageOption`: right-click a language entry → open a menu listing **Slimefun core + each
  addon** with a coverage bar/percent for that language (from `getCoverage`). The language list itself
  can show an overall % per language.

## Phasing
- **Phase 1 (this pass):** `ItemTranslationService` (load + getName/getLore/getDisplayItem + coverage),
  wire it into the Guide display, ship one sample non-English `items.yml` (proof), and the language ->
  per-plugin coverage UI. English fallback everywhere; non-translated langs simply show English at <100%.
- **Phase 2:** server-default language baked into physical items; addon `registerTranslations`.
- **Phase 3:** roll language files across the 15 addon repos; community translation content.

## Testing
- `:core:compileJava` + `:core:shadowJar` green; `languages/<lang>/items.yml` bundles.
- In-game: a player on a sample-translated language sees translated names in the Guide; the language
  picker right-click shows per-plugin %.

## Out of scope
- Per-player translation of physical/held/world items (Minecraft one-name-per-item limitation).
- Auto-translating the actual content (the files are filled by community/translators over time).
