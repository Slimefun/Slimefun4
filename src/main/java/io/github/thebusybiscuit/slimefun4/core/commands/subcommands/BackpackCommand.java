package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;

class BackpackCommand extends SubCommand {

    BackpackCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "backpack", false);
    }

    @Override
    protected String getDescription() {
        return "commands.backpack.description";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("slimefun.command.backpack")) {
                if (args.length != 3) {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf backpack <Player> <ID>"));
                    return;
                }

                if (!PatternUtils.NUMERIC.matcher(args[2]).matches()) {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "commands.backpack.invalid-id");
                    return;
                }

                @SuppressWarnings("deprecation")
                OfflinePlayer backpackOwner = Bukkit.getOfflinePlayer(args[1]);

                if (!(backpackOwner instanceof Player) && !backpackOwner.hasPlayedBefore()) {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "commands.backpack.player-never-joined");
                    return;
                }

                int id = Integer.parseInt(args[2]);

                PlayerProfile.get(backpackOwner, profile -> {
                    if (!profile.getBackpack(id).isPresent()) {
                        SlimefunPlugin.getLocalization().sendMessage(sender, "commands.backpack.backpack-does-not-exist");
                        return;
                    }

                    SlimefunPlugin.runSync(() -> {
                        ItemStack item = SlimefunItems.RESTORED_BACKPACK.clone();
                        SlimefunPlugin.getBackpackListener().setBackpackId(backpackOwner, item, 2, id);
                        ((Player) sender).getInventory().addItem(item);
                        SlimefunPlugin.getLocalization().sendMessage(sender, "commands.backpack.restored-backpack-given");
                    });
                });
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }
}
