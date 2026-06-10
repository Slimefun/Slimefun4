package io.github.thebusybiscuit.slimefun5.api.items;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.papermc.paper.inventory.ItemRarity;
import io.papermc.paper.inventory.tooltip.TooltipContext;
import io.papermc.paper.registry.set.RegistryKeySet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.items.ItemMetaSnapshot;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedPlayerHead;
import io.github.thebusybiscuit.slimefun5.api.MinecraftVersion;
import io.github.thebusybiscuit.slimefun5.api.exceptions.PrematureCodeException;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun5.utils.HeadTexture;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.VersionedItemFlag;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.ReflectionCompat;
import io.github.thebusybiscuit.slimefun5.utils.compatibility.MaterialCompat;

/**
 * The {@link SlimefunItemStack} functions as the base for any
 * {@link SlimefunItem}.
 * 
 * @author TheBusyBiscuit
 * @author Walshy
 *
 */
public class SlimefunItemStack {
    private ItemStack delegate;

    private String id;
    private ItemMetaSnapshot itemMetaSnapshot;

    private String texture = null;

    /**
     * Legacy-safe placeholder material. On servers older than the version that introduced a given
     * material, {@code XMaterial#parseMaterial()} returns {@code null}; the universal Java-8 jar
     * substitutes this material so the item still registers (with a fallback icon) instead of
     * crashing the whole plugin. {@code PAPER} exists on every supported Minecraft version.
     */
    private static final Material LEGACY_FALLBACK_MATERIAL = Material.PAPER;

    public SlimefunItemStack(@Nonnull String id, @Nonnull ItemStack item) {
        delegate = new ItemStack(item);

        Validate.notNull(id, "The Item id must never be null!");
        Validate.isTrue(id.equals(id.toUpperCase(Locale.ROOT)), "Slimefun Item Ids must be uppercase! (e.g. 'MY_ITEM_ID')");

        if (Slimefun.instance() == null) {
            throw new PrematureCodeException("A SlimefunItemStack must never be be created before your Plugin was enabled.");
        }

        this.id = id;

        ItemMeta meta = delegate.getItemMeta();

        Slimefun.getItemDataService().setItemData(meta, id);
        Slimefun.getItemTextureService().setTexture(meta, id);

        setItemMeta(meta);
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull ItemStack item, @Nonnull Consumer<ItemMeta> consumer) {
        this(id, item);

        ItemMeta im = delegate.getItemMeta();
        consumer.accept(im);
        setItemMeta(im);
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull Material type, @Nonnull Consumer<ItemMeta> consumer) {
        this(id, safeStack(type), consumer);
    }

