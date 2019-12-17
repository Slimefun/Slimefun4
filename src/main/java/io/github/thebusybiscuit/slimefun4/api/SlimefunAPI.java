package io.github.thebusybiscuit.slimefun4.api;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.blocks.SlimefunBlock;
import io.github.thebusybiscuit.slimefun4.api.items.ItemRestriction;
import io.github.thebusybiscuit.slimefun4.core.SlimefunWorld;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.api.PlayerProfile;

/**
 * This class will hold the declarations of important API methods for Slimefun.
 * Normally these methods will not be implemented in this interface, but some
 * methods will have a default implementation for convenience.
 * 
 * @author TheBusyBiscuit
 *
 */
public interface SlimefunAPI {
	
	/**
	 * This method will register the given restriction.
	 * Calling this should directly influence the outcome of {@link SlimefunAPI#getItemRestrictions()}.
	 * 
	 * @param restriction	The restriction to register
	 */
	void addItemRestriction(ItemRestriction restriction);
	
	/**
	 * This method will return a {@link Set} of all instances of {@link ItemRestriction} that will directly
	 * affect the outcome of {@link SlimefunAPI#isAllowedToUse(Player, ItemStack, boolean)}
	 * 
	 * @return	The Set of all registered item restrictions
	 */
	Set<ItemRestriction> getItemRestrictions();
	
	/**
	 * This method will return whether a Player is allowed to use the given {@link ItemStack}.
	 * If warningMessages is set to true, the Player will be informed about the outcome of this method.
	 * 
	 * Internally this method will loop through {@link SlimefunAPI#getItemRestrictions()} and perform
	 * checks using all available instances of {@link ItemRestriction}.
	 * 
	 * Do NOT warn Players about a restriction if this method returns false.
	 * Warnings should be exclusively handled via {@link ItemRestriction#warnPlayer(PlayerProfile, Player, SlimefunItem, ItemStack)}
	 * 
	 * @param p					The Player to perform the check on
	 * @param item				The Item to perform the check on
	 * @param sendWarnings		Whether to warn the Player about not being able to use the given item
	 * @return					Whether the Player is allowed to use the given item
	 */
	default boolean isAllowedToUse(Player p, ItemStack item, boolean sendWarnings) {
		PlayerProfile profile = PlayerProfile.get(p);
		SlimefunItem sfItem = SlimefunItem.getByItem(item);
		
		for (ItemRestriction restriction : getItemRestrictions()) {
			if (!restriction.isAllowed(profile, p, sfItem, item)) {
				if (sendWarnings) restriction.warnPlayer(profile, p, sfItem, item);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * This method returns a {@link Set} of all registered instances of {@link Category}.
	 * 
	 * @return	A Set of all Categories
	 */
	Set<Category> getCategories();
	
	/**
	 * This method returns A {@link Set} of all registered instances of {@link SlimefunItem}.
	 * Its default implementation generates a new Set based on all items found in the categories
	 * returned by {@link SlimefunAPI#getCategories()}
	 * 
	 * @return	A Set of all SlimefunItems
	 */
	default Set<SlimefunItem> getItems() {
		return getCategories().stream().flatMap(cat -> cat.getItems().stream()).collect(Collectors.toSet());
	}
	
	Optional<SlimefunItem> fromItemStack(ItemStack item);
	
	Optional<SlimefunBlock> fromBlock();
	
	SlimefunWorld fromWorld(World world);
	
	PlayerProfile fromPlayer(OfflinePlayer player);
	
}
