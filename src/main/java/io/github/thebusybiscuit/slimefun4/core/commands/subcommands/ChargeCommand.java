package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class ChargeCommand extends SubCommand {

    ChargeCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "charge", false);
    }

    protected String getDescription() {
        return "commands.charge.description";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("slimefun.charge.command")) {
                Player p = ((Player) sender).getPlayer();
                final ItemStack item = p.getInventory().getItemInMainHand();
                final SlimefunItem slimefunItem = SlimefunItem.getByItem(item);
                if (slimefunItem instanceof Rechargeable){
                    ((Rechargeable) slimefunItem).addItemCharge(item, ((Rechargeable) slimefunItem).getMaxItemCharge(item));
                    SlimefunPlugin.getLocalization().sendMessage(sender, "commands.charge.charge-success", true);
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "commands.charge.not-rechargeable", true);
                }
            }
            else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        }
        else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }
}
