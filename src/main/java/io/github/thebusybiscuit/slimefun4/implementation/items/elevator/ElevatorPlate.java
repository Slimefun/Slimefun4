package io.github.thebusybiscuit.slimefun4.implementation.items.elevator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.cscorelib2.chat.ChatColors;
import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockPlaceHandler;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockUseHandler;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.papermc.lib.PaperLib;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import me.mrCookieSlime.Slimefun.Lists.RecipeType;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

/**
 * The {@link ElevatorPlate} is a quick way of teleportation.
 * You can place multiple {@link ElevatorPlate ElevatorPlates} along the y axis
 * to teleport between them.
 * 
 * @author TheBusyBiscuit
 *
 */
public class ElevatorPlate extends SimpleSlimefunItem<BlockUseHandler> {

    /**
     * This is our key for storing the floor name.
     */
    private static final String DATA_KEY = "floor";

    /**
     * This is our {@link Set} of currently teleporting {@link Player Players}.
     * It is used to prevent them from triggering the {@link ElevatorPlate} they land on.
     */
    private final Set<UUID> users = new HashSet<>();

    /**
     * This holds the {@link ElevatorTokenMap} for any {@link Player} attempting to use
     * the {@link ElevatorPlate}.
     */
    private final Map<UUID, ElevatorTokenMap> tokens = new ConcurrentHashMap<>();

    @ParametersAreNonnullByDefault
    public ElevatorPlate(Category category, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe, ItemStack recipeOutput) {
        super(category, item, recipeType, recipe, recipeOutput);

        addItemHandler(onPlace());
    }

    @Nonnull
    private BlockPlaceHandler onPlace() {
        return new BlockPlaceHandler(false) {

            @Override
            public void onPlayerPlace(BlockPlaceEvent e) {
                Block b = e.getBlock();
                BlockStorage.addBlockInfo(b, DATA_KEY, ChatColor.WHITE + "Floor #0");
                BlockStorage.addBlockInfo(b, "owner", e.getPlayer().getUniqueId().toString());
            }
        };
    }

    @Override
    public BlockUseHandler getItemHandler() {
        return e -> {
            Block b = e.getClickedBlock().get();

            if (BlockStorage.getLocationInfo(b.getLocation(), "owner").equals(e.getPlayer().getUniqueId().toString())) {
                openEditor(e.getPlayer(), b);
            }
        };
    }

    @Nonnull
    public List<ElevatorFloor> getFloors(@Nonnull Block b) {
        List<ElevatorFloor> floors = new LinkedList<>();

        for (int y = b.getWorld().getMaxHeight(); y > 0; y--) {
            if (y == b.getY()) {
                String name = ChatColors.color(BlockStorage.getLocationInfo(b.getLocation(), DATA_KEY));
                floors.add(new ElevatorFloor(name, b));
                continue;
            }

            Block block = b.getWorld().getBlockAt(b.getX(), y, b.getZ());

            if (block.getType() == getItem().getType() && BlockStorage.check(block, getId())) {
                String name = ChatColors.color(BlockStorage.getLocationInfo(block.getLocation(), DATA_KEY));
                floors.add(new ElevatorFloor(name, block));
            }
        }

        return floors;
    }

    @ParametersAreNonnullByDefault
    public void openInterface(Player p, Block b) {
        if (users.remove(p.getUniqueId())) {
            return;
        }

        List<ElevatorFloor> floors = getFloors(b);

        if (floors.size() < 2) {
            SlimefunPlugin.getLocalization().sendMessage(p, "machines.ELEVATOR.no-destinations", true);
        } else {
            openFloorSelector(b, floors, p);
        }
    }

