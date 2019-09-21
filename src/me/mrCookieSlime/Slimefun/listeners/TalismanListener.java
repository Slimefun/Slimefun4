package me.mrCookieSlime.Slimefun.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.Talisman;
import me.mrCookieSlime.Slimefun.Setup.SlimefunManager;
import me.mrCookieSlime.Slimefun.api.Slimefun;

public class TalismanListener implements Listener {
	
	private Random random = new Random();
	
	public TalismanListener(SlimefunPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onDamageGet(EntityDamageEvent e) {
		if (!e.isCancelled()) {
			if (e instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) e).getDamager() instanceof Player && random.nextInt(100) < 45 && SlimefunManager.isItemSimiliar(((Player) ((EntityDamageByEntityEvent) e).getDamager()).getInventory().getItemInMainHand(), SlimefunItem.getItem("BLADE_OF_VAMPIRES"), true)) {
				((Player) ((EntityDamageByEntityEvent) e).getDamager()).playSound(((EntityDamageByEntityEvent) e).getDamager().getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 0.7F, 0.7F);
				((Player) ((EntityDamageByEntityEvent) e).getDamager()).addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
			}
			
			if (e.getEntity() instanceof Player) {
				if (e.getCause() == DamageCause.LAVA) Talisman.checkFor(e, SlimefunItem.getByID("LAVA_TALISMAN"));
				if (e.getCause() == DamageCause.DROWNING) Talisman.checkFor(e, SlimefunItem.getByID("WATER_TALISMAN"));
				if (e.getCause() == DamageCause.FALL) Talisman.checkFor(e, SlimefunItem.getByID("ANGEL_TALISMAN"));
				if (e.getCause() == DamageCause.FIRE) Talisman.checkFor(e, SlimefunItem.getByID("FIRE_TALISMAN"));
				if (e.getCause() == DamageCause.ENTITY_ATTACK) Talisman.checkFor(e, SlimefunItem.getByID("WARRIOR_TALISMAN"));
				if (e.getCause() == DamageCause.ENTITY_ATTACK) Talisman.checkFor(e, SlimefunItem.getByID("KNIGHT_TALISMAN"));
				if (e.getCause() == DamageCause.PROJECTILE && Talisman.checkFor(e, SlimefunItem.getByID("WHIRLWIND_TALISMAN")) && ((EntityDamageByEntityEvent) e).getDamager() instanceof Projectile) {
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
		if (Talisman.checkFor(e, SlimefunItem.getByID("ANVIL_TALISMAN"))) {
			PlayerInventory inv = e.getPlayer().getInventory();
			int slot = inv.getHeldItemSlot();
			
			// Did the tool in our hand broke or was it an Armorpiece?
			if (!inv.getItem(inv.getHeldItemSlot()).equals(e.getBrokenItem())) {
				for (int s: armorSlots) {
					if (inv.getItem(s).equals(e.getBrokenItem())) {
						slot = s;
						break;
					}
				}
			}
			
			final ItemStack item = e.getBrokenItem().clone();
			ItemMeta meta = item.getItemMeta();
			
			if (meta instanceof Damageable) {
				((Damageable) meta).setDamage(0);
			}
			
			item.setItemMeta(meta);
			
			final int itemSlot = slot;
			SlimefunPlugin.instance.getServer().getScheduler().runTaskLater(SlimefunPlugin.instance, () -> inv.setItem(itemSlot, item), 1L);
		}
	}
	
	@EventHandler
	public void onSprint(PlayerToggleSprintEvent e) {
		if (e.isSprinting()) Talisman.checkFor(e, SlimefunItem.getByID("TRAVELLER_TALISMAN"));
	}
	
	@EventHandler
	public void onEnchant(EnchantItemEvent e) {
		if (Talisman.checkFor(e, SlimefunItem.getByID("MAGICIAN_TALISMAN"))) {
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
		if (!e.getEnchantsToAdd().containsKey(Enchantment.SILK_TOUCH) && Enchantment.LOOT_BONUS_BLOCKS.canEnchantItem(e.getItem()) && Talisman.checkFor(e, SlimefunItem.getByID("WIZARD_TALISMAN"))) {
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
		List<ItemStack> drops = new ArrayList<>();
		ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
		int fortune = 1;

		if (item.getType() != Material.AIR && item.getAmount() > 0) {
			if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS) && !item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {
				fortune = random.nextInt(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2) - 1;
				if (fortune <= 0) fortune = 1;
				fortune = (e.getBlock().getType() == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1) * (fortune + 1);
			}

			if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH) && e.getBlock().getType().toString().endsWith("_ORE") && Talisman.checkFor(e, SlimefunItem.getByID("MINER_TALISMAN"))) {
				if (drops.isEmpty()) drops = (List<ItemStack>) e.getBlock().getDrops();
				for (ItemStack drop : new ArrayList<>(drops)) {
					if (!drop.getType().isBlock()) drops.add(new CustomItem(drop, fortune * 2));
				}
			}
		}
	}
}
