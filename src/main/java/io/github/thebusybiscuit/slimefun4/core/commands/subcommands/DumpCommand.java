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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides commands to dump data for triaging
 *
 * @author Sefiraat
 */
class DumpCommand extends SubCommand {

    private static final String PATH = Slimefun.instance().getDataFolder().getAbsolutePath() + "/dumps/";

    @ParametersAreNonnullByDefault
    DumpCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "dump", false);
    }

    @Override
    protected String getDescription() {
        return "commands.dump.description";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.command.dump") || sender instanceof ConsoleCommandSender) {
            String sub = args[1];
            if (sub.equalsIgnoreCase("blockstorage")) {
                Bukkit.getScheduler().runTaskAsynchronously(this.plugin, task -> dumpBlockStorage(sender));
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

    private String getAndCreatePath(@Nonnull String filePrefix) {
        final String dateTimeStamp = new SimpleDateFormat("'_'yyyyMMddHHmm'.txt'").format(new Date());
        final String fullPath = PATH + filePrefix + dateTimeStamp;
        final File directory = new File(PATH);

        if (!directory.exists()) {
            directory.mkdir();
        }

        return fullPath;
    }

    private void writeHeader(@Nonnull BufferedWriter writer) throws IOException {
        final String dateTimeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        writer.write("Dump created at: " + dateTimeStamp + "\n");
    }

    private void dumpBlockStorage(CommandSender sender) {
        final Map<String, Integer> map = new LinkedHashMap<>();
        for (World world : Bukkit.getWorlds()) {
            for (Config value : BlockStorage.getRawStorage(world).values()) {
                final String id = value.getString("id");
                map.merge(id, 1, Integer::sum);
            }
        }

        final String fullPath = getAndCreatePath("blockstorage_dump");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            writeHeader(writer);
            map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(
                entry -> {
                    final String message = entry.getKey() + " -> " + entry.getValue();
                    try {
                        writer.write(message + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            );
            Slimefun.getLocalization().sendMessage(
                sender,
                "commands.dump.blockstorage.complete",
                true,
                msg -> msg.replace("%path%", fullPath)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
