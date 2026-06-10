package io.github.thebusybiscuit.slimefun5.integrations;

import java.util.logging.Level;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.Server;

import io.github.bakedlibs.dough.protection.ProtectionManager;
import io.github.thebusybiscuit.slimefun5.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;

/**
 * This Service holds all interactions and hooks with third-party {@link Plugin Plugins}
 * that are not necessarily a dependency or a {@link SlimefunAddon}.
 *
 * <p>
 * TEMPORARY JAVA-8 STUB: during the multi-version (1.8+) port the soft-dependency hooks
 * (PlaceholderAPI, WorldEdit, mcMMO, ClearLag, ItemsAdder, Orebfuscator) are disabled because
 * their APIs require a modern JVM and cannot sit on the Java-8 compile classpath. The dough
 * {@link ProtectionManager} (Java-8 compatible) is retained. The hooks will be re-introduced via
 * reflection so they remain optional and JVM-version-agnostic.
 *
 * @author TheBusyBiscuit
 *
 * @see Slimefun
 */
public class IntegrationsManager {

    /**
     * This is our instance of {@link Slimefun}.
     */
    protected final Slimefun plugin;

    /**
     * Our {@link ProtectionManager} instance.
     */
    private ProtectionManager protectionManager;

    /**
     * This boolean determines whether {@link #start()} was run.
     */
    private boolean isEnabled = false;

    // Soft dependencies (disabled in the Java-8 stub; re-added later via reflection)
    private boolean isPlaceholderAPIInstalled = false;
    private boolean isWorldEditInstalled = false;
    private boolean isMcMMOInstalled = false;
    private boolean isClearLagInstalled = false;
    private boolean isItemsAdderInstalled = false;
    private boolean isOrebfuscatorInstalled = false;

    /**
     * This initializes the {@link IntegrationsManager}
     *
     * @param plugin
     *            Our instance of {@link Slimefun}
     */
    public IntegrationsManager(@Nonnull Slimefun plugin) {
        this.plugin = plugin;
    }

    /**
     * This method returns whether the {@link IntegrationsManager} was enabled yet.
     *
     * @return Whether this {@link IntegrationsManager} has been enabled already.
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    /**
     * This method initializes all third party integrations.
     */
    public final void start() {
        if (isEnabled) {
            // Prevent double-registration
            throw new UnsupportedOperationException("All integrations have already been loaded.");
        } else {
            isEnabled = true;
        }

        // Load any integrations which aren't dependencies (loadBefore)
        plugin.getServer().getScheduler().runTask(plugin, this::onServerStart);
    }

    /**
     * This method is called when the {@link Server} has finished loading all its {@link Plugin Plugins}.
     */
    private void onServerStart() {
        try {
            // Load Protection plugin integrations
            protectionManager = new ProtectionManager(plugin);
        } catch (Exception | LinkageError x) {
            Slimefun.logger().log(Level.WARNING, x, () -> "Failed to load Protection plugin integrations for Slimefun v" + Slimefun.getVersion());
        }

        // Java-8 universal port: these soft-dependency hooks are re-added via reflection so they stay
        // optional and JVM-version-agnostic. Each hook references no third-party type in its bytecode
        // and is only registered when its plugin is actually present.
        if (isPluginInstalled("ClearLag")) {
            isClearLagInstalled = register("ClearLag", () -> new ClearLagIntegration(plugin).register());
        }

        if (isPluginInstalled("mcMMO")) {
            isMcMMOInstalled = register("mcMMO", () -> new McMMOIntegration(plugin).register());
        }

        if (isPluginInstalled("Orebfuscator")) {
            isOrebfuscatorInstalled = register("Orebfuscator", () -> new OrebfuscatorIntegration(plugin).register());
        }

        // Detection-only hooks (no listener to register): ItemsAdder is queried on demand below.
        isItemsAdderInstalled = isPluginInstalled("ItemsAdder");

        // WorldEdit and PlaceholderAPI cannot be re-added via reflection (their hooks must subclass a
        // third-party class - AbstractDelegateExtent / PlaceholderExpansion - which is impossible
        // without the Java-17 API on the compile classpath). They remain detected but inactive.
        isWorldEditInstalled = isPluginInstalled("WorldEdit");
        isPlaceholderAPIInstalled = isPluginInstalled("PlaceholderAPI");
    }

    /**
     * Detects whether a {@link Plugin} is installed (loaded) on this server.
     *
     * @param name
     *            The plugin name
     *
     * @return Whether the plugin is present
     */
    private boolean isPluginInstalled(@Nonnull String name) {
        return Bukkit.getPluginManager().getPlugin(name) != null;
    }

