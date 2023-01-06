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

    private static final String PATH = Slimefun.instance().getDataFolder().getAbsolutePath() + "\\dumps\\";

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
                Bukkit.getScheduler().runTaskAsynchronously(
                    this.plugin,
                    task -> createDump(sender, "blockstorage", this::dumpBlockStorage)
                );
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }
    }

    @ParametersAreNonnullByDefault
    private void createDump(CommandSender sender, String filePrefix, Supplier<List<String>> dump) {
        final String dateTimeStamp = new SimpleDateFormat("'_'yyyyMMddHHmm'.txt'").format(new Date());
        final String fullPath = PATH + filePrefix + dateTimeStamp;
        final File directory = new File(PATH);

        if (!directory.exists()) {
            directory.mkdir();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullPath))) {
            writer.write("Dump created at: " + dateTimeStamp + "\n");
            for (String string : dump.get()) {
                writer.write(string + "\n");
            }

            Slimefun.getLocalization().sendMessage(
                sender,
                "commands.dump.complete",
                true,
                msg -> msg.replace("%path%", fullPath)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> dumpBlockStorage() {
        final Map<String, Integer> map = new LinkedHashMap<>();
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
