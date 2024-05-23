package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * The give command cheat items for player.
 *
 * @author TheBusyBiscuit
 * @author ybw0014
 */
class GiveCommand extends AbstractGiveCommand {
    @ParametersAreNonnullByDefault
    GiveCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "give", false);
    }
}