    /**
     * Runs a hook registration, logging and swallowing any failure so a broken integration never
     * disrupts the others.
     *
     * @param name
     *            The integration name (for logging)
     * @param registration
     *            The registration action
     *
     * @return Whether registration succeeded
     */
    private boolean register(@Nonnull String name, @Nonnull Runnable registration) {
        try {
            registration.run();
            return true;
        } catch (Exception | LinkageError x) {
            logError(name, x);
            return false;
        }
    }

    /**
     * This method logs a {@link Throwable} that was caused by a {@link Plugin}
     * we integrate into.
     *
     * @param name
     *            The name of the {@link Plugin}
     * @param throwable
     *            The {@link Throwable} to throw
     */
    @ParametersAreNonnullByDefault
    protected void logError(String name, Throwable throwable) {
        Plugin externalPlugin = Bukkit.getPluginManager().getPlugin(name);

        if (externalPlugin != null) {
            String version = externalPlugin.getDescription().getVersion();
            Slimefun.logger().log(Level.WARNING, "Is {0} v{1} up to date?", new Object[] { name, version });
            Slimefun.logger().log(Level.SEVERE, throwable, () -> "An unknown error was detected while interacting with \"" + name + " v" + version + "\"");
        } else {
            Slimefun.logger().log(Level.SEVERE, throwable, () -> "An unknown error was detected while interacting with the plugin \"" + name + "\"");
        }
    }

    /**
     * This returns our instance of the {@link ProtectionManager}.
     * This bridge is used to hook into any third-party protection {@link Plugin}.
     *
     * @return Our instance of the {@link ProtectionManager}
     */
    public @Nonnull ProtectionManager getProtectionManager() {
        return protectionManager;
    }

    /**
     * This checks if one of our third party integrations faked an {@link Event}.
     *
     * @param event
     *            The {@link Event} to test
     *
     * @return Whether this is a fake event
     */
    public boolean isEventFaked(@Nonnull Event event) {
        // mcMMO fires "fake" events (marked by com.gmail.nossr50.events.fake.FakeEvent) for its
        // abilities; resolve that marker reflectively so we don't act on them.
        if (!isMcMMOInstalled) {
            return false;
        }

        try {
            return Class.forName("com.gmail.nossr50.events.fake.FakeEvent").isInstance(event);
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * This checks if one of our third party integrations has placed a custom
     * {@link Block} at this location.
     *
     * @param block
     *            The {@link Block} to check
     *
     * @return Whether a different custom {@link Block} exists at that location
     */
    public boolean isCustomBlock(@Nonnull Block block) {
        // ItemsAdder: CustomBlock.byAlreadyPlaced(block) != null, resolved reflectively.
        if (!isItemsAdderInstalled) {
            return false;
        }

        try {
            Object result = ReflectionCompat.invokeStatic(Class.forName("dev.lone.itemsadder.api.CustomBlock"), "byAlreadyPlaced", block);
            return result != null;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * This checks if one of our third party integrations defines a given
     * {@link ItemStack} as custom.
     *
     * @param item
     *            The {@link ItemStack} to check
     *
     * @return Whether this {@link ItemStack} is a custom item
     */
    public boolean isCustomItem(@Nonnull ItemStack item) {
        // ItemsAdder: CustomStack.byItemStack(item) != null, resolved reflectively.
        if (!isItemsAdderInstalled) {
            return false;
        }

        try {
            Object result = ReflectionCompat.invokeStatic(Class.forName("dev.lone.itemsadder.api.CustomStack"), "byItemStack", item);
            return result != null;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * This method removes any temporary enchantments from the given {@link ItemStack}.
     *
     * @param item
     *            The {@link ItemStack}
     */
    public void removeTemporaryEnchantments(@Nonnull ItemStack item) {
        // mcMMO applies temporary ability buffs; remove them via SkillUtils reflectively.
        if (!isMcMMOInstalled) {
            return;
        }

        try {
            ReflectionCompat.invokeStatic(Class.forName("com.gmail.nossr50.util.skills.SkillUtils"), "removeAbilityBuff", item);
        } catch (Throwable e) {
            // mcMMO API mismatch - ignore.
        }
    }

    public boolean isPlaceholderAPIInstalled() {
        return isPlaceholderAPIInstalled;
    }

    public boolean isWorldEditInstalled() {
        return isWorldEditInstalled;
    }

    public boolean isMcMMOInstalled() {
        return isMcMMOInstalled;
    }

    public boolean isClearLagInstalled() {
        return isClearLagInstalled;
    }

    public boolean isItemsAdderInstalled() {
        return isItemsAdderInstalled;
    }

    public boolean isOrebfuscatorInstalled() {
        return isOrebfuscatorInstalled;
    }
}
