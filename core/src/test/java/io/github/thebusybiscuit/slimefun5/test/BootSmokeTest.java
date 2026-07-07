package io.github.thebusybiscuit.slimefun5.test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.VanillaItem;
import io.github.thebusybiscuit.slimefun5.implementation.setup.SlimefunItemSetup;

/**
 * Headless boot check: enables Slimefun in a mocked server and runs the FULL item-catalogue setup (which
 * the unit-test boot path normally skips). This exercises the real registration path - the one the id-only
 * lore rebuild kept breaking (Talisman/hazmat null-lore NPEs aborting registration) - at BUILD time instead
 * of on a live boot. Note: {@code Slimefun.loadItems()} swallows exceptions in production, so we call
 * {@link SlimefunItemSetup#setup} directly here so any registration failure actually fails the test.
 */
class BootSmokeTest {

    private static Slimefun plugin;

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        plugin = MockBukkit.load(Slimefun.class);
        // Run the real item registration (skipped by the unit-test boot). Throws if any item ctor NPEs.
        SlimefunItemSetup.setup(plugin);
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    @DisplayName("Slimefun enables cleanly")
    void testEnabled() {
        Assertions.assertNotNull(plugin, "Slimefun failed to load under MockBukkit");
        Assertions.assertTrue(plugin.isEnabled(), "Slimefun is not enabled after load");
    }

    @Test
    @DisplayName("Item registration completes and registers a bulk of items (no aborted setup)")
    void testItemsRegister() {
        int count = Slimefun.getRegistry().getEnabledSlimefunItems().size();
        // A 1.21 mock legitimately can't build a few items whose material is newer, so this is a floor, not
        // the exact catalogue size; the point is that setup ran to completion rather than aborting early.
        Assertions.assertTrue(count > 300, "expected 300+ registered items, got " + count
            + " - item registration likely aborted mid-setup (e.g. a null-lore NPE)");
    }

    @Test
    @DisplayName("Every registered item exposes a usable template (getItem() never throws / returns null)")
    void testItemsExposeTemplate() {
        // id-only items legitimately have NO template lore/name here (the resolver fills the display from
        // en/items.yml at runtime, which the unit-test boot doesn't run). What we guard is that getItem()
        // itself is sound for every registered item - a null/throwing template is what shows as a blank
        // "glass pane" in the guide.
        List<String> offenders = new ArrayList<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            try {
                ItemStack stack = item.getItem();

                if (stack == null || stack.getType() == null) {
                    offenders.add(item.getId());
                }
            } catch (Exception | LinkageError e) {
                offenders.add(item.getId() + " (" + e.getClass().getSimpleName() + ")");
            }
        }

        Assertions.assertTrue(offenders.isEmpty(),
            offenders.size() + " item(s) have a broken template: " + offenders.subList(0, Math.min(15, offenders.size())));
    }

    @Test
    @DisplayName("Every item identifies itself: getByItem(item.getItem()) round-trips to the same item")
    void testItemsRoundTrip() {
        // If our id/PDC/distinctive changes broke an item's self-identification, its own template no longer
        // resolves back to it - which is a whole class of "the item doesn't work anymore" migration breakage.
        List<String> offenders = new ArrayList<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            // VanillaItem entries are real vanilla items shown in the guide for their recipe - they carry
            // no Slimefun PDC id (you get the actual vanilla item), so not identifying is correct.
            if (item instanceof VanillaItem) {
                continue;
            }

            try {
                SlimefunItem resolved = SlimefunItem.getByItem(item.getItem());

                if (resolved == null || !resolved.getId().equals(item.getId())) {
                    offenders.add(item.getId() + " -> " + (resolved == null ? "null" : resolved.getId()));
                }
            } catch (Exception | LinkageError e) {
                offenders.add(item.getId() + " (" + e.getClass().getSimpleName() + ")");
            }
        }

        Assertions.assertTrue(offenders.isEmpty(),
            offenders.size() + " item(s) do not identify themselves: " + offenders.subList(0, Math.min(15, offenders.size())));
    }

    @Test
    @DisplayName("Every item has a resolvable display name (baked in code OR a name in en/items.yml)")
    void testEveryItemHasAName() {
        // The unit-test boot doesn't run the runtime resolver, so an id-only item (no name in code) shows
        // its raw material name at runtime UNLESS en/items.yml carries a name for its id. Items that have
        // neither are exactly the "blank / material-name" reports (e.g. the nameless Networks pane).
        YamlConfiguration en = new YamlConfiguration();

        try (InputStream in = getClass().getResourceAsStream("/languages/en/items.yml")) {
            Assertions.assertNotNull(in, "en/items.yml not found on the classpath");
            en.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (Exception e) {
            Assertions.fail("Could not read en/items.yml: " + e);
        }

        List<String> nameless = new ArrayList<>();

        for (SlimefunItem item : Slimefun.getRegistry().getEnabledSlimefunItems()) {
            // VanillaItem entries intentionally show the vanilla client name (no custom name), so skip them.
            if (item instanceof VanillaItem) {
                continue;
            }

            ItemMeta meta = item.getItem().getItemMeta();
            boolean bakedName = meta != null && meta.hasDisplayName();
            String resourceName = en.getString(item.getId() + ".name");
            boolean resourceHasName = resourceName != null && !resourceName.trim().isEmpty();

            if (!bakedName && !resourceHasName) {
                nameless.add(item.getId());
            }
        }

        Assertions.assertTrue(nameless.isEmpty(),
            nameless.size() + " item(s) will render with their raw material name (no code name, no en/items.yml name): " + nameless);
    }
}
