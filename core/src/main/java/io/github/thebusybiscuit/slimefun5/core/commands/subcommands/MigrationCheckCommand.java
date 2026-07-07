package io.github.thebusybiscuit.slimefun5.core.commands.subcommands;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun5.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun5.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun5.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun5.implementation.Slimefun;

/**
 * {@code /sf migrationcheck [player]} scans an inventory for items that carry a Slimefun id (as written
 * by any Slimefun 4/5 version) but no longer resolve to a registered item - the signature of a world
 * migrated from a server whose addon set differs. Without an argument it checks the sender's own
 * inventory; with a player name it checks that (online) player's inventory, so it works from console too.
 * Admin-only ({@code slimefun.command.migrationcheck}, default op) and purely diagnostic - it changes nothing.
 */
class MigrationCheckCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    MigrationCheckCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "migrationcheck", false);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onExecute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("slimefun.command.migrationcheck")) {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
            return;
        }

        Player target;
        if (args.length > 1) {
            target = Bukkit.getPlayerExact(args[1]);
            if (target == null) {
                Slimefun.getLocalization().sendMessage(sender, "messages.not-online", true, msg -> msg.replace("%player%", args[1]));
                return;
            }
        } else if (sender instanceof Player) {
            target = (Player) sender;
        } else {
            // From console the target is not implicit - require a name.
            Slimefun.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf migrationcheck <Player>"));
            return;
        }

        scan(sender, target);
    }

    /** Scans {@code target}'s inventory and reports the result to {@code out} (which may be an admin or console). */
    private void scan(@Nonnull CommandSender out, @Nonnull Player target) {
        if (out != target) {
            Slimefun.getLocalization().sendMessage(out, "messages.migration.target", true, msg -> msg.replace("%player%", target.getName()));
        }

        Slimefun.getLocalization().sendMessage(out, "messages.migration.scanning", true);

        int resolved = 0;
        // First-seen order preserved; unresolved ids deduped to a summed stack count.
        Map<String, Integer> unresolved = new LinkedHashMap<>();

        for (ItemStack stack : target.getInventory().getContents()) {
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
        Slimefun.getLocalization().sendMessage(out, "messages.migration.summary", true,
            msg -> msg.replace("%resolved%", String.valueOf(resolvedCount)).replace("%unresolved%", String.valueOf(unresolvedCount)));

        if (unresolved.isEmpty()) {
            Slimefun.getLocalization().sendMessage(out, "messages.migration.all-good", true);
        } else {
            Slimefun.getLocalization().sendMessage(out, "messages.migration.unresolved-header", true);
            for (Map.Entry<String, Integer> entry : unresolved.entrySet()) {
                Slimefun.getLocalization().sendMessage(out, "messages.migration.unresolved-entry", true,
                    msg -> msg.replace("%id%", entry.getKey()).replace("%amount%", String.valueOf(entry.getValue())));
            }
        }
    }
}
