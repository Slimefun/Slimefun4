package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config;
import me.mrCookieSlime.Slimefun.api.BlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Checks the contents of BlockStorage and creates a file of the contents.
 *
 * @author Sefiraat
 */
class BlockStorageCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    BlockStorageCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "blockstorage", false);
    }

    @Override
    protected String getDescription() {
        return "commands.blockstorage.description";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.command.blockstorage") || sender instanceof ConsoleCommandSender) {
            final Map<String, Integer> map = new LinkedHashMap<>();
            for (World world : Bukkit.getWorlds()) {
                for (Config value : BlockStorage.getRawStorage(world).values()) {
                    final String id = value.getString("id");
                    map.merge(id, 1, Integer::sum);
                }
            }

            final String path = Slimefun.instance().getDataFolder().getAbsolutePath();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/BlockStorageSummary.txt"))) {
                map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(
                    entry -> {
                        final String message = entry.getKey() + " -> " + entry.getValue();
                        try {
                            writer.write(message + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // sender.sendMessage(message);
                    }
                );
                Slimefun.getLocalization().sendMessage(
                    sender,
                    "commands.blockstorage.complete",
                    true,
                    msg -> msg.replace("%path%", path)
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }
}
