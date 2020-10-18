package io.github.thebusybiscuit.slimefun4.implementation.items.cargo;

import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.TickingBlock;
import io.github.thebusybiscuit.slimefun4.core.attributes.TickingMethod;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.cargo.CargoNet;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.holograms.SimpleHologram;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class CargoManager extends SimpleSlimefunItem<BlockUseHandler> implements TickingBlock {

    public CargoManager(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe);

        registerBlockHandler(getId(), (p, b, tool, reason) -> {
            SimpleHologram.remove(b);
            return true;
        });
    }

    @Override
    public BlockUseHandler getItemHandler() {
        return e -> {
            Optional<Block> block = e.getClickedBlock();

            if (block.isPresent()) {
                Player p = e.getPlayer();
                Block b = block.get();

                if (BlockStorage.getLocationInfo(b.getLocation(), "visualizer") == null) {
                    BlockStorage.addBlockInfo(b, "visualizer", "disabled");
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&4\u2718"));
                } else {
                    BlockStorage.addBlockInfo(b, "visualizer", null);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCargo Net Visualizer: " + "&2\u2714"));
                }
            }
        };
    }

    @Override
    public TickingMethod getTickingMethod() {
        return TickingMethod.SEPERATE_THREAD;
    }

    @Override
    public void tick(@Nonnull Block b) {
        CargoNet network = CargoNet.getNetworkFromLocationOrCreate(b.getLocation());
        network.tick(b);
    }

}
