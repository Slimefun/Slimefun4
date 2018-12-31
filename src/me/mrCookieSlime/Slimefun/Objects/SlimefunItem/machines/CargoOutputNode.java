package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines;

import me.mrCookieSlime.CSCoreLibPlugin.compatibility.MaterialHelper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.mrCookieSlime.CSCoreLibPlugin.CSCoreLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu.MenuClickHandler;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ClickAction;
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

public class CargoOutputNode extends SlimefunItem {
	
	private static final int[] border = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};


	public CargoOutputNode(Category category, ItemStack item, String name, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		super(category, item, name, recipeType, recipe, recipeOutput);
		
		new BlockMenuPreset(name, "&6Output Node") {
			
			@Override
			public void init() {
				constructMenu(this);
			}

			@Override
			public void newInstance(final BlockMenu menu, final Block b) {
				try {

					menu.replaceExistingItem(12, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI1OTliZDk4NjY1OWI4Y2UyYzQ5ODg1MjVjOTRlMTlkZGQzOWZhZDA4YTM4Mjg0YTE5N2YxYjcwNjc1YWNjIn19fQ=="), "&bChannel", "", "&e> Click to decrease the Channel ID by 1"));
					menu.addMenuClickHandler(12, new MenuClickHandler() {

						@Override
						public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
							int channel = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "frequency")) - 1;
							if (channel < 0) {
								if (CargoNet.EXTRA_CHANNELS) channel = 16;
								else channel = 15;
							}
							BlockStorage.addBlockInfo(b, "frequency", String.valueOf(channel));
							newInstance(menu, b);
							return false;
						}
					});
					
					int channel = ((!BlockStorage.hasBlockInfo(b) || BlockStorage.getLocationInfo(b.getLocation(), "frequency") == null) ? 0: (Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "frequency"))));

					if (channel == 16) {
						menu.replaceExistingItem(13, new CustomItem(SlimefunItems.CHEST_TERMINAL, "&bChannel ID: &3" + (channel + 1)));
						menu.addMenuClickHandler(13, new MenuClickHandler() {

							@Override
							public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
								return false;
							}
						});
					}
					else {
						menu.replaceExistingItem(13, new CustomItem(new ItemStack(MaterialHelper.WoolColours[channel]), "&bChannel ID: &3" + (channel + 1)));
						menu.addMenuClickHandler(13, new MenuClickHandler() {

							@Override
							public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
								return false;
							}
						});
					}

					menu.replaceExistingItem(14, new CustomItem(CustomSkull.getItem("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzJmOTEwYzQ3ZGEwNDJlNGFhMjhhZjZjYzgxY2Y0OGFjNmNhZjM3ZGFiMzVmODhkYjk5M2FjY2I5ZGZlNTE2In19fQ=="), "&bChannel", "", "&e> Click to increase the Channel ID by 1"));
					menu.addMenuClickHandler(14, new MenuClickHandler() {

						@Override
						public boolean onClick(Player p, int arg1, ItemStack arg2, ClickAction arg3) {
							int channel = Integer.parseInt(BlockStorage.getLocationInfo(b.getLocation(), "frequency")) + 1;

							if (CargoNet.EXTRA_CHANNELS) {
								if (channel > 16) channel = 0;
							}
							else {
								if (channel > 15) channel = 0;
							}
							
							BlockStorage.addBlockInfo(b, "frequency", String.valueOf(channel));
							newInstance(menu, b);
							return false;
						}
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
				BlockStorage.addBlockInfo(b, "frequency", "0");
			}
			
			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				return true;
			}
		});
	}
	
	protected void constructMenu(BlockMenuPreset preset) {
		for (int i: border) {
			preset.addItem(i, new CustomItem(new ItemStack(Material.CYAN_STAINED_GLASS_PANE), " "),
			new MenuClickHandler() {

				@Override
				public boolean onClick(Player arg0, int arg1, ItemStack arg2, ClickAction arg3) {
					return false;
				}
				
			});
		}
	}
}
