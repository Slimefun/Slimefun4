package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHelper;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.Item.CustomItem;
import me.mrCookieSlime.CSCoreLibPlugin.general.World.CustomSkull;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Setup.Messages;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenu;
import me.mrCookieSlime.Slimefun.api.inventory.BlockMenuPreset;
import me.mrCookieSlime.Slimefun.api.item_transport.CargoNet;
import me.mrCookieSlime.Slimefun.api.item_transport.ItemTransportFlow;

public class AdvancedCargoOutputNode extends SlimefunItem {

	private static final int[] border = {0, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 17, 18, 22, 23, 24, 26, 27, 31, 32, 33, 34, 35, 36, 40, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};

	public AdvancedCargoOutputNode(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		super(category, item, name, recipeType, recipe, recipeOutput);
		
		new BlockMenuPreset(name, "&cOutput Node") {
			
			@Override
			public void init() {
				constructMenu(this);
			}

			@Override
			public void newInstance(final BlockMenu menu, final Block b) {
				try {
					if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "filter-type") == null || BlockStorage.getLocationInfo(b.getLocation(), "filter-type").equals("whitelist")) {
						menu.replaceExistingItem(15, new CustomItem(new ItemStack(Material.WHITE_WOOL), "&7Type: &rWhitelist", "", "&e> Click to change it to Blacklist"));
						menu.addMenuClickHandler(15, (p, slot, item, action) -> {
							BlockStorage.addBlockInfo(b, "filter-type", "blacklist");
							newInstance(menu, b);
							return false;
						});
					}
					else {
						menu.replaceExistingItem(15, new CustomItem(new ItemStack(Material.BLACK_WOOL), "&7Type: &8Blacklist", "", "&e> Click to change it to Whitelist"));
						menu.addMenuClickHandler(15, (p, slot, item, action) -> {
							BlockStorage.addBlockInfo(b, "filter-type", "whitelist");
							newInstance(menu, b);
							return false;
						});
					}
					
					if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "filter-durability") == null || BlockStorage.getLocationInfo(b.getLocation(), "filter-durability").equals("false")) {
						menu.replaceExistingItem(16, new CustomItem(new ItemStack(Material.STONE_SWORD, (byte) 20), "&7Include Sub-IDs/Durability: &4\u2718", "", "&e> Click to toggle whether the Durability has to match"));
						menu.addMenuClickHandler(16, (p, slot, item, action) -> {
							BlockStorage.addBlockInfo(b, "filter-durability", "true");
							newInstance(menu, b);
							return false;
						});
					}
					else {
						menu.replaceExistingItem(16, new CustomItem(new ItemStack(Material.GOLDEN_SWORD, (byte) 20), "&7Include Sub-IDs/Durability: &2\u2714", "", "&e> Click to toggle whether the Durability has to match"));
						menu.addMenuClickHandler(16, (p, slot, item, action) -> {
							BlockStorage.addBlockInfo(b, "filter-durability", "false");
							newInstance(menu, b);
							return false;
						});
					}
					
					if (!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "filter-lore") == null || BlockStorage.getLocationInfo(b.getLocation(), "filter-lore").equals("true")) {
						menu.replaceExistingItem(25, new CustomItem(new ItemStack(Material.MAP), "&7Include Lore: &2\u2714", "", "&e> Click to toggle whether the Lore has to match"));
						menu.addMenuClickHandler(25, (p, slot, item, action) -> {
							BlockStorage.addBlockInfo(b, "filter-lore", "false");
							newInstance(menu, b);
							return false;
						});
					}
					else {
						menu.replaceExistingItem(25, new CustomItem(new ItemStack(Material.MAP), "&7Include Lore: &4\u2718", "", "&e> Click to toggle whether the Lore has to match"));
						menu.addMenuClickHandler(25, (p, slot, item, action) -> {
							BlockStorage.addBlockInfo(b, "filter-lore", "true");
							newInstance(menu, b);
							return false;
						});
					}

					menu.replaceExistingItem(41, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ=="), "&bChannel", "", "&e> Click to decrease the Channel ID by 1"));
					menu.addMenuClickHandler(41, (p, slot, item, action) -> {
						int channel = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "frequency")) - 1;
						if (channel < 0) {
							if (CargoNet.EXTRA_CHANNELS) channel = 16;
							else channel = 15;
						}
						BlockStorage.addBlockInfo(b, "frequency", String.valueOf(channel));
						newInstance(menu, b);
						return false;
					});

					int channel = ((!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "frequency") == null) ? 0: (Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "frequency"))));

					if (channel == 16) {
						menu.replaceExistingItem(42, new CustomItem(SlimefunItems.CHEST_TERMINAL, "&bChannel ID: &3" + (channel + 1)));
						menu.addMenuClickHandler(42,
							(p, slot, item, action) -> false
						);
					}
					else {
						menu.replaceExistingItem(42, new CustomItem(new ItemStack(MaterialHelper.WoolColours[channel]), "&bChannel ID: &3" + (channel + 1)));
						menu.addMenuClickHandler(42,
							(p, slot, item, action) -> false
						);
					}

					menu.replaceExistingItem(43, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJmOTEwYzQ3ZGEwNDJlNGFhMjhhZjZjYzgxY2Y0OGFjNmNhZjM3ZGFiMzVmODhkYjk5M2FjY2I5ZGZlNTE2In19fQ=="), "&bChannel", "", "&e> Click to increase the Channel ID by 1"));
					menu.addMenuClickHandler(43, (p, slot, item, action) -> {
						int channeln = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "frequency")) + 1;

						if (CargoNet.EXTRA_CHANNELS) {
							if (channeln > 16) channeln = 0;
						}
						else {
							if (channeln > 15) channeln = 0;
						}

						BlockStorage.addBlockInfo(b, "frequency", String.valueOf(channeln));
						newInstance(menu, b);
						return false;
					});
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public boolean canOpen(Block b, Player p) {
				boolean open = CSCoreLib.getLib().getProtectionManager().canAccessChest(p.getUniqueId(), b) || p.hasPermission("slimefun.cargo.bypass");
				if (!open) {
					Messages.local.sendTranslation(p, "inventory.no-access", true);
				}
				return open;
			}

			@Override
			public int[] getSlotsAccessedByItemTransport(ItemTransportFlow flow) {
				return new int[0];
			}
		};
		
		registerBlockHandler(name, new SlimefunBlockHandler() {
			
			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
				BlockStorage.addBlockInfo(b, "index", "0");
				BlockStorage.addBlockInfo(b, "frequency", "0");
				BlockStorage.addBlockInfo(b, "filter-type", "whitelist");
				BlockStorage.addBlockInfo(b, "filter-lore", "true");
				BlockStorage.addBlockInfo(b, "filter-durability", "false");
			}
			
			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				BlockMenu inv = BlockStorage.getInventory(b);
				if (inv != null) {
					for (int slot: getInputSlots()) {
						if (inv.getItemInSlot(slot) != null) {
							b.getWorld().dropItemNaturally(b.getLocation(), inv.getItemInSlot(slot));
							inv.replaceExistingItem(slot, null);
						}
					}
				}
				return true;
			}
		});
	}
	
	protected void constructMenu(BlockMenuPreset preset) {
		for (int i : border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
				(p, slot, item, action) -> false
			);
		}

		preset.addItem(2, new CustomItem(new ItemStack(Material.PAPER), "&3Items", "", "&bPut in all Items you want to", "&bblacklist/whitelist"),
			(p, slot, item, action) -> false
		);
	}
	
	public int[] getInputSlots() {
		return new int[] {19, 20, 21, 28, 29, 30, 37, 38, 39};
	}

}
