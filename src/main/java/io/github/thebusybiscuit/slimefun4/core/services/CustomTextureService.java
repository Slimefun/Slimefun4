package io.github.thebusybiscuit.slimefun4.core.services;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This Service is responsible for applying custom model data to any {@link SlimefunItemStack}
 * if a Server Owner configured Slimefun to use those.
 * We simply use {@link ItemMeta#setCustomModelData(Integer)} for this.
 * 
 * @author TheBusyBiscuit
 *
 */
public class CustomTextureService {

    private final Plugin plugin;
    private Config config;
    private boolean modified = false;

    public CustomTextureService(Plugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        config = new Config(plugin, "item-models.yml");

        config.getConfiguration().options().header(
            "This file is used to assign items from Slimefun or any of its addons\n" +
            "the 'CustomModelData' NBT tag. This can be used in conjunction with a custom resource pack\n" +
            "to give items custom textures.\n\n" +
            "There is no official Slimefun resource pack at the moment."
        );

        config.getConfiguration().options().copyHeader(true);
    }

    public void register(Iterable<SlimefunItem> items) {
        config.setDefaultValue("SLIMEFUN_GUIDE", 0);

        config.setDefaultValue("_UI_BACKGROUND", 0);
        config.setDefaultValue("_UI_BACK", 0);
        config.setDefaultValue("_UI_MENU", 0);
        config.setDefaultValue("_UI_SEARCH", 0);
        config.setDefaultValue("_UI_WIKI", 0);
        config.setDefaultValue("_UI_PREVIOUS_ACTIVE", 0);
        config.setDefaultValue("_UI_PREVIOUS_INACTIVE", 0);
        config.setDefaultValue("_UI_NEXT_ACTIVE", 0);
        config.setDefaultValue("_UI_NEXT_INACTIVE", 0);

        for (SlimefunItem item : items) {
            if (item != null && item.getID() != null) {
                config.setDefaultValue(item.getID(), 0);

                if (config.getInt(item.getID()) != 0) {
                    modified = true;
                }
            }
        }

        config.save();
    }

    public String getVersion() {
        return config.getString("version");
    }

    public boolean isActive() {
        return modified;
    }

    public int getModelData(String id) {
        return config.getInt(id);
    }

    public void setTexture(ItemStack item, String id) {
        ItemMeta im = item.getItemMeta();
        setTexture(im, id);
        item.setItemMeta(im);
    }

    public void setTexture(ItemMeta im, String id) {
        int data = getModelData(id);
        im.setCustomModelData(data == 0 ? null : data);
    }

}
