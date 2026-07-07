package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.attributes.EnergyNetComponent;

/**
 * Composes an item's lore from ordered blocks: Type -> Description -> Stats -> Usage, each separated by
 * a single blank line, empty blocks omitted. Forcing every migrated item through this one structure is
 * what eliminates lore inconsistency. Per-type static {@code %placeholder%} tokens are resolved from the
 * item's attributes; unknown tokens pass through unchanged (Phase 2 adds per-instance live values).
 *
 * Three modes:
 *  - structural blocks present (type/stats/usage) -> compose [Type, Description?, Stats, Usage] only.
 *  - only a description authored -> legacy fallback base + Description? (the Feature-4 behaviour).
 *  - nothing authored -> the legacy/base lore unchanged.
 */
public final class LoreComposer {

    private LoreComposer() {}

    @Nonnull
    public static List<String> compose(@Nonnull SlimefunItem item, @Nonnull List<String> type, @Nonnull List<String> description,
                                       @Nonnull List<String> stats, @Nonnull List<String> usage, @Nonnull List<String> fallbackBase,
                                       boolean includeDescription) {
        List<String> desc = includeDescription ? description : new ArrayList<String>();

        // Enchantment lines render directly under the Type category (not vanilla's default spot above the
        // lore). EnchantDisplay returns empty unless the item is enchanted AND the vanilla tooltip can be
        // hidden on this version, so this never double-renders.
        List<String> enchantLines = EnchantDisplay.lines(item);

        boolean hasStructuralBlocks = !type.isEmpty() || !stats.isEmpty() || !usage.isEmpty() || !enchantLines.isEmpty();

        if (hasStructuralBlocks) {
            List<String> typeBlock = type;

            if (!enchantLines.isEmpty()) {
                typeBlock = new ArrayList<>(type);
                typeBlock.addAll(enchantLines);
            }

            return joinBlocks(item, Arrays.asList(typeBlock, desc, stats, usage));
        }

        if (!description.isEmpty()) {
            return joinBlocks(item, Arrays.asList(fallbackBase, desc));
        }

        // No authored blocks: the item's own lore IS its description, so the toggle hides it on physical
        // items (the guide passes includeDescription=true, so the guide still shows it).
        return includeDescription ? renderBlock(item, fallbackBase) : new ArrayList<String>();
    }

    /** Concatenates non-empty blocks with one blank line between them. */
    @Nonnull
    private static List<String> joinBlocks(@Nonnull SlimefunItem item, @Nonnull List<List<String>> blocks) {
        List<String> out = new ArrayList<>();

        for (List<String> block : blocks) {
            if (block.isEmpty()) {
                continue;
            }

            if (!out.isEmpty()) {
                out.add("");
            }

            out.addAll(renderBlock(item, block));
        }

        return out;
    }

    /** Resolves placeholders and translates '&' colour codes for each line of a block. */
    @Nonnull
    private static List<String> renderBlock(@Nonnull SlimefunItem item, @Nonnull List<String> lines) {
        List<String> out = new ArrayList<>(lines.size());

        for (String line : lines) {
            out.add(ChatColor.translateAlternateColorCodes('&', resolvePlaceholders(item, line)));
        }

        return out;
    }

    /**
     * Resolves per-type static placeholders. Phase 1 supports {@code %capacity%} (energy buffer/capacity);
     * unknown tokens are left intact so authors may write literal values. Guarded so a broken attribute
     * never throws during rendering.
     */
    @Nonnull
    private static String resolvePlaceholders(@Nonnull SlimefunItem item, @Nonnull String line) {
        if (line.indexOf('%') < 0) {
            return line;
        }

        String result = line;

        if (result.contains("%capacity%") && item instanceof EnergyNetComponent) {
            try {
                result = result.replace("%capacity%", String.valueOf(((EnergyNetComponent) item).getCapacity()));
            } catch (Exception | LinkageError ignored) {
                // Leave the token intact if the attribute cannot be read.
            }
        }

        return result;
    }
}
