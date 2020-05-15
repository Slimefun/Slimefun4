package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Talisman extends SlimefunItem {

    protected static final Category TALISMANS_CATEGORY = new Category(new NamespacedKey(SlimefunPlugin.instance, "talismans"), new CustomItem(SlimefunItems.TALISMAN, "&7Talismans - &aTier I"), 2);

    private final SlimefunItemStack enderTalisman;

    protected final String suffix;
    protected final boolean consumable;
    protected final boolean cancel;
    protected final PotionEffect[] effects;
    protected final int chance;

    public Talisman(SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, PotionEffect... effects) {
        this(item, recipe, consumable, cancelEvent, messageSuffix, 100, effects);
    }

    public Talisman(SlimefunItemStack item, ItemStack[] recipe, String messageSuffix, int chance, PotionEffect... effects) {
        this(item, recipe, true, true, messageSuffix, chance, effects);
    }

    public Talisman(SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, int chance, PotionEffect... effects) {
        this(TALISMANS_CATEGORY, item, recipe, consumable, cancelEvent, messageSuffix, chance, effects);
    }

    protected Talisman(Category category, SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, String messageSuffix, int chance, PotionEffect... effects) {
        super(category, item, RecipeType.MAGIC_WORKBENCH, recipe, new CustomItem(item, consumable ? 4 : 1));

        this.consumable = consumable;
        this.cancel = cancelEvent;
        this.suffix = messageSuffix;
        this.effects = effects;
        this.chance = chance;

        if (!(this instanceof EnderTalisman)) {
            String name = "&5Ender " + ChatColor.stripColor(getItem().getItemMeta().getDisplayName());
            List<String> lore = new ArrayList<>();
            lore.add("&7&oEnder Infused");
            lore.add("");

            for (String line : getItem().getItemMeta().getLore()) {
                lore.add(line);
            }

            enderTalisman = new SlimefunItemStack("ENDER_" + getID(), getItem().getType(), name, lore.toArray(new String[0]));
        }
        else {
            enderTalisman = null;
        }
    }

    public boolean isConsumable() {
        return consumable;
    }

    public int getChance() {
        return chance;
    }

    public PotionEffect[] getEffects() {
        return effects;
    }

    protected String getMessageSuffix() {
        return suffix;
    }

    protected boolean isEventCancelled() {
        return cancel;
    }

    private SlimefunItemStack getEnderVariant() {
        return enderTalisman;
    }

    @Override
    public void postRegister() {
        EnderTalisman talisman = new EnderTalisman(this, getEnderVariant());
        talisman.register(addon);
    }

    @Override
    public void load() {
        super.load();
        createEnderTalisman();
    }

    protected void createEnderTalisman() {
        EnderTalisman talisman = (EnderTalisman) SlimefunItem.getByItem(getEnderVariant());
        Optional<Research> research = Research.getResearch(new NamespacedKey(SlimefunPlugin.instance, "ender_talismans"));

        if (talisman != null && research.isPresent()) {
            talisman.setResearch(research.get());
        }
    }

    private static boolean hasMessage(Talisman talisman) {
        return !("").equalsIgnoreCase(talisman.getMessageSuffix());
    }

    public static boolean checkFor(Event e, SlimefunItemStack stack) {
        SlimefunItem item = SlimefunItem.getByItem(stack);
        return checkFor(e, item);
    }

    public static boolean checkFor(Event e, SlimefunItem item) {
        if (!(item instanceof Talisman)) {
            return false;
        }

        Talisman talisman = (Talisman) item;
        if (ThreadLocalRandom.current().nextInt(100) > talisman.getChance()) {
            return false;
        }

        Player p = getPlayerByEventType(e);
        if (p == null || !pass(p, talisman)) {
            return false;
        }

        ItemStack talismanItem = talisman.getItem();

        if (p.getInventory().containsAtLeast(talismanItem, 1)) {
            if (Slimefun.hasUnlocked(p, talismanItem, true)) {
                activateTalisman(e, p, p.getInventory(), talisman, talismanItem);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            ItemStack enderTalisman = talisman.getEnderVariant();

            if (p.getEnderChest().containsAtLeast(enderTalisman, 1)) {
                if (Slimefun.hasUnlocked(p, enderTalisman, true)) {
                    activateTalisman(e, p, p.getEnderChest(), talisman, enderTalisman);
                    return true;
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }
        }
    }

    private static void activateTalisman(Event e, Player p, Inventory inv, Talisman talisman, ItemStack talismanItem) {
        consumeItem(inv, talisman, talismanItem);
        applyTalismanEffects(p, talisman);
        cancelEvent(e, talisman);
        sendMessage(p, talisman);

    }

    private static void applyTalismanEffects(Player p, Talisman talisman) {
        for (PotionEffect effect : talisman.getEffects()) {
            p.addPotionEffect(effect);
        }
    }

    private static void cancelEvent(Event e, Talisman talisman) {
        if (e instanceof Cancellable && talisman.isEventCancelled()) {
            ((Cancellable) e).setCancelled(true);
        }
    }

    private static void sendMessage(Player p, Talisman talisman) {
        if (hasMessage(talisman)) {
            SlimefunPlugin.getLocal().sendMessage(p, "messages.talisman." + talisman.getMessageSuffix(), true);
        }
    }

    private static void consumeItem(Inventory inv, Talisman talisman, ItemStack talismanItem) {
        if (talisman.isConsumable()) {
            inv.removeItem(talismanItem);
        }
    }

    private static Player getPlayerByEventType(Event e) {
        if (e instanceof EntityDeathEvent) {
            return ((EntityDeathEvent) e).getEntity().getKiller();
        }
        else if (e instanceof BlockBreakEvent) {
            return ((BlockBreakEvent) e).getPlayer();
        }
        else if (e instanceof PlayerEvent) {
            return ((PlayerEvent) e).getPlayer();
        }
        else if (e instanceof EntityEvent) {
            return (Player) ((EntityEvent) e).getEntity();
        }
        else if (e instanceof EnchantItemEvent) {
            return ((EnchantItemEvent) e).getEnchanter();
        }

        return null;
    }

    private static boolean pass(Player p, SlimefunItem talisman) {
        for (PotionEffect effect : ((Talisman) talisman).getEffects()) {
            if (effect != null && p.hasPotionEffect(effect.getType())) {
                return false;
            }
        }

        return true;
    }

}
