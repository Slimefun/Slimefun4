package io.github.thebusybiscuit.slimefun4.utils.itemstack;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility object to provide a fluent interface to edit the {@link ItemMeta} on an {@link ItemStack}
 * The fluent methods do not return a new {@link ItemStackEditor} instance, they mutate the existing instance.
 * <br>
 * Unless otherwise stated, all methods will mutate this instance.
 * This class is meant to be created, immediately used, and then effectively discarded at the call site.
 * <br>
 * See the following to actually apply the transformations onto {@link ItemStack}s
 *
 * @author md5sha256
 * @see #edit(ItemStack)
 * @see #editCopy(ItemStack)
 * @see #createAs(Material)
 */
@ParametersAreNonnullByDefault
public class ItemStackEditor {

    private Consumer<ItemMeta> transform = meta -> {
    };

    /**
     * Chains a consumer
     *
     * @param consumer The consumer to chain
     * @return Returns this
     */
    public ItemStackEditor withMetaTransform(@Nonnull Consumer<ItemMeta> consumer) {
        this.transform = this.transform.andThen(consumer);
        return this;
    }

    /**
     * Chains a consumer for a specific {@link ItemMeta} subtype.
     *
     * @param metaClass The class of the {@link ItemMeta}
     * @param consumer  The consumer to chain
     * @param <T>       The {@link ItemMeta} subtype
     * @return Returns this
     */
    public <T extends ItemMeta> ItemStackEditor withMetaTransform(@Nonnull Class<T> metaClass, Consumer<T> consumer) {
        Consumer<ItemMeta> wrapper = meta -> {
            if (metaClass.isInstance(meta)) {
                consumer.accept(metaClass.cast(meta));
            }
        };
        return withMetaTransform(wrapper);
    }

    /**
     * Set the lore as the given strings. The strings will have their color codes translated.
     *
     * @param lore The lore to set
     * @return Returns this
     */
    public ItemStackEditor withLoreString(String... lore) {
        return withMetaTransform(ItemStackUtil.editLoreString(lore));
    }

    /**
     * Set the lore as the given strings. The strings will have their color codes translated.
     *
     * @param lore The lore to set
     * @return Returns this
     */
    public ItemStackEditor withLoreString(List<String> lore) {
        return withMetaTransform(ItemStackUtil.editLoreString(lore));
    }

    /**
     * Set the color attribute. See {@link ItemStackUtil#editColor(Color)}
     *
     * @param color The color to set
     * @return Returns this
     */
    public ItemStackEditor withColor(Color color) {
        return withMetaTransform(ItemStackUtil.editColor(color));
    }

    /**
     * Set the display name as the given string. The string will be color code translated.
     *
     * @param name The name to set
     * @return Returns this
     */
    public ItemStackEditor withNameString(String name) {
        return withMetaTransform(ItemStackUtil.editNameString(name));
    }

    /**
     * Set the display name as the given {@link Component}.
     *
     * @param name The name to set
     * @return Returns this
     */
    public ItemStackEditor withName(Component name) {
        return withMetaTransform(ItemStackUtil.editName(name));
    }

    /**
     * Set the lore as the given {@link Component}s
     *
     * @param lore The lore to set
     * @return Returns this
     */
    public ItemStackEditor withLore(List<? extends Component> lore) {
        return withMetaTransform(ItemStackUtil.editLore(lore));
    }

    /**
     * Set the lore as the given {@link Component}s
     *
     * @param lore The lore to set
     * @return Returns this
     */
    public ItemStackEditor withLore(Component... lore) {
        return withMetaTransform(ItemStackUtil.editLore(lore));
    }

    /**
     * Set the custom model data
     *
     * @param data The custom model data
     * @return Returns this
     */
    public ItemStackEditor withCustomModel(@Nullable Integer data) {
        return withMetaTransform(ItemStackUtil.editCustomModel(data));
    }

    /**
     * Adds additional item flags
     *
     * @param flags The flags to add
     * @return Returns this
     */
    public ItemStackEditor withAdditionalFlags(ItemFlag... flags) {
        return withMetaTransform(meta -> meta.addItemFlags(flags));
    }

    /**
     * Get the current transform which would be applied to an {@link ItemStack}
     * This method is pure.
     *
     * @return {@link Consumer}
     */
    public Consumer<ItemMeta> getTransform() {
        return this.transform;
    }

    /**
     * Edits the given {@link ItemStack} with this {@link ItemStackEditor}s transform
     * This method is pure
     *
     * @param itemStack The item stack to edit
     * @return Returns the provided {@link ItemStack}
     */
    public ItemStack edit(ItemStack itemStack) {
        itemStack.editMeta(this.transform);
        return itemStack;
    }

    /**
     * Edits a clone of the given {@link ItemStack} with this {@link ItemStackEditor}s transform.
     * This method is pure.
     *
     * @param itemStack The item stack to edit
     * @return Returns the clone of {@link ItemStack} with edits applied
     */
    public ItemStack editCopy(ItemStack itemStack) {
        return edit(itemStack.clone());
    }

    /**
     * Create a new {@link ItemStack} with the given {@link Material} and then applies the {@link ItemStackEditor}s
     * transform onto it.
     * This method is pure.
     *
     * @param material The material of the item
     * @return Returns a new {@link ItemStack} with the given material and edits applied
     */
    public ItemStack createAs(Material material) {
        return edit(new ItemStack(material));
    }

}
