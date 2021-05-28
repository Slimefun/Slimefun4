package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.players.PlayerList;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

class GiveCommand extends SubCommand {

    private static final String PLACEHOLDER_PLAYER = "%player%";
    private static final String PLACEHOLDER_ITEM = "%item%";
    private static final String PLACEHOLDER_AMOUNT = "%amount%";

    @ParametersAreNonnullByDefault
    GiveCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "give", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.cheat.items") || !(sender instanceof Player)) {
            if (args.length > 2) {
                Optional<Player> player = PlayerList.findByName(args[1]);

                if (player.isPresent()) {
                    Player p = player.get();

                    SlimefunItem sfItem = SlimefunItem.getByID(args[2].toUpperCase(Locale.ROOT));

                    if (sfItem != null) {
                        giveItem(sender, p, sfItem, args);
                    } else {
                        SlimefunPlugin.getLocalization().sendMessage(sender, "messages.invalid-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, args[2]));
                    }
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "messages.not-online", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]));
                }
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf give <Player> <Slimefun Item> [Amount]"));
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

    private void giveItem(CommandSender sender, Player p, SlimefunItem sfItem, String[] args) {
        if (sfItem instanceof MultiBlockMachine) {
            SlimefunPlugin.getLocalization().sendMessage(sender, "guide.cheat.no-multiblocks");
        } else {
            int amount = parseAmount(args);

            if (amount > 0) {
                SlimefunPlugin.getLocalization().sendMessage(p, "messages.given-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, sfItem.getItemName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(amount)));
                Map<Integer, ItemStack> excess = p.getInventory().addItem(new CustomItem(sfItem.getItem(), amount));
                if (SlimefunPlugin.getCfg().getBoolean("options.drop-excess-sf-give-items") && !excess.isEmpty()) {
                    for (ItemStack is : excess.values()) {
                        p.getWorld().dropItem(p.getLocation(), is);
                    }
                }

                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.give-item", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]).replace(PLACEHOLDER_ITEM, sfItem.getItemName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(amount)));
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.invalid-amount", true, msg -> msg.replace(PLACEHOLDER_AMOUNT, args[3]));
            }
        }
    }

    private int parseAmount(String[] args) {
        int amount = 1;

        if (args.length == 4) {
            if (PatternUtils.NUMERIC.matcher(args[3]).matches()) {
                amount = Integer.parseInt(args[3]);
            } else {
                return 0;
            }
        }

        return amount;
    }

}
