package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.core.attributes.Rechargeable;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * {@link ChargeCommand} adds an in game command which charges any {@link Rechargeable}
 * item to maximum charge, defined by {@link Rechargeable#getMaxItemCharge(ItemStack)}.
 *
 * @author FluffyBear
 *
 */
class ChargeCommand extends SubCommand {

    ChargeCommand(SlimefunPlugin plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "charge", false);
    }

    @Override
    protected String getDescription() {
        return "commands.charge.description";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("slimefun.charge.command")) {
                Player p = (Player) sender;
                ItemStack item = p.getInventory().getItemInMainHand();
                SlimefunItem slimefunItem = SlimefunItem.getByItem(item);
                if (slimefunItem instanceof Rechargeable) {
                    Rechargeable rechargeableItem = (Rechargeable) slimefunItem;
                    rechargeableItem.setItemCharge(item, rechargeableItem.getMaxItemCharge(item));
                    SlimefunPlugin.getLocalization().sendMessage(sender, "commands.charge.charge-success", true);
                } else {
                    SlimefunPlugin.getLocalization().sendMessage(sender, "commands.charge.not-rechargeable", true);
                }
            } else {
                SlimefunPlugin.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        } else {
            SlimefunPlugin.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }
}
