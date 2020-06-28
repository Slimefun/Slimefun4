package io.github.thebusybiscuit.slimefun4.core.services;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.cscorelib2.config.Config;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
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

    private final Config config;

    private String version = null;
    private boolean modified = false;

    public CustomTextureService(Config config) {
        this.config = config;
        config.getConfiguration().options().header("This file is used to assign items from Slimefun or any of its addons\n" + "the 'CustomModelData' NBT tag. This can be used in conjunction with a custom resource pack\n" + "to give items custom textures.\n0 means there is no data assigned to that item.\n\n" + "There is no official Slimefun resource pack at the moment.");
        config.getConfiguration().options().copyHeader(true);
    }

    /**
     * This method registers the given {@link SlimefunItem SlimefunItems} to this {@link CustomTextureService}.
     * If saving is enabled, it will save them to the {@link Config} file.
     * 
     * @param items
     *            The {@link SlimefunItem SlimefunItems} to register
     * @param save
     *            Whether to save this file
     */
    public void register(Collection<SlimefunItem> items, boolean save) {
        Validate.notEmpty(items, "items must neither be null or empty.");

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

        version = config.getString("version");

        if (save) {
            config.save();
        }
    }

    public String getVersion() {
        return version;
    }

    public boolean isActive() {
        return modified;
    }

    public int getModelData(String id) {
        Validate.notNull(id, "Cannot get the ModelData for 'null'");
        return config.getInt(id);
    }

    public void setTexture(ItemStack item, String id) {
        ItemMeta im = item.getItemMeta();
        setTexture(im, id);
        item.setItemMeta(im);
    }

    public void setTexture(ItemMeta im, String id) {
        int data = getModelData(id);

        if (SlimefunPlugin.getMinecraftVersion().isAtLeast(MinecraftVersion.MINECRAFT_1_14)) {
            im.setCustomModelData(data == 0 ? null : data);
        }
    }

}
