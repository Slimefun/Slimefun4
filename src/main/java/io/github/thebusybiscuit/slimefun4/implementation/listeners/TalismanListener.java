package io.github.thebusybiscuit.slimefun4.implementation.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class TalismanListener implements Listener {
	
	public TalismanListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onDamageGet(EntityDamageEvent e) {
		if (!e.isCancelled()) {
			if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player && ThreadLocalRandom.current().nextInt(100) < 45 && SlimefunManager.isItemSimilar(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInMainHand(), SlimefunItems.BLADE_OF_VAMPIRES, true)) {
				((Player) ((EntityDamageByEntityEvent) e).getDamager()).playSound(((EntityDamageByEntityEvent) e).getDamager().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7F, 0.7F);
				((Player) ((EntityDamageByEntityEvent) e).getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
			}
			
			if (e.getEntity() instanceof Player) {
				if (e.getCause() == DamageCause.LAVA) Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_LAVA);
				if (e.getCause() == DamageCause.DROWNING) Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_WATER);
				if (e.getCause() == DamageCause.FALL) Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_ANGEL);
				if (e.getCause() == DamageCause.FIRE) Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_FIRE);
				if (e.getCause() == DamageCause.ENTITY_ATTACK) Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_WARRIOR);
				if (e.getCause() == DamageCause.ENTITY_ATTACK) Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_KNIGHT);
				if (e.getCause() == DamageCause.PROJECTILE && Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_WHIRLWIND) && ((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile) {
					Vector direction = ((Player) e.getEntity()).getEyeLocation().getDirection().multiply(2.0);
					Projectile projectile = (Projectile) e.getEntity().getWorld().spawnEntity(((LivingEntity) e.getEntity()).getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), ((EntityDamageByEntityEvent) e).getDamager().getType());
					projectile.setVelocity(direction);
					((EntityDamageByEntityEvent) e).getDamager().remove();
				}
			}
		}
	}
	
	private final int[] armorSlots = {39, 38, 37, 36};
	
	@EventHandler
	public void onItemBreak(PlayerItemBreakEvent e) {
		if (Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_ANVIL)) {
			PlayerInventory inv = e.getPlayer().getInventory();
			int slot = inv.getHeldItemSlot();
			
			// Did the tool in our hand broke or was it an Armorpiece?
			if (!inv.getItem(inv.getHeldItemSlot()).equals(e.getBrokenItem())) {
				for (int s : armorSlots) {
					if (inv.getItem(s).equals(e.getBrokenItem())) {
						slot = s;
						break;
					}
				}
			}
			
			ItemStack item = e.getBrokenItem().clone();
			ItemMeta meta = item.getItemMeta();
			
			if (meta instanceof Damageable) {
				((Damageable) meta).setDamage(0);
			}
			
			item.setItemMeta(meta);
			
			int itemSlot = slot;
			Slimefun.runSync(() -> inv.setItem(itemSlot, item), 1L);
		}
	}
	
	@EventHandler
	public void onSprint(PlayerToggleSprintEvent e) {
		if (e.isSprinting()) {
			Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_TRAVELLER);
		}
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		Random random = ThreadLocalRandom.current();
		
		if (Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_MAGICIAN)) {
			List<String> enchantments = new ArrayList<>();
			for (Enchantment en : Enchantment.values()) {
				for (int i = 1; i <= en.getMaxLevel(); i++) {
					if ((boolean) Slimefun.getItemValue("MAGICIAN_TALISMAN", "allow-enchantments." + en.getKey().getKey() + ".level." + i) && en.canEnchantItem(e.getItem())) {
						enchantments.add(en.getKey().getKey() + '-' + i);
					}
				}
			}
			String enchant = enchantments.get(random.nextInt(enchantments.size()));
			e.getEnchantsToAdd().put(Enchantment.getByKey(NamespacedKey.minecraft(enchant.split("-")[0])), Integer.parseInt(enchant.split("-")[1]));
		}
		
		if (!e.getEnchantsToAdd().containsKey(Enchantment.SILK_TOUCH) && Enchantment.LOOT_BONUS_BLOCKS.canEnchantItem(e.getItem()) && Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_WIZARD)) {
			if (e.getEnchantsToAdd().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) e.getEnchantsToAdd().remove(Enchantment.LOOT_BONUS_BLOCKS);
			Set<Enchantment> enchantments = e.getEnchantsToAdd().keySet();
			for (Enchantment en : enchantments) {
				if (random.nextInt(100) < 40) e.getEnchantsToAdd().put(en, random.nextInt(3) + 1);
			}
			e.getItem().addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, random.nextInt(3) + 3);
		}
	}

	/**
	 *
	 * @param e BlockBreakEvent
	 * @since 4.2.0
	 */
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Collection<ItemStack> drops = new ArrayList<>();
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		int fortune = 1;
		Random random = ThreadLocalRandom.current();
		
		if (item.getType() != Material.AIR && item.getAmount() > 0) {
			if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) && !item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
				fortune = random.nextInt(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2) - 1;
				if (fortune <= 0) fortune = 1;
				fortune = (e.getBlock().getType() == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1) * (fortune + 1);
			}

			if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH) && e.getBlock().getType().toString().endsWith("_ORE") && Talisman.checkFor(e, (SlimefunItemStack) SlimefunItems.TALISMAN_MINER)) {
				if (drops.isEmpty()) {
					drops = e.getBlock().getDrops();
				}
				
				for (ItemStack drop : new ArrayList<>(drops)) {
					if (!drop.getType().isBlock()) {
						drops.add(new CustomItem(drop, fortune * 2));
					}
				}
			}
		}
	}
}
