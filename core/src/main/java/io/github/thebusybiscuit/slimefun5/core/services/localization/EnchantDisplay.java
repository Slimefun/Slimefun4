package io.github.thebusybiscuit.slimefun5.core.services.localization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedItemFlag;

/**
 * Renders an item's enchantments as lore lines placed with the composed blocks (under the Type category)
 * instead of the vanilla client's default position above the lore. To do that the vanilla enchantment
 * tooltip must be hidden ({@link VersionedItemFlag#HIDE_ENCHANTS}); on Minecraft versions where that flag
 * no longer exists (1.21.5+, where hiding moved to a data component the universal jar does not touch), we
 * canNOT reliably hide it, so we DON'T re-render either - leaving vanilla's default rendering untouched
 * rather than risk showing the enchantments twice.
 */
public final class EnchantDisplay {

    private EnchantDisplay() {}

    /** Whether we can hide the vanilla enchant tooltip on this version (and thus safely re-render it). */
    public static boolean canReposition() {
        return VersionedItemFlag.HIDE_ENCHANTS != null;
    }

    /** Hides the vanilla enchantment tooltip so the re-rendered lore lines are the only ones shown. No-op when unsupported. */
    public static void hide(@Nonnull ItemMeta meta) {
        if (canReposition()) {
            VersionedItemFlag.addFlags(meta, VersionedItemFlag.HIDE_ENCHANTS);
        }
    }

    /**
     * Grey enchantment lines (e.g. {@code Knockback II}) for the item's template enchantments, to sit
     * under the Type block. Empty when the item has no enchantments or the vanilla tooltip cannot be
     * hidden on this version (so we never double-render).
     */
    @Nonnull
    public static List<String> lines(@Nonnull SlimefunItem item, @Nullable String languageId) {
        if (!canReposition()) {
            return Collections.emptyList();
        }

        Map<Enchantment, Integer> enchantments;

        try {
            enchantments = item.getItem().getEnchantments();
        } catch (Exception | LinkageError e) {
            return Collections.emptyList();
        }

        if (enchantments == null || enchantments.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> out = new ArrayList<>(enchantments.size());

        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            out.add(ChatColor.GRAY + prettyName(entry.getKey(), languageId) + " " + roman(entry.getValue()));
        }

        return out;
    }

    /** A human-readable enchantment name: the data-driven translation, else version-safe title-casing of the raw key. */
    @Nonnull
    private static String prettyName(@Nonnull Enchantment enchantment, @Nullable String languageId) {
        String raw = null;

        try {
            Object key = ReflectionCompat.invoke(enchantment, "getKey"); // NamespacedKey on 1.13+
            if (key != null) {
                Object keyName = ReflectionCompat.invoke(key, "getKey");
                if (keyName != null) {
                    raw = String.valueOf(keyName);
                }
            }
        } catch (Exception | LinkageError ignored) {
            // fall through to the legacy name
        }

        if (raw == null || raw.isEmpty() || "null".equals(raw)) {
            try {
                raw = enchantment.getName(); // deprecated legacy name (1.8+)
            } catch (Exception | LinkageError e) {
                raw = "enchantment";
            }
        }

        String resolved = Slimefun.getEnchantTranslationService().name(languageId, raw);
        return resolved != null ? resolved : titleCase(raw);
    }

    @Nonnull
    private static String titleCase(@Nonnull String raw) {
        String[] parts = raw.toLowerCase(java.util.Locale.ROOT).split("[_\\s]+");
        StringBuilder sb = new StringBuilder();

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append(' ');
            }

            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }

        return sb.length() > 0 ? sb.toString() : raw;
    }

    /** Roman numeral for enchantment levels 1..10; falls back to the plain number beyond that. */
    @Nonnull
    private static String roman(int level) {
        switch (level) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            case 6: return "VI";
            case 7: return "VII";
            case 8: return "VIII";
            case 9: return "IX";
            case 10: return "X";
            default: return String.valueOf(level);
        }
    }
}
