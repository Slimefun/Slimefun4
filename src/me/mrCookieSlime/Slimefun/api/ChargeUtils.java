package me.mrCookieSlime.Slimefun.api;


@Deprecated
public class ChargeUtils {
	
//	public static double chargeInventory(Player p, double adding, boolean includehand) {
//		ItemStack chest = p.getInventory().getChestplate();
//		ItemStack pants = p.getInventory().getLeggings();
//		ItemStack boots = p.getInventory().getBoots();
//		ItemStack hand = includehand ? p.getInventory().getItemInMainHand(): null;
//		
//		double rest = 0.00;
//		
//		if (SlimefunManager.isChargable(SlimefunItem.getByItem(chest), false))  {
//			double charge = Double.valueOf(ChatColor.stripColor(chest.getItemMeta().getLore().get(1)).replace("Charge: ", "").replace(" J", ""));
//			double capacity = Double.valueOf(ChatColor.stripColor(chest.getItemMeta().getLore().get(2)).replace("Capacity: ", "").replace(" J", ""));
//			double newcharge = Double.valueOf(new DecimalFormat("##.##").format(charge + adding).replace(",", "."));
//			if (newcharge > capacity && charge < capacity) {
//				newcharge = capacity;
//				rest = rest + (newcharge - capacity);
//			}
//			if (charge < capacity) {
//				ItemMeta im = chest.getItemMeta();
//				List<String> lore = im.getLore();
//				lore.set(1, ChatColor.GRAY + "Charge: " + ChatColor.AQUA + newcharge + " J");
//				im.setLore(lore);
//				chest.setItemMeta(im);
//				p.getInventory().setChestplate(chest);
//			}
//		}
//		if (SlimefunManager.isChargable(SlimefunItem.getByItem(pants), false))  {
//			double charge = Double.valueOf(ChatColor.stripColor(pants.getItemMeta().getLore().get(1)).replace("Charge: ", "").replace(" J", ""));
//			double capacity = Double.valueOf(ChatColor.stripColor(pants.getItemMeta().getLore().get(2)).replace("Capacity: ", "").replace(" J", ""));
//			double newcharge = Double.valueOf(new DecimalFormat("##.##").format(charge + adding).replace(",", "."));
//			if (newcharge > capacity && charge < capacity) {
//				newcharge = capacity;
//				rest = rest + (newcharge - capacity);
//			}
//			if (charge < capacity) {
//				ItemMeta im = pants.getItemMeta();
//				List<String> lore = im.getLore();
//				lore.set(1, ChatColor.GRAY + "Charge: " + ChatColor.AQUA + newcharge + " J");
//				im.setLore(lore);
//				pants.setItemMeta(im);
//				p.getInventory().setLeggings(pants);
//			}
//		}
//		if (SlimefunManager.isChargable(SlimefunItem.getByItem(boots), false))  {
//			double charge = Double.valueOf(ChatColor.stripColor(boots.getItemMeta().getLore().get(1)).replace("Charge: ", "").replace(" J", ""));
//			double capacity = Double.valueOf(ChatColor.stripColor(boots.getItemMeta().getLore().get(2)).replace("Capacity: ", "").replace(" J", ""));
//			double newcharge = Double.valueOf(new DecimalFormat("##.##").format(charge + adding).replace(",", "."));
//			if (newcharge > capacity && charge < capacity) {
//				newcharge = capacity;
//				rest = rest + (newcharge - capacity);
//			}
//			if (charge < capacity) {
//				ItemMeta im = boots.getItemMeta();
//				List<String> lore = im.getLore();
//				lore.set(1, ChatColor.GRAY + "Charge: " + ChatColor.AQUA + newcharge + " J");
//				im.setLore(lore);
//				boots.setItemMeta(im);
//				p.getInventory().setBoots(boots);
//			}
//		}
//		if (SlimefunManager.isChargable(SlimefunItem.getByItem(hand), false))  {
//			double charge = Double.valueOf(ChatColor.stripColor(hand.getItemMeta().getLore().get(1)).replace("Charge: ", "").replace(" J", ""));
//			double capacity = Double.valueOf(ChatColor.stripColor(hand.getItemMeta().getLore().get(2)).replace("Capacity: ", "").replace(" J", ""));
//			double newcharge = Double.valueOf(new DecimalFormat("##.##").format(charge + adding).replace(",", "."));
//			if (newcharge > capacity && charge < capacity) {
//				newcharge = capacity;
//				rest = rest + (newcharge - capacity);
//			}
//			if (charge < capacity) {
//				ItemMeta im = hand.getItemMeta();
//				List<String> lore = im.getLore();
//				lore.set(1, ChatColor.GRAY + "Charge: " + ChatColor.AQUA + newcharge + " J");
//				im.setLore(lore);
//				hand.setItemMeta(im);
//				p.getInventory().setItemInMainHand(hand);
//			}
//		}
//		return rest;
//	}
//	
//	@SuppressWarnings("deprecation")
//	public static double chargeArmor(Player p, double adding) {
//		ItemStack chest = p.getInventory().getChestplate();
//		ItemStack pants = p.getInventory().getLeggings();
//		ItemStack boots = p.getInventory().getBoots();
//		
//		if (adding > 0.0 && SlimefunManager.isChargable(SlimefunItem.getByItem(chest), false))  {
//			double charge = Double.valueOf(ChatColor.stripColor(chest.getItemMeta().getLore().get(1)).replace("Charge: ", "").replace(" J", ""));
//			double capacity = Double.valueOf(ChatColor.stripColor(chest.getItemMeta().getLore().get(2)).replace("Capacity: ", "").replace(" J", ""));
//			double newcharge = Double.valueOf(new DecimalFormat("##.##").format(charge + adding).replace(",", "."));
//			if (newcharge > capacity && charge < capacity) {
//				newcharge = capacity;
//				adding = Double.valueOf(new DecimalFormat("##.##").format(adding + (newcharge - capacity)).replace(",", "."));
//			}
//			if (charge < capacity) {
//				ItemMeta im = chest.getItemMeta();
//				List<String> lore = im.getLore();
//				lore.set(1, ChatColor.GRAY + "Charge: " + ChatColor.AQUA + newcharge + " J");
//				im.setLore(lore);
//				chest.setItemMeta(im);
//				p.getInventory().setChestplate(chest);
//				if (newcharge < capacity) adding = 0.00;
//				else if (Double.compare(newcharge, capacity) == 0) adding = Double.valueOf(new DecimalFormat("##.##").format(adding - newcharge).replace(",", "."));
//				p.getWorld().playSound(p.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
//				if (SlimefunItem.getByItem(chest) instanceof DamagableChargableItem) Messages.local.sendTranslation(p, "messages.battery.add", true, new Variable("%charge%", String.valueOf(Calculator.fixDouble(newcharge - charge, 2))), new Variable("%machine%", ((DamagableChargableItem) SlimefunItem.getByItem(chest)).getChargeType()));
//				else if (SlimefunItem.getByItem(chest) instanceof ChargableItem) Messages.local.sendTranslation(p, "messages.battery.add", true, new Variable("%charge%", String.valueOf(Calculator.fixDouble(newcharge - charge, 2))), new Variable("%machine%", ((ChargableItem) SlimefunItem.getByItem(chest)).getChargeType()));
//			}
//		}
//		if (adding > 0.0 && SlimefunManager.isChargable(SlimefunItem.getByItem(pants), false))  {
//			double charge = Double.valueOf(ChatColor.stripColor(pants.getItemMeta().getLore().get(1)).replace("Charge: ", "").replace(" J", ""));
//			double capacity = Double.valueOf(ChatColor.stripColor(pants.getItemMeta().getLore().get(2)).replace("Capacity: ", "").replace(" J", ""));
//			double newcharge = Double.valueOf(new DecimalFormat("##.##").format(charge + adding).replace(",", "."));
//			if (newcharge > capacity && charge < capacity) {
//				newcharge = capacity;
//				adding = Double.valueOf(new DecimalFormat("##.##").format(adding + (newcharge - capacity)).replace(",", "."));
//			}
//			if (charge < capacity) {
//				ItemMeta im = pants.getItemMeta();
//				List<String> lore = im.getLore();
//				lore.set(1, ChatColor.GRAY + "Charge: " + ChatColor.AQUA + newcharge + " J");
//				im.setLore(lore);
//				pants.setItemMeta(im);
//				p.getInventory().setLeggings(pants);
//				if (newcharge < capacity) adding = 0.00;
//				else if (Double.compare(newcharge, capacity) == 0) adding = Double.valueOf(new DecimalFormat("##.##").format(adding - newcharge).replace(",", "."));
//				if (SlimefunItem.getByItem(pants) instanceof DamagableChargableItem) Messages.local.sendTranslation(p, "messages.battery.add", true, new Variable("%charge%", String.valueOf(Calculator.fixDouble(newcharge - charge, 2))), new Variable("%machine%", ((DamagableChargableItem) SlimefunItem.getByItem(pants)).getChargeType()));
//				else if (SlimefunItem.getByItem(pants) instanceof ChargableItem) Messages.local.sendTranslation(p, "messages.battery.add", true, new Variable("%charge%", String.valueOf(Calculator.fixDouble(newcharge - charge, 2))), new Variable("%machine%", ((ChargableItem) SlimefunItem.getByItem(pants)).getChargeType()));
//			}
//		}
//		if (adding > 0.0 && SlimefunManager.isChargable(SlimefunItem.getByItem(boots), false))  {
//			double charge = Double.valueOf(ChatColor.stripColor(boots.getItemMeta().getLore().get(1)).replace("Charge: ", "").replace(" J", ""));
//			double capacity = Double.valueOf(ChatColor.stripColor(boots.getItemMeta().getLore().get(2)).replace("Capacity: ", "").replace(" J", ""));
//			double newcharge = Double.valueOf(new DecimalFormat("##.##").format(charge + adding).replace(",", "."));
//			if (newcharge > capacity && charge < capacity) {
//				newcharge = capacity;
//				adding = Double.valueOf(new DecimalFormat("##.##").format(adding + (newcharge - capacity)).replace(",", "."));
//			}
//			if (charge < capacity) {
//				ItemMeta im = boots.getItemMeta();
//				List<String> lore = im.getLore();
//				lore.set(1, ChatColor.GRAY + "Charge: " + ChatColor.AQUA + newcharge + " J");
//				im.setLore(lore);
//				boots.setItemMeta(im);
//				p.getInventory().setBoots(boots);
//				if (newcharge < capacity) adding = 0.00;
//				else if (Double.compare(newcharge, capacity) == 0) adding = Double.valueOf(new DecimalFormat("##.##").format(adding - newcharge).replace(",", "."));
//				if (SlimefunItem.getByItem(boots) instanceof DamagableChargableItem) Messages.local.sendTranslation(p, "messages.battery.add", true, new Variable("%charge%", String.valueOf(Calculator.fixDouble(newcharge - charge, 2))), new Variable("%machine%", ((DamagableChargableItem) SlimefunItem.getByItem(boots)).getChargeType()));
//				else if (SlimefunItem.getByItem(boots) instanceof ChargableItem) Messages.local.sendTranslation(p, "messages.battery.add", true, new Variable("%charge%", String.valueOf(Calculator.fixDouble(newcharge - charge, 2))), new Variable("%machine%", ((ChargableItem) SlimefunItem.getByItem(boots)).getChargeType()));
//			}
//		}
//		if (adding < 0.0) adding = 0.00;
//		return adding;
//	}
//
//	public static Charge getCharge(ItemStack item) {
//		if (!SlimefunManager.isChargable(SlimefunItem.getByItem(item), false)) return null;
//		return new Charge(
//				Double.valueOf(ChatColor.stripColor(item.getItemMeta().getLore().get(1)).replace("Charge: ", "").replace(" J", "")), 
//				Double.valueOf(ChatColor.stripColor(item.getItemMeta().getLore().get(2)).replace("Capacity: ", "").replace(" J", "")));
//	}
//	
//	public static ItemStack updateCharge(ItemStack item, double increment) {
//		ItemMeta im = item.getItemMeta();
//		Charge charge = getCharge(item);
//		if (charge == null) return item;
//		double newcharge = Double.valueOf(new DecimalFormat("##.##").format(charge.getStoredEnergy() + increment).replace(",", "."));
//		if (newcharge > charge.getCapacity() && charge.getStoredEnergy() < charge.getCapacity()) newcharge = charge.getCapacity();
//		if (charge.getStoredEnergy() < charge.getCapacity()) {
//			List<String> lore = im.getLore();
//			lore.set(1, ChatColor.GRAY + "Charge: " + ChatColor.AQUA + newcharge + " J");
//			im.setLore(lore);
//			item.setItemMeta(im);
//			return item;
//		}
//		return item;
//	}
}
