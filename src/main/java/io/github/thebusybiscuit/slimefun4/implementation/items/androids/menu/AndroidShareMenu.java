package io.github.thebusybiscuit.slimefun4.implementation.items.androids.menu;

import io.github.thebusybiscuit.cscorelib2.inventory.ChestMenu;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.skull.SkullItem;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.androids.ProgrammableAndroid;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import io.github.thebusybiscuit.slimefun4.utils.HeadTexture;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

import org.apache.commons.lang.Validate;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * The {@link AndroidShareMenu} is responsibility to handle trusted user using the {@link
 * ProgrammableAndroid}
 *
 * @author StarWishsama
 * @see ProgrammableAndroid
 */
public final class AndroidShareMenu {
	private static final int startSlot = 9;
	private static final int perPageDisplay = 45;
	private static final String key = "share-users";

	private AndroidShareMenu() {}

	/**
	 * Open a share menu for player.
	 *
	 * @param p player
	 * @param b android
	 * @param page page
	 */
	public static void openShareMenu(@Nonnull Player p, @Nonnull Block b, int page) {
		Validate.notNull(p, "The player cannot be null!");
		Validate.notNull(b, "The android block cannot be null!");
		Validate.isTrue(page >= 0, "The page must be above or equals 0!");

		ChestMenu menu = new ChestMenu(SlimefunPlugin.instance(), SlimefunPlugin.getLocalization().getMessage("android.access-manager.title"));

		menu.setEmptySlotsClickable(false);

		List<String> users = getTrustedUsers(b);

		int pages = Math.max(0, users.size() / 36);

		// Draw background start
		for (int i = 0; i < 9; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (pl, slot, item, cursor, action) -> false);
		}

