package me.mrCookieSlime.Slimefun.Objects.SlimefunItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.Categories;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.Research;
import me.mrCookieSlime.Slimefun.api.Slimefun;

/**
 * @since 4.0
 */
public class Talisman extends SlimefunItem {

    protected String suffix;
    protected boolean consumable = true;
    protected boolean cancel = true;
    protected PotionEffect[] effects;
    protected int chance = 100;

    public Talisman(ItemStack item, String id, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, PotionEffect... effects) {
        super(Categories.TALISMANS_1, item, id, RecipeType.MAGIC_WORKBENCH, recipe, new CustomItem(item, consumable ? 4 : 1));
        this.consumable = consumable;
        this.cancel = cancelEvent;
        this.suffix = messageSuffix;
        this.effects = effects;
    }

    public Talisman(ItemStack item, String id, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, int chance, PotionEffect... effects) {
        super(Categories.TALISMANS_1, item, id, RecipeType.MAGIC_WORKBENCH, recipe, new CustomItem(item, consumable ? 4 : 1));
        this.consumable = consumable;
        this.cancel = cancelEvent;
        this.suffix = messageSuffix;
        this.effects = effects;
        this.chance = chance;
    }

    public Talisman(ItemStack item, String id, ItemStack[] recipe, String messageSuffix, int chance, PotionEffect... effects) {
        super(Categories.TALISMANS_1, item, id, RecipeType.MAGIC_WORKBENCH, recipe);
        this.suffix = messageSuffix;
        this.effects = effects;
        this.chance = chance;
    }

    protected Talisman(Category category, ItemStack item, String id, ItemStack[] recipe) {
        super(category, item, id, RecipeType.MAGIC_WORKBENCH, recipe);
    }

    public String getSuffix() {
        return this.suffix;
    }

    public boolean isConsumable() {
        return this.consumable;
    }

    public boolean isEventCancelled() {
        return this.cancel;
    }

    public PotionEffect[] getEffects() {
        return this.effects;
    }

    public int getChance() {
        return this.chance;
    }

    public ItemStack upgrade() {
        List<String> lore = new ArrayList<>();
        lore.add("&7&oEnder Infused");
        lore.add("");
        for (String line : getItem().getItemMeta().getLore()) {
            lore.add(line);
        }
        return new CustomItem(getItem().getType(), "&5Ender " + ChatColor.stripColor(getItem().getItemMeta().getDisplayName()), lore.toArray(new String[lore.size()]));
    }

    @Override
    public void create() {
        EnderTalisman talisman = new EnderTalisman(this);
        talisman.register(!isAddonItem());
    }

    @Override
    public void install() {
        EnderTalisman talisman = (EnderTalisman) SlimefunItem.getByItem(upgrade());
        Research research = Research.getByID(112);
        if (talisman != null) {
            Slimefun.addOfficialWikiPage(talisman.getID(), "Talismans");
            if (research != null) talisman.bindToResearch(research);
        }

        Slimefun.addOfficialWikiPage(getID(), "Talismans");
    }

    private static boolean isTalismanMessage(Talisman talisman){
        return !("").equalsIgnoreCase(talisman.getSuffix());
    }
    
    public static boolean checkFor(Event e, SlimefunItem item) {
        if (!(item instanceof Talisman)) {
            return false;
        }

        Talisman talisman = (Talisman) item;
        if (new Random().nextInt(100) < talisman.getChance()) {
            return false;
        }

        Player p = getPlayerByEventType(e);
        if (p == null || !pass(p, talisman)) {
            return false;
        }

        if (p.getInventory().containsAtLeast(talisman.getItem(), 1)) {
            if (Slimefun.hasUnlocked(p, talisman.getItem(), true)) {
                executeTalismanAttributes(e,p,talisman);
                return true;
            } 
            else return false;
        } 
        else if (p.getEnderChest().containsAtLeast(talisman.upgrade(), 1)) {
            if (Slimefun.hasUnlocked(p, talisman.upgrade(), true)) {
                executeTalismanAttributes(e,p,talisman);
                return true;
            } 
            else return false;
        } 
        else return false;
    }

    private static void executeTalismanAttributes(Event e, Player p, Talisman talisman){
        consumeItem(p,talisman);
        applyTalismanEffects(p,talisman);
        cancelEvent(e,talisman);
        sendMessage(p,talisman);
    }

    private static void applyTalismanEffects(Player p, Talisman talisman){
        for (PotionEffect effect : talisman.getEffects()) {
            p.addPotionEffect(effect);
        }
    }

    private static void cancelEvent(Event e, Talisman talisman){
        if (e instanceof Cancellable && talisman.isEventCancelled())
            ((Cancellable) e).setCancelled(true);
    }

    private static void sendMessage(Player p, Talisman talisman){
        if (isTalismanMessage(talisman))
            SlimefunPlugin.getLocal().sendMessage(p, "messages.talisman." + talisman.getSuffix(), true);
    }

    private static void consumeItem(Player p, Talisman talisman){
        if (talisman.isConsumable())
            p.getInventory().removeItem(talisman.getItem());
    }

    private static Player getPlayerByEventType(Event e) {
        if (e instanceof EntityDeathEvent) return ((EntityDeathEvent) e).getEntity().getKiller();
        else if (e instanceof BlockBreakEvent) return ((BlockBreakEvent) e).getPlayer();
        else if (e instanceof PlayerEvent) return ((PlayerEvent) e).getPlayer();
        else if (e instanceof EntityEvent) return (Player) ((EntityEvent) e).getEntity();
        else if (e instanceof EnchantItemEvent) return ((EnchantItemEvent) e).getEnchanter();
        
        return null;
    }

    private static boolean pass(Player p, SlimefunItem talisman) {
        for (PotionEffect effect : ((Talisman) talisman).getEffects()) {
            if (effect != null && p.hasPotionEffect(effect.getType())) return false;
        }
        
        return true;
    }

}
