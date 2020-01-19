package me.mrCookieSlime.Slimefun.Objects.SlimefunItem.machines.electric.gps;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.chat.json.ChatComponent;
import io.github.thebusybiscuit.cscorelib2.chat.json.ClickEvent;
import io.github.thebusybiscuit.cscorelib2.chat.json.ClickEventAction;
import io.github.thebusybiscuit.cscorelib2.chat.json.CustomBookInterface;
import io.github.thebusybiscuit.cscorelib2.chat.json.HoverEvent;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunBlockHandler;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SimpleSlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.UnregisterReason;
import me.mrCookieSlime.Slimefun.Objects.handlers.ItemInteractionHandler;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;

public class ElevatorPlate extends SimpleSlimefunItem<ItemInteractionHandler> {
	
	private final Set<UUID> users = new HashSet<>();

	public ElevatorPlate(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
		super(category, item, recipeType, recipe, recipeOutput);
		
		SlimefunItem.registerBlockHandler(getID(), new SlimefunBlockHandler() {

			@Override
			public void onPlace(Player p, Block b, SlimefunItem item) {
				BlockStorage.addBlockInfo(b, "floor", "&rFloor #0");
				BlockStorage.addBlockInfo(b, "owner", p.getUniqueId().toString());
			}

			@Override
			public boolean onBreak(Player p, Block b, SlimefunItem item, UnregisterReason reason) {
				return true;
			}
		});
	}
	
	public Set<UUID> getUsers() {
		return users;
	}
	
	@Override
	protected boolean areItemHandlersPrivate() {
		return false;
	}

	@Override
	public ItemInteractionHandler getItemHandler() {
		return (e, p, item) -> {
			Block b = e.getClickedBlock();
			if (b == null) return false;
			
			String id = BlockStorage.checkID(b);
			if (id == null || !id.equals(getID())) return false;

			if (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(p.getUniqueId().toString())) {
				openEditor(p, b);
			}
			
			return true;
		};
	}
	
	public List<Block> getFloors(Block b) {
		List<Block> floors = new LinkedList<>();
		
		for (int y = b.getWorld().getMaxHeight(); y > 0; y--) {
			if (y == b.getY()) {
				floors.add(b);
				continue;
			}
			
			Block block = b.getWorld().getBlockAt(b.getX(), y, b.getZ());
			
			if (block.getType() == getItem().getType() && BlockStorage.check(block, getID())) {
				floors.add(block);
			}
		}
		
		return floors;
	}
	
	public void open(Player p, Block b) {
		if (users.remove(p.getUniqueId())) {
			return;
		}
		
		CustomBookInterface book = new CustomBookInterface(SlimefunPlugin.instance);
		ChatComponent page = null;
		
		List<Block> floors = getFloors(b);
		
		if (floors.size() < 2) {
			SlimefunPlugin.getLocal().sendMessage(p, "machines.ELEVATOR.no-destinations", true);
		}
		
		for (int i = 0; i < floors.size(); i++) {
			if (i % 10 == 0) {
				if (page != null) book.addPage(page);
				
				page = new ChatComponent(ChatColors.color(SlimefunPlugin.getLocal().getMessage(p, "machines.ELEVATOR.pick-a-floor")) + "\n");
			}
			
			Block block = floors.get(i);
			String floor = ChatColors.color(BlockStorage.getLocationInfo(block.getLocation(), "floor"));
			ChatComponent line;
			
			if (block.getY() == b.getY()) {
				line = new ChatComponent("\n" + ChatColor.GRAY + "> " + (floors.size() - i) + ". " + ChatColor.RESET + floor);
				line.setHoverEvent(new HoverEvent(ChatColors.color(SlimefunPlugin.getLocal().getMessage(p, "machines.ELEVATOR.current-floor")), ChatColor.RESET + floor, ""));
			}
			else {
				line = new ChatComponent("\n" + ChatColor.GRAY.toString() + (floors.size() - i) + ". " + ChatColor.RESET + floor);
				line.setHoverEvent(new HoverEvent(ChatColors.color(SlimefunPlugin.getLocal().getMessage(p, "machines.ELEVATOR.click-to-teleport")), ChatColor.RESET + floor, ""));
				line.setClickEvent(new ClickEvent(ClickEventAction.RUN_COMMAND, "/sf elevator " + block.getX() + ' ' + block.getY() + ' ' + block.getZ() + " "));
			}
			
			page.append(line);
		}
		
		book.addPage(page);
		book.open(p);
	}
	
	public void openEditor(Player p, Block b) {
		ChestMenu menu = new ChestMenu("Elevator Settings");
		
		menu.addItem(4, new CustomItem(Material.NAME_TAG, "&7Floor Name &e(Click to edit)", "", "&r" + ChatColors.color(BlockStorage.getLocationInfo(b.getLocation(), "floor"))));
		menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
			pl.closeInventory();
			pl.sendMessage("");
			SlimefunPlugin.getLocal().sendMessage(p, "machines.ELEVATOR.enter-name");
			pl.sendMessage("");
			
			ChatUtils.awaitInput(pl, message -> {
				BlockStorage.addBlockInfo(b, "floor", message.replace(ChatColor.COLOR_CHAR, '&'));
				
				pl.sendMessage("");
				SlimefunPlugin.getLocal().sendMessage(p, "machines.ELEVATOR.named", msg -> msg.replace("%floor%", message));
				pl.sendMessage("");
				
				openEditor(pl, b);
			});
			
			return false;
		});
		
		menu.open(p);
	}

}
