package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Optional;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.cscorelib2.item.CustomItem;
import io.github.thebusybiscuit.cscorelib2.players.PlayerList;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

class GiveCommand extends SubCommand {

    private static final String PLACEHOLDER_PLAYER = "%player%";
    private static final String PLACEHOLDER_ITEM = "%item%";
    private static final String PLACEHOLDER_AMOUNT = "%amount%";

    public GiveCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "give";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.cheat.items") || !(sender instanceof Player)) {
            if (args.length > 2) {
                Optional<Player> player = PlayerList.findByName(args[1]);

                if (player.isPresent()) {
                    Player p = player.get();

                    SlimefunItem sfItem = SlimefunItem.getByID(args[2].toUpperCase());
                    if (sfItem != null) {
                        int amount = 1;

                        if (args.length == 4) {
                            if (args[3].chars().allMatch(Character::isDigit)) {
                                amount = Integer.parseInt(args[3]);
                            }
                            else {
                                SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-amount", true, msg -> msg.replace(PLACEHOLDER_AMOUNT, args[3]));
                                return;
                            }
                        }

                        int finalAmount = amount;

                        if (amount > 0) {
                            SlimefunPlugin.getLocal().sendMessage(p, "messages.given-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, sfItem.getItemName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(finalAmount)));
                            p.getInventory().addItem(new CustomItem(sfItem.getItem(), amount));
                            SlimefunPlugin.getLocal().sendMessage(sender, "messages.give-item", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]).replace(PLACEHOLDER_ITEM, sfItem.getItemName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(finalAmount)));
                        }
                        else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-amount", true, msg -> msg.replace(PLACEHOLDER_AMOUNT, String.valueOf(finalAmount)));
                    }
                    else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-valid-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, args[2]));
                }
                else SlimefunPlugin.getLocal().sendMessage(sender, "messages.not-online", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]));
            }
            else SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf give <Player> <Slimefun Item> [Amount]"));
        }
        else SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
    }

}
