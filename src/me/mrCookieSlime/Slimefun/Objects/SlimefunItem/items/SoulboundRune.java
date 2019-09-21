package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemDropHandler;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class SoulboundRune extends SimpleSlimefunItem<ItemDropHandler> {

    public SoulboundRune(Category category, ItemStack item, String id, RecipeType type, ItemStack[] recipe) {
        super(category, item, id, type, recipe);
    }

    @Override
    public ItemDropHandler getItemHandler() {
        return (e, p, i) -> {
            ItemStack item = i.getItemStack();
            if (SlimefunManager.isItemSimiliar(item, SlimefunItems.RUNE_SOULBOUND, true)) {
                
            	if (!Slimefun.hasUnlocked(p, SlimefunItems.RUNE_SOULBOUND, true)) {
                	return true;
                }
            	
            	Bukkit.getScheduler().scheduleSyncDelayedTask(SlimefunPlugin.instance, () -> {
                    // Being sure the entity is still valid and not picked up or whatsoever.
                    if (!i.isValid()) return;

                    Location l = i.getLocation();
                    Collection<Entity> entites = l.getWorld().getNearbyEntities(l, 1.5, 1.5, 1.5,
                            entity -> entity instanceof Item && !SlimefunManager.isItemSoulbound(((Item) entity).getItemStack()) &&
                                    !SlimefunManager.isItemSimiliar(((Item) entity).getItemStack(), SlimefunItems.RUNE_SOULBOUND, true)
                    );
                    
                    if (entites.isEmpty()) return;

                    Entity entity = entites.stream().findFirst().get();
                    ItemStack ench = ((Item) entity).getItemStack();
                    Item ent = (Item) entity;

                    if (ench.getAmount() == 1) {
                        e.setCancelled(true);

                        ItemMeta enchMeta = ench.getItemMeta();
                        List<String> lore = enchMeta.hasLore() ? enchMeta.getLore(): new ArrayList<>();

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

                                SlimefunPlugin.getLocal().sendMessage(p, "messages.soulbound-rune.success", true);
                            }
                        }, 10L);
                    } 
                    else {
                        SlimefunPlugin.getLocal().sendMessage(p, "messages.soulbound-rune.fail", true);
                    }
                }, 20L);
                
                return true;
            }
            return false;
        };
    }

}
