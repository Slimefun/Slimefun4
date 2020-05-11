package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.utils.PatternUtils;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class BackpackCommand extends SubCommand {

    BackpackCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd);
    }

    @Override
    public String getName() {
        return "backpack";
    }

    @Override
    public boolean isHidden() {
        return false;
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player) || !(sender.hasPermission("slimefun.command.backpack"))) {
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.no-permission", true);
            return;
        }
        if (args.length != 3) {
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true,
                    msg -> msg.replace("%usage%", "/sf backpack <Player> <ID>"));
            return;
        }

        final Player p = (Player) sender;
        final String ownerName = args[1];
        if (!PatternUtils.NUMERIC.matcher(args[2]).matches()){
            SlimefunPlugin.getLocal().sendMessage(sender, "guide.backpack.invalid-id");
            return;
        }
        final int id = Integer.parseInt(args[2]);
        final OfflinePlayer owner = Bukkit.getOfflinePlayer(ownerName);
        if (!owner.hasPlayedBefore()) {
            SlimefunPlugin.getLocal().sendMessage(sender, "guide.backpack.player-never-joined");
            return;
        }

        PlayerProfile.get(owner, profile -> {
            if (!profile.getBackpack(id).isPresent()) {
                SlimefunPlugin.getLocal().sendMessage(sender, "guide.backpack.backpack-does-not-exist");
                return;
            }
            Slimefun.runSync(() -> {
                p.getInventory().addItem(SlimefunItems.RESTORED_BACKPACK);
                SlimefunPlugin.getLocal().sendMessage(sender, "guide.backpack.restored-backpack-given");
            });
        });
    }
}