    /**
     * Builds an {@link ItemStack} from the given {@link Material}, falling back to
     * {@link #LEGACY_FALLBACK_MATERIAL} when the material is {@code null} (i.e. it does not exist
     * on the running legacy server). This keeps the universal jar from crashing on items whose
     * material was introduced in a newer Minecraft version.
     */
    @Nonnull
    private static ItemStack safeStack(@Nullable Material type) {
        return new ItemStack(type != null ? type : LEGACY_FALLBACK_MATERIAL);
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull Material type, @Nullable String name, @Nonnull Consumer<ItemMeta> consumer) {
        this(id, type, meta -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            consumer.accept(meta);
        });
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull ItemStack item, @Nullable String name, String... lore) {
        this(id, item, im -> {
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
        });
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull Material type, @Nullable String name, String... lore) {
        this(id, safeStack(type), name, lore);
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull Material type, @Nonnull Color color, @Nullable String name, String... lore) {
        this(id, type, im -> {
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

            if (im instanceof LeatherArmorMeta) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) im;                leatherArmorMeta.setColor(color);
            }

            if (im instanceof PotionMeta) {
                PotionMeta potionMeta = (PotionMeta) im;
                ReflectionCompat.invoke(potionMeta, "setColor", color);
            }
        });
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull Color color, @Nonnull PotionEffect effect, @Nullable String name, String... lore) {
        this(id, Material.POTION, im -> {
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
                PotionMeta potionMeta = (PotionMeta) im;
                ReflectionCompat.invoke(potionMeta, "setColor", color);
                potionMeta.addCustomEffect(effect, true);

                if (effect.getType().equals(PotionEffectType.SATURATION)) {
                    VersionedItemFlag.addFlags(im, VersionedItemFlag.HIDE_ADDITIONAL_TOOLTIP);
                }
            }
        });
    }

    public SlimefunItemStack(@Nonnull SlimefunItemStack item, int amount) {
        this(item.getItemId(), item.item());
        setAmount(amount);
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull String texture, @Nullable String name, String... lore) {
        this(id, getSkull(id, texture), name, lore);
        this.texture = getTexture(id, texture);
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull HeadTexture head, @Nullable String name, String... lore) {
        this(id, head.getTexture(), name, lore);
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull String texture, @Nullable String name, @Nonnull Consumer<ItemMeta> consumer) {
        this(id, getSkull(id, texture), meta -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }

            consumer.accept(meta);
        });

        this.texture = getTexture(id, texture);
    }

    public SlimefunItemStack(@Nonnull String id, @Nonnull String texture, @Nonnull Consumer<ItemMeta> consumer) {
        this(id, getSkull(id, texture), consumer);
        this.texture = getTexture(id, texture);
    }

    /**
     * Returns the id that was given to this {@link SlimefunItemStack}.
     * 
     * @return The {@link SlimefunItem} id for this {@link SlimefunItemStack}
     */
    public final @Nonnull String getItemId() {
        return id;
    }

    /**
     * Gets the {@link SlimefunItem} associated for this {@link SlimefunItemStack}.
     * Null if no item is found.
     *
     * @return The {@link SlimefunItem} for this {@link SlimefunItemStack}, null if not found.
     */
    public @Nullable SlimefunItem getItem() {
        return SlimefunItem.getById(id);
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

    public @Nullable <T extends SlimefunItem> T getItem(@Nonnull Class<T> type) {
        SlimefunItem item = getItem();
        return type.isInstance(item) ? type.cast(item) : null;
    }

    public @Nonnull ItemMetaSnapshot getItemMetaSnapshot() {
        return itemMetaSnapshot;
    }

    public boolean setItemMeta(ItemMeta meta) {
        itemMetaSnapshot = new ItemMetaSnapshot(meta);

        return delegate.setItemMeta(meta);
    }

    public void setType(Material type) {
        delegate.setType(type);
    }

    public void setAmount(int amount) {
        delegate.setAmount(amount);
    }

    public @Nonnull Optional<String> getSkullTexture() {
        return Optional.ofNullable(texture);
    }

    public @Nullable String getDisplayName() {
        if (itemMetaSnapshot == null) {
            // Just to be extra safe
            return null;
        }

        return itemMetaSnapshot.getDisplayName().orElse(null);
    }

    private static @Nonnull ItemStack getSkull(@Nonnull String id, @Nonnull String texture) {
        if (Slimefun.getMinecraftVersion() == MinecraftVersion.UNIT_TEST) {
            return MaterialCompat.stack(XMaterial.PLAYER_HEAD);
        }

        return VersionedPlayerHead.getItemStack(getTexture(id, texture));
    }

    private static @Nonnull String getTexture(@Nonnull String id, @Nonnull String texture) {
        Validate.notNull(id, "The id cannot be null");
        Validate.notNull(texture, "The texture cannot be null");

        if (texture.startsWith("ey")) {
            return texture;
        } else if (CommonPatterns.HEXADECIMAL.matcher(texture).matches()) {
            String value = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + texture + "\"}}}";
            return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new IllegalArgumentException("The provided texture for Item \"" + id + "\" does not seem to be a valid texture String!");
        }
    }

    @Override
    public @Nonnull SlimefunItemStack clone() {
        return new SlimefunItemStack(id, delegate.clone());
    }

    @Override
    public String toString() {
        return "SlimefunItemStack (" + id + (delegate.getAmount() > 1 ? (" x " + delegate.getAmount()) : "") + ')';
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals(Object obj) {
        // We don't want people to override this, it should use the super method
        return super.equals(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int hashCode() {
        // We don't want people to override this, it should use the super method
        return super.hashCode();
    }

    /**
     * @return underlying ItemStack used
     */
    public @Nonnull ItemStack item() {
        return delegate.clone();
    }

    public Material getType() {
        return this.delegate.getType();
    }

    public ItemStack withType(Material type) {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "withType", type);
    }

    public int getAmount() {
        return this.delegate.getAmount();
    }

    /** @deprecated */
    @Deprecated
    public MaterialData getData() {
        return this.delegate.getData();
    }

    /** @deprecated */
    @Deprecated
    public void setData(MaterialData data) {
        this.delegate.setData(data);
    }

    /** @deprecated */
    @Deprecated
    public void setDurability(short durability) {
        this.delegate.setDurability(durability);
    }

    /** @deprecated */
    @Deprecated
    public short getDurability() {
        return this.delegate.getDurability();
    }

    public int getMaxStackSize() {
        return this.delegate.getMaxStackSize();
    }

    public boolean isSimilar(ItemStack stack) {
        return this.delegate.isSimilar(stack);
    }

    public boolean containsEnchantment(Enchantment ench) {
        return this.delegate.containsEnchantment(ench);
    }

    public int getEnchantmentLevel(Enchantment ench) {
        return this.delegate.getEnchantmentLevel(ench);
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.delegate.getEnchantments();
    }

    public void addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.delegate.addEnchantments(enchantments);
    }

    public void addEnchantment(Enchantment ench, int level) {
        this.delegate.addEnchantment(ench, level);
    }

    public void addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        this.delegate.addUnsafeEnchantments(enchantments);
    }

    public void addUnsafeEnchantment(Enchantment ench, int level) {
        this.delegate.addUnsafeEnchantment(ench, level);
    }

    public int removeEnchantment(Enchantment ench) {
        return this.delegate.removeEnchantment(ench);
    }

    public void removeEnchantments() {
        ReflectionCompat.invoke(this.delegate, "removeEnchantments");
    }

    public Map<String, Object> serialize() {
        return this.delegate.serialize();
    }

    public boolean editMeta(Consumer<? super ItemMeta> consumer) {
        Object result = ReflectionCompat.invoke(this.delegate, "editMeta", consumer);
        return result instanceof Boolean ? (Boolean) result : false;
    }

    public <M extends ItemMeta> boolean editMeta(Class<M> metaClass, Consumer<? super M> consumer) {
        Object result = ReflectionCompat.invoke(this.delegate, "editMeta", metaClass, consumer);
        return result instanceof Boolean ? (Boolean) result : false;
    }

    public ItemMeta getItemMeta() {
        return this.delegate.getItemMeta();
    }

    public boolean hasItemMeta() {
        return this.delegate.hasItemMeta();
    }

    /** @deprecated */
    @Deprecated
    // Java-8 port: the methods below mirror modern Paper ItemStack APIs that are absent at the 1.8.8
    // compile floor. Their bodies go through ReflectionCompat so they fully work on modern servers and
    // degrade gracefully (null / sensible default) on legacy ones. Signatures use compileOnly-stubbed
    // Paper/Adventure types. @SuppressWarnings: the Object->generic casts are unchecked by nature.
    @SuppressWarnings("unchecked")
    public String getTranslationKey() {
        return (String) ReflectionCompat.invoke(this.delegate, "getTranslationKey");
    }

    public ItemStack enchantWithLevels(int levels, boolean allowTreasure, Random random) {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "enchantWithLevels", levels, allowTreasure, random);
    }

    public ItemStack enchantWithLevels(int levels, RegistryKeySet<Enchantment> keySet, Random random) {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "enchantWithLevels", levels, keySet, random);
    }

    @SuppressWarnings("unchecked")
    public HoverEvent<HoverEvent.ShowItem> asHoverEvent(UnaryOperator<HoverEvent.ShowItem> op) {
        return (HoverEvent<HoverEvent.ShowItem>) ReflectionCompat.invoke(this.delegate, "asHoverEvent", op);
    }

    public Component displayName() {
        return (Component) ReflectionCompat.invoke(this.delegate, "displayName");
    }

    public ItemStack ensureServerConversions() {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "ensureServerConversions");
    }

    public byte[] serializeAsBytes() {
        Object result = ReflectionCompat.invoke(this.delegate, "serializeAsBytes");
        return result instanceof byte[] ? (byte[]) result : new byte[0];
    }

    /** @deprecated */
    public String getI18NDisplayName() {
        return (String) ReflectionCompat.invoke(this.delegate, "getI18NDisplayName");
    }

    /** @deprecated */
    public int getMaxItemUseDuration() {
        Object result = ReflectionCompat.invoke(this.delegate, "getMaxItemUseDuration");
        return result instanceof Integer ? (Integer) result : 0;
    }

    public int getMaxItemUseDuration(LivingEntity entity) {
        Object result = ReflectionCompat.invoke(this.delegate, "getMaxItemUseDuration", entity);
        return result instanceof Integer ? (Integer) result : 0;
    }

    public ItemStack asOne() {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "asOne");
    }

    public ItemStack asQuantity(int qty) {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "asQuantity", qty);
    }

    public ItemStack add() {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "add");
    }

    public ItemStack add(int qty) {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "add", qty);
    }

    public ItemStack subtract() {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "subtract");
    }

    public ItemStack subtract(int qty) {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "subtract", qty);
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public List<String> getLore() {
        return (List<String>) ReflectionCompat.invoke(this.delegate, "getLore");
    }

    @SuppressWarnings("unchecked")
    public List<Component> lore() {
        return (List<Component>) ReflectionCompat.invoke(this.delegate, "lore");
    }

    @Deprecated
    public void setLore(List<String> lore) {
        ReflectionCompat.invoke(this.delegate, "setLore", lore);
    }

    public void lore(List<? extends Component> lore) {
        ReflectionCompat.invoke(this.delegate, "lore", lore);
    }

    public void addItemFlags(ItemFlag... itemFlags) {
        ReflectionCompat.invoke(this.delegate, "addItemFlags", (Object) itemFlags);
    }

    public void removeItemFlags(ItemFlag... itemFlags) {
        ReflectionCompat.invoke(this.delegate, "removeItemFlags", (Object) itemFlags);
    }

    @SuppressWarnings("unchecked")
    public Set<ItemFlag> getItemFlags() {
        Object result = ReflectionCompat.invoke(this.delegate, "getItemFlags");
        return result instanceof Set ? (Set<ItemFlag>) result : java.util.Collections.emptySet();
    }

    public boolean hasItemFlag(ItemFlag flag) {
        Object result = ReflectionCompat.invoke(this.delegate, "hasItemFlag", flag);
        return result instanceof Boolean ? (Boolean) result : false;
    }

    public String translationKey() {
        return (String) ReflectionCompat.invoke(this.delegate, "translationKey");
    }

    /** @deprecated */
    @Deprecated
    public ItemRarity getRarity() {
        return (ItemRarity) ReflectionCompat.invoke(this.delegate, "getRarity");
    }

    public boolean isRepairableBy(ItemStack repairMaterial) {
        Object result = ReflectionCompat.invoke(this.delegate, "isRepairableBy", repairMaterial);
        return result instanceof Boolean ? (Boolean) result : false;
    }

    public boolean canRepair(ItemStack toBeRepaired) {
        Object result = ReflectionCompat.invoke(this.delegate, "canRepair", toBeRepaired);
        return result instanceof Boolean ? (Boolean) result : false;
    }

    public ItemStack damage(int amount, LivingEntity livingEntity) {
        return (ItemStack) ReflectionCompat.invoke(this.delegate, "damage", amount, livingEntity);
    }

    public boolean isEmpty() {
        Object result = ReflectionCompat.invoke(this.delegate, "isEmpty");
        return result instanceof Boolean ? (Boolean) result : (this.delegate == null || this.delegate.getType() == Material.AIR);
    }

    @SuppressWarnings("unchecked")
    public List<Component> computeTooltipLines(TooltipContext tooltipContext, Player player) {
        return (List<Component>) ReflectionCompat.invoke(this.delegate, "computeTooltipLines", tooltipContext, player);
    }

    @SuppressWarnings("unchecked")
    public HoverEvent<HoverEvent.ShowItem> asHoverEvent() {
        return (HoverEvent<HoverEvent.ShowItem>) ReflectionCompat.invoke(this.delegate, "asHoverEvent");
    }
}

