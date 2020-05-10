package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import me.mrCookieSlime.Slimefun.Lists.SlimefunItems;
import me.mrCookieSlime.Slimefun.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.api.Slimefun;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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
            SlimefunPlugin.getLocal().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf backpack <Player> <Number>"));
            return;
        }

        Player p = (Player) sender;
        String ownerName = args[1];
        int id = Integer.parseInt(args[2]);
        OfflinePlayer owner = Bukkit.getPlayer(ownerName);

        if (owner == null) {
            owner = Bukkit.getOfflinePlayer(ownerName);
            if (!owner.hasPlayedBefore()) {
                SlimefunPlugin.getLocal().sendMessage(sender, "guide.backpack.player-never-joined");
                return;
            }
        }

        PlayerProfile.get(owner, profile -> {
            if (!profile.getBackpack(id).isPresent()) {
                SlimefunPlugin.getLocal().sendMessage(sender, "guide.backpack.backpack-does-not-exist");
                return;
            }
            PlayerBackpack bp = profile.getBackpack(id).get();
            //No point switching for size, would still miss soulbound
            ItemStack item = SlimefunItems.BACKPACK_MEDIUM;
            ItemMeta meta = item.getItemMeta();
            List lore = meta.getLore();
            lore.set(2, ChatColor.GRAY + "ID: " + profile.getUUID().toString() + "#" + id);
            meta.setLore(lore);
            item.setItemMeta(meta);
            Slimefun.runSync(() -> p.getInventory().addItem(item));
        });
    }
}
