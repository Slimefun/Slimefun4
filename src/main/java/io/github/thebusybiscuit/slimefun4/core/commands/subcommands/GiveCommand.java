package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.bakedlibs.dough.common.CommonPatterns;
import io.github.bakedlibs.dough.common.PlayerList;
import io.github.bakedlibs.dough.items.CustomItemStack;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.core.multiblocks.MultiBlockMachine;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

class GiveCommand extends SubCommand {

    private static final String PLACEHOLDER_PLAYER = "%player%";
    private static final String PLACEHOLDER_ITEM = "%item%";
    private static final String PLACEHOLDER_AMOUNT = "%amount%";

    @ParametersAreNonnullByDefault
    GiveCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "give", false);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("slimefun.cheat.items") && sender instanceof Player) {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
            return;
        }

        if (args.length <= 2) {
            Slimefun.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf give <Player> <Slimefun Item> [Amount]"));
            return;
        }

        Optional<Player> player = PlayerList.findByName(args[1]);
        if (player.isEmpty()) {
            Slimefun.getLocalization().sendMessage(sender, "messages.not-online", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]));
            return;
        }

        Player p = player.get();
        SlimefunItem sfItem = SlimefunItem.getById(args[2].toUpperCase(Locale.ROOT));
        if (sfItem == null) {
            Slimefun.getLocalization().sendMessage(sender, "messages.invalid-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, args[2]));
            return;
        }

        giveItem(sender, p, sfItem, args);
    }

    private void giveItem(CommandSender sender, Player p, SlimefunItem sfItem, String[] args) {
        if (sfItem instanceof MultiBlockMachine) {
            Slimefun.getLocalization().sendMessage(sender, "guide.cheat.no-multiblocks");
            return;
        }

        int amount = parseAmount(args);
        if (amount <= 0) {
            Slimefun.getLocalization().sendMessage(sender, "messages.invalid-amount", true, msg -> msg.replace(PLACEHOLDER_AMOUNT, args[3]));
            return;
        }

        Slimefun.getLocalization().sendMessage(p, "messages.given-item", true, msg -> msg.replace(PLACEHOLDER_ITEM, sfItem.getItemName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(amount)));
        Map<Integer, ItemStack> excess = p.getInventory().addItem(new CustomItemStack(sfItem.getItem(), amount));
        if (Slimefun.getCfg().getBoolean("options.drop-excess-sf-give-items") && !excess.isEmpty()) {
            for (ItemStack is : excess.values()) {
                p.getWorld().dropItem(p.getLocation(), is);
            }
        }

        Slimefun.getLocalization().sendMessage(sender, "messages.give-item", true, msg -> msg.replace(PLACEHOLDER_PLAYER, args[1]).replace(PLACEHOLDER_ITEM, sfItem.getItemName()).replace(PLACEHOLDER_AMOUNT, String.valueOf(amount)));
    }

    private int parseAmount(String[] args) {
        int amount = 1;

        if (args.length == 4) {
            if (CommonPatterns.NUMERIC.matcher(args[3]).matches()) {
                amount = Integer.parseInt(args[3]);
            } else {
                return 0;
            }
        }

        return amount;
    }

}
