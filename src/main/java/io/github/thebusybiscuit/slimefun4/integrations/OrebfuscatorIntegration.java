package io.github.thebusybiscuit.slimefun4.integrations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.thebusybiscuit.slimefun4.api.events.BlockPlacerPlaceEvent;
import io.github.thebusybiscuit.slimefun4.api.events.ExplosiveToolBreakBlocksEvent;
import io.github.thebusybiscuit.slimefun4.api.events.ReactorExplodeEvent;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import net.imprex.orebfuscator.api.OrebfuscatorService;

public class OrebfuscatorIntegration implements Listener {

	private final SlimefunPlugin plugin;
	private OrebfuscatorService service;

	public OrebfuscatorIntegration(@Nonnull SlimefunPlugin plugin) {
        this.plugin = plugin;
    }

    public void register() {
        RegisteredServiceProvider<OrebfuscatorService> provider = Bukkit.getServer().getServicesManager().getRegistration(OrebfuscatorService.class);
        this.service = provider.getProvider();

        Bukkit.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onBlockPlacerPlace(BlockPlacerPlaceEvent event) {
        this.service.deobfuscate(Arrays.asList(event.getBlock(), event.getBlockPlacer()));
    }

    @EventHandler
    public void onExplosiveToolBreakBlocks(ExplosiveToolBreakBlocksEvent event) {
        Set<Block> blocks = new HashSet<>();
        blocks.addAll(event.getAdditionalBlocks());
        blocks.add(event.getPrimaryBlock());
        this.service.deobfuscate(blocks);
    }

    @EventHandler
    public void onReactorExplode(ReactorExplodeEvent event) {
        this.service.deobfuscate(Arrays.asList(event.getLocation().getBlock()));
    }
}