    @ParametersAreNonnullByDefault
    private void openFloorSelector(Block b, List<ElevatorFloor> floors, Player p) {
        ElevatorTokenMap map = new ElevatorTokenMap();
        tokens.put(p.getUniqueId(), map);

        List<Component> pages = new ArrayList<>();
        TextComponent.Builder page = null;

        for (int i = 0; i < floors.size(); i++) {
            if (i % 10 == 0) {
                if (page != null) {
                    pages.add(page.build());
                }

                String heading = ChatColors.color(SlimefunPlugin.getLocalization().getMessage(p, "machines.ELEVATOR.pick-a-floor"));
                page = Component.text().content(heading).append(Component.newline()).append(Component.newline());
            }

            ElevatorFloor floor = floors.get(i);

            TextComponent.Builder line = Component.text().content("> " + (floors.size() - i) + ". ").color(NamedTextColor.DARK_GRAY);
            line.append(Component.text(floor.getName(), NamedTextColor.BLACK));
            line.append(Component.newline());

            if (floor.getLocation().getBlockY() == b.getY()) {
                TextComponent.Builder tooltip = Component.text().content(ChatColors.color(SlimefunPlugin.getLocalization().getMessage(p, "machines.ELEVATOR.current-floor")));
                tooltip.append(Component.newline());
                tooltip.append(Component.newline());
                tooltip.append(Component.text(floor.getName(), NamedTextColor.WHITE));

                line.hoverEvent(tooltip.build());
            } else {
                TextComponent.Builder tooltip = Component.text().content(ChatColors.color(SlimefunPlugin.getLocalization().getMessage(p, "machines.ELEVATOR.click-to-teleport")));
                tooltip.append(Component.newline());
                tooltip.append(Component.newline());
                tooltip.append(Component.text(floor.getName(), NamedTextColor.WHITE));

                line.hoverEvent(tooltip.build());

                String token = map.add(floor);
                line.clickEvent(ClickEvent.runCommand("/slimefun:elevator " + token));
            }

            page.append(line);
        }

        if (page != null) {
            pages.add(page.build());
        }

        Book book = Book.book(Component.text("Elevator Menu"), Component.text("Slimefun4"), pages);
        Audience audience = SlimefunPlugin.getAudienceProvider().player(p);
        audience.openBook(book);
    }

    /**
     * This makes the given {@link Player} redeem an elevator token.
     * This method is for internal purposes only.
     * 
     * @param p
     *            The {@link Player} to teleport
     * @param token
     *            The token for the elevator {@link ElevatorFloor}
     * 
     * @return Whether or not the operation was successful
     */
    public boolean useElevator(@Nonnull Player p, @Nonnull String token) {
        Validate.notNull(p, "Cannot teleport a null Player");
        Validate.notEmpty(token, "Token is null or empty");

        // To prevent memory leaks -> We will remove the Player from the map
        ElevatorTokenMap map = tokens.remove(p.getUniqueId());

        if (map != null) {
            ElevatorFloor floor = map.get(token);

            if (floor != null) {
                teleport(p, floor);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void clearTokens(@Nonnull Player p) {
        Validate.notNull(p, "Player must not be null");

        tokens.remove(p.getUniqueId());
    }

    private void teleport(@Nonnull Player player, @Nonnull ElevatorFloor floor) {
        SlimefunPlugin.runSync(() -> {
            users.add(player.getUniqueId());

            float yaw = player.getEyeLocation().getYaw() + 180;

            if (yaw > 180) {
                yaw = -180 + (yaw - 180);
            }

            Location loc = floor.getLocation();
            Location destination = new Location(player.getWorld(), loc.getX() + 0.5, loc.getY() + 0.4, loc.getZ() + 0.5, yaw, player.getEyeLocation().getPitch());

            PaperLib.teleportAsync(player, destination).thenAccept(teleported -> {
                if (teleported.booleanValue()) {
                    player.sendTitle(ChatColor.WHITE + ChatColors.color(floor.getName()), null, 20, 60, 20);
                }
            });
        });
    }

    @ParametersAreNonnullByDefault
    public void openEditor(Player p, Block b) {
        ChestMenu menu = new ChestMenu("Elevator Settings");

        menu.addItem(4, new CustomItem(Material.NAME_TAG, "&7Floor Name &e(Click to edit)", "", ChatColor.WHITE + ChatColors.color(BlockStorage.getLocationInfo(b.getLocation(), DATA_KEY))));
        menu.addMenuClickHandler(4, (pl, slot, item, action) -> {
            pl.closeInventory();
            pl.sendMessage("");
            SlimefunPlugin.getLocalization().sendMessage(p, "machines.ELEVATOR.enter-name");
            pl.sendMessage("");

            ChatUtils.awaitInput(pl, message -> {
                BlockStorage.addBlockInfo(b, DATA_KEY, message.replace(ChatColor.COLOR_CHAR, '&'));

                pl.sendMessage("");
                SlimefunPlugin.getLocalization().sendMessage(p, "machines.ELEVATOR.named", msg -> msg.replace("%floor%", message));
                pl.sendMessage("");

                openEditor(pl, b);
            });

            return false;
        });

        menu.open(p);
    }

}
