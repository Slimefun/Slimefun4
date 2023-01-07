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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Provides commands to dump data for triaging
 *
 * @author Sefiraat
 */
class DumpCommand extends SubCommand {

    private static final String PATH = Paths.get(Slimefun.instance().getDataFolder().getAbsolutePath(), "dumps").toString();

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
            if (args.length > 1) {
                String sub = args[1];
                if (sub.equalsIgnoreCase("blockstorage")) {
                    Bukkit.getScheduler().runTaskAsynchronously(
                        this.plugin,
                        task -> createDump(sender, "blockstorage", this::dumpBlockStorage)
                    );
                }
            } else {
                Slimefun.getLocalization().sendMessage(
                    sender,
                    "messages.usage",
                    true,
                    msg -> msg.replace("%usage%", "/sf dump <Dump Type>")
                );
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

    @ParametersAreNonnullByDefault
    private void createDump(CommandSender sender, String filePrefix, Supplier<List<String>> dump) {
        String dateTimeStamp = new SimpleDateFormat("'_'yyyyMMddHHmm'.txt'").format(new Date());
        String fullPath = Paths.get(PATH, filePrefix + dateTimeStamp).toString();
        File directory = new File(PATH);

        if (!directory.exists()) {
            directory.mkdir();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            writer.write("Dump created at: " + dateTimeStamp + "\n");
            for (String string : dump.get()) {
                writer.write(string + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Slimefun.getLocalization().sendMessage(
                sender,
                "commands.dump.complete",
                true,
                msg -> msg.replace("%path%", fullPath)
            );
        }
    }

    private List<String> dumpBlockStorage() {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (World world : Bukkit.getWorlds()) {
            for (Config value : BlockStorage.getRawStorage(world).values()) {
                final String id = value.getString("id");
                map.merge(id, 1, Integer::sum);
            }
        }
        return map.entrySet().stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .map(stringIntegerEntry -> stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue())
            .toList();
    }
}