		for (int i = 45; i < 54; i++) {
			menu.addItem(i, new CustomItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE), " "));
			menu.addMenuClickHandler(i, (pl, slot, item, cursor, action) -> false);
		}
		// Draw background end

		// Add trusted player slot
		menu.addItem(0, new CustomItem(HeadTexture.SCRIPT_UP.getAsItemStack(), SlimefunPlugin.getLocalization().getMessage("android.access-manager.menu.add-player-title"), SlimefunPlugin.getLocalization().getMessage("android.access-manager.menu.add-player")));
		menu.addMenuClickHandler(
				0,
				(p1, slot, item, cursor, action) -> {
					p1.closeInventory();

					SlimefunPlugin.getLocalization().sendMessage(p1, "android.access-manager.messages.input");

					ChatUtils.awaitInput(
							p1,
							message -> {
								Player target = Bukkit.getPlayerExact(message);

								if (target == null) {
									SlimefunPlugin.getLocalization().sendMessage(p1, "android.access-manager.messages.cannot-find-player", msg -> msg.replace("%player%", p1.getName()));
								} else {
									addPlayer(p1, target, b, users);
								}
							});
					return false;
				});

		// Display added trusted player(s)
		if (!users.isEmpty()) {
			List<String> displayUsers = users.subList(page, Math.min(users.size() - 1, perPageDisplay));

			for (int index = 0; index < displayUsers.size(); index++) {
				int slot = index + startSlot;
				OfflinePlayer current = Bukkit.getOfflinePlayer(UUID.fromString(displayUsers.get(index)));
				menu.addItem(
						slot,
						new CustomItem(SkullItem.fromPlayer(current), "&b" + current.getName(), SlimefunPlugin.getLocalization().getMessage("android.access-manager.menu.delete-player")));
				menu.addMenuClickHandler(
						slot,
						(p1, slot1, item, cursor, action) -> {
							if (action.isLeftClick()) {
								removePlayer(p1, current, b, users);
							}

							return false;
						});
			}
		}

		if (pages > 0) {
			menu.addItem(47, ChestMenuUtils.getPreviousButton(p, page, pages));
			menu.addMenuClickHandler(
					46,
					(pl, slot, item, cursor, action) -> {
						int next = page - 1;
						if (next < 1) next = pages;
						if (next != page) {
							openShareMenu(p, b, next);
						}
						return false;
					});

			menu.addItem(51, ChestMenuUtils.getNextButton(p, page, pages));
			menu.addMenuClickHandler(
					50,
					(pl, slot, item, cursor, action) -> {
						int next = page + 1;
						if (next > pages) {
							next = 1;
						}
						if (next != page) {
							openShareMenu(p, b, next);
						}
						return false;
					});
		}

		menu.open(p);
	}

	private static void addPlayer(
			@Nonnull Player owner,
			@Nonnull OfflinePlayer p,
			@Nonnull Block android,
			@Nonnull List<String> users) {
		Validate.notNull(owner, "The android cannot be null!");
		Validate.notNull(p, "The target player cannot be null!");
		Validate.notNull(android, "The android block cannot be null!");
		Validate.notNull(users, "The trusted users list cannot be null!");

		if (users.contains(p.getUniqueId().toString())) {
			SlimefunPlugin.getLocalization().sendMessage(owner, "android.access-manager.messages.is-trusted-player", msg -> msg.replace("%player%", p.getName()));
		} else if (owner.getUniqueId() == p.getUniqueId()) {
			SlimefunPlugin.getLocalization().sendMessage(owner, "android.access-manager.messages.cannot-add-yourself");
		} else {
			users.add(p.getUniqueId().toString());
			SlimefunPlugin.getLocalization().sendMessage(owner, "android.access-manager.messages.add-success", msg -> msg.replace("%player%", p.getName()));

			BlockStorage.addBlockInfo(android, key, users.toString());
		}
	}

	private static void removePlayer(
			@Nonnull Player owner,
			@Nonnull OfflinePlayer p,
			@Nonnull Block android,
			@Nonnull List<String> users) {
		Validate.notNull(owner, "The android cannot be null!");
		Validate.notNull(p, "The target player cannot be null!");
		Validate.notNull(android, "The android block cannot be null!");
		Validate.notNull(users, "The trusted users list cannot be null!");

		if (users.contains(p.getUniqueId().toString())) {
			users.remove(p.getUniqueId().toString());
			SlimefunPlugin.getLocalization().sendMessage(owner, "android.access-manager.messages.delete-success", msg -> msg.replace("%player%", p.getName()));

			BlockStorage.addBlockInfo(android, key, users.toString());
		} else {
			SlimefunPlugin.getLocalization().sendMessage(owner, "android.access-manager.messages.is-not-trusted-player", msg -> msg.replace("%player%", p.getName()));
		}
	}

	/**
	 * Parse trusted player list raw string to List.
	 *
	 * @param value list raw string
	 * @return trusted player list
	 */
	private @Nonnull static List<String> parseBlockInfoToList(@Nonnull String value) {
		Validate.notNull(value, "The trusted player list cannot be null!");

		String replacedText = value.replace("[", "").replace("]", "");

		if (replacedText.isEmpty()) {
			return Collections.emptyList();
		} else {
			return Arrays.asList(replacedText.split(", "));
		}
	}

	/**
	 * Get a trusted users list of specific android.
	 *
	 * @param b the block of a Android
	 * @return Trusted users
	 */
	public @Nonnull static List<String> getTrustedUsers(@Nonnull Block b) {
		Validate.notNull(b, "The android block cannot be null!");

		String list = BlockStorage.getLocationInfo(b.getLocation(), key);

		// Checks for old Android
		if (list == null) {
			BlockStorage.addBlockInfo(b, "share-users", Collections.emptyList().toString());
			return Collections.emptyList();
		}

		return parseBlockInfoToList(list);
	}

	/**
	 * Checks user is in trusted users list.
	 *
	 * @param b the block of a Android
	 * @param uuid user's UUID
	 * @return whether is the trusted user of android or not
	 */
	public static boolean isTrustedUsers(@Nonnull Block b, @Nonnull UUID uuid) {
		Validate.notNull(b, "The android block cannot be null!");
		Validate.notNull(uuid, "The UUID of player to check cannot be null!");

		String trustUsers = BlockStorage.getLocationInfo(b.getLocation(), key);

		if (trustUsers == null) {
			return false;
		}

		return trustUsers.contains(uuid.toString());
	}
}