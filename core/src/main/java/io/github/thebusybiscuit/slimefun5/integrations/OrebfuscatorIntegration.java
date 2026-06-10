package io.github.thebusybiscuit.slimefun5.integrations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicesManager;

import io.github.thebusybiscuit.slimefun5.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun5.api.events.ExplosiveToolBreakBlocksEvent;
import io.github.thebusybiscuit.slimefun5.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun5.api.events.ReactorExplodeEvent;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.implementation.items.tools.GoldPan;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;

/**
 * This handles block breaks with Orebfuscator.
 *
 * <p>
 * Java-8 universal port: {@code net.imprex.orebfuscator.api.OrebfuscatorService} is Java-17 bytecode
 * and cannot sit on the Java-8 compile classpath, so the service is held as an opaque {@link Object}
 * and {@code deobfuscate(Collection)} is invoked reflectively. All of this class's event handlers are
 * on Slimefun's own events, so the class itself references no Orebfuscator type in its bytecode and is
 * only ever registered when Orebfuscator is actually installed.
 *
 * @author NgLoader
 */
class OrebfuscatorIntegration implements Listener {

    private static final String SERVICE_CLASS = "net.imprex.orebfuscator.api.OrebfuscatorService";

    private final Slimefun plugin;
    private Object service;

    OrebfuscatorIntegration(@Nonnull Slimefun plugin) {
        this.plugin = plugin;
    }

    /**
     * Resolves the Orebfuscator service reflectively and registers the listener.
     */
    public void register() {
        ServicesManager servicesManager = Bukkit.getServer().getServicesManager();

        try {
            Class<?> serviceClass = Class.forName(SERVICE_CLASS);
            this.service = servicesManager.getRegistration(serviceClass.asSubclass(Object.class)).getProvider();
        } catch (ClassNotFoundException | RuntimeException e) {
            // Re-throw so the IntegrationsManager logs and skips this integration.
            throw new IllegalStateException("Could not resolve the Orebfuscator service", e);
        }

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void deobfuscate(@Nonnull Object blocks) {
        ReflectionCompat.invoke(service, "deobfuscate", blocks);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlacerPlace(BlockPlacerPlaceEvent event) {
        deobfuscate(Arrays.asList(event.getBlock(), event.getBlockPlacer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplosiveToolBreakBlocks(ExplosiveToolBreakBlocksEvent event) {
        Set<Block> blocks = new HashSet<>();
        blocks.addAll(event.getAdditionalBlocks());
        blocks.add(event.getPrimaryBlock());
        deobfuscate(blocks);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onReactorExplode(ReactorExplodeEvent event) {
        deobfuscate(Arrays.asList(event.getLocation().getBlock()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGoldPanUse(PlayerRightClickEvent event) {
        if (event.getSlimefunItem().isPresent() && event.getClickedBlock().isPresent() && event.getSlimefunItem().get() instanceof GoldPan) {
            deobfuscate(Arrays.asList(event.getClickedBlock().get()));
        }
    }
}
