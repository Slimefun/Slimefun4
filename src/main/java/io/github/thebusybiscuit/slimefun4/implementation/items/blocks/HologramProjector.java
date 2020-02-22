package io.github.thebusybiscuit.slimefun4.implementation.items.blocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.math.DoubleHandler;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.holograms.HologramProjectorHologram;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockUseHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class HologramProjector extends SimpleSlimefunItem<BlockUseHandler> {

	public HologramProjector(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		super(category, item, recipeType, recipe, recipeOutput);
		
		SlimefunItem.registerBlockHandler(getID(), new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "text", "右键投影仪编辑文本");
				BlockStorage.addBlockInfo(b, "offset", "0.5");
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());

				HologramProjectorHologram.getArmorStand(b, true);
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				HologramProjectorHologram.remove(b);
				return true;
			}
		});
	}
	
	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}

	@Override
	public BlockUseHandler getItemHandler() {
		return e -> {
			e.cancel();
			
			Player p = e.getPlayer();
			Block b = e.getClickedBlock().get();
			
			if (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString())) {
				openEditor(p, b);
			}
		};
	}

	private void openEditor(Player p, Block projector) {
		ChestMenu menu = new ChestMenu("Hologram Settings");
		
		menu.addItem(0, new CustomItem(Material.NAME_TAG, "&7文本 &e(单击编辑)", "", "&r" + ChatColors.color(BlockStorage.getLocationInfo(projector.getLocation(), "text"))));
		menu.addMenuClickHandler(0, (pl, slot, item, action) -> {
			pl.closeInventory();
			SlimefunPlugin.getLocal().sendMessage(pl, "machines.HOLOGRAM_PROJECTOR.enter-text", true);
			
			ChatUtils.awaitInput(pl, message -> {
				ArmorStand hologram = HologramProjectorHologram.getArmorStand(projector, true);
				hologram.setCustomName(ChatColors.color(message));
				BlockStorage.addBlockInfo(projector, "text", hologram.getCustomName());
				openEditor(pl, projector);
			});
			return false;
		});
		
		menu.addItem(1, new CustomItem(new ItemStack(Material.CLOCK), "&7文本高度: &e" + DoubleHandler.fixDouble(Double.valueOf(BlockStorage.getLocationInfo(projector.getLocation(), "offset")) + 1.0D), "", "&r左键: &7+0.1", "&r右键: &7-0.1"));
		menu.addMenuClickHandler(1, (pl, slot, item, action) -> {
			double offset = DoubleHandler.fixDouble(Double.valueOf(BlockStorage.getLocationInfo(projector.getLocation(), "offset")) + (action.isRightClicked() ? -0.1F : 0.1F));
			ArmorStand hologram = HologramProjectorHologram.getArmorStand(projector, true);
			Location l = new Location(projector.getWorld(), projector.getX() + 0.5, projector.getY() + offset, projector.getZ() + 0.5);
			hologram.teleport(l);
			
			BlockStorage.addBlockInfo(projector, "offset", String.valueOf(offset));
			openEditor(pl, projector);
			return false;
		});
		
		menu.open(p);
	}
}
