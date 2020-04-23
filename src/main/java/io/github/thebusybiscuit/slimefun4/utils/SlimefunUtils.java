package io.github.thebusybiscuit.slimefun4.utils;

import java.util.List;
import java.util.Optional;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import io.github.thebusybiscuit.cscorelib2.item.ImmutableItemMeta;
import io.github.thebusybiscuit.slimefun4.core.attributes.Soulbound;
import io.github.thebusybiscuit.slimefun4.implementation.items.altar.AncientPedestal;
import me.mrCookieSlime.EmeraldEnchants.EmeraldEnchants;
import me.mrCookieSlime.EmeraldEnchants.ItemEnchantment;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This utility class holds method that are directly linked to Slimefun.
 * It provides a very crucial method for {@link ItemStack} comparison, as well as a simple method
 * to check if an {@link ItemStack} is {@link Soulbound} or not.
 * 
 * @author TheBusyBiscuit
 * @author Walshy
 *
 */
public final class SlimefunUtils {

    private static final String EMERALDENCHANTS_LORE = ChatColor.YELLOW.toString() + ChatColor.YELLOW.toString() + ChatColor.GRAY.toString();
    private static final String SOULBOUND_LORE = ChatColor.GRAY + "Soulbound";
    private static final String NO_PICKUP_METADATA = "no_pickup";

    private SlimefunUtils() {}

    /**
     * This method quickly returns whether an {@link Item} was marked as "no_pickup" by
     * a Slimefun device.
     * 
     * @param item
     *            The {@link Item} to query
     * @return Whether the {@link Item} is excluded from being picked up
     */
    public static boolean hasNoPickupFlag(Item item) {
        return item.hasMetadata(NO_PICKUP_METADATA);
    }

    /**
     * This will prevent the given {@link Item} from being picked up.
     * This is useful for display items which the {@link AncientPedestal} uses.
     * 
     * @param item
     *            The {@link Item} to prevent from being picked up
     * @param context
     *            The context in which this {@link Item} was flagged
     */
    public static void markAsNoPickup(Item item, String context) {
        item.setMetadata(NO_PICKUP_METADATA, new FixedMetadataValue(SlimefunPlugin.instance, context));
    }

