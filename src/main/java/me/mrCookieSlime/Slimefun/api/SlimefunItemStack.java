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
import io.github.thebusybiscuit.slimefun4.api.exceptions.WrongItemStackException;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

public class SlimefunItemStack extends CustomItem {

    private String id;
    private ImmutableItemMeta immutableMeta;

    private boolean locked = false;
    private String texture = null;

    public SlimefunItemStack(String id, Material type, String name, String... lore) {
        super(type, name, lore);

        setItemId(id);
    }

    public SlimefunItemStack(String id, Material type, Color color, String name, String... lore) {
        super(new ItemStack(type), color, name, lore);

        setItemId(id);
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

        setItemId(id);
    }

    public SlimefunItemStack(String id, ItemStack item, String name, String... lore) {
        super(item, name, lore);

        setItemId(id);
    }

    public SlimefunItemStack(String id, ItemStack item) {
        super(item);

        setItemId(id);
    }

    public SlimefunItemStack(SlimefunItemStack item, int amount) {
        this(item.getItemId(), item);
        setAmount(amount);
    }

    public SlimefunItemStack(String id, ItemStack item, Consumer<ItemMeta> consumer) {
        super(item, consumer);

        setItemId(id);
    }

    public SlimefunItemStack(String id, Material type, String name, Consumer<ItemMeta> consumer) {
        super(type, meta -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            consumer.accept(meta);
        });

        setItemId(id);
    }

    public SlimefunItemStack(String id, String texture, String name, String... lore) {
        super(getSkull(id, texture), name, lore);
        this.texture = getTexture(id, texture);

        setItemId(id);
    }

    public SlimefunItemStack(String id, HeadTexture head, String name, String... lore) {
        this(id, head.getTexture(), name, lore);
    }

    public SlimefunItemStack(String id, String texture, String name, Consumer<ItemMeta> consumer) {
        super(getSkull(id, texture), meta -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            consumer.accept(meta);
        });

        this.texture = getTexture(id, texture);

        setItemId(id);
    }

    public SlimefunItemStack(String id, String texture, Consumer<ItemMeta> consumer) {
        super(getSkull(id, texture), consumer);
        this.texture = getTexture(id, texture);

        setItemId(id);
    }

    private void setItemId(String id) {
        Validate.notNull(id, "The Item id must never be null!");
        Validate.isTrue(id.equals(id.toUpperCase(Locale.ROOT)), "Slimefun Item Ids must be uppercase! (e.g. 'MY_ITEM_ID')");

        if (SlimefunPlugin.instance() == null) {
            throw new PrematureCodeException("A SlimefunItemStack must never be be created before your Plugin was enabled.");
        }

        this.id = id;

        ItemMeta meta = getItemMeta();

        SlimefunPlugin.getItemDataService().setItemData(meta, id);
        SlimefunPlugin.getItemTextureService().setTexture(meta, id);

        setItemMeta(meta);
    }

    /**
     * Returns the id that was given to this {@link SlimefunItemStack}.
     * 
     * @return The {@link SlimefunItem} id for this {@link SlimefunItemStack}
     */
    public String getItemId() {
        return id;
    }

    /**
     * Gets the {@link SlimefunItem} associated for this {@link SlimefunItemStack}.
     * Null if no item is found.
     *
     * @return The {@link SlimefunItem} for this {@link SlimefunItemStack}, null if not found.
     */
    public SlimefunItem getItem() {
        return SlimefunItem.getByID(id);
    }

    /**
     * This method returns the associated {@link SlimefunItem} and casts it to the provided
     * {@link Class}.
     * 
     * If no item was found or the found {@link SlimefunItem} is not of the requested type,
     * the method will return null.
     * 
     * @param <T>
     *            The type of {@link SlimefunItem} to cast this to
     * @param type
     *            The {@link Class} of the target {@link SlimefunItem}
     * 
     * @return The {@link SlimefunItem} this {@link SlimefunItem} represents, casted to the given type
     */
    public <T extends SlimefunItem> T getItem(Class<T> type) {
        SlimefunItem item = getItem();
        return type.isInstance(item) ? type.cast(item) : null;
    }

    public ImmutableItemMeta getImmutableMeta() {
        return immutableMeta;
    }

    @Override
    public boolean setItemMeta(ItemMeta meta) {
        validate();
        immutableMeta = new ImmutableItemMeta(meta);

        return super.setItemMeta(meta);
    }

    @Override
    public void setType(Material type) {
        validate();
        super.setType(type);
    }

    @Override
    public void setAmount(int amount) {
        validate();
        super.setAmount(amount);
    }

    private void validate() {
        if (locked) {
            throw new WrongItemStackException(id + " is not mutable.");
        }
    }

    public void lock() {
        locked = true;
    }

    @Override
    public ItemStack clone() {
        return new SlimefunItemStack(id, this);
    }

    @Override
    public String toString() {
        return "SlimefunItemStack (" + id + (getAmount() > 1 ? (" x " + getAmount()) : "") + ')';
    }

    public Optional<String> getSkullTexture() {
        return Optional.ofNullable(texture);
    }

    public String getDisplayName() {
        if (immutableMeta == null) {
            // Just to be extra safe
            return null;
        }
        
        return immutableMeta.getDisplayName().orElse(null);
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
