package io.github.thebusybiscuit.slimefun4.implementation.items.magical;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import io.github.thebusybiscuit.cscorelib2.protection.ProtectableAction;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemUseHandler;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

/**
 * This {@link SlimefunItem} casts a {@link LightningStrike} where you are pointing.
 * Unlike the other Staves, it has a limited amount of uses.
 * 
 * @author Linox
 *
 */
public class StormStaff extends SimpleSlimefunItem<ItemUseHandler> {

    public static final int MAX_USES = 8;

    private static final NamespacedKey usageKey = new NamespacedKey(SlimefunPlugin.instance, "stormstaff_usage");

    public StormStaff(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(category, item, recipeType, recipe, getCraftedOutput());
    }

    private static ItemStack getCraftedOutput() {
        ItemStack item = SlimefunItems.STAFF_STORM.clone();
        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore();

        lore.set(4, ChatColor.translateAlternateColorCodes('&', "&e" + MAX_USES + " Uses &7left"));

        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Player p = e.getPlayer();
            ItemStack item = e.getItem();

            if (!item.hasItemMeta()) return;
            ItemMeta itemMeta = item.getItemMeta();
            if (!itemMeta.hasLore()) return;
            List<String> itemLore = itemMeta.getLore();

            ItemStack sfItem = getItem();
            ItemMeta sfItemMeta = sfItem.getItemMeta();
            List<String> sfItemLore = sfItemMeta.getLore();

            // Index 1 and 3 in SlimefunItems.STAFF_STORM has lores with words and stuff so we check for them.
            if (itemLore.size() < 6 && itemLore.get(1).equals(sfItemLore.get(1)) && itemLore.get(3).equals(sfItemLore.get(3))) {
                if (p.getFoodLevel() >= 4 || p.getGameMode() == GameMode.CREATIVE) {
                    // Get a target block with max. 30 blocks of distance
                    Location loc = p.getTargetBlock(null, 30).getLocation();

                    if (loc.getWorld() != null && loc.getChunk().isLoaded()) {
                        if (loc.getWorld().getPVP() && SlimefunPlugin.getProtectionManager().hasPermission(p, loc, ProtectableAction.PVP)) {
                            loc.getWorld().strikeLightning(loc);

                            if (p.getInventory().getItemInMainHand().getType() != Material.SHEARS && p.getGameMode() != GameMode.CREATIVE) {
                                FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, p.getFoodLevel() - 4);
                                Bukkit.getPluginManager().callEvent(event);
                                p.setFoodLevel(event.getFoodLevel());
                            }

                            int currentUses = itemMeta.getPersistentDataContainer().getOrDefault(usageKey, PersistentDataType.INTEGER, MAX_USES);

                            e.cancel();
                            if (currentUses == 1) {
                                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                                item.setAmount(0);
                            }
                            else {
                                itemMeta.getPersistentDataContainer().set(usageKey, PersistentDataType.INTEGER, --currentUses);
                                itemLore.set(4, ChatColor.translateAlternateColorCodes('&', "&e" + currentUses + ' ' + (currentUses > 1 ? "Uses" : "Use") + " &7left"));
                                itemMeta.setLore(itemLore);
                                item.setItemMeta(itemMeta);
                            }
                            return;
                        }
                        else {
                            SlimefunPlugin.getLocal().sendMessage(p, "messages.no-pvp", true);
                        }
                    }
                }
                else {
                    SlimefunPlugin.getLocal().sendMessage(p, "messages.hungry", true);
                }

                return;
            }
        };
    }

}
