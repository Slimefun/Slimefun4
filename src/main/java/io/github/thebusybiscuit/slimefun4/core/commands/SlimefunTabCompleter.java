package io.github.thebusybiscuit.slimefun4.core.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import me.mrCookieSlime.Slimefun.SlimefunPlugin;

import javax.annotation.Nonnull;

class SlimefunTabCompleter implements TabCompleter {

    private static final int MAX_SUGGESTIONS = 50;

    private final SlimefunCommand command;

    public SlimefunTabCompleter(SlimefunCommand command) {
        this.command = command;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, String[] args) {
        if (args.length == 1) {
            return createReturnList(command.getTabArguments(), args[0]);
        }
        else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("give")) {
                return createReturnList(SlimefunPlugin.getRegistry().getEnabledSlimefunItemIds().stream()
                        .map(String::toLowerCase)
                        .collect(Collectors.toList()), args[2]);
            }
            else if (args[0].equalsIgnoreCase("research")) {
                Set<NamespacedKey> researches = SlimefunPlugin.getRegistry().getResearchIds().keySet();
                List<String> suggestions = new LinkedList<>();

                suggestions.add("all");
                suggestions.add("reset");

                for (NamespacedKey key : researches) {
                    suggestions.add(key.toString().toLowerCase(Locale.ROOT));
                }

                return createReturnList(suggestions, args[2]);
            }
            else {
                return Collections.emptyList();
            }
        }
        else {
            return Collections.emptyList();
        }
    }

    /***
     * Returns a sublist from a given list containing items that start with the given string if string is not empty
     * 
     * @param list
     *            The list to process
     * @param string
     *            The typed string
     * @return Sublist if string is not empty
     */
    private List<String> createReturnList(List<String> list, String string) {
        if (string.equals("")) return list;

        String input = string.toLowerCase(Locale.ROOT);
        List<String> returnList = new ArrayList<>();

        for (String item : list) {
            if (item.startsWith(input)) {
                returnList.add(item);

                if (returnList.size() >= MAX_SUGGESTIONS) {
                    break;
                }
            }
            else if (item.equalsIgnoreCase(input)) {
                return Collections.emptyList();
            }
        }

        return returnList;
    }

}
