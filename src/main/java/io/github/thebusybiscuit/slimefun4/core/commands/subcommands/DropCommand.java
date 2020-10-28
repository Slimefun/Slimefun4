package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * The drop command drops a {@link SlimefunItem} of
 * a specified quantity at the specified coordinates.
 *
 * @author NCBPFluffyBear
 * @author TheBusyBiscuit (Based off of GiveCommand)
 */
class DropCommand extends SubCommand {

    private static final String PLACEHOLDER_WORLD = "%world%";
    private static final String PLACEHOLDER_X = "%x%";
    private static final String PLACEHOLDER_Y = "%y%";
    private static final String PLACEHOLDER_Z = "%z%";
    private static final String PLACEHOLDER_ITEM = "%item%";
    private static final String PLACEHOLDER_AMOUNT = "%amount%";

    DropCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "drop", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission("slimefun.cheat.items")) {
            if (args.length > 2) {
                if (args.length > 5) {
                    SlimefunItem sfItem = SlimefunItem.getByID(args[5].toUpperCase(Locale.ROOT));

                    if (sfItem != null) {
                        dropItem(sender, args[1], args[2], args[3], args[4], sfItem, args);
                    } else {
                        SlimefunPlugin.getLocalization().sendMessage(sender, "messages.not-valid-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, args[2]));
                    }
                }
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf drop <World> <X> <Y> <Z> <Slimefun Item> [Amount]"));
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

    private void dropItem(CommandSender sender, String world, String x, String y, String z, SlimefunItem sfItem, String[] args) {
        if (sfItem instanceof MultiBlockMachine) {
            SlimefunPlugin.getLocalization().sendMessage(sender, "guide.cheat.no-multiblocks");
        } else {
            int amount = parseAmount(args);

            if (amount > 0) {
                Bukkit.getWorld(world).dropItem(new Location(Bukkit.getWorld(world), Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(z)), new CustomItem(sfItem.getItem(), amount));
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.drop-item", true, msg -> msg.replace(PLACEHOLDER_WORLD, args[1]).replace(PLACEHOLDER_X, args[2]).replace(PLACEHOLDER_Y, args[3]).replace(PLACEHOLDER_Z, args[4]).replace(PLACEHOLDER_ITEM, sfItem.getItemName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(amount)));
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.not-valid-amount", true, msg -> msg.replace(PLACEHOLDER_AMOUNT, args[3]));
            }
        }
    }

    private int parseAmount(String[] args) {
        int amount = 1;

        if (args.length == 7) {
            if (PatternUtils.NUMERIC.matcher(args[6]).matches()) {
                amount = Integer.parseInt(args[6]);
            } else {
                return 0;
            }
        }

        return amount;
    }

}
