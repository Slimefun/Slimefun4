package io.github.thebusybiscuit.slimefun4.implementation.items.magical.talismans;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import io.github.thebusybiscuit.cscorelib2.inventory.ItemUtils;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class Talisman extends SlimefunItem {

    protected static final Category TALISMANS_CATEGORY = new Category(new NamespacedKey(SlimefunPlugin.instance(), "talismans"), new CustomItem(SlimefunItems.COMMON_TALISMAN, "&7Talismans - &aTier I"), 2);

    private final SlimefunItemStack enderTalisman;

    protected final String suffix;
    protected final boolean consumable;
    protected final boolean cancel;
    protected final PotionEffect[] effects;
    protected final int chance;

    @ParametersAreNonnullByDefault
    public Talisman(SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, @Nullable String messageSuffix, PotionEffect... effects) {
        this(item, recipe, consumable, cancelEvent, messageSuffix, 100, effects);
    }

    @ParametersAreNonnullByDefault
    public Talisman(SlimefunItemStack item, ItemStack[] recipe, @Nullable String messageSuffix, int chance, PotionEffect... effects) {
        this(item, recipe, true, true, messageSuffix, chance, effects);
    }

    @ParametersAreNonnullByDefault
    public Talisman(SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, @Nullable String messageSuffix, int chance, PotionEffect... effects) {
        this(TALISMANS_CATEGORY, item, recipe, consumable, cancelEvent, messageSuffix, chance, effects);
    }

    @ParametersAreNonnullByDefault
    protected Talisman(Category category, SlimefunItemStack item, ItemStack[] recipe, boolean consumable, boolean cancelEvent, @Nullable String messageSuffix, int chance, PotionEffect... effects) {
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

            enderTalisman = new SlimefunItemStack("ENDER_" + getId(), getItem().getType(), name, lore.toArray(new String[0]));
        } else {
            enderTalisman = null;
        }
    }

    /**
     * This returns whether the {@link Talisman} will be consumed upon use.
     * 
     * @return Whether this {@link Talisman} is consumed on use.
     */
    public boolean isConsumable() {
        return consumable;
    }

    /**
     * This returns the chance of this {@link Talisman} activating.
     * The chance will be between 1 and 100.
     * 
     * @return The chance of this {@link Talisman} activating.
     */
    public int getChance() {
        return chance;
    }

    @Nonnull
    public PotionEffect[] getEffects() {
        return effects;
    }

    protected String getMessageSuffix() {
        return suffix;
    }

    protected boolean isEventCancelled() {
        return cancel;
    }

    @Nullable
    private SlimefunItemStack getEnderVariant() {
        return enderTalisman;
    }

    @Override
    public void postRegister() {
        EnderTalisman talisman = new EnderTalisman(this, getEnderVariant());
        talisman.register(getAddon());
    }

    @Override
    public void load() {
        super.load();
        loadEnderTalisman();
    }

    void loadEnderTalisman() {
        EnderTalisman talisman = (EnderTalisman) SlimefunItem.getByItem(getEnderVariant());
        Optional<Research> research = Research.getResearch(new NamespacedKey(SlimefunPlugin.instance(), "ender_talismans"));

        if (talisman != null && research.isPresent()) {
            talisman.setResearch(research.get());
        }
    }

    private static boolean hasMessage(@Nonnull Talisman talisman) {
        return talisman.getMessageSuffix() != null;
    }

    public static boolean tryActivate(@Nonnull Event e, @Nonnull SlimefunItemStack stack) {
        return tryActivateAndGet(e, stack.getItem()) != null;
    }

    public static boolean tryActivate(@Nonnull Event e, @Nonnull SlimefunItem item) {
        return tryActivateAndGet(e, item) != null;
    }

    public static ItemStack tryActivateAndGet(@Nonnull Event e, @Nonnull SlimefunItemStack stack) {
        return tryActivateAndGet(e, stack.getItem());
    }

    @Nullable
    public static ItemStack tryActivateAndGet(@Nonnull Event e, @Nonnull SlimefunItem item) {
        if (!(item instanceof Talisman)) {
            return null;
        }

        Talisman talisman = (Talisman) item;
        if (ThreadLocalRandom.current().nextInt(100) > talisman.getChance()) {
            return null;
        }

        Player p = getPlayerByEventType(e);
        if (p == null || !pass(p, talisman)) {
            return null;
        }

        ItemStack possibleTalisman = retrieveTalismanFromInventory(p.getInventory(), talisman.getItem());

        if (possibleTalisman != null && Slimefun.hasUnlocked(p, talisman, true)) {
            activateTalisman(e, p, p.getInventory(), talisman, possibleTalisman);
            return possibleTalisman;
        }

        possibleTalisman = retrieveTalismanFromInventory(p.getEnderChest(), talisman.getEnderVariant());

        if (possibleTalisman != null && Slimefun.hasUnlocked(p, talisman, true)) {
            activateTalisman(e, p, p.getEnderChest(), talisman, possibleTalisman);
            return possibleTalisman;
        }

        return possibleTalisman;
    }

    @Nullable
    private static ItemStack retrieveTalismanFromInventory(@Nonnull Inventory inv, @Nonnull ItemStack talismanItem) {
        for (ItemStack item : inv) {
            if (SlimefunUtils.isItemSimilar(item, talismanItem, false, false) {
                return item;
            }
        }

        return null;
    }

    @ParametersAreNonnullByDefault
    private static void activateTalisman(Event e, Player p, Inventory inv, Talisman talisman, ItemStack talismanItem) {
        consumeItem(inv, talisman, talismanItem);
        applyTalismanEffects(p, talisman);
        cancelEvent(e, talisman);
        sendMessage(p, talisman);
    }

    @ParametersAreNonnullByDefault
    private static void applyTalismanEffects(Player p, Talisman talisman) {
        for (PotionEffect effect : talisman.getEffects()) {
            p.addPotionEffect(effect);
        }
    }

    @ParametersAreNonnullByDefault
    private static void cancelEvent(Event e, Talisman talisman) {
        if (e instanceof Cancellable && talisman.isEventCancelled()) {
            ((Cancellable) e).setCancelled(true);
        }
    }

    @ParametersAreNonnullByDefault
    private static void sendMessage(Player p, Talisman talisman) {
        if (hasMessage(talisman)) {
            SlimefunPlugin.getLocalization().sendMessage(p, "messages.talisman." + talisman.getMessageSuffix(), true);
        }
    }

    @ParametersAreNonnullByDefault
    private static void consumeItem(Inventory inv, Talisman talisman, ItemStack talismanItem) {
        if (talisman.isConsumable()) {
            ItemUtils.consumeItem(talismanItem, false);
        }
    }

    @Nullable
    private static Player getPlayerByEventType(@Nonnull Event e) {
        if (e instanceof PlayerDeathEvent) {
            return ((PlayerDeathEvent) e).getEntity();
        } else if (e instanceof EntityDeathEvent) {
            return ((EntityDeathEvent) e).getEntity().getKiller();
        } else if (e instanceof BlockBreakEvent) {
            return ((BlockBreakEvent) e).getPlayer();
        } else if (e instanceof BlockDropItemEvent) {
            return ((BlockDropItemEvent) e).getPlayer();
        } else if (e instanceof PlayerEvent) {
            return ((PlayerEvent) e).getPlayer();
        } else if (e instanceof EntityEvent) {
            return (Player) ((EntityEvent) e).getEntity();
        } else if (e instanceof EnchantItemEvent) {
            return ((EnchantItemEvent) e).getEnchanter();
        }

        return null;
    }

    private static boolean pass(@Nonnull Player p, @Nonnull SlimefunItem talisman) {
        for (PotionEffect effect : ((Talisman) talisman).getEffects()) {
            if (effect != null && p.hasPotionEffect(effect.getType())) {
                return false;
            }
        }

        return true;
    }
}
