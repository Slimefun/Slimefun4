package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

class RecipeCommand extends SubCommand {

    @ParametersAreNonnullByDefault
    RecipeCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "recipe", false);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender.hasPermission("slimefun.recipe.reload") && sender instanceof Player) {
            Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
        }

        if (args.length == 1) {
            Slimefun.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf recipe <subcommand>"));
        }

        switch (args[1]) {
            case "reload":
                if (args.length == 2) {
                    Slimefun.getRecipeService().loadAllRecipes();
                } else {
                    for (int i = 2; i < args.length; i++) {
                        Slimefun.getRecipeService().loadRecipesFromFile(args[i]);
                    }
                }
                break;
            case "save":
                if (args.length != 2) {
                    Slimefun.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf recipe save"));
                    break;
                }
                Slimefun.getRecipeService().saveAllRecipes();
                break;
            default:
                Slimefun.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf recipe <subcommand>"));
                break;
        }
    }
}
