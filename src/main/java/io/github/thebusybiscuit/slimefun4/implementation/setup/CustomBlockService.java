package io.github.thebusybiscuit.slimefun4.implementation.setup;

import io.github.bakedlibs.dough.config.Config;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.logging.Level;

public class CustomBlockService {

    private final Config config;
    private final Slimefun plugin;

    public CustomBlockService(@Nonnull Slimefun plugin){
        config = new Config(plugin, "blocks.yml");
        this.plugin = plugin;
    }

    public void register(@Nonnull Collection<SlimefunItem> items, boolean save) {
        Validate.notEmpty(items, "items must neither be null or empty.");

        loadDefaultValues();

        for (SlimefunItem item : items) {
            if (item != null) {
                if (item.getId().startsWith("THORNYA")) {
                    config.setDefaultValue("blocks." + item.getId(), "down,east,north,south,up,west");
                }
            }
        }

        if (save) {
            config.save();
        }
    }

    private void loadDefaultValues() {
        InputStream inputStream = Slimefun.class.getResourceAsStream("/blocks.yml");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(reader);

            for (String key : cfg.getKeys(false)) {
                if (key.startsWith("THORNYA")) {
                    config.setDefaultValue("blocks." + key, cfg.getInt(key));
                }
            }
        } catch (Exception e) {
            Slimefun.logger().log(Level.SEVERE, "Failed to load default blocks.yml file", e);
        }
    }

    public void setCustomBlock(Block block, String id){
        BlockData blockData = block.getBlockData();
        MultipleFacing multiFacing = (MultipleFacing) blockData;
        String faces = config.getString("blocks." + id);
        multiFacing.getAllowedFaces().forEach(face -> multiFacing.setFace(face, false));
        String[] faceArray = faces.split(",");
        for(String face : faceArray){
            if(face.equalsIgnoreCase("none")) break;
            BlockFace blockFace = BlockFace.valueOf(face.toUpperCase());
            multiFacing.setFace(blockFace, true);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            block.setBlockData(multiFacing);
        });

    }
}
