package io.github.thebusybiscuit.slimefun4.utils.itemstack;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class to edit the {@link ItemMeta} on an {@link ItemStack}
 *
 * @author md5sha256
 */
@ParametersAreNonnullByDefault
public final class ItemStackUtil {

    private ItemStackUtil() {
        throw new IllegalStateException("Static utility class cannot be instantiated");
    }

    /**
     * Curries a {@link Consumer} which sets the display name to the given {@link Component}
     *
     * @param name The name to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editName(@Nonnull Component name) {
        return (meta) -> meta.displayName(name);
    }

    /**
     * Curries a {@link Consumer} which sets the display name to the given {@link String}.
     * The string will have its color codes translated.
     *
     * @param name The name to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editNameString(@Nullable String name) {
        return (meta) -> {
            if (name != null) {
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            }
        };
    }

    /**
     * Curries a {@link Consumer} which sets the lore to the given {@link String}s.
     * The strings will have their color codes translated.
     *
     * @param lore The lore to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editLoreString(@Nonnull String... lore) {
        return editLoreString(Arrays.asList(lore));
    }


    /**
     * Curries a {@link Consumer} which sets the lore to the given {@link String}s.
     * The strings will have their color codes translated.
     *
     * @param lore The lore to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editLoreString(@Nonnull List<String> lore) {
        return (meta) -> {
            if (lore.isEmpty()) {
                meta.lore(Collections.emptyList());
                return;
            }
            List<String> newLore = lore.stream()
                    .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                    .toList();
            meta.setLore(newLore);
        };
    }

    /**
     * Curries a {@link Consumer} which sets the lore to the given {@link Component}s.
     *
     * @param lore The lore to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editLore(@Nonnull Component... lore) {
        return editLore(Arrays.asList(lore));
    }

    /**
     * Curries a {@link Consumer} which sets the lore to the given {@link Component}s.
     *
     * @param lore The lore to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editLore(@Nonnull List<? extends Component> lore) {
        return meta -> meta.lore(lore);
    }

    /**
     * Curries a {@link Consumer} which calls {@link PotionMeta#setColor(Color)} or {@link LeatherArmorMeta#setColor(Color)}
     * if the {@link ItemMeta} is an instance of either.
     *
     * @param color The color to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editColor(@Nonnull Color color) {
        return (meta) -> {
            if (meta instanceof LeatherArmorMeta leatherArmorMeta) {
                leatherArmorMeta.setColor(color);
            }
            if (meta instanceof PotionMeta potionMeta) {
                potionMeta.setColor(color);
            }
        };
    }

    /**
     * Curries a {@link Consumer} which sets the custom model data to the given integer
     *
     * @param data The custom model data to set
     * @return Returns a {@link Consumer}
     */
    public static Consumer<ItemMeta> editCustomModel(@Nullable Integer data) {
        return (meta) -> meta.setCustomModelData(data);
    }

    /**
     * Sets the display name and lore on a copy of an {@link ItemStack}
     *
     * @param item The item
     * @param name The name
     * @param lore The lore
     * @return Returns the cloned item stack with the given name and lore
     */
    public static ItemStack withNameLoreString(ItemStack item, String name, String... lore) {
        Consumer<ItemMeta> consumer = editNameString(name).andThen(editLoreString(lore));
        ItemStack itemStack = item.clone();
        itemStack.editMeta(consumer);
        return itemStack;
    }

    /**
     * Sets the display name and lore on a copy of an {@link ItemStack}
     *
     * @param item The item
     * @param name The name
     * @param lore The lore
     * @return Returns the cloned item stack with the given name and lore
     */
    public static ItemStack withNameLoreString(ItemStack item, String name, List<String> lore) {
        Consumer<ItemMeta> consumer = editNameString(name).andThen(editLoreString(lore));
        ItemStack itemStack = item.clone();
        itemStack.editMeta(consumer);
        return itemStack;
    }

    /**
     * Sets the name on a newly created {@link ItemStack} with the given {@link Material}
     *
     * @param material The material
     * @param name     The name
     * @return Returns a new stack with the given name, lore, and material
     */
    public static ItemStack withNameString(Material material, String name) {
        return withNameString(new ItemStack(material), name);
    }

    /**
     * Sets the display name on a copy of an {@link ItemStack}
     *
     * @param item The item
     * @param name The name
     * @return Returns the cloned item stack with the given name
     */
    public static ItemStack withNameString(ItemStack item, String name) {
        ItemStack itemStack = item.clone();
        itemStack.editMeta(editNameString(name));
        return itemStack;
    }

    /**
     * Sets the lore on a copy of an {@link ItemStack}
     *
     * @param item The item
     * @param lore The lore
     * @return Returns the cloned item stack with the given lore
     */
    public static ItemStack withLoreString(ItemStack item, String... lore) {
        return withLoreString(item, Arrays.asList(lore));
    }

    /**
     * Sets the lore on a copy of an {@link ItemStack}
     *
     * @param item The item
     * @param lore The lore
     * @return Returns the cloned item stack with the given lore
     */
    public static ItemStack withLoreString(ItemStack item, List<String> lore) {
        ItemStack itemStack = item.clone();
        itemStack.editMeta(editLoreString(lore));
        return itemStack;
    }

    /**
     * Sets the display name and lore on a new {@link ItemStack} with the given {@link Material}
     *
     * @param material The material
     * @param name     The display name
     * @param lore     The lore
     * @return Returns the new item stack with the given name, lore, and material
     */
    public static ItemStack withNameLoreString(Material material, String name, String... lore) {
        return withNameLoreString(material, name, Arrays.asList(lore));
    }

    /**
     * Sets the display name and lore on a new {@link ItemStack} with the given {@link Material}
     *
     * @param material The material
     * @param name     The display name
     * @param lore     The lore
     * @return Returns the new item stack with the given name, lore, and material
     */
    public static ItemStack withNameLoreString(Material material, String name, List<String> lore) {
        return withNameLoreString(new ItemStack(material), name, lore);
    }

    /**
     * Sets the lore on a new {@link ItemStack} with the given {@link Material}
     *
     * @param material The material
     * @param lore     The lore
     * @return Returns the new item stack with the given name, lore, and material
     */
    public static ItemStack withLoreString(Material material, List<String> lore) {
        return withLoreString(new ItemStack(material), lore);
    }
}
