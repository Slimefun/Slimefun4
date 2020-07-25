package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.entity.Item;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.NotPlaceable;
import io.github.thebusybiscuit.slimefun4.core.attributes.RandomMobDrop;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;


public class PiglinBarterListener implements Listener {

    /**
     * Listens to the EntityDropItemEvent event to inject a {@link RandomMobDrop} if its dropChance() check passes.
     * this would only be possible if the {@link RecipeType} is of PiglinBarter type. This listener will only ever
     * be enabled if the server version is 1.16 or above.
     * 
     * @author dNiym
     * 
     * @see getBarterDrops
     * @see RandomMobDrop
     * 
     */
    private static boolean hasPiglins = false;

    public PiglinBarterListener(SlimefunPlugin plugin) {
        
        try {
            Class.forName("org.bukkit.entity.Piglin");
            hasPiglins = true;
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
        } catch (ClassNotFoundException ignored) { }
            
    }

    @EventHandler
    public void onPiglinDropItem(EntityDropItemEvent e) {
        
        if(e.getEntity() instanceof Piglin) {
            
            Set<ItemStack> drops = SlimefunPlugin.getRegistry().getBarterDrops(); 
            
            /*
             * NOTE:  Getting a new random number each iteration because multiple items could have the same
             * % chance to drop, and if one fails all items with that number will fail.  Getting a new random number
             * will allow multiple items with the same % chance to drop.
             */
            
            for(ItemStack is: drops) {
                SlimefunItem sfi = SlimefunItem.getByItem(is);
                if(sfi instanceof RandomMobDrop && ((RandomMobDrop)sfi).getMobDropChance() >= ThreadLocalRandom.current().nextInt(100)) {
                    Item drop = e.getEntity().getWorld().dropItemNaturally(((Piglin)e.getEntity()).getEyeLocation(), sfi.getItem());
                    drop.setVelocity(e.getItemDrop().getVelocity());
                    e.getItemDrop().remove();
                    return;
                } 
                
            }
        }
        
    }
    
    public static boolean hasPiglins() {
        return hasPiglins;
    }
}
