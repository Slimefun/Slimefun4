package me.mrCookieSlime.Slimefun.api;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.item.ImmutableItemMeta;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun4.api.exceptions.PrematureCodeException;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class SlimefunItemStack extends CustomItem {

    private String id;
    private ImmutableItemMeta immutableMeta;

    private String texture = null;

    public SlimefunItemStack(String id, Material type, String name, String... lore) {
        super(type, name, lore);

        setID(id);
    }

    public SlimefunItemStack(String id, Material type, Color color, String name, String... lore) {
        super(new ItemStack(type), color, name, lore);

        setID(id);
    }

    public SlimefunItemStack(String id, Color color, PotionEffect effect, String name, String... lore) {
        super(Material.POTION, im -> {
            if (name != null) {
                im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            if (lore.length > 0) {
                List<String> lines = new ArrayList<>();

                for (String line : lore) {
                    lines.add(ChatColor.translateAlternateColorCodes('&', line));
                }

                im.setLore(lines);
            }

            if (im instanceof PotionMeta) {
                ((PotionMeta) im).setColor(color);
                ((PotionMeta) im).addCustomEffect(effect, true);

                if (effect.getType().equals(PotionEffectType.SATURATION)) {
                    im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                }
            }
        });

        setID(id);
    }

    public SlimefunItemStack(String id, ItemStack item, String name, String... lore) {
        super(item, name, lore);

        setID(id);
    }

    public SlimefunItemStack(String id, ItemStack item) {
        super(item);

        setID(id);
    }

    public SlimefunItemStack(String id, ItemStack item, Consumer<ItemMeta> consumer) {
        super(item, consumer);

        setID(id);
    }

    public SlimefunItemStack(String id, Material type, String name, Consumer<ItemMeta> consumer) {
        super(type, meta -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            consumer.accept(meta);
        });

        setID(id);
    }

    public SlimefunItemStack(String id, String texture, String name, String... lore) {
        super(getSkull(id, texture), name, lore);
        this.texture = getTexture(id, texture);

        setID(id);
    }

    public SlimefunItemStack(String id, String texture, String name, Consumer<ItemMeta> consumer) {
        super(getSkull(id, texture), meta -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            consumer.accept(meta);
        });

        this.texture = getTexture(id, texture);

        setID(id);
    }

    public SlimefunItemStack(String id, String texture, Consumer<ItemMeta> consumer) {
        super(getSkull(id, texture), consumer);
        this.texture = getTexture(id, texture);

        setID(id);
    }

    private void setID(String id) {
        Validate.isTrue(id.equals(id.toUpperCase(Locale.ROOT)), "Slimefun Item Ids must be uppercase! (e.g. 'MY_ITEM_ID')");

        if (SlimefunPlugin.instance == null) {
            throw new PrematureCodeException("A SlimefunItemStack must never be be created before your Plugin was enabled.");
        }

        this.id = id;

        ItemMeta meta = getItemMeta();

        SlimefunPlugin.getItemDataService().setItemData(meta, id);
        SlimefunPlugin.getItemTextureService().setTexture(meta, id);

        setItemMeta(meta);
    }

    public String getItemID() {
        return id;
    }

    /**
     * Gets the {@link SlimefunItem} associated for this {@link SlimefunItemStack}. Null if no item is found.
     *
     * @return The {@link SlimefunItem} for this {@link SlimefunItemStack}, null if not found.
     */
    public SlimefunItem getItem() {
        return SlimefunItem.getByID(id);
    }

    public ImmutableItemMeta getImmutableMeta() {
        return immutableMeta;
    }

    @Override
    public boolean setItemMeta(ItemMeta meta) {
        immutableMeta = new ImmutableItemMeta(meta);

        return super.setItemMeta(meta);
    }

    @Override
    public ItemStack clone() {
        return new SlimefunItemStack(id, this);
    }

    public Optional<String> getSkullTexture() {
        return Optional.ofNullable(texture);
    }

    private static ItemStack getSkull(String id, String texture) {
        if (SlimefunPlugin.getMinecraftVersion() == MinecraftVersion.UNIT_TEST) {
            return new ItemStack(Material.PLAYER_HEAD);
        }

        return SkullItem.fromBase64(getTexture(id, texture));
    }

    private static String getTexture(String id, String texture) {
        if (texture.startsWith("ey")) {
            return texture;
        }
        else if (PatternUtils.ALPHANUMERIC.matcher(texture).matches()) {
            return Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + texture + "\"}}}").getBytes(StandardCharsets.UTF_8));
        }
        else {
            throw new IllegalArgumentException("The provided texture for Item \"" + id + "\" does not seem to be a valid texture String!");
        }
    }

}