    /**
     * This method checks whether the given {@link ItemStack} is considered {@link Soulbound}.
     * 
     * @param item
     *            The {@link ItemStack} to check for
     * @return Whether the given item is soulbound
     */
    public static boolean isSoulbound(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }
        else {
            SlimefunItem backpack = SlimefunItems.BOUND_BACKPACK.getItem();

            if (backpack != null && backpack.isItem(item)) {
                return !backpack.isDisabled();
            }
            else {
                ItemStack strippedItem = item.clone();

                if (SlimefunPlugin.getThirdPartySupportService().isEmeraldEnchantsInstalled()) {
                    for (ItemEnchantment enchantment : EmeraldEnchants.getInstance().getRegistry().getEnchantments(item)) {
                        EmeraldEnchants.getInstance().getRegistry().applyEnchantment(strippedItem, enchantment.getEnchantment(), 0);
                    }
                }

                SlimefunItem sfItem = SlimefunItem.getByItem(strippedItem);

                if (sfItem instanceof Soulbound && !sfItem.isDisabled()) {
                    return true;
                }
                else if (item.hasItemMeta()) {
                    ItemMeta im = item.getItemMeta();
                    return (im.hasLore() && im.getLore().contains(SOULBOUND_LORE));
                }

                return false;
            }
        }
    }

    public static boolean containsSimilarItem(Inventory inventory, ItemStack itemStack, boolean checkLore) {
        if (inventory == null || itemStack == null) return false;

        for (ItemStack is : inventory.getStorageContents()) {
            if (is == null || is.getType() == Material.AIR) continue;
            if (isItemSimilar(is, itemStack, checkLore)) return true;
        }

        return false;
    }

    public static boolean isItemSimilar(ItemStack item, ItemStack sfitem, boolean checkLore) {
        return isItemSimilar(item, sfitem, checkLore, true);
    }

    public static boolean isItemSimilar(ItemStack item, ItemStack sfitem, boolean checkLore, boolean checkAmount) {
        if (item == null) return sfitem == null;
        if (sfitem == null) return false;
        if (item.getType() != sfitem.getType()) return false;
        if (checkAmount && item.getAmount() < sfitem.getAmount()) return false;

        if (sfitem instanceof SlimefunItemStack && item instanceof SlimefunItemStack) {
            return ((SlimefunItemStack) item).getItemID().equals(((SlimefunItemStack) sfitem).getItemID());
        }

        boolean sfItemHasMeta = sfitem.hasItemMeta();
        if (item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();
            if (sfitem instanceof SlimefunItemStack) {
                Optional<String> id = SlimefunPlugin.getItemDataService().getItemData(itemMeta);
                if (id.isPresent()) {
                    return id.get().equals(((SlimefunItemStack) sfitem).getItemID());
                }

                ImmutableItemMeta meta = ((SlimefunItemStack) sfitem).getImmutableMeta();
                return equalsItemMeta(itemMeta, meta, checkLore);
            }

            if (sfItemHasMeta) {
                return equalsItemMeta(itemMeta, sfitem.getItemMeta(), checkLore);
            }
        }
        else {
            return !sfItemHasMeta;
        }

        return false;
    }

    private static boolean equalsItemMeta(ItemMeta itemMeta, ImmutableItemMeta meta, boolean checkLore) {
        Optional<String> displayName = meta.getDisplayName();

        if (itemMeta.hasDisplayName() && displayName.isPresent()) {
            if (itemMeta.getDisplayName().equals(displayName.get())) {
                Optional<List<String>> itemLore = meta.getLore();

                if (checkLore) {
                    if (itemMeta.hasLore() && itemLore.isPresent()) {
                        return equalsLore(itemMeta.getLore(), itemLore.get());
                    }
                    else return !itemMeta.hasLore() && !itemLore.isPresent();
                }
                else return true;
            }
            else return false;
        }
        else if (!itemMeta.hasDisplayName() && !displayName.isPresent()) {
            Optional<List<String>> itemLore = meta.getLore();

            if (checkLore) {
                if (itemMeta.hasLore() && itemLore.isPresent()) {
                    return equalsLore(itemMeta.getLore(), itemLore.get());
                }
                else return !itemMeta.hasLore() && !itemLore.isPresent();
            }
            else return true;
        }
        else return false;
    }

    private static boolean equalsItemMeta(ItemMeta itemMeta, ItemMeta sfitemMeta, boolean checkLore) {
        if (itemMeta.hasDisplayName() && sfitemMeta.hasDisplayName()) {
            if (itemMeta.getDisplayName().equals(sfitemMeta.getDisplayName())) {
                if (checkLore) {
                    if (itemMeta.hasLore() && sfitemMeta.hasLore()) {
                        return equalsLore(itemMeta.getLore(), sfitemMeta.getLore());
                    }
                    else return !itemMeta.hasLore() && !sfitemMeta.hasLore();
                }
                else return true;
            }
            else return false;
        }
        else if (!itemMeta.hasDisplayName() && !sfitemMeta.hasDisplayName()) {
            if (checkLore) {
                if (itemMeta.hasLore() && sfitemMeta.hasLore()) {
                    return equalsLore(itemMeta.getLore(), sfitemMeta.getLore());
                }
                else return !itemMeta.hasLore() && !sfitemMeta.hasLore();
            }
            else return true;
        }
        else return false;
    }

    private static boolean equalsLore(List<String> lore, List<String> lore2) {
        StringBuilder string1 = new StringBuilder();
        StringBuilder string2 = new StringBuilder();

        for (String string : lore) {
            if (!string.equals(SOULBOUND_LORE) && !string.startsWith(EMERALDENCHANTS_LORE)) {
                string1.append("-NEW LINE-").append(string);
            }
        }

        for (String string : lore2) {
            if (!string.equals(SOULBOUND_LORE) && !string.startsWith(EMERALDENCHANTS_LORE)) {
                string2.append("-NEW LINE-").append(string);
            }
        }

        return string1.toString().equals(string2.toString());
    }

}
