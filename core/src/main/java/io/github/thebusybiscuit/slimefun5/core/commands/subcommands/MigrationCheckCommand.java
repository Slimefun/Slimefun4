package io.github.thebusybiscuit.slimefun5.core.commands.subcommands;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun5.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * {@code /sf migrationcheck} scans the sender's inventory for items that carry a Slimefun id (as
 * written by any Slimefun 4/5 version) but no longer resolve to a registered item - the signature of
 * a world migrated from a server whose addon set differs. Purely diagnostic; it changes nothing.
 */
class MigrationCheckCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    MigrationCheckCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "migrationcheck", false);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            Slimefun.getLocalization().sendMessage(sender, "messages.only-players", true);
            return;
        }

        if (!sender.hasPermission("slimefun.command.migrationcheck")) {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
            return;
        }

        Player player = (Player) sender;
        Slimefun.getLocalization().sendMessage(player, "messages.migration.scanning", true);

        int resolved = 0;
        // First-seen order preserved; unresolved ids deduped to a summed stack count.
        Map<String, Integer> unresolved = new LinkedHashMap<>();

        for (ItemStack stack : player.getInventory().getContents()) {
            if (stack == null) {
                continue;
            }

            String id = Slimefun.getItemDataService().getItemData(stack).orElse(null);
            if (id == null) {
                continue;
            }

            if (SlimefunItem.getById(id) != null) {
                resolved++;
            } else {
                unresolved.merge(id, stack.getAmount(), Integer::sum);
            }
        }

        int resolvedCount = resolved;
        int unresolvedCount = unresolved.size();
        Slimefun.getLocalization().sendMessage(player, "messages.migration.summary", true,
            msg -> msg.replace("%resolved%", String.valueOf(resolvedCount)).replace("%unresolved%", String.valueOf(unresolvedCount)));

        if (unresolved.isEmpty()) {
            Slimefun.getLocalization().sendMessage(player, "messages.migration.all-good", true);
        } else {
            Slimefun.getLocalization().sendMessage(player, "messages.migration.unresolved-header", true);
            for (Map.Entry<String, Integer> entry : unresolved.entrySet()) {
                Slimefun.getLocalization().sendMessage(player, "messages.migration.unresolved-entry", true,
                    msg -> msg.replace("%id%", entry.getKey()).replace("%amount%", String.valueOf(entry.getValue())));
            }
        }
    }
}
