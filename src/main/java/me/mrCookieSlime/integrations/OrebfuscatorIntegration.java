package me.mrCookieSlime.integrations;

import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.events.ExplosiveToolBreakBlocksEvent;
import io.github.thebusybiscuit.slimefun4.api.events.ReactorExplodeEvent;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import net.imprex.orebfuscator.api.OrebfuscatorService;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This handles block breaks with orebfuscator
 *
 * @author NgLoader
 */
class OrebfuscatorIntegration implements Listener {

    private final Slimefun plugin;
    private OrebfuscatorService service;

    OrebfuscatorIntegration(@Nonnull Slimefun plugin) {
        this.plugin = plugin;
    }

    /**
     * Init orebfuscation service and register listener
     */
    public void register() {
        this.service = Bukkit.getServer().getServicesManager().getRegistration(OrebfuscatorService.class).getProvider();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlacerPlace(BlockPlacerPlaceEvent event) {
        this.service.deobfuscate(Arrays.asList(event.getBlock(), event.getBlockPlacer()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplosiveToolBreakBlocks(ExplosiveToolBreakBlocksEvent event) {
        Set<Block> blocks = new HashSet<>(event.getAdditionalBlocks());
        blocks.add(event.getPrimaryBlock());
        this.service.deobfuscate(blocks);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onReactorExplode(ReactorExplodeEvent event) {
        this.service.deobfuscate(Collections.singletonList(event.getLocation().getBlock()));
    }
}
