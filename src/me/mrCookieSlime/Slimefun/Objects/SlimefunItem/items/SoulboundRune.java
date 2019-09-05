package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemDropHandler;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SoulboundRune extends SimpleSlimefunItem<ItemDropHandler> {

    public SoulboundRune(Category category, ItemStack item, String id, RecipeType type, ItemStack[] recipe) {
        super(category, item, id, type, recipe);
    }

    @Override
    public ItemDropHandler getItemHandler() {
        return (e, p, i) -> {
            ItemStack item = i.getItemStack();
            if (SlimefunManager.isItemSimiliar(item, SlimefunItems.RUNE_SOULBOUND, true)) {
                if (!Slimefun.hasUnlocked(p, SlimefunItems.RUNE_SOULBOUND, false)) {
                    Messages.local.sendTranslation(p, "messages.not-researched", true);
                    return true;
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
                    // Being sure the entity is still valid and not picked up or whatsoever.
                    if (!i.isValid()) return;

                    Location l = i.getLocation();
                    Collection<Entity> entites = l.getWorld().getNearbyEntities(l, 1.5, 1.5, 1.5,
                            entity -> entity.getType() == EntityType.DROPPED_ITEM && entity instanceof Item &&
                                    !SlimefunManager.isItemSimiliar(((Item) entity).getItemStack(), SlimefunItems.RUNE_SOULBOUND, true) &&
                                    !SlimefunManager.isItemSoulbound(((Item) entity).getItemStack())
                    );
                    if (entites.size() < 1) return;

                    ItemStack ench;
                    Item ent;
                    // Collections do not have a #get method so we need to use a for loop.
                    // We do not use streams for foreach loops as they are more resource consuming.
                    Entity entity = entites.stream().findFirst().get();
                    ench = ((Item) entity).getItemStack();
                    ent = (Item) entity;

                    if (ench.getAmount() == 1) {
                        e.setCancelled(true);

                        ItemMeta enchMeta = ench.getItemMeta();

                        List<String> lore;
                        if (enchMeta.hasLore()) lore = enchMeta.getLore();
                        else lore = new ArrayList<>();

                        // This lightning is just an effect, it deals no damage.
                        l.getWorld().strikeLightningEffect(l);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {

                            // Being sure entities are still valid and not picked up or whatsoever.
                            if (i.isValid() && ent.isValid()) {

                                l.getWorld().createExplosion(l, 0.0F);
                                l.getWorld().playSound(l, Sound.ENTITY_GENERIC_EXPLODE, 0.3F, 1F);

                                lore.add(ChatColor.GRAY + "Soulbound");

                                enchMeta.setLore(lore);
                                ench.setItemMeta(enchMeta);

                                ent.remove();
                                i.remove();
                                l.getWorld().dropItemNaturally(l, ench);

                                Messages.local.sendTranslation(p, "messages.soulbound-rune.success", true);
                            }
                        }, 10L);
                    } else {
                        Messages.local.sendTranslation(p, "messages.soulbound-rune.fail", true);
                    }
                }, 20L);
                return true;
            }
            return false;
        };
    }

}
