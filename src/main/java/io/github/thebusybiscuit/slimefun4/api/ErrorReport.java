package io.github.thebusybiscuit.slimefun4.api;

import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetProvider;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * This class represents an {@link ErrorReport}.
 * Error reports are thrown when a {@link BlockTicker} is causing problems.
 * To ensure that the console doesn't get too spammy, we destroy the block and generate
 * an {@link ErrorReport} instead.
 * Error reports get saved in the plugin folder.
 * 
 * @author TheBusyBiscuit
 *
 */
public class ErrorReport<T extends Throwable> {

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm", Locale.ROOT);
    private static final AtomicInteger count = new AtomicInteger(0);

    private SlimefunAddon addon;
    private T throwable;
    private File file;

    @ParametersAreNonnullByDefault
    public ErrorReport(T throwable, SlimefunAddon addon, Consumer<PrintStream> printer) {
        this.throwable = throwable;
        this.addon = addon;

        SlimefunPlugin.runSync(() -> print(printer));
    }

    @ParametersAreNonnullByDefault
    public ErrorReport(T throwable, Location l, SlimefunItem item) {
        this(throwable, item.getAddon(), stream -> {
            stream.println("Block Info:");
            stream.println("  World: " + l.getWorld().getName());
            stream.println("  X: " + l.getBlockX());
            stream.println("  Y: " + l.getBlockY());
            stream.println("  Z: " + l.getBlockZ());
            stream.println("  Material: " + l.getBlock().getType());
            stream.println("  Block Data: " + l.getBlock().getBlockData().getClass().getName());
            stream.println("  State: " + l.getBlock().getState().getClass().getName());
            stream.println();

            if (item.getBlockTicker() != null) {
                stream.println("Ticker-Info:");
                stream.println("  Type: " + (item.getBlockTicker().isSynchronized() ? "Synchronized" : "Asynchronous"));
                stream.println();
            }

            if (item instanceof EnergyNetProvider) {
                stream.println("Ticker-Info:");
                stream.println("  Type: Indirect (Energy Network)");
                stream.println();
            }

            stream.println("Slimefun Data:");
            stream.println("  ID: " + item.getId());
            stream.println("  Inventory: " + BlockStorage.getStorage(l.getWorld()).hasInventory(l));
            stream.println("  Data: " + BlockStorage.getBlockInfoAsJson(l));
            stream.println();
        });
    }

    @ParametersAreNonnullByDefault
    public ErrorReport(T throwable, SlimefunItem item) {
        this(throwable, item.getAddon(), stream -> {
            stream.println("SlimefunItem:");
            stream.println("  ID: " + item.getId());
            stream.println("  Plugin: " + (item.getAddon() == null ? "Unknown" : item.getAddon().getName()));
            stream.println();
        });
    }

    /**
     * This method returns the {@link File} this {@link ErrorReport} has been written to.
     * 
     * @return The {@link File} for this {@link ErrorReport}
     */
    @Nonnull
    public File getFile() {
        return file;
    }

    /**
     * This returns the {@link Throwable} that was thrown.
     * 
     * @return The {@link Throwable}
     */
    @Nonnull
    public T getThrown() {
        return throwable;
    }

    /**
     * This method returns the amount of {@link ErrorReport ErrorReports} created in this session.
     * 
     * @return The amount of {@link ErrorReport ErrorReports} created.
     */
    public static int count() {
        return count.get();
    }

    private void print(@Nonnull Consumer<PrintStream> printer) {
        this.file = getNewFile();
        count.incrementAndGet();

        try (PrintStream stream = new PrintStream(file, StandardCharsets.UTF_8.name())) {
            stream.println();

            stream.println("Java Environment:");
            stream.println("  Operating System: " + System.getProperty("os.name"));
            stream.println("  Java Version: " + System.getProperty("java.version"));
            stream.println();

            String serverSoftware = PaperLib.isSpigot() && !PaperLib.isPaper() ? "Spigot" : Bukkit.getName();
            stream.println("Server Software: " + serverSoftware);
            stream.println("  Build: " + Bukkit.getVersion());
            stream.println("  Minecraft v" + Bukkit.getBukkitVersion());
            stream.println();

            stream.println("Slimefun Environment:");
            stream.println("  CS-CoreLib v" + SlimefunPlugin.getCSCoreLibVersion());
            stream.println("  Slimefun v" + SlimefunPlugin.getVersion());
            stream.println("  Caused by: " + addon.getName() + " v" + addon.getPluginVersion());
            stream.println();

            List<String> plugins = new ArrayList<>();
            List<String> addons = new ArrayList<>();

            scanPlugins(plugins, addons);

            stream.println("Installed Addons (" + addons.size() + ")");
            addons.forEach(stream::println);

            stream.println();

            stream.println("Installed Plugins (" + plugins.size() + "):");
            plugins.forEach(stream::println);

            stream.println();

            printer.accept(stream);

            stream.println("Stacktrace:");
            stream.println();
            throwable.printStackTrace(stream);

            addon.getLogger().log(Level.WARNING, "");
            addon.getLogger().log(Level.WARNING, "An Error occurred! It has been saved as: ");
            addon.getLogger().log(Level.WARNING, "/plugins/Slimefun/error-reports/{0}", file.getName());
            addon.getLogger().log(Level.WARNING, "Please put this file on https://pastebin.com/ and report this to the developer(s).");

            if (addon.getBugTrackerURL() != null) {
                addon.getLogger().log(Level.WARNING, "Bug Tracker: {0}", addon.getBugTrackerURL());
            }

            addon.getLogger().log(Level.WARNING, "");
        } catch (Exception x) {
            addon.getLogger().log(Level.SEVERE, x, () -> "An Error occurred while saving an Error-Report for Slimefun " + SlimefunPlugin.getVersion());
        }
    }

    private static void scanPlugins(@Nonnull List<String> plugins, @Nonnull List<String> addons) {
        String dependency = "Slimefun";

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            if (Bukkit.getPluginManager().isPluginEnabled(plugin)) {
                plugins.add("  + " + plugin.getName() + ' ' + plugin.getDescription().getVersion());

                if (plugin.getDescription().getDepend().contains(dependency) || plugin.getDescription().getSoftDepend().contains(dependency)) {
                    addons.add("  + " + plugin.getName() + ' ' + plugin.getDescription().getVersion());
                }
            } else {
                plugins.add("  - " + plugin.getName() + ' ' + plugin.getDescription().getVersion());

                if (plugin.getDescription().getDepend().contains(dependency) || plugin.getDescription().getSoftDepend().contains(dependency)) {
                    addons.add("  - " + plugin.getName() + ' ' + plugin.getDescription().getVersion());
                }
            }
        }
    }

    @Nonnull
    private static File getNewFile() {
        String path = "plugins/Slimefun/error-reports/" + dateFormat.format(LocalDateTime.now());
        File newFile = new File(path + ".err");

        if (newFile.exists()) {
            IntStream stream = IntStream.iterate(1, i -> i + 1).filter(i -> !new File(path + " (" + i + ").err").exists());
            int id = stream.findFirst().getAsInt();

            newFile = new File(path + " (" + id + ").err");
        }

        return newFile;
    }

    public static void tryCatch(@Nonnull Function<Exception, ErrorReport<Exception>> function, @Nonnull Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception x) {
            function.apply(x);
        }
    }

}
