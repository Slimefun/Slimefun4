package me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item;

import java.util.Arrays;

import java.util.Collections;
import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * An old remnant of CS-CoreLib.
 * This will be removed once we updated everything.
 * Don't look at the code, it will be gone soon, don't worry.
 * 
 * @deprecated This was a horrible idea. Don't use it.
 *
 */
@Deprecated
public class CustomItemSerializer {

    public enum ItemFlag {

        MATERIAL(0),
        DATA(1),
        AMOUNT(2),
        DURABILITY(3),
        ENCHANTMENTS(4),
        ITEMMETA_DISPLAY_NAME(5),
        ITEMMETA_LORE(6);

        private int weight;

        ItemFlag(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return this.weight;
        }

    }

    private static ItemFlagComparator comparator = new ItemFlagComparator();

    public static String serialize(ItemStack item, ItemFlag... flags) {
        if (item == null)
            return "NULL";
        List<ItemFlag> flaglist = Arrays.asList(flags);

        Collections.sort(flaglist, comparator);

        StringBuilder builder = new StringBuilder();

        int i = 0;
        for (ItemFlag flag : flags) {
            if (i > 0)
                builder.append(" </sep> ");
            builder.append(flag.toString() + "=");

            switch (flag) {
                case AMOUNT: {
                    builder.append(item.getAmount());
                    break;
                }
                case DATA: {
                    builder.append((int) item.getData().getData());
                    break;
                }
                case DURABILITY: {
                    builder.append((int) item.getDurability());
                    break;
                }
                case ENCHANTMENTS:
                    for (Enchantment enchantment : Enchantment.values()) {
                        if (item.getEnchantments().containsKey(enchantment)) {
                            builder.append(enchantment.getName() + ":" + item.getEnchantmentLevel(enchantment));
                        } else {
                            builder.append(enchantment.getName() + ":0");
                        }
                    }
                    break;
                case ITEMMETA_DISPLAY_NAME: {
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        builder.append(item.getItemMeta().getDisplayName().replaceAll("\\u00a7", "&"));
                    } else {
                        builder.append("NONE");
                    }
                    break;
                }
                case ITEMMETA_LORE: {
                    if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                        builder.append(item.getItemMeta().getLore().toString().replaceAll("\\u00a7", "&"));
                    } else {
                        builder.append("NONE");
                    }
                    break;
                }
                case MATERIAL: {
                    builder.append(item.getType().toString());
                    break;
                }
                default:
                    break;
            }

            i++;
        }

        return builder.toString();
    }

    public static boolean equals(ItemStack stack1, ItemStack stack2, ItemFlag... flags) {
        return serialize(stack1, flags).equals(serialize(stack2, flags));
    }

}
